package raytracer;

import utils.Vec3;

public class Ray {

    private Vec3 mStartPoint;
    private Vec3 mEndPoint;
    private Vec3 mDirection;
    private float mDistance;

    public Ray(Vec3 startPoint, Vec3 endPoint){
        mStartPoint = startPoint;
        mEndPoint = endPoint;
        mDirection = mEndPoint.sub(mStartPoint);
        recalculateDistance();
    }

    public Ray(Vec3 startPoint, Vec3 direction, float param){
        mStartPoint = startPoint;
        mDirection = direction;
        mEndPoint = mDirection.multScalar(param);
        recalculateDistance();
    }

    public void recalculateDistance(float distance){
        mDistance = distance;
        recalculateEndPoint();
    }

    public void recalculateEndPoint(){
        mEndPoint = mStartPoint.add( mDirection.multScalar(mDistance) );
    }

    public void recalculateDistance(){
        mDistance = mStartPoint.sub( mEndPoint ).length();
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
