package raytracer;

import utils.Vec3;

public class Ray {

    public Vec3 getStartPoint() {
        return mStartPoint;
    }

    public Vec3 getDirection() {
        return mDirection;
    }

    private Vec3 mStartPoint;
    private Vec3 mEndPoint;
    private Vec3 mDirection;

    public Ray(Vec3 startPoint, Vec3 endPoint){
        mStartPoint = startPoint;
        mEndPoint = endPoint;
        mDirection = mEndPoint.sub(mStartPoint);
    }

    public Ray(Vec3 startPoint, Vec3 direction, float param){
        mStartPoint = startPoint;
        mDirection = direction;
        mEndPoint = mDirection.multScalar(param);
    }
}
