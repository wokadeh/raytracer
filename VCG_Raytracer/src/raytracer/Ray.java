package raytracer;

import scene.materials.Material;
import utils.algebra.Vec3;

public class Ray {

    private Vec3 mStartPoint;
    private Vec3 mEndPoint;
    private Vec3 mDirection;
    private float mDistance;
    private boolean mIsEntering;

    private float mCurrentMaterialCoeff;

    public Ray(Vec3 startPoint, Vec3 direction, float param, boolean enteringMode){
        init(startPoint, direction, param, enteringMode);
    }

    public Ray(Vec3 startPoint, Vec3 direction, float param, boolean enteringMode, float materialCoeff){
        init(startPoint, direction, param, enteringMode, materialCoeff);
    }

    public Ray(Vec3 startPoint, Vec3 direction, float param){
        init(startPoint, direction, param, true);
    }

    public Ray(Vec3 startPoint, Vec3 endPoint, boolean enteringMode){
        mEndPoint = endPoint;
        mDirection = endPoint.sub(startPoint).normalize();
        init(startPoint, endPoint.sub(startPoint), mDirection.length(), enteringMode);
    }

    private void init(Vec3 startPoint, Vec3 direction, float param, boolean enteringMode){
        mStartPoint = startPoint;
        mDirection = direction.normalize();
        mDistance = param;
        mEndPoint = mDirection.multScalar(param).add(mStartPoint);
        mIsEntering = enteringMode;
        mCurrentMaterialCoeff = Material.AIR;
    }

    private void init(Vec3 startPoint, Vec3 direction, float param, boolean enteringMode, float materialCoeff){
        init(startPoint, direction, param, enteringMode);
        mCurrentMaterialCoeff = materialCoeff;
    }

    public Vec3 getStartPoint() {
        return mStartPoint;
    }

    public Vec3 getEndPoint() {
        return mEndPoint;
    }

    public Vec3 getDirection() {
        return mDirection;
    }

    public float getDistance() {
        return mDistance;
    }

    public float getCurrentMaterial() {
        return mCurrentMaterialCoeff;
    }

    public boolean isEntering() { return mIsEntering; }

    public void setEnteringMode(boolean isEntering) {
        mIsEntering = isEntering;
    }

    public void setCurrentMaterial(float mCurrentMaterial) {
        this.mCurrentMaterialCoeff = mCurrentMaterial;
    }

    @Override
    public String toString(){
        return "Start: " + mStartPoint.toString() + ", Direction: " + mDirection.toString() + ", Distance: " + mDistance;
    }
}
