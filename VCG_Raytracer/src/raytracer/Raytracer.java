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

import multithreading.MultiThreader;
import multithreading.RenderBlock;
import scene.lights.AreaLight;
import scene.lights.Light;
import scene.Scene;
import scene.lights.PointLight;
import scene.shapes.Shape;
import ui.Window;
import utils.*;
import utils.algebra.Vec2;
import utils.algebra.Vec3;
import utils.io.Log;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Raytracer {

    public static int ANTI_ALIASING_NONE = 1;
    public static int ANTI_ALIASING_LOW = 2;
    public static int ANTI_ALIASING_MEDIUM = 4;
    public static int ANTI_ALIASING_HIGH = 8;
    public static int ANTI_ALIASING_INSANE = 16;

    public static int MULTI_THREADING_NONE = 1;
    public static int MULTI_THREADING_LOW = 2;
    public static int MULTI_THREADING_MEDIUM = 4;
    public static int MULTI_THREADING_HIGH = 8;
    public static int MULTI_THREADING_INSANE = 16;

    private static float GI_FACTOR = (float) (1f / Math.PI);

    private BufferedImage mBufferedImage;
    private ArrayList<Shape> mShapeList;
    private ArrayList<Light> mLightList;
    private Scene mScene;

    private Window mRenderWindow;

    private int mMaxRecursions;
    private int mGiLevel;
    private int mGiSamples;

    private float mAntiAliasingFactor;
    private float mAntiAliasingCounter;
    private int mAntiAliasingSamples;

    private int mMultiThreadingLevel;

    private boolean mUseBlurryRef;
    private int mBlurryLevel;
    private boolean mUseGI;
    private float mPDFFactor;

    private RgbColor mBackgroundColor;
    private RgbColor mAmbientLight;

    private boolean mDebug;
    private long tStart;

    public Raytracer(Scene scene, Window renderWindow, int recursions, boolean useGi, int giLevel, int giSamples, boolean useBlurryRefs, int blurryLevel, RgbColor backColor, RgbColor ambientLight, int antiAliasingSamples, int multithreading, boolean debugOn){
        Log.print(this, "Init");
        mMaxRecursions = recursions;
        mUseBlurryRef = useBlurryRefs;
        mUseGI = useGi;
        mBlurryLevel = blurryLevel;

        if(!mUseBlurryRef){
            mBlurryLevel = 1;
        }

        if(mUseGI) {
            mGiLevel = giLevel;
            mGiSamples = giSamples;
        }

        mBufferedImage = renderWindow.getBufferedImage();
        mBackgroundColor = backColor;
        mAmbientLight = ambientLight;
        mScene = scene;
        mRenderWindow = renderWindow;
        mShapeList = scene.getShapeList();
        mLightList = scene.getLightList();
        mAntiAliasingFactor = 1f / (antiAliasingSamples * antiAliasingSamples);
        mAntiAliasingCounter = 1f / antiAliasingSamples;
        mDebug = debugOn;
        tStart = System.currentTimeMillis();
        mAntiAliasingSamples = antiAliasingSamples;
        mMultiThreadingLevel = multithreading;

        mPDFFactor = (float) (1f / (2f* Math.PI));
    }

    public BufferedImage getBufferedImage() {
        return mBufferedImage;
    }

    public Window getRenderWindow() {
        return mRenderWindow;
    }

    public void exportRendering(){
        mRenderWindow.exportRendering(String.valueOf(stopTime(tStart)), mMaxRecursions, mAntiAliasingSamples, mDebug, mGiLevel, mGiSamples);
    }

    private static double stopTime(long tStart){
        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        return tDelta / 1000.0;
    }

    public void renderScene(){
        Log.print(this, "Start rendering");

        MultiThreader rayMultiThreader = new MultiThreader(this);
        rayMultiThreader.startMultiThreading(mMultiThreadingLevel);
    }

    public void renderBlock(RenderBlock renderBlock){
        // Rows
        for (int y = renderBlock.yMin; y < renderBlock.yMax; y++) {
            // Columns
            for (int x = renderBlock.xMin; x < renderBlock.xMax; x++) {
                RgbColor antiAlisedColor = this.calculateAntiAliasedColor(y, x);
                this.getRenderWindow().setPixel(this.getBufferedImage(), antiAlisedColor, new Vec2(x, y));
            }
        }
    }

    public RgbColor calculateAntiAliasedColor(int y, int x) {
        Vec2 screenPosition;
        RgbColor antiAlisedColor = RgbColor.BLACK;

        // Start super sampling
        for(float i = x -0.5f; i < x + 0.5f; i += mAntiAliasingCounter){
            for(float j = y -0.5f; j < y + 0.5f; j += mAntiAliasingCounter){
                screenPosition = new Vec2(i, j);
                antiAlisedColor = antiAlisedColor.add(this.sendPrimaryRay(screenPosition).multScalar(mAntiAliasingFactor));
            }
        }
        return antiAlisedColor;
    }

    private RgbColor sendPrimaryRay(Vec2 pixelPoint){
        if(pixelPoint.equals(new Vec2(400.5f, 450f))){
            Log.print(this, "out");
        }
        Vec3 startPoint = mScene.getCamPos();
        Vec3 destinationDir = mScene.getCamPixelDirection(pixelPoint);
        Ray primaryRay = new Ray(startPoint, destinationDir, 1f);

        return traceRay(mMaxRecursions, mGiLevel, primaryRay, mBackgroundColor, null);
    }

    private RgbColor traceRay(int recursionCounter, int giLevelCounter, Ray inRay, RgbColor localColor, Intersection prevIntersec){
        RgbColor directLight = localColor;

        // For each pixel testing each shape to get nearest intersection; the range of the Ray is this time unlimited
        Intersection intersection = this.getIntersectionOnShapes(inRay, prevIntersec);

        if( intersection.isHit() ){
            // Stop! Enter, if the last recursion level is reached, but it is not the final ray to the light
            if( recursionCounter <= 0 ) {
                // If recursion is done and it is not the last ray then trace the ray to all lights to see if any obstacle exists
                return directLight;
            }

            RgbColor shadedColor = shade( intersection );

            // Calculate the color of every object, that was hit in between, depending on recursive level
            directLight = directLight.add( shadedColor );

            // Further recursions through objects, if the recursion is not finished and object is not diffuse
            if ( intersection.getShape().isReflective() ) {
                recursionCounter -= 1;

                Vec3 reflectionColorVec = new Vec3();

                for(int i = 0; i < mBlurryLevel; i++) {
                    //float reflectivity = intersection.getShape().getMaterial().getReflectivity();
                    float reflectivity = intersection.calculateReflectivity();
                    RgbColor reflectionColor = this.traceRay(recursionCounter, giLevelCounter, intersection.calculateReflectionRay(mUseBlurryRef), directLight, intersection).multScalar(reflectivity);
                    reflectionColorVec = reflectionColorVec.add(reflectionColor.colors);
                }

                reflectionColorVec = reflectionColorVec.multScalar(1f / mBlurryLevel);

                directLight = directLight.add(new RgbColor(reflectionColorVec));
            }
            if ( intersection.getShape().isRefractive() ) {
                recursionCounter -= 1;
                float transparency = intersection.getShape().getMaterial().getRefractivity();
                RgbColor transmissionColor = this.traceRay(recursionCounter, giLevelCounter, intersection.calculateRefractionRay(), directLight, intersection).multScalar(transparency);
                directLight = directLight.add( transmissionColor.multScalar(0.75f) );
            }
            if ( mUseGI && intersection.getShape().getMaterial().isGiOn()){
                // direct illumination + indirect illumination

                Vec3 indirectLight = this.calculateGiIntersections(giLevelCounter, new Vec3(), intersection);

                //Vec3 colorVec = directLight.colors.multScalar(GI_FACTOR).add(indirectLight.multScalar(2f)).multScalar(0.058f);
                Vec3 colorVec = (directLight.colors.add(indirectLight.multScalar(GI_FACTOR * 0.25f)));


                directLight = new RgbColor(colorVec);
            }

            // Add ambient term
            RgbColor ambientTerm = intersection.getShape().getMaterial().getAmbientCoeff().multRGB( this.mAmbientLight );
            directLight = directLight.add( ambientTerm );
        }

        return directLight;
    }

    private Vec3 giTraceRay(int giLevelCounter, Vec3 outColor, Intersection prevIntersec){
        Ray randomRay = prevIntersec.calculateRandomRay();

        Intersection intersection = this.getIntersectionOnShapes(randomRay, prevIntersec);

        if( intersection.isHit() && intersection.getShape().getMaterial().isGiOn()){

            // calculate the shaded color at one point
            for(Light light : mLightList){
                Vec3 giColor = intersection.getShape().getColor(light, intersection).colors;

                // start a new GI iteration
                outColor = outColor.add(this.calculateGiIntersections(giLevelCounter, giColor, intersection));
            }
            outColor = outColor.multScalar(randomRay.getDirection().scalar(intersection.getNormal()));
        }

        return outColor;
    }

    private Vec3 calculateGiIntersections(int giLevelCounter, Vec3 outColor, Intersection intersection){
        if( giLevelCounter > 0 ) {
            Vec3 indirectLight = new Vec3();

            giLevelCounter -= 1;

            // object is diffuse; send additional rays
            for (int i = 0; i < mGiSamples; i++) {

                Vec3 giColor = this.giTraceRay(giLevelCounter, outColor, intersection).multScalar(1f / mPDFFactor).multScalar(intersection.getCosTheta());
                indirectLight = indirectLight.add(giColor);
            }

            if(mGiSamples != 0) {
                outColor = indirectLight.multScalar( 1f / (float) mGiSamples);
            }
        }

        return outColor;
    }

    private RgbColor shade(Intersection finalIntersection) {
        RgbColor illuColor = RgbColor.BLACK;

        // Check the ray from the intersection point to any light source
        for( Light light : mLightList ) {
            illuColor = illuColor.add( this.traceIllumination( illuColor, light, finalIntersection ) );
        }
        // If illuColor is BLACK nothing was hit the position is in shadow - do nothing

        return illuColor;
    }

    private RgbColor traceIllumination(RgbColor illuColor, Light light, Intersection finalIntersection){
        if( light.isType().equals("PointLight")){
            illuColor = this.getColorFromPointLight(illuColor, light, finalIntersection);
        }
        if( light.isType().equals("AreaLight")){
            illuColor = this.getColorFromAreaLight(illuColor, light, finalIntersection);
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

        Intersection lightIntersection = this.getIntersectionBetweenLight(lightRay, finalIntersection);

        // Only if no intersection is happening between the last intersection Point and the light source draw the color
        if(!lightIntersection.isHit() || lightIntersection.isOutOfDistance(lightRay.getDistance()) ){

            // This was the last ray and nothing was hit on the ray from the last object to the light source
            illuColor = illuColor.add( this.calculateLocalIllumination(light, finalIntersection ));
        }
        return illuColor;
    }

    private Intersection getIntersectionOnShapes(Ray inRay, Intersection prevIntersec) {
        Intersection finalIntersection = new Intersection(inRay, null);
        float tempDistance = Float.MAX_VALUE;

        boolean skip = false;

        // 2: Intersection test with all shapes
        for( Shape shape : mShapeList ){
            // Important: Avoid intersection with itself as long as it is not transparent
            if( prevIntersec != null ) {
                if ( ( prevIntersec.getShape().equals(shape) )) {
                    skip = true;
                }
            }

            if(!skip) {
                Intersection intersection = shape.intersect(inRay);

                // Shape was not hit + the distance is adequate
                if (intersection.isHit()                                  // is Hit and coming from the correct side
                        && (intersection.getDistance() < tempDistance)    // shortest distance of all
                        && (intersection.getDistance() > 0.00001))        // minimum distance
                {
                    tempDistance = intersection.getDistance();
                    finalIntersection = intersection;
                }
            }
            skip = false;
        }
        return finalIntersection;
    }

    private Intersection getIntersectionBetweenLight(Ray inRay, Intersection prevIntersec) {
        // 2: Intersection test with all shapes
        for( Shape shape : mShapeList ){
            // Important: Avoid intersection with itself
            if( !prevIntersec.getShape().equals( shape ) && ( shape.isRaytraced() == true ) ) {

                // Find intersection between shape and the light source
                Intersection intersection = shape.intersect( inRay );

                // Shape was not hit + the ray is incoming + the distance is adequate
                if( intersection.isHit() && intersection.getDistance() < inRay.getDistance() ) {
                    // There was something in the way, can terminate now
                    return intersection;
                }
            }
        }
        return new Intersection(inRay, null);
    }

    private RgbColor calculateLocalIllumination(Light light, Intersection intersection){
        return intersection.getShape().getColor(light, mScene.getCamPos(), intersection);
    }
}
