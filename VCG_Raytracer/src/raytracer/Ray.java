package raytracer;

import utils.Matrix;
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

    public Ray transform(Matrix mat){
        Vec4 start = new Vec4(mStartPoint.x, mStartPoint.y, mStartPoint.z, 1);
        Vec4 direction = new Vec4(mDirection.x, mDirection.y, mDirection.z, 0);

        Vec4 transformedStart = start.multMatrix(mat);
        Vec4 transformedDir = direction.multMatrix(mat);
        transformedDir.w = 0;
        transformedDir.normalize();

        Ray transformedRay = new Ray(new Vec3(transformedStart.x, transformedStart.y, transformedStart.z), new Vec3(transformedDir.x, transformedDir.y, transformedDir.z), 1 );

        return transformedRay;
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
