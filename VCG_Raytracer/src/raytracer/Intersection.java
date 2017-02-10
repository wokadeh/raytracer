package raytracer;

import scene.shapes.Shape;
import utils.algebra.Vec3;
import utils.io.Log;

public class Intersection {

    private Vec3 mIntersectionPoint;

    private Vec3 mNormal;
    private Ray mInRay;
    private Shape mShape;
    private float mDistanceToIntersection;

    private boolean mHit;

    public Intersection(Ray inRay, Shape shape){
        mInRay = inRay;
        mHit = false;
        mShape = shape;
        mDistanceToIntersection = Float.MAX_VALUE;
    }

    public Ray calculateReflectionRay() {
        // like in Phong, but take vector opposite to incoming direction
        Vec3 directN = mInRay.getDirection();//.negate();
        return calculateReflectionRay( directN );
    }

    public Ray calculateReflectionRay( Vec3 inDir ) {
        float normDotIn = mNormal.scalar( inDir.normalize() );

        Vec3 r = inDir.sub(mNormal.multScalar(normDotIn).multScalar(2f));

        return new Ray(mIntersectionPoint, r, Float.MAX_VALUE);
    }

    public Ray calculateRefractionRay() {
        Vec3 inRay = mInRay.getDirection().negate().normalize();
        mNormal = mNormal.normalize();

        boolean rayIsEnteringMedium = mInRay.isEntering();

        float normDotIn = mNormal.scalar(inRay);

        if(normDotIn < 0.0f){
            rayIsEnteringMedium = false;
        }

        float n;

        if (rayIsEnteringMedium == false) {
            Log.error(this, "coeff switch");
            n = mShape.getSwitchedMaterialCoeff();
            rayIsEnteringMedium = true;
        } else {
            n = mShape.getMaterial().getFractionCoeff();
            rayIsEnteringMedium = false;
        }

        float sinBeta2 = n * n;
        float cosBetaDet = 1f - sinBeta2;

        if(cosBetaDet > 0) {
            float cosBeta = inRay.scalar(mNormal);
            float sinSqrBeta = sinBeta2 * (1 - cosBeta * cosBeta);

            float a = n * cosBeta;
            float b = (float) Math.sqrt(1 - sinSqrBeta);

            Vec3 out = inRay.negate().multScalar(n).add(mNormal.multScalar(a - b));
            return new Ray(mIntersectionPoint, out, Float.MAX_VALUE, rayIsEnteringMedium);
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

    public boolean isHit() {
        return mHit;
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
