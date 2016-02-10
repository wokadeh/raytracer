package scene;

import raytracer.Intersection;
import raytracer.Ray;
import utils.Log;
import utils.RgbColor;
import utils.Vec3;
import utils.Vec4;

import java.util.ArrayList;

public abstract class Shape extends SceneObject{

    private Intersection mIntersection;
    private Material mMaterial;

    public Shape(Vec3 pos, Material mat) {
        super(pos);
        mMaterial = mat;
        Log.print(this, "Init");
    }

    public abstract Intersection intersect(Ray ray);

    public RgbColor getColor(Light light, Vec3 camPos){
        if(mIntersection != null) {
            return mMaterial.getColor(light, mIntersection.getNormal(), mIntersection.getIntersectionPoint(), camPos);
        }
        return new RgbColor(0,0,0);
    }
}
