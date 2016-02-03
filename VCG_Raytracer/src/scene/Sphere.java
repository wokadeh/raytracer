package scene;

import raytracer.Intersection;
import raytracer.Ray;
import utils.Vec4;

public class Sphere implements IShape{

    public Sphere(){

    }

    @Override
    public Intersection intersect(Ray ray) {
        return new Intersection();
    }
}
