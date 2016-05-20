package raytracer;

import utils.Vec3;
import utils.Vec4;

public class Ray {

    private Vec3 mStartPoint;
    private Vec3 mEndPoint;
    private Vec3 mDirection;
    private float mDistance;

    public Ray(Vec3 startPoint, Vec3 endPoint){
        mStartPoint = startPoint;
        mEndPoint = endPoint;
        mDirection = mEndPoint.sub(mStartPoint).normalize();
        mDistance = mEndPoint.sub(mStartPoint).length();
    }

    public Ray(Vec3 startPoint, Vec3 direction, float param){
        mStartPoint = startPoint;
        mDirection = direction.normalize();
        mEndPoint = mDirection.multScalar(param);
        mDistance = mEndPoint.sub(mStartPoint).length();
    }

    public Vec3 getStartPoint() {
        return mStartPoint;
    }

    public Vec3 getDirection() {
        return mDirection;
    }

    public float getDistance() {
        return mDistance;
    }

    public Vec3 getEndPoint() {
        return mEndPoint;
    }

    @Override
    public String toString(){
        return "Start: " + mStartPoint.toString() + ", End: " + mEndPoint.toString() + ", Direction: " + mDirection.toString() + ", Distance: " + mDistance;
    }
}
