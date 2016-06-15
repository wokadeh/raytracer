package scene.shapes;

import raytracer.Intersection;
import raytracer.Ray;
import scene.materials.Material;
import scene.SceneObject;
import scene.lights.Light;
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

    public Shape(Vec3 pos, Material mat, Matrix4x4 transf, String type) {
        super(pos);
        this.material = mat;
        this.type = type;
        this.orgTransformation = transf;
        this.invTransformation = transf.invert();
        this.trpTransformation = transf.transpose();
        Log.print(this, "Init " + this.orgTransformation);
    }

    public abstract Intersection intersect(Ray ray);

    //public abstract boolean equals(Shape shape);

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

    public boolean isReflective(){
        return this.material.isReflective();
    }

    public boolean isTransparent(){
        return this.material.isTransparent();
    }

    @Override
    public String toString(){
        return this.type;
    }
}
