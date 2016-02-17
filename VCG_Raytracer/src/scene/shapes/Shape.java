package scene.shapes;

import raytracer.Intersection;
import raytracer.Ray;
import scene.materials.Material;
import scene.SceneObject;
import scene.lights.Light;
import utils.Log;
import utils.RgbColor;
import utils.Vec3;

public abstract class Shape extends SceneObject {

    private Material material;
    protected String type;

    public Shape(Vec3 pos, Material mat, String type) {
        super(pos);
        this.material = mat;
        this.type = type;
        Log.print(this, "Init");
    }

    public abstract Intersection intersect(Ray ray);

    public String isType() {
        return this.type;
    }

    public RgbColor getColor(Light light, Vec3 camPos, Intersection intersection){
        if(intersection != null) {
            if(intersection.getNormal() != null) {
                return this.material.getColor(light, intersection.getNormal(), intersection.getIntersectionPoint(), camPos);
            }
        }
        return this.material.ambient;
    }
}
