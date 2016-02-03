package scene;

import raytracer.Ray;
import utils.Vec4;

public interface IShape {
    public Vec4 intersect(Ray ray);
}
