package scene;

import raytracer.Intersection;
import raytracer.Ray;
import utils.Vec4;

public class Plane implements IShape
{
    public Plane(){

    }

    @Override
    public Intersection intersect(Ray ray) {
        return new Intersection();
    }
}
