package scene.materials;


import scene.lights.Light;
import utils.RgbColor;
import utils.Vec3;

public class BlinnMaterial extends Material {
    public BlinnMaterial(RgbColor diffuseCoefficient, RgbColor specularCoefficient, float shininess, int type) {
        super(diffuseCoefficient, specularCoefficient, shininess, "BLINN");
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
