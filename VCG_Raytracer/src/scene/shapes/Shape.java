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

    public Shape(Vec3 pos, Material mat) {
        super(pos);
        this.material = mat;
        Log.print(this, "Init");
    }

    public abstract Intersection intersect(Ray ray);

    public RgbColor getColor(Light light, Vec3 camPos, Intersection intersection){
        if(intersection != null) {
            if(intersection.getNormal() != null) {
                return this.material.getColor(light, intersection.getNormal(), intersection.getIntersectionPoint(), camPos);
            }
        }
        return this.material.ambient;
    }
}
