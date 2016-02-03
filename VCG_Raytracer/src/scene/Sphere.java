package scene;

import raytracer.Intersection;
import raytracer.Ray;
import utils.Vec3;

public class Sphere extends Shape{

    public Sphere(Vec3 pos) {
        super(pos);
    }

    @Override
    public Intersection intersect(Ray ray) {
        return new Intersection();
    }
}
