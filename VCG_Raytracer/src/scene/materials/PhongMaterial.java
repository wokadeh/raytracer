package scene.materials;

import scene.lights.Light;
import utils.Log;
import utils.RgbColor;
import utils.Vec3;

public class PhongMaterial extends Material {


    public PhongMaterial(RgbColor ambientColor, RgbColor diffuseCoefficient, RgbColor specularCoefficient, float shininess) {
        super(ambientColor, diffuseCoefficient, specularCoefficient, shininess, "PHONG");
    }

    @Override
    public RgbColor getColor(Light light, Vec3 normal, Vec3 vertexPos, Vec3 camPos) {
        Vec3 normalN = normal.normalize();

        Vec3 lightVecN = (light.getPosition().sub(vertexPos)).normalize();
        float angle = clampAngle(normalN.scalar(lightVecN));

        RgbColor diffuse = this.calculateDiffuseColor(light.getColor(), angle);
        RgbColor specular = this.calculateSpecularColor(normalN, lightVecN, light.getColor(), vertexPos, camPos, angle);

        return diffuse.add(specular);
    }
}
