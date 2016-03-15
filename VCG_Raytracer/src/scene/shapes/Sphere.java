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
        Intersection emptyIntersectionTest = new Intersection(ray, this);
        Vec3 distanceToPos = ray.getStartPoint().sub(this.getPosition());
        float distanceToPosSq = distanceToPos.scalar(distanceToPos);

        if (distanceToPosSq <= mRadius)
            return emptyIntersectionTest;

        return fillIntersectionInfo(ray, distanceToPosSq, emptyIntersectionTest, distanceToPos);
    }

    private Intersection fillIntersectionInfo(Ray ray, float distanceToPosSq, Intersection emptyIntersectionTest, Vec3 distanceToPos) {
        float t;
        float compB = -distanceToPos.scalar(ray.getDirection());
        float discriminant = (compB * compB) - distanceToPosSq + mSqrRadius;

        if (discriminant < 0.0f)
            return emptyIntersectionTest;

        discriminant = (float) Math.sqrt(discriminant);
        float t0 = compB - discriminant;
        float t1 = compB + discriminant;

        if (t0 > 0 && t1 > 0)
            t = t0;
        else if (t0 < 0.0f && t1 > 0)
            t = t1;
        else if (t0 == t1)
            t = t0;
        else
            return emptyIntersectionTest;

        return createIntersection(emptyIntersectionTest, t, ray);
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
