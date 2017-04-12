package raytracer;

import scene.shapes.Shape;
import utils.algebra.Matrix4x4;
import utils.algebra.Vec3;
import utils.algebra.Vec4;
import utils.io.Log;

import java.util.Random;

public class Intersection {

    private Vec3 mIntersectionPoint;

    private Vec3 mNormal;
    private Vec3 mXVec;
    private Vec3 mZVec;
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

    private void calculateXZVectors(){
        // xNormalVec must be perpendicular to mNormal
        if( mNormal.z < mNormal.x ) {
            mXVec = new Vec3(mNormal.y, -mNormal.x, 0);
        }
        else{
            mXVec = new Vec3(0, -mNormal.z, mNormal.x);
        }

        mZVec = mXVec.cross(mNormal).normalize();
    }

    public Ray calculateRandomRay(){
        Random rand1 = new Random();
        Random rand2 = new Random();
        Random rand3 = new Random();

        // calculate random value between 0-90
        // calculate random -1 or 1
        // calculate random rotation between 0-180

        Vec3 startPoint = getIntersectionPoint();



        // --- new idea ---
        // random x value between 0-200 => -100-100
        // random z value between 0-200 => -100-100
        // random y value between 0-100 => 0-100
        // normalise vector
        // calculate transformation T between (0,1,0) and our new vector
        // multiply our normal with transformation T

        float randX = (rand1.nextInt(200) - 100) / 200f;
        float randZ = (rand2.nextInt(200) - 100) / 200f;
        float randY = rand3.nextInt(100) / 100f;

        Vec4 randomEndDirection = new Vec4(randX, randY, randZ, 0f).normalize();

        Matrix4x4 transfMatrix = calculateRandomTransformationMatrix(mNormal, mZVec, mXVec).translateXYZ(mIntersectionPoint);

        Vec4 transformedRandomEndDirection = transfMatrix.multVec3(randomEndDirection);

        Ray outRay = new Ray(startPoint, new Vec3(transformedRandomEndDirection.x, transformedRandomEndDirection.y, transformedRandomEndDirection.z).normalize());

        // if mNormal x, y, or z are 0 return mNormal

        return outRay;
    }

    private Matrix4x4 calculateRandomTransformationMatrix(Vec3 u, Vec3 v, Vec3 s){
        Matrix4x4 transfMatrix = new Matrix4x4(); // we will multiply the normal with it

        // first row of transformation matrix
        transfMatrix.setValueAt(0, 0, s.x);
        transfMatrix.setValueAt(0, 1, s.y);
        transfMatrix.setValueAt(0, 2, s.z);

        // second row of transformation matrix
        transfMatrix.setValueAt(1, 0, u.x);
        transfMatrix.setValueAt(1, 1, u.y);
        transfMatrix.setValueAt(1, 2, u.z);

        // third row of transformation matrix
        transfMatrix.setValueAt(2, 0, v.x);
        transfMatrix.setValueAt(2, 1, v.y);
        transfMatrix.setValueAt(2, 2, v.z);

        return transfMatrix;
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
        this.calculateXZVectors();
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
