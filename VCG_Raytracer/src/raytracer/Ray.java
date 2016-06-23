package raytracer;

import utils.algebra.Vec3;

public class Ray {

    private Vec3 mStartPoint;
    private Vec3 mEndPoint;
    private Vec3 mDirection;
    private float mDistance;

    public Ray(Vec3 startPoint, Vec3 direction, float param){
        mStartPoint = startPoint;
        mDirection = direction.normalize();
        mDistance = param;
        mEndPoint = mDirection.multScalar(param).add(mStartPoint);
    }

    public Ray(Vec3 startPoint, Vec3 endPoint){
        mStartPoint = startPoint;
        mEndPoint = endPoint;
        mDirection = endPoint.sub(startPoint);
        mDistance = mDirection.length();
        mDirection = mDirection.normalize();
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

    @Override
    public String toString(){
        return "Start: " + mStartPoint.toString() + ", Direction: " + mDirection.toString() + ", Distance: " + mDistance;
    }
}
