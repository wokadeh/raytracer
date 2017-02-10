package raytracer;

import utils.algebra.Vec3;

public class Ray {

    private Vec3 mStartPoint;
    private Vec3 mEndPoint;
    private Vec3 mDirection;
    private float mDistance;
    private boolean mIsEntering;

    public Ray(Vec3 startPoint, Vec3 direction, float param, boolean enteringMode){
        init(startPoint, direction, param, enteringMode);
    }

    public Ray(Vec3 startPoint, Vec3 direction, float param){
        init(startPoint, direction, param, true);
    }

    public Ray(Vec3 startPoint, Vec3 endPoint){
        mStartPoint = startPoint;
        mEndPoint = endPoint;
        mDirection = endPoint.sub(startPoint);
        mDistance = mDirection.length();
        mDirection = mDirection.normalize();
        mIsEntering = false;
    }

    private void init(Vec3 startPoint, Vec3 direction, float param, boolean enteringMode){
        mStartPoint = startPoint;
        mDirection = direction.normalize();
        mDistance = param;
        mEndPoint = mDirection.multScalar(param).add(mStartPoint);
        mIsEntering = enteringMode;
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

    public boolean isEntering() { return mIsEntering; }

    public void setEnteringMode(boolean isEntering) {
        mIsEntering = isEntering;
    }

    @Override
    public String toString(){
        return "Start: " + mStartPoint.toString() + ", Direction: " + mDirection.toString() + ", Distance: " + mDistance;
    }
}
