package raytracer;

import scene.materials.Material;
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

    private Vec3 mNormalT;
    private Vec3 mNormalB;

    public float getCosTheta() {
        return mCosTheta;
    }

    public Ray getInRay() {
        return mInRay;
    }

    public void setInRay(Ray mInRay) {
        this.mInRay = mInRay;
    }

    private float mCosTheta = 1;

    public Intersection(Ray inRay, Shape shape){
        mInRay = inRay;
        mHit = false;
        mShape = shape;
        mDistanceToIntersection = Float.MAX_VALUE;
    }

    private Vec3 calculateRandomValues(){
        Random rand1 = new Random();

        float randNr1 = rand1.nextInt(100) / 100f;
        float randNr2 = rand1.nextInt(100) / 100f;
        float cosTheta = rand1.nextInt(100) / 100f;

        return new Vec3(randNr1, cosTheta, randNr2);
    }

    private Vec3 calculateGaussianValues(){
        Random rand1 = new Random();

        float blurryness = mShape.getReflection().blurryness;

        if (blurryness != 0) {

            float randNr1 = (float) (rand1.nextGaussian() / blurryness);
            float randNr2 = (float) (rand1.nextGaussian() / blurryness);
            float cosTheta = (float) Math.abs(rand1.nextGaussian());

            return new Vec3(randNr1, cosTheta, randNr2);
        }
        return calculateRandomValues();
    }

    private Vec3 calculateRandomDirection(boolean useGaussian){
        // --- new idea ---
        // random x value between 0-200 => -100-100
        // random z value between 0-200 => -100-100
        // random y value between 0-100 => 0-100
        // normalise vector
        // calculate transformation T between (0,1,0) and our new vector
        // multiply our normal with transformation T

        // This way is a lot faster
//        float randX = (rand1.nextInt(2000) - 1000) / 2000f;
//        float randZ = (rand1.nextInt(2000) - 1000) / 2000f;
//        float randY = rand1.nextInt(1000) / 1000f;
//
//        return new Vec3(randX, randY, randZ).normalize();

        // NEW IDEA
        Vec3 randomValues;

        if(useGaussian){
            randomValues = calculateGaussianValues();
        }
        else{
            randomValues = calculateRandomValues();
        }

        return randomValues;

//        double sinTheta = Math.sqrt(1 - randomValues.x * randomValues.x);
//
//        double phi = 2 * Math.PI * randomValues.z;
//
//        float x = (float) (sinTheta * Math.cos(phi));
//
//        float z = (float) (sinTheta * Math.sin(phi));
//
//        return new Vec3(x, randomValues.y, z);
    }

    public Vec3 calculateTransformedRandomEndDirection(Vec3 vecA, Vec3 vecB, Vec3 vecC, boolean useGaussian){
        // calculate random value between 0-90
        // calculate random -1 or 1
        // calculate random rotation between 0-180

        Vec3 randomEndDirection = calculateRandomDirection(useGaussian);

        Vec3 transformedRandomEndDirection = new Vec3(
                randomEndDirection.x * vecB.x + randomEndDirection.y * vecA.x + randomEndDirection.z * vecC.x,
                randomEndDirection.x * vecB.y + randomEndDirection.y * vecA.y + randomEndDirection.z * vecC.y,
                randomEndDirection.x * vecB.z + randomEndDirection.y * vecA.z + randomEndDirection.z * vecC.z).normalize();

        // if mNormal x, y, or z are 0 return mNormal
        return transformedRandomEndDirection;
    }


    public Ray calculateRandomRay(){

        Vec3 transformedRandomEndDirection = calculateTransformedRandomEndDirection(mNormal, mNormalB, mNormalT, false);

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
        CoordinateSystem coordSys = this.calculateCoordinateSystem(mNormal);

        mNormalT = coordSys.vecB;
        mNormalB = coordSys.vecC;
    }


    public Ray calculateReflectionRay() {
        // like in Phong, but take vector opposite to incoming direction
        Vec3 directN = mInRay.getDirection();

        if(mShape.getReflection().blurryness != 0){
            CoordinateSystem coordSys = this.calculateCoordinateSystem(directN);

            Vec3 directNB = coordSys.vecB;
            Vec3 directNC = coordSys.vecC;

            directN = this.calculateTransformedRandomEndDirection(directN, directNB, directNC, true);
        }

        return calculateReflectionRay(directN);
    }

    public Ray calculateReflectionRay(Vec3 inDir, float outMat) {
        Ray refRay = calculateReflectionRay(inDir);
        refRay.setCurrentMaterial(outMat);

        return refRay;
    }

    public Ray calculateReflectionRay(Vec3 inDir ) {
        inDir = inDir.normalize();
        float normDotIn = mNormal.scalar( inDir );

        Vec3 r = inDir.sub(mNormal.multScalar(normDotIn).multScalar(2f));

        return new Ray(mIntersectionPoint, r, Float.MAX_VALUE);
    }

    public float calculateReflectivity(){
        float n1 = mInRay.getCurrentMaterial();
        float n2 = mShape.getMaterial().getFractionCoeff();

        float n = this.calculateMaterialCoeff(n1, n2);

        float normDotIn = -mNormal.scalar(mInRay.getDirection());

        float sinT2 = n * n * (1f - normDotIn * normDotIn);

        if(sinT2 > 1f){
            //Log.error(this, "Total internal reflection");
            return 1f;
        }

        float cosT = (float) Math.sqrt(1f - sinT2);

        float rOrth = (n1 * normDotIn - n2 * cosT) / (n1 * normDotIn + n2 * cosT);
        float rPara = (n2 * normDotIn - n1 * cosT) / (n2 * normDotIn + n1 * cosT);

        return (rOrth * rOrth + rPara * rPara)/2f;
    }

