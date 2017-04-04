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
        Vec3 directN = mInRay.getDirection();
        return calculateReflectionRay( directN );
    }

    public Ray calculateReflectionRay( Vec3 inDir ) {
        inDir = inDir.normalize();
        float normDotIn = mNormal.scalar( inDir );

        Vec3 r = inDir.sub(mNormal.multScalar(normDotIn).multScalar(2f));

        return new Ray(mIntersectionPoint, r, Float.MAX_VALUE);
    }

    public Ray calculateRefractionRay() {
        Vec3 inRay = mInRay.getDirection().negate().normalize();

        float n1 = mInRay.getCurrentMaterial();
        float n2 = mShape.getMaterial().getFractionCoeff();

        boolean rayIsEnteringMedium = mInRay.isEntering();

        float normDotIn = mNormal.scalar(inRay);

        if(normDotIn < 0.0f){
            rayIsEnteringMedium = false;
        }

        float n = (rayIsEnteringMedium == true) ? calculateMaterialCoeff(n1, n2) : calculateMaterialCoeff(n2, n1);
        rayIsEnteringMedium = !rayIsEnteringMedium;

        float squareCoeff = n * n;
        float cosBeta = squareCoeff * (1 - normDotIn * normDotIn);
        cosBeta = 1 - cosBeta;

        if(cosBeta > 0) {
            Vec3 bVec = mNormal.multScalar((float) Math.sqrt(cosBeta));
            Vec3 aVec = mNormal.multScalar(normDotIn);
            aVec = aVec.sub(inRay);
            aVec = aVec.multScalar(n);
            Vec3 out = aVec.sub(bVec);
            return new Ray(mIntersectionPoint, out, Float.MAX_VALUE, rayIsEnteringMedium, n2);
        }
        // Total internal reflection
        else{
            //Log.error(this, "Total internal reflection");
            return calculateReflectionRay(mInRay.getDirection());
        }
    }

    protected float calculateMaterialCoeff(float n1, float n2){
        if (n2 != 0) {
            return n1 /n2 ;
        }
        else{
            return 0;
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
