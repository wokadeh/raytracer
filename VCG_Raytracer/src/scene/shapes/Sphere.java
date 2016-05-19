package scene.shapes;

import raytracer.Intersection;
import raytracer.Ray;
import scene.materials.Material;
import utils.Log;
import utils.Vec3;

public class Sphere extends Shape {

    private float mSqrRadius;

    private float mRadius;

    private final float EPSILON = 0.00001f;

    public Sphere(Vec3 pos, Material mat, float radius) {
        super(pos, mat, "SPHERE_" + pos.toString());

        mRadius = radius;
        mSqrRadius = radius * radius;
    }

    @Override
    public Intersection intersect(Ray ray) {
        Intersection emptyIntersectionTest = new Intersection(ray, this);

        // Transformation, otherwise the sphere is in the origin
        Vec3 position = ray.getStartPoint().sub(this.getPosition());

        // B = 2(x0xd + y0yd + z0zd)
        float compB = 2 * position.scalar( ray.getDirection() );

        // C = x0^2 + y0^2 + z0^2 - r^2
        float compC = position.scalar(position) - mSqrRadius;

        // D = B*B - 4C
        float discriminant = (compB * compB) - 4 * compC;

        if (discriminant < 0.0f)
            return emptyIntersectionTest;

        discriminant = (float) Math.sqrt(discriminant);
        float t1 = ( -compB - discriminant ) / 2f;
        float t2 = ( -compB + discriminant ) / 2f;

        float t = EPSILON;

        if(t2 < EPSILON && t1 < EPSILON){
            t = Math.max(t1, t2);
        }
        if(t2 > EPSILON && t1 > EPSILON){
            t = Math.min(t1,t2);
        }
        if(t2 > EPSILON && t1 < EPSILON){
            t = t2;
        }
        if(t2 < EPSILON && t1 > EPSILON){
            t = t1;
        }
        if( t < EPSILON ){
            return emptyIntersectionTest;
        }

        emptyIntersectionTest.setIncoming( t > EPSILON );

        return createIntersection(emptyIntersectionTest, t, ray);
    }

    private Intersection createIntersection(Intersection intersectionTest, float t, Ray ray){
        intersectionTest.setIntersectionPoint( ray.getDirection().multScalar( t ).add( ray.getStartPoint() ) );
        intersectionTest.setNormal( intersectionTest.getIntersectionPoint().sub( getPosition() ).multScalar( 1f / mRadius) );
        intersectionTest.setDistance( t );
        intersectionTest.setHit( true );

        if(intersectionTest.isIncoming() == false){
            Log.error(this, "normal switch");
            intersectionTest.setNormal( intersectionTest.getNormal().multScalar( -1f ));
        }

        return intersectionTest;
    }
}
