package raytracer;

import scene.shapes.Shape;
import utils.Log;
import utils.Vec3;

public class Intersection {

    private Vec3 mIntersectionPoint;

    private Vec3 mNormal;
    private Ray mInRay;
    private Shape mShape;
    private float mDistanceToIntersection;

    private boolean mIncoming;

    private boolean mHit;

    public Intersection(Ray inRay, Shape shape){
        mInRay = inRay;
        mHit = false;
        mShape = shape;
    }

    public Ray calculateReflectionRay() {
        // like in Phong, but take vector opposite to incoming direction
        Vec3 directN = mInRay.getDirection().negate().normalize();
        return calculateReflectionRay( directN );
    }

    public Ray calculateReflectionRay( Vec3 inDir ) {
        float angle = mNormal.scalar( inDir );

        Vec3 reflVec = mNormal.multScalar(angle).multScalar(2f);
        reflVec = reflVec.sub( inDir ).normalize();

        return new Ray(mIntersectionPoint, reflVec, Float.MAX_VALUE);
    }

    public Ray calculateRefractionRay() {
        Vec3 inRay = mInRay.getDirection();
        float angle = mNormal.scalar(inRay);
        float n = isIncoming() ? mShape.getMaterial().getFractionCoeff() : 1f / mShape.getMaterial().getFractionCoeff();

        if (angle < 0.0f) {
            n = mShape.getSwitchedMaterialCoeff();
        }

        float k = (float) Math.sqrt(1.0f - n * n * (1.0f - angle * angle));

        if (k < 0.0f) { // Total internal reflection
            return calculateReflectionRay( mInRay.getDirection());
        }

        Vec3 temp_a = mNormal.multScalar( n * angle - k );
        Vec3 temp_b =  mInRay.getDirection().multScalar( n );
        return new Ray(mIntersectionPoint, temp_a.sub( temp_b ), Float.MAX_VALUE);
    }

    public void setHit(boolean mHit) {
        this.mHit = mHit;
    }

    public void setIntersectionPoint(Vec3 mIntersectionPoint) {
        this.mIntersectionPoint = mIntersectionPoint;
    }

    public void setNormal(Vec3 mNormal) {
        this.mNormal = mNormal.normalize();
    }

    public void setDistance(float dist){
        mDistanceToIntersection = dist;
    }

    public boolean isOutOfDistance( float distanceToObject ) {
        return (mDistanceToIntersection > distanceToObject);
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

    public Vec3 getNormal() {
        return mNormal;
    }

    public float getDistance() { return mDistanceToIntersection; }

    public Shape getShape() {
        return mShape;
    }
}
