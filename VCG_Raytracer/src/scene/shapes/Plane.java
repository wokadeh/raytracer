package scene.shapes;

import raytracer.Intersection;
import raytracer.Ray;
import scene.materials.Material;
import utils.io.Log;
import utils.algebra.Matrix4x4;
import utils.algebra.Vec3;

public class Plane extends Shape {

    private Vec3 mNormal;

    public static int FACING_RIGHT = 0;
    public static int FACING_LEFT = 1;
    public static int FACING_DOWN = 2;
    public static int FACING_UP = 3;
    public static int FACING_FRONT = 4;
    public static int FACING_BACK = 5;

    public Plane(Vec3 pos, Material mat, int facingDirection) {
        super(pos, mat, new Matrix4x4().translateXYZ(pos), "PLANE" + facingDirection);

        // The normal of a plane is always the vector coming from the position showing to the center of the scene
        mNormal = getFacingNormal( facingDirection );
    }

    // TODO: Fix plane generation for planes not along the axis!!
    public Plane(Vec3 pos, Material mat, Matrix4x4 transf, Vec3 normal) {
        super(pos, mat, transf, "PLANE");

        mNormal = normal.normalize();
    }

    public Vec3 getNormal(){
        return mNormal;
    }

    public Vec3 getFacingNormal(int facingDirection){
        switch( facingDirection ){
            case 0 : return new Vec3( 1, 0, 0);         // RIGHT
            case 1 : return new Vec3( -1, 0, 0);        // LEFT
            case 2 : return new Vec3( 0, -1, 0);        // DOWN
            case 3 : return new Vec3( 0, 1, 0);         // UP
            case 4 : return new Vec3( 0, 0, -1);        // FRONT
            case 5 : return new Vec3( 0, 0, 1);         // BACK
            default:
                Log.warn(this, "Plane cannot be created. The facing direction is unclear.");
            return new Vec3( 0, 0 ,0 );
        }
    }

    @Override
    public Intersection intersect(Ray ray) {
        Intersection emptyIntersectionTest = new Intersection(ray, this);

        Vec3 localDirection = this.invTransformation.multVec3( ray.getDirection(), false ).normalize();
        Vec3 localOrigin = this.invTransformation.multVec3( ray.getStartPoint(), true );

        // when scaling the normal must be transformed, too!

        // Pn * D from slides
        float angle = mNormal.scalar( localDirection );

        // Don't draw a plane facing the same direction as the camera: both have normal (0,0,-1)
        if( angle >= 0 ){
            return emptyIntersectionTest;
        }

        float t = - ( localOrigin.scalar( mNormal ) / angle );

        // Is from behind
        if (t < 10E-5 ) {
            return emptyIntersectionTest;
        }

        Ray localRay = new Ray( localOrigin, localDirection, t );

        return createIntersection(emptyIntersectionTest, ray, localRay);
    }

    private Intersection createIntersection(Intersection intersectionTest, Ray ray, Ray localRay){
        Vec3 intersectionPoint = this.orgTransformation.multVec3( localRay.getEndPoint(), true );
        intersectionTest.setIntersectionPoint( intersectionPoint );
        intersectionTest.setNormal( mNormal );

        float distance = intersectionPoint.sub( ray.getStartPoint() ).length();
        intersectionTest.setDistance( distance );

        // Count only as hit, if the distance of the ray is higher than the distance to the intersection point
        //intersectionTest.setHit( distance < localRay.getDistance() );
        intersectionTest.setHit( true );

        return intersectionTest;
    }
}
