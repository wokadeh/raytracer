package raytracer;

import scene.shapes.Shape;
import utils.Vec3;

public class Intersection {

    private Vec3 mIntersectionPoint;

    private Vec3 mNormal;
    private Ray mInRay;
    private Ray mOutRay;

    private Shape mShape;
    private float mDistance;

    private boolean mIncoming;

    private boolean mOutOfDistance;

    private boolean mHit;

    public Intersection(Ray inRay, Shape shape){
        mInRay = inRay;
        mHit = false;
        mShape = shape;
        mOutOfDistance = false;
    }

    private void createOutRay() {
        Vec3 normalN = mNormal.normalize();
        Vec3 directN = mInRay.getDirection().negate().normalize();
        float angle = normalN.scalar(directN);
        Vec3 reflVec = normalN.multScalar(angle).multScalar(2f);
        reflVec = reflVec.sub(directN).normalize();
        mOutRay = new Ray(mIntersectionPoint, reflVec, Float.MAX_VALUE);
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

    public void setDistance(float dist){
        mDistance = dist;
    }

    public void setOutOfDistance(boolean outOfDistance) {
        this.mOutOfDistance = outOfDistance;
    }

    public boolean isOutOfDistance() {
        return mOutOfDistance;
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

    public float getDistance() { return mDistance; }

    public Shape getShape() {
        return mShape;
    }
}
