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

        if (distanceToPosSq <= mRadius)
            return intersectionTest;

        return fillIntersectionInfo(ray, distanceToPosSq, intersectionTest, distanceToPos);
    }

    private Intersection fillIntersectionInfo(Ray ray, float distanceToPosSq, Intersection intersectionTest, Vec3 distanceToPos) {
        float t;
        float compB = distanceToPos.negate().scalar(ray.getDirection());
        float compC = (compB * compB) - distanceToPosSq + (mRadius * mRadius);

        if (compC < 0.0f)
            return intersectionTest;

        compC = (float) Math.sqrt(compC);
        float t0 = compB - compC;
        float t1 = compB + compC;

        if (t0 > 0 && t1 > 0)
            t = t0;
        else if (t0 < 0.0f && t1 < 0)
            return intersectionTest;
        else if (t0 < 0.0f && t1 > 0)
            t = t1;
        else if (t0 == t1)
            t = t0;
        else
            return intersectionTest;

        return createIntersection(intersectionTest, t, ray);
    }

    private Intersection createIntersection(Intersection intersectionTest, float t, Ray ray){
        intersectionTest.setIntersectionPoint(ray.getDirection().multScalar(t).add(ray.getStartPoint()));
        intersectionTest.setNormal(intersectionTest.getIntersectionPoint().sub(getPosition().multScalar( 1f / mRadius)));
        intersectionTest.setDistance(t);
        intersectionTest.setHit(true);
        intersectionTest.setIncoming(true);

        return intersectionTest;
    }
}
