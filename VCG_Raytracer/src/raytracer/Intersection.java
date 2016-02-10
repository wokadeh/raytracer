package raytracer;

import scene.Shape;
import utils.Log;
import utils.Vec3;

public class Intersection {

    private Vec3 mIntersectionPoint;

    private Vec3 mNormal;
    private Ray mInRay;
    private Ray mOutRay;
    private Shape mShape;

    private boolean mIncoming;

    private boolean mHit;

    public Intersection(Ray inRay, Shape shape){
        mInRay = inRay;
        mHit = false;
        mShape = shape;
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

    public void setIncoming(boolean mIncoming) {
        this.mIncoming = mIncoming;
    }

    public boolean isHit() {
        return mHit;
    }

    public boolean isIncoming() {
        return mIncoming;
    }

    public Vec3 getIntersectionPoint() {
        return mIntersectionPoint;
    }

    public Ray getOutRay() {
        return mOutRay;
    }

    public Vec3 getNormal() {
        return mNormal;
    }
}
