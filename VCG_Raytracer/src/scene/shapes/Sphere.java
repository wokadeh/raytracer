package scene.shapes;

import raytracer.Intersection;
import raytracer.Ray;
import scene.materials.Material;
import utils.Vec3;

public class Sphere extends Shape {

    private float mSqrRadius;
    private float mRadius;

    public Sphere(Vec3 pos, Material mat, float radius) {
        super(pos, mat, "SPHERE");

        mRadius = radius;
        mSqrRadius = radius * radius;
    }

    @Override
    public Intersection intersect(Ray ray){
        Vec3 distanceToPos = ray.getStartPoint().sub(this.getPosition());
        float distanceToPosSq = distanceToPos.scalar(distanceToPos);

        if( distanceToPosSq <= mSqrRadius || mRadius == 0 ){
            return null;
        }

        float compB = 2 * distanceToPos.scalar(ray.getDirection());
        float compC = ray.getStartPoint().scalar(ray.getStartPoint()) - mSqrRadius;

        float discriminant = compB * compB - 4 * compC;

        if( discriminant < 0 ){
            return new Intersection(ray, this);
        }

        return fillIntersectionInfo(discriminant, ray, compB);
    }

    private Intersection fillIntersectionInfo(float discriminant, Ray ray, float compB){
        float t0 = (float) ((- compB - Math.sqrt(discriminant) ) * 0.5f);
        float t1 = (float) ((- compB + Math.sqrt(discriminant) ) * 0.5f);

        // The smaller positive t is the closest intersection point
        if( t0 > 0 ){
            return createIntersection(true, t0, ray);
        }
        else if(t1 > 0){
            return createIntersection(false, t1, ray);
        }

        return new Intersection(ray, this);
    }

    private Intersection createIntersection(boolean isIncoming, float t, Ray ray){
        Intersection intersectionTest = new Intersection(ray, this);

        intersectionTest.setHit(true);
        Vec3 intersectionPoint = ray.getStartPoint().add(ray.getDirection().multScalar(t));

        intersectionTest.setIncoming(isIncoming);
        intersectionTest.setDistance(Math.abs(t));
        intersectionTest.setIntersectionPoint(intersectionPoint);
        intersectionTest.setNormal((intersectionPoint.sub(this.getPosition())).multScalar( 1f / mRadius ));

        return intersectionTest;
    }
}
