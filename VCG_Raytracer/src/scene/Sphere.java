package scene;

import raytracer.Intersection;
import raytracer.Ray;
import utils.Vec4;

public class Sphere extends Shape{

    public Sphere(Vec4 pos) {
        super(pos);
    }

    @Override
    public Intersection intersect(Ray ray) {
        return new Intersection();
    }
}
