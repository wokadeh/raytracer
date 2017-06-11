package scene.materials;

import scene.lights.Light;
import utils.RgbColor;
import utils.algebra.Vec3;

public class PhongMaterial extends Material {

    public static float SHINY = 100;
    public static float VERY_SHINY = 1000;
    public static float NOT_SHINY = 1;

    public PhongMaterial(RgbColor ambientCoefficient, RgbColor diffuseCoefficient, RgbColor specularCoefficient, float shininess, Reflection reflection, float fractionCoeff) {
        super(ambientCoefficient, diffuseCoefficient, specularCoefficient, shininess, "PHONG");

        this.reflection = reflection;
        this.fractionCoeff = fractionCoeff;
    }

    @Override
    public RgbColor getColor(Light light, Vec3 normal, Vec3 vertexPos) {
        Vec3 normalN = normal.normalize();
        Vec3 lightVecN = (light.getPosition().sub(vertexPos)).normalize();

        float angle = this.clampAngle(normalN.scalar(lightVecN));

        return this.calculateDiffuseColor(light.getColor(), angle);
    }

    @Override
    public RgbColor getColor(Light light, Vec3 normal, Vec3 vertexPos, Vec3 camPos) {
        Vec3 normalN = normal.normalize();
        Vec3 lightVecN = (light.getPosition().sub(vertexPos)).normalize();

        float angle = this.clampAngle(normalN.scalar(lightVecN));

        RgbColor diffuseColor = getColor(light, normal, vertexPos);

        RgbColor specularColor = this.calculateSpecularColor(normalN, lightVecN, light.getColor(), vertexPos, camPos, angle);

        RgbColor outputColor = diffuseColor.add(specularColor);

        return outputColor;
    }
}
