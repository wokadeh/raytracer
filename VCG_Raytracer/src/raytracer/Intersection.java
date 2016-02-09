package raytracer;

import utils.Log;
import utils.Vec3;

public class Intersection {


    private Vec3 mIntersectionPoint;
    private Vec3 mObjectPosition;
    private Vec3 mNormal;
    private Ray mInRay;
    private Ray mOutRay;

    private boolean mIncoming;

    private boolean mHit;

    public Intersection(Ray inRay){
        mInRay = inRay;
        mHit = false;
    }

    public Intersection(Ray inRay, Vec3 intersectionPoint, Vec3 normal){
        mInRay = inRay;
        mOutRay = createOutRay();
        mIntersectionPoint = intersectionPoint;
        mNormal = normal;
        mHit = false;
    }

    private Ray createOutRay() {
        Vec3 normalN = mNormal.normalize();
        Vec3 directN = mInRay.getDirection().normalize();
        float angle = normalN.scalar(directN);
        Vec3 reflVec = normalN.multScalar(angle).multScalar(2f);
        reflVec = reflVec.sub(directN);
        mOutRay = new Ray(mIntersectionPoint, reflVec, 50);
        return mOutRay;
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

    public boolean isHit() {
        return mHit;
    }

    public void setHit(boolean mHit) {
        this.mHit = mHit;
    }

    public void setIntersectionPoint(Vec3 mIntersectionPoint) {
        this.mIntersectionPoint = mIntersectionPoint;
    }

    public void setNormal(Vec3 mNormal) {
        this.mNormal = mNormal;
        this.createOutRay();
    }

    public boolean isIncoming() {
        return mIncoming;
    }

    public void setIncoming(boolean mIncoming) {
        this.mIncoming = mIncoming;
    }

}
