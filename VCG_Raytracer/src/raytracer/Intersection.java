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
        mNormal = mNormal.normalize();
        Vec3 reflVec = mNormal.multScalar(angle).multScalar(2f);
        reflVec = reflVec.sub( inDir ).normalize();

        return new Ray(mIntersectionPoint, reflVec, Float.MAX_VALUE);
    }

    public Ray calculateRefractionRay() {
        Vec3 inRay = mInRay.getDirection().negate().normalize();
        mNormal = mNormal.normalize();

        float normDotIn = mNormal.scalar(inRay);
        float n = mShape.getMaterial().getFractionCoeff();

        if (normDotIn < 0.0f) {
            n = mShape.getSwitchedMaterialCoeff();
        }

        float sinBeta2 = n * n;
        float cosBetaDet = 1f - sinBeta2;

        if(cosBetaDet > 0) {
            //float cosBeta = (float) Math.sqrt(cosBetaDet);
            float cosBeta = (float) Math.sqrt(1.0f - sinBeta2 * (1.0f - normDotIn * normDotIn));
            //Vec3 refDir = (mNormal.multScalar(normDotIn)).multScalar(n).sub(inRay).sub(mNormal.multScalar(nCosBeta));

            //Ray refRay = new Ray(mIntersectionPoint.add(refDir.multScalar(0.001f)), refDir, Float.MAX_VALUE);

            Vec3 temp_b = mNormal.multScalar(cosBeta);
            Vec3 temp_a = temp_b.negate().multScalar(cosBeta).sub(inRay).multScalar(n);
            Vec3 refRay = temp_a.sub(temp_b);
            return new Ray(mIntersectionPoint, refRay, Float.MAX_VALUE);
            //return refRay;
        }
        // Total internal reflection
        else{
            return calculateReflectionRay(mInRay.getDirection());
        }
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
