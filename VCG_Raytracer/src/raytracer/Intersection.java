package raytracer;

import utils.Vec4;

public class Intersection {

    private Vec4 mIntersectionPoint;
    private Ray mInRay;
    private Ray mOutRay;

    public Intersection(Ray inRay, Ray outRay, Vec4 intersectionPoint){
        mInRay = inRay;
        mOutRay = outRay;
        mIntersectionPoint = intersectionPoint;
    }

    public Intersection(){
        mInRay = new Ray(new Vec4(0,0,0,0), new Vec4(0,0,0,0));
        mOutRay = new Ray(new Vec4(0,0,0,0), new Vec4(0,0,0,0));
        mIntersectionPoint = new Vec4(0,0,0,0);
    }

    public Ray getInRay(){
        return mInRay;
    }

    public Ray getOutRay(){
        return mOutRay;
    }

    public Vec4 getIntersectionPoint(){
        return mIntersectionPoint;
    }
}
