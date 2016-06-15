package scene.materials;


import scene.lights.Light;
import utils.RgbColor;
import utils.algebra.Vec3;

public class BlinnMaterial extends Material {
    public BlinnMaterial(RgbColor ambientCoefficient, RgbColor diffuseCoefficient, RgbColor specularCoefficient, float shininess, float reflectivity) {
        super(ambientCoefficient, diffuseCoefficient, specularCoefficient, shininess, "BLINN");

        this.calculateMaterialCoeff(reflectivity);
    }

    @Override
    public boolean isReflective() {
        return false;
    }

    @Override
    public boolean isTransparent() {
        return false;
    }

    @Override
    public RgbColor getColor(Light light, Vec3 normal, Vec3 vertexPos, Vec3 camPos) {
        return new RgbColor(0,0,0);
    }
}
