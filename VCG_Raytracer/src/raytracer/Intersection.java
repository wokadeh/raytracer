package raytracer;

import utils.Vec3;

public class Intersection {

    private Vec3 mIntersectionPoint;
    private Ray mInRay;
    private Ray mOutRay;

    public Intersection(Ray inRay, Ray outRay, Vec3 intersectionPoint){
        mInRay = inRay;
        mOutRay = outRay;
        mIntersectionPoint = intersectionPoint;
    }

    public Intersection(){
        mInRay = new Ray(new Vec3(0,0,0), new Vec3(0,0,0));
        mOutRay = new Ray(new Vec3(0,0,0), new Vec3(0,0,0));
        mIntersectionPoint = new Vec3(0,0,0);
    }

    public Ray getInRay(){
        return mInRay;
    }

    public Ray getOutRay(){
        return mOutRay;
    }

    public Vec3 getIntersectionPoint(){
        return mIntersectionPoint;
    }
}
