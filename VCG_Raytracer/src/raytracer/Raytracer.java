/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    1. Send primary ray
    2. intersection test with all shapes
    3. if hit:
    3a: send secondary ray to the light source
    3b: 2
        3b.i: if hit:
            - Shape is in the shade
            - Pixel color = ambient value
        3b.ii: in NO hit:
            - calculate local illumination
    4. if NO hit:
        - set background color

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package raytracer;

import scene.lights.AreaLight;
import scene.lights.Light;
import scene.Scene;
import scene.lights.PointLight;
import scene.shapes.Shape;
import ui.Window;
import utils.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Raytracer {

    public static int ANTI_ALIASING_LOW = 2;
    public static int ANTI_ALIASING_MEDIUM = 4;
    public static int ANTI_ALIASING_HIGH = 8;
    public static int ANTI_ALIASING_INSANE = 16;

    private BufferedImage mBufferedImage;
    private ArrayList<Shape> mShapeList;
    private ArrayList<Light> mLightList;
    private Scene mScene;
    private Window mRenderWindow;

    private int mMaxRecursions;
    private float mAntiAliasingFactor;
    private float mAntiAliasingCounter;

    private RgbColor mBackgroundColor;
    private RgbColor mAmbientLight;

    public Raytracer(Scene scene, Window renderWindow, int recursions, RgbColor backColor, RgbColor ambientLight, int antiAliasingSamples){
        Log.print(this, "Init");
        mMaxRecursions = recursions;
        mBufferedImage = renderWindow.getBufferedImage();
        mBackgroundColor = backColor;
        mAmbientLight = ambientLight;
        mScene = scene;
        mRenderWindow = renderWindow;
        mShapeList = scene.getShapeList();
        mLightList = scene.getLightList();
        mAntiAliasingFactor = 1f / (antiAliasingSamples * antiAliasingSamples);
        mAntiAliasingCounter = 1f / antiAliasingSamples;
    }

    public void renderScene(){
        Log.print(this, "Start rendering");

        Vec2 screenPosition;
        // Rows
        for (int y = 0; y < mBufferedImage.getHeight(); y++) {
            // Columns
            for (int x = 0; x < mBufferedImage.getWidth(); x++) {

                RgbColor antiAlisedColor = calculateAntiAliasedColor(y, x);
                screenPosition = new Vec2(x, y);
                mRenderWindow.setPixel(mBufferedImage, antiAlisedColor, screenPosition);
            }
        }

        IO.saveImageToPng(mBufferedImage, "raytracing.png");
    }

    private RgbColor calculateAntiAliasedColor(int y, int x) {
        Vec2 screenPosition;
        RgbColor antiAlisedColor = RgbColor.BLACK;
        // Start super sampling
        for(float i = x; i < x + 1f; i += mAntiAliasingCounter){
            for(float j = y; j < y + 1f; j += mAntiAliasingCounter){

                screenPosition = new Vec2(i, j);
                antiAlisedColor = antiAlisedColor.add( sendPrimaryRay(screenPosition).multScalar( mAntiAliasingFactor ) );
            }
        }
        return antiAlisedColor;
    }

    private RgbColor sendPrimaryRay(Vec2 pixelPoint){
        Vec3 startPoint = mScene.getCamPos();
        Vec3 destinationDir = mScene.getCamPixelDirection(pixelPoint);
        Ray primaryRay = new Ray(startPoint, destinationDir, 1f);

        return traceRay(mMaxRecursions, primaryRay, mBackgroundColor, null);
    }

    private RgbColor traceRay(int recursionCounter, Ray inRay, RgbColor localColor, Intersection prevIntersec){
        RgbColor outColor = localColor;

        // For each pixel testing each shape to get nearest intersection; the range of the Ray is this time unlimited
        Intersection intersection = getIntersectionOnShapes(inRay, Float.MAX_VALUE, prevIntersec);

        if( intersection.isHit() && intersection.isIncoming() ){
            // Stop! Enter, if the last recursion level is reached, but it is not the final ray to the light
            if( recursionCounter == 0) {
                // If recursion is done and it is not the last ray then trace the ray to all lights to see if any obstacle exists
                return outColor;
            }
            // Further recursions through objects, if the recursion is not finished and object is not diffuse
            if(intersection.getShape().isReflective()){
                recursionCounter = recursionCounter - 1;
                outColor = outColor.add( traceRay( recursionCounter, intersection.calculateReflectionRay(), outColor, intersection ));
            }
            if(intersection.getShape().isTransparent()){
                recursionCounter = recursionCounter - 1;
                outColor = outColor.add( traceRay( recursionCounter, intersection.calculateRefractionRay(), outColor, intersection ));
            }
            // Calculate the color of every object, that was hit in between, depending on recursive level
            outColor = outColor.add( shade( intersection ) );
        }

        return outColor;
    }

    private RgbColor shade(Intersection finalIntersection) {
        RgbColor illuColor = RgbColor.BLACK;

        // Check the ray from the intersection point to any light source
        for( Light light : mLightList ) {
            illuColor = illuColor.add(traceIllumination( illuColor, light, finalIntersection ));
        }

        // Shadow: Something was hit in between of the light source and the current shape. Draw ambient
        if( illuColor.equals( RgbColor.BLACK ) ){
            illuColor = illuColor.add( calculateShadowColor( finalIntersection.getShape() )).multScalar( 1f / (( float )mLightList.size() * 20f));
        }

        // No shadow: finally add ambient color to each object only once
        return illuColor.add( mAmbientLight );
    }

    private RgbColor traceIllumination(RgbColor illuColor, Light light, Intersection finalIntersection){
        if( light.isType().equals("PointLight")){
            illuColor = getColorFromPointLight(illuColor, light, finalIntersection);
        }
        if( light.isType().equals("AreaLight")){
            illuColor = getColorFromAreaLight(illuColor, light, finalIntersection);
        }
        return illuColor;
    }

    private RgbColor getColorFromAreaLight(RgbColor illuColor, Light light, Intersection finalIntersection) {

        ArrayList<PointLight> lightPoints = ((AreaLight) light).getPositionList();

        float factor = 1.0f / lightPoints.size();

        for( PointLight subLight : lightPoints ) {
            illuColor = illuColor.add( getColorFromPointLight(illuColor, subLight, finalIntersection).multScalar( factor ) );
        }

        return illuColor;
    }

    private RgbColor getColorFromPointLight(RgbColor illuColor, Light light, Intersection finalIntersection) {
        Ray lightRay = new Ray(finalIntersection.getIntersectionPoint(), light.getPosition());

        Intersection lightIntersection = getIntersectionBetweenLight(lightRay, finalIntersection);

        // Only if no intersection is happening between the last intersection Point and the light source draw the color
        if(!lightIntersection.isHit() || lightIntersection.isOutOfDistance(lightRay.getDistance()) ){

            // This was the last ray and nothing was hit on the ray from the last object to the light source
            illuColor = illuColor.add( calculateLocalIllumination(light, finalIntersection.getShape(), finalIntersection ));
        }
        return illuColor;
    }

    private Intersection getIntersectionOnShapes(Ray inRay, float tempDistance, Intersection prevIntersec) {
        Intersection finalIntersection = new Intersection(inRay, null);

        // 2: Intersection test with all shapes
        for( Shape shape : mShapeList ){
            // Important: Avoid intersection with itself
            if(prevIntersec != null) {
                if (prevIntersec.getShape().equals(shape)) {
                    return finalIntersection;
                }
            }

            Intersection intersection = shape.intersect(inRay);

            // Shape was not hit + the ray is incoming + the distance is adequate
            if( intersection.isHit() && intersection.isIncoming() && (intersection.getDistance() < tempDistance) ) {
                tempDistance = intersection.getDistance();
                finalIntersection = intersection;
            }
        }
        return finalIntersection;
    }

    private Intersection getIntersectionBetweenLight(Ray inRay, Intersection prevIntersec) {
        // 2: Intersection test with all shapes
        for( Shape shape : mShapeList ){
            // Important: Avoid intersection with itself
            if( !prevIntersec.getShape().equals( shape ) ) {

                // Find intersection between shape and the light source
                Intersection intersection = shape.intersect( inRay );

                // Shape was not hit + the ray is incoming + the distance is adequate
                if (intersection.isHit() && intersection.isIncoming()) {
                    // There was something in the way, can terminate now
                    return intersection;
                }
            }
        }
        return new Intersection(inRay, null);
    }

    private RgbColor calculateLocalIllumination(Light light, Shape shape, Intersection intersection){
        return shape.getColor(light, mScene.getCamPos(), intersection);
    }

    private RgbColor calculateShadowColor(Shape shape){
        return mAmbientLight;
    }
}
