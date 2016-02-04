package scene;

import raytracer.Intersection;
import raytracer.Ray;
import utils.Vec3;

public class Sphere extends Shape{

    float mRadius;

    public Sphere(Vec3 pos, float radius) {
        super(pos);

        mRadius = radius;
    }

    @Override
    public Intersection intersect(Ray ray){
        float compB = ray.getStartPoint().scalar(ray.getDirection());
        float compC = ray.getDirection().x * ray.getDirection().x
                    + ray.getDirection().y * ray.getDirection().y
                    + ray.getDirection().z * ray.getDirection().z
                    - mRadius * mRadius;

        float discrimant = compB * compB - 4 * compC;

        if(discrimant < 0){
            return null;
        }

        float t0 = (float) ((- compB - Math.sqrt(discrimant) )/ 2f);
        float t1 = (float) ((- compB + Math.sqrt(discrimant) )/ 2f);

        return null;
    }
}
