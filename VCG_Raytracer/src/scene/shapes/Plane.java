package scene.shapes;

import raytracer.Intersection;
import raytracer.Ray;
import scene.SceneObject;
import scene.materials.Material;
import utils.Log;
import utils.Vec3;

public class Plane extends Shape {

    private Vec3 mNormal;

    public static int FACING_RIGHT = 0;
    public static int FACING_LEFT = 1;
    public static int FACING_DOWN = 2;
    public static int FACING_UP = 3;
    public static int FACING_FRONT = 4;

    public Plane(Vec3 pos, Material mat, int facingDirection) {
        super(pos, mat, "PLANE");

        // The normal of a plane is always the vector coming from the position showing to the center of the scene
        mNormal = getFacingNormal(facingDirection);
    }

    public Plane(Vec3 pos, Material mat, Vec3 normal) {
        super(pos, mat, "PLANE");

        mNormal = normal.normalize();
    }

    public Vec3 getFacingNormal(int facingDirection){
        switch( facingDirection ){
            case 0 : return new Vec3( 1, 0, 0);         // RIGHT
            case 1 : return new Vec3( -1, 0, 0);        // LEFT
            case 2 : return new Vec3( 0, -1, 0);        // DOWN
            case 3 : return new Vec3( 0, 1, 0);         // UP
            case 4 : return new Vec3( 0, 0, -1);        // FRONT
            default:
                Log.warn(this, "Plane cannot be created. The facing direction is unclear.");
            return new Vec3( 0, 0 ,0 );
        }
    }

    @Override
    public Intersection intersect(Ray ray) {
        // Pn * D from slides
        float intersectionTestValue = mNormal.scalar(ray.getDirection());

        if( intersectionTestValue < 0 ){
            return new Intersection(ray, this);
        } else{
            return fillIntersectionInfo(ray, intersectionTestValue);
        }
    }

    private Intersection fillIntersectionInfo(Ray ray, float intersectionTestValue){

        Intersection intersectionTest = new Intersection(ray, this);
        intersectionTest.setHit(true);
        intersectionTest.setIncoming( true );

        float distanceToOrigin = getPosition().sub(ray.getStartPoint()).scalar(mNormal);

        // Q from slides
        float t = ( distanceToOrigin / intersectionTestValue );

        Vec3 intersectionPoint = ray.getStartPoint().add( ray.getDirection().multScalar(t) );

        intersectionTest.setIntersectionPoint(intersectionPoint);
        intersectionTest.setNormal(mNormal);

        return intersectionTest;
    }
}
