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
import utils.RgbColor;
import utils.Vec3;
import utils.Vec4;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Raytracer {

    BufferedImage mBufferedImage;
    ArrayList<Shape> mShapeList;
    ArrayList<Light> mLightList;

    int mMaxRecursions;

    RgbColor mAmbientColor;

    public Raytracer(Scene scene, List<Vec3> screenPointList, BufferedImage bufferedImage, int recursions, RgbColor ambientColor){
        mMaxRecursions = recursions;
        mBufferedImage = bufferedImage;
        mAmbientColor = ambientColor;
        mShapeList = scene.getShapeList();
        mLightList = scene.getLightList();

        // 1: send primary rays for every screen point
        for(Vec3 screenPoint : screenPointList) {
            sendPrimaryRay(screenPoint);
        }
    }

    public BufferedImage getBufferedImage(){
        return mBufferedImage;
    }

    private RgbColor traceRay(int recursionCounter, Ray inRay){
        RgbColor outColor = mAmbientColor;
        while(recursionCounter > 0){
            recursionCounter--;

            outColor = findIntersection(recursionCounter, inRay, new RgbColor(0,0,0), null, false);
        }

        return outColor;
    }

    private RgbColor findIntersection(int recursionCounter, Ray inRay, RgbColor localColor, Light inLight, boolean isLastRay){
        RgbColor outColor = localColor;
        // 2: Intersection test with all shapes
        for(Object shape : mShapeList){
            Intersection intersection = ((Shape)shape).intersect(inRay);

            // Was hit
            if(!intersection.getOutRay().equals(inRay)){
                // 3a: send secondary ray to the light source
                if(recursionCounter == 0){
                    for(Object light : mLightList) {
                        Light outLight = (Light) light;
                        Vec4 endPoint = outLight.getPosition();
                        Ray lightRay = new Ray(intersection.getIntersectionPoint(), endPoint);

                        localColor.add(findIntersection(recursionCounter, lightRay, localColor, outLight, true));
                    }
                }
                else {
                    traceRay(recursionCounter, intersection.getOutRay());
                }
                // If the last ray from an object is still intersected with an object the plan shadow color is drawn
                if(isLastRay){
                    return calculateShadowColor();
                }
            }
            else{
                // If the last ray from an object to the light is not intersected calculate the color on that point
                if(isLastRay){
                    return calculateLocalIllumination(inLight, (Shape) shape);
                }
            }
        }
        return outColor;
    }

    private RgbColor calculateShadowColor(){
        RgbColor outColor = new RgbColor(0,0,0);

        return outColor;
    }

    private RgbColor sendPrimaryRay(Vec3 screenPoint){
        RgbColor finalColor = new RgbColor(0f,0f,0f);
        Vec4 startPoint = new Vec4(screenPoint.x, screenPoint.y, screenPoint.z, 1);
        Vec4 endPoint = calculateDestinationPoint();
        Ray primaryRay = new Ray(startPoint, endPoint);

        traceRay(mMaxRecursions, primaryRay);

        // 4: set background color
        return finalColor;
    }

    private RgbColor calculateLocalIllumination(Light light, Shape shape){
        return mAmbientColor;
    }

    private Vec4 calculateDestinationPoint(){
        return new Vec4(0,0,0,0);
    }
}
