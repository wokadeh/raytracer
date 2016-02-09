package scene;

import raytracer.Intersection;
import raytracer.Ray;
import utils.Log;
import utils.RgbColor;
import utils.Vec3;
import utils.Vec4;

public class Shape extends SceneObject{

    private Intersection mIntersection;
    private Material mMaterial;

    public Shape(Vec3 pos) {
        super(pos);
        Log.print(this, "Init");
    }

    public Intersection intersect(Ray ray){
        return mIntersection;
    }

    public RgbColor getColor(){
        return new RgbColor(0,0,1);
    }
}