//    public Ray calculateRefractionRay() {
//        Vec3 inRay = mInRay.getDirection().negate().normalize();
//        Vec3 normal = mNormal.normalize();
//
//        float n1 = mInRay.getCurrentMaterial();
//        float n2 = mShape.getMaterial().getFractionCoeff();
//
//        boolean rayIsEnteringObject = mInRay.isEntering();
//
//        float normDotIn = normal.scalar(inRay);
//
//        if(normDotIn < 0.0f){
//            //Log.print(this, "Is minor 0");
//            rayIsEnteringObject = true;
//        }
//
//        float n = (rayIsEnteringObject == false) ? calculateMaterialCoeff(n1, n2) : calculateMaterialCoeff(n2, n1);
//        rayIsEnteringObject = !rayIsEnteringObject;
//
//        float squareCoeff = n * n;
//        float cosBeta = squareCoeff * (1 - normDotIn * normDotIn);
//        cosBeta = 1 - cosBeta;
//
//        if(cosBeta > 0) {
//            Vec3 bVec = normal.multScalar((float) Math.sqrt(cosBeta));
//            Vec3 aVec = normal.multScalar(normDotIn);
//            aVec = aVec.sub(inRay);
//            aVec = aVec.multScalar(n);
//            Vec3 out = aVec.sub(bVec);
//            return new Ray(mIntersectionPoint, out, Float.MAX_VALUE, rayIsEnteringObject, n2);
//        }
//        // Total internal reflection
//        else{
//            //Log.error(this, "Total internal reflection");
//            return calculateReflectionRay(mInRay.getDirection());
//        }
//    }

    public Ray calculateRefractionRay() {
        float n1 = mShape.getMaterial().getFractionCoeff();
        float n2 = mInRay.getCurrentMaterial();

        boolean rayIsEnteringMedium = mInRay.isEntering();

        float normDotIn = -mNormal.scalar(mInRay.getDirection());

        if(normDotIn < 0.0f){
            rayIsEnteringMedium = false;
        }

        float n = (rayIsEnteringMedium == true) ? this.calculateMaterialCoeff(n1, n2) : this.calculateMaterialCoeff(n2, n1);
        rayIsEnteringMedium = !rayIsEnteringMedium;

        float sinT2 = n * n * (1f - normDotIn * normDotIn);

        if(sinT2 > 1f){
            //Log.error(this, "Total internal reflection");
            return this.calculateReflectionRay(mInRay.getDirection(), n);
        }

        float cosT = (float) Math.sqrt(1f - sinT2);

        Vec3 out = mInRay.getDirection().multScalar(n).add(mNormal.multScalar( n * normDotIn - cosT));

        return new Ray(mIntersectionPoint, out, Float.MAX_VALUE, rayIsEnteringMedium, n);
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
