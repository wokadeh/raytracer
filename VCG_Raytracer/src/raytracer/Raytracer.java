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

            outColor = findIntersection(recursionCounter, inRay, mBackgroundColor, null, false);
        }

        return outColor;
    }

    private RgbColor findIntersection(int recursionCounter, Ray inRay, RgbColor localColor, Light inLight, boolean isLastRay){
        RgbColor outColor = localColor;
        // 2: Intersection test with all shapes
        for(Object shape : mShapeList){
            Intersection intersection = ((Shape)shape).intersect(inRay);

            //Log.error(this, "findintersect");
            // Was hit
            if(intersection.isHit() == false){
                return new RgbColor(0,0,1);
//                // 3a: send secondary ray to the light source
//                if(recursionCounter == 0){
//                    for(Object light : mLightList) {
//                        Light outLight = (Light) light;
//                        Vec3 endPoint = outLight.getPosition();
//                        Ray lightRay = new Ray(intersection.getIntersectionPoint(), endPoint);
//
//                        localColor.add(findIntersection(recursionCounter, lightRay, localColor, outLight, true));
//                    }
//                }
//                else {
//                    traceRay(recursionCounter, intersection.getOutRay());
//                }
//                // If the last ray from an object is still intersected with an object the plan shadow color is drawn
//                if(isLastRay){
//                    return calculateShadowColor();
//                }
            }
            else{
                Log.error(this, "lalala");

                // If the last ray from an object to the light is not intersected calculate the color on that point
                if(isLastRay){
                    return calculateLocalIllumination(inLight, (Shape) shape);
                }
                return new RgbColor(1,0,0);
            }
        }
        return outColor;
    }

    private RgbColor calculateShadowColor(){
        RgbColor outColor = mAmbientColor;

        return outColor;
    }

    private RgbColor sendPrimaryRay(Vec2 pixelPoint){
        RgbColor finalColor;
        Vec3 startPoint = mScene.getCamPos();
        Vec3 destinationPoint = mScene.getCamCoords(pixelPoint);
        Ray primaryRay = new Ray(startPoint, destinationPoint);

       Log.warn(this, startPoint.toString() + ", " + destinationPoint.toString());

        finalColor = traceRay(mMaxRecursions, primaryRay);
        //if (!finalColor.equals(new RgbColor(0,0,1)))
          //  Log.error(this, finalColor.toString());
        // 4: set background color
        return finalColor;
    }

    private RgbColor calculateLocalIllumination(Light light, Shape shape){
        return shape.getColor();
    }

    private Vec3 calculateDestinationPoint(){
        return new Vec3(0,0,0);
    }
}
