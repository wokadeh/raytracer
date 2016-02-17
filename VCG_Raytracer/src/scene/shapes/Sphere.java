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
        Intersection intersectionTest = new Intersection(ray, this);

        Vec3 distanceToPos = ray.getStartPoint().sub(this.getPosition());
        float distanceToPosSq = distanceToPos.scalar(distanceToPos);

        if( distanceToPosSq <= mSqrRadius || mRadius == 0 ){
            return intersectionTest;
        }

        float compB = 2 * distanceToPos.scalar(ray.getDirection());
        float compC = ray.getStartPoint().scalar(ray.getStartPoint()) - mSqrRadius;

        float discriminant = compB * compB - 4 * compC;

        if( discriminant < 0 ){
            return intersectionTest;
        }

        return fillIntersectionInfo(intersectionTest, discriminant, ray, compB);
    }

    private Intersection fillIntersectionInfo(Intersection intersectionTest, float discriminant, Ray ray, float compB){
        float t0 = (float) ((- compB - Math.sqrt(discriminant) ) * 0.5f);
        float t1 = (float) ((- compB + Math.sqrt(discriminant) ) * 0.5f);

        Vec3 intersectionPoint = new Vec3(-1,-1,-1);

        if( t0 > 0 ){
            intersectionTest.setHit(true);
            intersectionPoint = ray.getStartPoint().add(ray.getDirection().multScalar(t0));

            intersectionTest.setIncoming(true);
        }
        else if(t1 > 0){
            intersectionTest.setHit(true);
            intersectionPoint = ray.getStartPoint().add(ray.getDirection().multScalar(t1));

            intersectionTest.setIncoming(false);
        }

        intersectionTest.setIntersectionPoint(intersectionPoint);
        intersectionTest.setNormal(intersectionPoint.sub(this.getPosition()).multScalar( 1f / mRadius ));

        return intersectionTest;
    }
}
