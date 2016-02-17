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

        mNormal = normal;
    }

    public Vec3 getFacingNormal(int facingDirection){
        switch( facingDirection ){
            case 0 : return new Vec3( 1, 0, 0);
            case 1 : return new Vec3( -1, 0, 0);
            case 2 : return new Vec3( 0, -1, 0);
            case 3 : return new Vec3( 0, 1, 0);
            case 4 : return new Vec3( 0, 0, -1);
            default:
                Log.warn(this, "Plane cannot be created. The facing direction is unclear.");
            return new Vec3( 0, 0 ,0 );
        }
    }

    @Override
    public Intersection intersect(Ray ray) {
        Intersection intersectionTest = new Intersection(ray, this);

        // Pn * D from slides
        float intersectionTestValue = mNormal.scalar(ray.getDirection());

        if( intersectionTestValue < 0 ){
            return intersectionTest;
        } else if( intersectionTestValue > 0 ){
            intersectionTest.setIncoming( true );
            return fillIntersectionInfo(intersectionTest, ray, intersectionTestValue);
        }
        else{
            intersectionTest.setIncoming( false );
            return fillIntersectionInfo(intersectionTest, ray, intersectionTestValue);
        }
    }

    private Intersection fillIntersectionInfo(Intersection intersectionTest, Ray ray, float intersectionTestValue){

        intersectionTest.setHit(true);

        float distanceToOrigin = getPosition().sub(ray.getStartPoint()).scalar(mNormal);

        // Q from slides
        float t = ( distanceToOrigin / intersectionTestValue );

        //Vec3 intersectionPoint = ray.getStartPoint().add( ray.getDirection().multScalar(intersectionTestValue) );
        Vec3 intersectionPoint = ray.getStartPoint().add( ray.getDirection().multScalar(t) );

        intersectionTest.setIntersectionPoint(intersectionPoint);
        intersectionTest.setNormal(mNormal);

        //Log.error(this, intersectionPoint.toString());

        return intersectionTest;
    }
}
