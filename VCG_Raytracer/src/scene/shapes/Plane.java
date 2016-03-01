package scene.shapes;

import raytracer.Intersection;
import raytracer.Ray;
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
    public static int FACING_BACK = 5;

    public Plane(Vec3 pos, Material mat, int facingDirection) {
        super(pos, mat, "PLANE" + facingDirection);

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
            case 5 : return new Vec3( 0, 0, 1);        // FRONT
            default:
                Log.warn(this, "Plane cannot be created. The facing direction is unclear.");
            return new Vec3( 0, 0 ,0 );
        }
    }

    @Override
    public Intersection intersect(Ray ray) {
        Intersection intersectionTest = new Intersection(ray, this);
        // Pn * D from slides
        Vec3 vecToRay = getPosition().sub(ray.getStartPoint());
        float angle = mNormal.scalar(ray.getDirection());
        float t = vecToRay.scalar(mNormal) / angle;

        if (t < 0)
            return intersectionTest;

        return createIntersection(intersectionTest, t, ray);
    }

//    @Override
//    public boolean equals(Shape shape) {
//        return getPosition().equals(shape.getPosition());
//    }

    private Intersection createIntersection(Intersection intersectionTest, float t, Ray ray){
        intersectionTest.setIntersectionPoint(ray.getDirection().multScalar(t).add(ray.getStartPoint()));
        intersectionTest.setNormal(mNormal.normalize());
        intersectionTest.setDistance(t);
        // Count only as hit, if the distance of the ray is higher than the distance to the intersection point
        intersectionTest.setHit(ray.getDistance() > t);
        intersectionTest.setIncoming(true);
        //intersectionTest.setOutOfDistance(ray.getDistance() < t);

        return intersectionTest;
    }
}
