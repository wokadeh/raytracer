package raytracer;

import scene.shapes.Shape;
import utils.algebra.Matrix4x4;
import utils.algebra.Vec3;
import utils.algebra.Vec4;

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

    private static Vec3 calculateRandomDirection(){
        // --- new idea ---
        // random x value between 0-200 => -100-100
        // random z value between 0-200 => -100-100
        // random y value between 0-100 => 0-100
        // normalise vector
        // calculate transformation T between (0,1,0) and our new vector
        // multiply our normal with transformation T

        Random rand1 = new Random();

//        float randX = (rand1.nextInt(200) - 100) / 200f;
//        float randZ = (rand1.nextInt(200) - 100) / 200f;
//        float randY = rand1.nextInt(100) / 100f;
//
//        return new Vec3(randX, randY, randZ).normalize();

        // NEW IDEA

        double randNr1 = rand1.nextInt(100) / 100f;
        double randNr2 = rand1.nextInt(100) / 100f;

        double randNry = rand1.nextInt(100) / 100f;

        double sinTheta = Math.sqrt(1 - randNr1 * randNr1);

        double phi = 2 * Math.PI * randNr2;

        float x = (float) (sinTheta * Math.cos(phi));

        float z = (float) (sinTheta * Math.sin(phi));

        return new Vec3(x, (float) randNry, z);
    }

    public Ray calculateRandomRay(){
        // calculate random value between 0-90
        // calculate random -1 or 1
        // calculate random rotation between 0-180

        Vec3 startPoint = this.getIntersectionPoint();

        if(Float.isNaN(mNormalT.x)) this.calculateCoordinateSystem();

        Vec3 randomEndDirection = calculateRandomDirection();

        //Vec4 transformedRandomEndDirection = mTransfMatrix.multVec3( new Vec4(randomEndDirection.x, randomEndDirection.y, randomEndDirection.z, 0f));

        Vec3 transformedRandomEndDirection = new Vec3(
                randomEndDirection.x * mNormalB.x + randomEndDirection.y * mNormal.x + randomEndDirection.z * mNormalT.x,
                randomEndDirection.x * mNormalB.y + randomEndDirection.y * mNormal.y + randomEndDirection.z * mNormalT.y,
                randomEndDirection.x * mNormalB.z + randomEndDirection.y * mNormal.z + randomEndDirection.z * mNormalT.z);

        transformedRandomEndDirection = transformedRandomEndDirection.normalize();

        Ray outRay = new Ray(startPoint, new Vec3(transformedRandomEndDirection.x, transformedRandomEndDirection.y, transformedRandomEndDirection.z), Float.MAX_VALUE);

        // if mNormal x, y, or z are 0 return mNormal
        return outRay;
    }

    private void calculateCoordinateSystem(){
        if(Math.abs(mNormal.x) > Math.abs(mNormal.y)){
            mNormalT = new Vec3(mNormal.z, 0, -mNormal.x).normalize();
        }
        else{
            mNormalT = new Vec3(0, -mNormal.z, mNormal.y).normalize();
        }

        mNormalB = mNormal.cross(mNormalT);
    }

    private Matrix4x4 calculateRandomTransformationMatrix(){
        Matrix4x4 transfMatrix = new Matrix4x4(); // we will multiply the normal with it

        if(mNormalT == null) this.calculateCoordinateSystem();

        transfMatrix.setValueAt(0,0, mNormalT.x);
        transfMatrix.setValueAt(0,1, mNormalT.y);
        transfMatrix.setValueAt(0,2, mNormalT.z);

        transfMatrix.setValueAt(1,0, mNormal.x);
        transfMatrix.setValueAt(1,1, mNormal.y);
        transfMatrix.setValueAt(1,2, mNormal.z);

        transfMatrix.setValueAt(2,0, mNormalB.x);
        transfMatrix.setValueAt(2,1, mNormalB.y);
        transfMatrix.setValueAt(2,2, mNormalB.z);

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

        this.mTransfMatrix = calculateRandomTransformationMatrix();
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
