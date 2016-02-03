package scene;

import raytracer.Intersection;
import raytracer.Ray;
import utils.Vec3;
import utils.Vec4;

public interface IShape {
    public Intersection intersect(Ray ray);
}
