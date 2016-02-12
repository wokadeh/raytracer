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

import scene.Light;
import scene.Scene;
import scene.Shape;
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

    private RgbColor traceRay(int recursionCounter, Ray inRay){
        RgbColor outColor = mBackgroundColor;
        while(recursionCounter > 0){
            recursionCounter--;

            outColor = findIntersection(recursionCounter, inRay, outColor, null, null, null, false);
        }

        return outColor;
    }

    private RgbColor findIntersection(int recursionCounter, Ray inRay, RgbColor localColor, Light inLight, Shape lastInterShape, Intersection lastIntersection, boolean isLastRay){
        RgbColor outColor = localColor;
        // 2: Intersection test with all shapes
        for( Shape shape : mShapeList ){
            Intersection intersection = shape.intersect(inRay);
            // Shape was hit
            if( intersection.isHit() ){
                outColor = new RgbColor(0,0,0);
                for( Light light : mLightList ) {
                    Ray lightRay = new Ray(intersection.getIntersectionPoint(), light.getPosition());

                    outColor = outColor.add(calculateLocalIllumination(light, shape, intersection));
                }
            }
        }
        return outColor;
    }

//    private RgbColor findIntersection(int recursionCounter, Ray inRay, RgbColor localColor, Light inLight, Shape lastInterShape, Intersection lastIntersection, boolean isLastRay){
//        RgbColor outColor = localColor;
//        // 2: Intersection test with all shapes
//        for( Shape shape : mShapeList ){
//            Intersection intersection = shape.intersect(inRay);
//            // Shape was hit
//            if( intersection.isHit() ){
//                // 3a: send secondary ray to the light source
//                if( recursionCounter == 0 && !isLastRay ){
//                    for( Light light : mLightList ) {
//                        Ray lightRay = new Ray(intersection.getIntersectionPoint(), light.getPosition());
//
//                        outColor = outColor.add(findIntersection(recursionCounter, lightRay, localColor, light, shape, intersection, true));
//                    }
//                }
//                // If the last ray from an object is still intersected with an object the plan shadow color is drawn
//                //else if(recursionCounter == 0 && isLastRay){
//                //    return calculateShadowColor();
//                //}
//                // Further recursions through objects
//                else {
//                    outColor = outColor.add( mAmbientColor);
//                //    outColor = traceRay(recursionCounter, intersection.getOutRay());
//                }
//            }
//            else{
//                // If the last ray from an object to the light is not intersected calculate the color on that point
//                if( isLastRay ){
//                    outColor = outColor.add(new RgbColor(1,0,0));// = outColor.add(mAmbientColor.add(calculateLocalIllumination(inLight, lastInterShape, lastIntersection)));
//                }
//                else {
//                    outColor = mBackgroundColor;
//                }
//            }
//        }
//        return outColor;
//    }

    private RgbColor calculateShadowColor(){
        Log.warn(this, "Painting shadow");
        RgbColor outColor = mAmbientColor;

        return outColor;
    }

    private RgbColor sendPrimaryRay(Vec2 pixelPoint){
        Vec3 startPoint = mScene.getCamPos();
        Vec3 destinationDir = mScene.getCamPixelDirection(pixelPoint);
        Ray primaryRay = new Ray(startPoint, destinationDir, 1f);

       //Log.warn(this, startPoint.toString() + ", " + destinationPoint.toString());

        //if (!finalColor.equals(new RgbColor(0,0,1)))
          //  Log.error(this, finalColor.toString());
        // 4: set background color
        return traceRay(mMaxRecursions, primaryRay);
    }

    private RgbColor calculateLocalIllumination(Light light, Shape shape, Intersection intersection){
        //Log.error(this, "calc Illu");
        return shape.getColor(light, mScene.getCamPos(), intersection);
    }

    private Vec3 calculateDestinationPoint(){
        return new Vec3(0,0,0);
    }
}
