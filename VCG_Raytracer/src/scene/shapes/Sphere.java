package scene.shapes;

import raytracer.Intersection;
import raytracer.Ray;
import scene.materials.Material;
import utils.Log;
import utils.Matrix4;
import utils.Vec3;

public class Sphere extends Shape {

    private float mSqrRadius;
    private float mRadius;
    private final float EPSILON = 0.00001f;

    public Sphere(Vec3 pos, Material mat, float radius) {
        super(pos, mat, new Matrix4().translate(pos).scale(radius), "SPHERE_" + pos.toString());

        mRadius = radius;
        mSqrRadius = mRadius * mRadius;
    }

    @Override
    public Intersection intersect(Ray ray) {
        Intersection emptyIntersectionTest = new Intersection(ray, this);

        // Transformation of the center of the sphere to the origin (to LCS)
        Vec3 localOrigin = this.invTransformation.multVec3( ray.getStartPoint(), true );
        Vec3 localDirection = this.invTransformation.multVec3( ray.getDirection(), false ).normalize();

        // B = 2(x0xd + y0yd + z0zd)
        float compB = 2 * localOrigin.scalar( localDirection );

        // C = x0^2 + y0^2 + z0^2 - r^2
        float compC = localOrigin.scalar( localOrigin ) - mSqrRadius;

    //    float p = compB * invA;
    //    float q = compB * invA;

        // D = B*B - 4CA
        float discriminant = (compB * compB) - 4 * compC;

        if (discriminant < 0.0f)
            return emptyIntersectionTest;

        discriminant = (float) Math.sqrt(discriminant);
        float t1 = ( -compB - discriminant ) / (2f);
        float t2 = ( -compB + discriminant ) / (2f);

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

        Ray localRay = new Ray(localOrigin, localDirection, t);

        return createIntersection(emptyIntersectionTest, t, localRay);
    }

    private Intersection createIntersection(Intersection intersectionTest, float t, Ray localRay){
        // Get intersection point in LCS. The point is somewhere on the ray
        Vec3 intersectionPoint = localRay.getDirection().multScalar( t ).add( localRay.getStartPoint() );

        // Transform intersection point to WCS
        intersectionPoint = this.orgTransformation.multVec3(intersectionPoint, true);

        // The normal does not need to be transformed, it is calculated from the WCS intersection directly
        Vec3 normal = intersectionPoint.sub( this.getPosition() ).multScalar( 1f / mRadius);

        intersectionTest.setIntersectionPoint( intersectionPoint );
        intersectionTest.setNormal( normal );
        intersectionTest.setDistance( t );
        intersectionTest.setHit( true );

        if(intersectionTest.isIncoming() == false){
            Log.error(this, "normal switch");
            intersectionTest.setNormal( intersectionTest.getNormal().multScalar( -1f ));
        }

        return intersectionTest;
    }
}
