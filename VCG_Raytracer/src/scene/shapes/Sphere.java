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
    public Intersection intersect(Ray ray) {
        Intersection emptyIntersectionTest = new Intersection(ray, this);

        // Transformation, otherwise the sphere is in the origin
        Vec3 position = ray.getStartPoint().sub(this.getPosition());

        // B = 2(x0xd + y0yd + z0zd)
        float compB = -2f * position.scalar( ray.getDirection() );

        // C = x0^2 + y0^2 + z0^2 - r^2
        float compC = position.scalar(position) - mSqrRadius;

        float discriminant = (compB * compB) - 4 * compC;

        if (discriminant < 0.0f)
            return emptyIntersectionTest;

        discriminant = (float) Math.sqrt(discriminant);
        float t0 = compB - discriminant;
        float t1 = compB + discriminant;

        float t;

        if (t0 < 0 && t1 < 0) {
            return emptyIntersectionTest;
        }
        else if (t0 < 0){
            t = t1;
        }
        else if (t1 < 0){
            t = t0;
        }
        else if ((t0 * t0) < (t1 * t1)){
            t = t0;
        }
        else {
            t = t1;
        }

        return createIntersection(emptyIntersectionTest, t, ray);
    }

    private Intersection createIntersection(Intersection intersectionTest, float t, Ray ray){
        intersectionTest.setIntersectionPoint( ray.getDirection().multScalar( t ).add( ray.getStartPoint() ) );
        intersectionTest.setNormal( intersectionTest.getIntersectionPoint().sub( getPosition() ).multScalar( 1f / mRadius) );
        intersectionTest.setDistance( t );
        intersectionTest.setHit( true );
        intersectionTest.setIncoming( true );

        return intersectionTest;
    }
}
