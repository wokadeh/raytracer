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

import scene.ILight;
import scene.IShape;
import scene.Scene;
import utils.RgbColor;
import utils.Vec3;
import utils.Vec4;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Raytracer {

    BufferedImage mBufferedImage;
    ArrayList mShapeList;
    ArrayList mLightList;

    int mRecursions;

    public Raytracer(Scene scene, List<Vec3> screenPointList, BufferedImage bufferedImage, int recursions){
        mRecursions = recursions;
        mBufferedImage = bufferedImage;
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

    private Vec4 calculateDestinationPoint(Vec3 startPoint){

        // ...
        Vec4 endPoint = new Vec4(0,0,0,0);
        return endPoint;
    }


    private RgbColor sendPrimaryRay(Vec3 screenPoint){
        RgbColor finalColor = new RgbColor(0,0,0);
        Vec4 startPoint = new Vec4(screenPoint.x, screenPoint.y, screenPoint.z, 1);
        Vec4 endPoint = calculateDestinationPoint(screenPoint);
        Ray ray = new Ray(startPoint, endPoint);

        Vec4 deadPoint = new Vec4(-1, -1, -1, -1);

        // 2: Intersection test with all shapes
        for(Object shape : mShapeList){
            Vec4 intersectionPoint = ((IShape)shape).intersect(ray);

            // Was hit
            if(intersectionPoint.equals(deadPoint)){
                // 3a: send secondary ray to the light source
                return sendSecondaryRay(intersectionPoint);

            }
        }

        // 4: set background color
        return finalColor;
    }

    private RgbColor sendSecondaryRay(Vec4 intersectionPoint){
        for(Object light : mLightList) {
            Vec4 endPoint = ((ILight) light).getPosition();
            Ray ray = new Ray(intersectionPoint, endPoint);

        }

        return new RgbColor(0,0,0);
    }
}
