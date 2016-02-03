package scene;

import raytracer.Ray;
import utils.Vec4;

public class Plane implements IShape
{
    public Plane(){

    }

    @Override
    public Vec4 intersect(Ray ray) {
        return new Vec4(0,0,0,0);
    }
}
