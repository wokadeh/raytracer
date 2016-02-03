package scene;

import raytracer.Intersection;
import raytracer.Ray;
import utils.Vec3;
import utils.Vec4;

public class Shape extends SceneObject{

    private Intersection mIntersection;

    public Shape(Vec3 pos) {
        super(pos);
    }

    public Intersection intersect(Ray ray){

        return mIntersection;
    };
}
