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

import scene.lights.Light;
import scene.Scene;
import scene.shapes.Shape;
import ui.Window;
import utils.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Raytracer {

    private BufferedImage mBufferedImage;
    private ArrayList<Shape> mShapeList;
    private ArrayList<Light> mLightList;
    private Scene mScene;
    private Window mRenderWindow;

    private int mMaxRecursions;

    private RgbColor mAmbientColor;
    private RgbColor mBackgroundColor;

    public Raytracer(Scene scene, Window renderWindow, int recursions, RgbColor ambientColor, RgbColor backColor){
        Log.print(this, "Init");

        mMaxRecursions = recursions;
        mBufferedImage = renderWindow.getBufferedImage();
        mAmbientColor = ambientColor;
        mBackgroundColor = backColor;
        mScene = scene;
        mRenderWindow = renderWindow;
        mShapeList = scene.getShapeList();
        mLightList = scene.getLightList();
    }

    public void renderScene(){
        Log.print(this, "Start rendering");

        RgbColor pixelColor;
        // Columns
        for (int y = 0; y < mBufferedImage.getHeight(); y++) {
            // Rows
            for (int x = 0; x < mBufferedImage.getWidth(); x++) {
                Vec2 screenPosition = new Vec2(x, y);
                pixelColor = sendPrimaryRay(screenPosition);
                mRenderWindow.setPixel(mBufferedImage, pixelColor, screenPosition);
            }
        }

        IO.saveImageToPng(mBufferedImage, "raytracing.png");
    }

    private RgbColor traceRay(int recursionCounter, Ray inRay, RgbColor localColor, Light inLight, Shape lastInterShape, Intersection lastIntersection, boolean isLastRay){
        RgbColor outColor = localColor;
        // 2: Intersection test with all shapes
        for( Shape shape : mShapeList ){
            Intersection intersection = shape.intersect( inRay );
            // Shape was hit
            if( intersection.isHit() ){
                // Enter, if the last recursion level is reached, but it is not the final ray to the light
                if( recursionCounter == 0 && !isLastRay ) {
                    RgbColor illuColor = new RgbColor( 0 ,0 ,0 );
                    for( Light light : mLightList ) {
                        Ray lightRay = new Ray(intersection.getIntersectionPoint(), light.getPosition());

                        illuColor = illuColor.add( traceRay(recursionCounter, lightRay, outColor, light, shape, intersection, true) );
                    }
                    outColor = illuColor.add(mAmbientColor);
                }
                else if(recursionCounter == 0 && isLastRay){
                    //return calculateShadowColor();
                }
                // Further recursions through objects, if the recursion is not finished
                else if( recursionCounter != 0 ){
                   recursionCounter = recursionCounter - 1;
                   outColor = traceRay( recursionCounter, inRay, outColor, inLight, shape, intersection, false );
                }
            }
            if( isLastRay ) {
                outColor = calculateLocalIllumination( inLight, lastInterShape, lastIntersection );
            }
        }
        return outColor;
    }

    private RgbColor calculateShadowColor(){
        Log.warn(this, "Painting shadow");
        RgbColor outColor = mAmbientColor;

        return outColor;
    }

    private RgbColor sendPrimaryRay(Vec2 pixelPoint){
        Vec3 startPoint = mScene.getCamPos();
        Vec3 destinationDir = mScene.getCamPixelDirection(pixelPoint);
        Ray primaryRay = new Ray(startPoint, destinationDir, 1f);

        return traceRay(mMaxRecursions, primaryRay, mBackgroundColor, null, null, null, false);
    }

    private RgbColor calculateLocalIllumination(Light light, Shape shape, Intersection intersection){
        return shape.getColor(light, mScene.getCamPos(), intersection);
    }

    private Vec3 calculateDestinationPoint(){
        return new Vec3(0,0,0);
    }
}
