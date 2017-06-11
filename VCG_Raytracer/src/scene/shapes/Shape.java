package scene.shapes;

import raytracer.Intersection;
import raytracer.Ray;
import scene.materials.Material;
import scene.SceneObject;
import scene.lights.Light;
import scene.materials.Reflection;
import utils.io.Log;
import utils.algebra.Matrix4x4;
import utils.RgbColor;
import utils.algebra.Vec3;

public abstract class Shape extends SceneObject {

    private Material material;
    protected String type;
    protected Matrix4x4 orgTransformation;
    protected Matrix4x4 invTransformation;
    protected Matrix4x4 trpTransformation;

    protected boolean raytraced;

    public Shape(Vec3 pos, Material mat, Matrix4x4 transf, String type) {
        super(pos);
        this.material = mat;
        this.type = type;
        this.raytraced = true;
        this.orgTransformation = transf;
        this.invTransformation = transf.invert();
        this.trpTransformation = transf.transpose();
        Log.print(this, "Init " + this.orgTransformation);
    }

    public Shape(Vec3 pos, Material mat, Matrix4x4 transf, String type, boolean raytraced) {
        this(pos, mat, transf, type);
        this.raytraced = raytraced;
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
        return new RgbColor(0,0,0);
    }

    public RgbColor getColor(Light light, Intersection intersection){
        if(intersection != null) {
            if(intersection.getNormal() != null) {
                return this.material.getColor(light, intersection.getNormal(), intersection.getIntersectionPoint());
            }
        }
        return new RgbColor(0,0,0);
    }

    public Material getMaterial(){
        return this.material;
    }

    public Matrix4x4 getTransformation() { return this.orgTransformation; };

    public float getSwitchedMaterialCoeff(){
        return this.material.getSwitchedFractionCoeff();
    }

    public boolean isDiffuse(){
        return this.material.isType("LAMBERT");
    }

    public boolean isRaytraced() {
        return raytraced;
    }

    public boolean isReflective(){
        return this.material.isReflective();
    }

    public boolean isRefractive(){
        return this.material.isTransparent();
    }

    public Reflection getReflection(){
        return this.material.getReflection();
    }

    public void setRaytraced(boolean raytraced) {
        this.raytraced = raytraced;
    }

    @Override
    public String toString(){
        return this.type;
    }
}
