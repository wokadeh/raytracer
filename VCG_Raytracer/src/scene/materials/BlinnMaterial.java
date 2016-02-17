package scene.materials;


import scene.lights.Light;
import utils.RgbColor;
import utils.Vec3;

public class BlinnMaterial extends Material {
    public BlinnMaterial(RgbColor ambientColor, RgbColor diffuseCoefficient, RgbColor specularCoefficient, float shininess, int type) {
        super(ambientColor, diffuseCoefficient, specularCoefficient, shininess, "BLINN");
    }

    @Override
    public RgbColor getColor(Light light, Vec3 normal, Vec3 vertexPos, Vec3 camPos) {
        return this.ambient;
    }
}
