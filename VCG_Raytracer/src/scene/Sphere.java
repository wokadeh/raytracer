package scene;

import raytracer.Intersection;
import raytracer.Ray;
import utils.Log;
import utils.Vec3;

public class Sphere extends Shape{

    private float mSqrRadius;

    public Sphere(Vec3 pos, Material mat, float radius) {
        super(pos, mat);

        mSqrRadius = radius * radius;
    }

    @Override
    public Intersection intersect(Ray ray){
        Intersection intersectionTest = new Intersection(ray, this);

        Vec3 distanceToPos = ray.getStartPoint().sub(this.getPosition());
        float distanceToPosSq = distanceToPos.scalar(distanceToPos);

        if(distanceToPosSq <= mSqrRadius){
            return intersectionTest;
        }

        float compB = 2 * distanceToPos.scalar(ray.getDirection());
        float compC = ray.getStartPoint().scalar(ray.getStartPoint()) - mSqrRadius;

        float discriminant = compB * compB - 4 * compC;

        if( discriminant < 0 ){
            return intersectionTest;
        }

        float t0 = (float) ((- compB - Math.sqrt(discriminant) ) * 0.5f);
        float t1 = (float) ((- compB + Math.sqrt(discriminant) ) * 0.5f);

        Vec3 intersectionPoint = new Vec3(-1,-1,-1);

        if( t0 > 0 ){
            intersectionTest.setHit(true);
            intersectionPoint = ray.getStartPoint().add(ray.getDirection().multScalar(t0));

            intersectionTest.setIncoming(false);
        }
        else if(t1 > 0){
            intersectionTest.setHit(true);
            intersectionPoint = ray.getStartPoint().add(ray.getDirection().multScalar(t1));

            intersectionTest.setIncoming(true);
        }

        intersectionTest.setIntersectionPoint(intersectionPoint);
        intersectionTest.setNormal(intersectionPoint.sub(this.getPosition()));

        return intersectionTest;
    }
}
