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
    private Ray mInRay;
    private Shape mShape;
    private float mDistanceToIntersection;

    private boolean mHit;

    Matrix4x4 mTransfMatrix;

    private Vec3 mNormalT;
    private Vec3 mNormalB;

    public float getCosTheta() {
        return mCosTheta;
    }

    private float mCosTheta = 1;

    public Intersection(Ray inRay, Shape shape){
        mInRay = inRay;
        mHit = false;
        mShape = shape;
        mDistanceToIntersection = Float.MAX_VALUE;


    }

    private static Vec3 calculateRandomDirection(){
        // --- new idea ---
        // random x value between 0-200 => -100-100
        // random z value between 0-200 => -100-100
        // random y value between 0-100 => 0-100
        // normalise vector
        // calculate transformation T between (0,1,0) and our new vector
        // multiply our normal with transformation T

        Random rand1 = new Random();

        // This way is a lot faster
//        float randX = (rand1.nextInt(2000) - 1000) / 2000f;
//        float randZ = (rand1.nextInt(2000) - 1000) / 2000f;
//        float randY = rand1.nextInt(1000) / 1000f;
//
//        return new Vec3(randX, randY, randZ).normalize();

        // NEW IDEA

        double randNr1 = rand1.nextInt(100) / 100f;
        double randNr2 = rand1.nextInt(100) / 100f;

        float mCosTheta = rand1.nextInt(100) / 100f;

        double sinTheta = Math.sqrt(1 - randNr1 * randNr1);

        double phi = 2 * Math.PI/20f * randNr2;

        float x = (float) (sinTheta * Math.cos(phi));

        float z = (float) (sinTheta * Math.sin(phi));

        return new Vec3(x, mCosTheta, z);
    }

    public Vec3 calculateTransformedRandomEndDirection(Vec3 vecA, Vec3 vecB, Vec3 vecC){
        // calculate random value between 0-90
        // calculate random -1 or 1
        // calculate random rotation between 0-180

        Vec3 randomEndDirection = calculateRandomDirection();

        Vec3 transformedRandomEndDirection = new Vec3(
                randomEndDirection.x * vecB.x + randomEndDirection.y * vecA.x + randomEndDirection.z * vecC.x,
                randomEndDirection.x * vecB.y + randomEndDirection.y * vecA.y + randomEndDirection.z * vecC.y,
                randomEndDirection.x * vecB.z + randomEndDirection.y * vecA.z + randomEndDirection.z * vecC.z).normalize();

        // if mNormal x, y, or z are 0 return mNormal
        return transformedRandomEndDirection;
    }


    public Ray calculateRandomRay(){

        Vec3 transformedRandomEndDirection = calculateTransformedRandomEndDirection(mNormal, mNormalB, mNormalT);

        return new Ray(this.getIntersectionPoint(), transformedRandomEndDirection, Float.MAX_VALUE);
    }

    private CoordinateSystem calculateCoordinateSystem(Vec3 startVec){
        Vec3 vecB, vecC;

        if(Math.abs(startVec.x) > Math.abs(startVec.y)){
            vecB = new Vec3(startVec.z, 0, -startVec.x).normalize();
        }
        else{
            vecB = new Vec3(0, -startVec.z, startVec.y).normalize();
        }

        vecC = startVec.cross(vecB);

        return new CoordinateSystem(startVec, vecB, vecC);
    }

    private void calculateCoordinateSystem(){
        CoordinateSystem coordSys = calculateCoordinateSystem(mNormal);

        mNormalT = coordSys.vecB;
        mNormalB = coordSys.vecC;
    }

//    private Matrix4x4 calculateRandomTransformationMatrix(){
//        Matrix4x4 transfMatrix = new Matrix4x4(); // we will multiply the normal with it
//
//        if(mNormalT == null) this.calculateCoordinateSystem();
//
//        transfMatrix.setValueAt(0,0, mNormalT.x);
//        transfMatrix.setValueAt(0,1, mNormalT.y);
//        transfMatrix.setValueAt(0,2, mNormalT.z);
//
//        transfMatrix.setValueAt(1,0, mNormal.x);
//        transfMatrix.setValueAt(1,1, mNormal.y);
//        transfMatrix.setValueAt(1,2, mNormal.z);
//
//        transfMatrix.setValueAt(2,0, mNormalB.x);
//        transfMatrix.setValueAt(2,1, mNormalB.y);
//        transfMatrix.setValueAt(2,2, mNormalB.z);
//
//        return transfMatrix;
//    }

    public Ray calculateReflectionRay(boolean useBlurryRef) {
        // like in Phong, but take vector opposite to incoming direction
        Vec3 directN = mInRay.getDirection();

        if(useBlurryRef){
            CoordinateSystem coordSys = calculateCoordinateSystem(directN);

            Vec3 directNB = coordSys.vecB;
            Vec3 directNC = coordSys.vecC;

            directN = calculateTransformedRandomEndDirection(directN, directNB, directNC);
        }

        return calculateReflectionRay(directN );
    }

    public Ray calculateReflectionRay(Vec3 inDir ) {
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

        float n = (rayIsEnteringMedium == true) ? this.calculateMaterialCoeff(n1, n2) : this.calculateMaterialCoeff(n2, n1);
        rayIsEnteringMedium = !rayIsEnteringMedium;

        float cosBeta = 1 - ( (n * n) * (1 - normDotIn * normDotIn));

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
            return this.calculateReflectionRay(inRay);
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

        this.calculateCoordinateSystem();
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

    class CoordinateSystem{
        public Vec3 vecA;
        public Vec3 vecB;
        public Vec3 vecC;
        CoordinateSystem(Vec3 vecA, Vec3 vecB, Vec3 vecC){
            this.vecA = vecA;
            this.vecB = vecB;
            this.vecC = vecC;
        }
    }
}
