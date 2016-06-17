package scene.materials;

import scene.lights.Light;
import utils.RgbColor;
import utils.algebra.Vec3;

public class PhongMaterial extends Material {

    public static float SHINY = 10;
    public static float VERY_SHINY = 100;
    public static float NOT_SHINY = 0;

    public PhongMaterial(RgbColor ambientCoefficient, RgbColor diffuseCoefficient, RgbColor specularCoefficient, float shininess, float reflectivity, float fractionCoeff, float transparency) {
        super(ambientCoefficient, diffuseCoefficient, specularCoefficient, shininess, "PHONG");

        this.reflectionCoeff = reflectivity;
        this.transparency = transparency;

        this.calculateMaterialCoeff(fractionCoeff);
    }

    @Override
    public RgbColor getColor(Light light, Vec3 normal, Vec3 vertexPos, Vec3 camPos) {
        Vec3 normalN = normal.normalize();
        Vec3 lightVecN = (light.getPosition().sub(vertexPos)).normalize();

        float angle = this.clampAngle(normalN.scalar(lightVecN));

        RgbColor diffuseColor = this.calculateDiffuseColor(light.getColor(), angle);
        RgbColor specularColor = this.calculateSpecularColor(normalN, lightVecN, light.getColor(), vertexPos, camPos, angle);

        RgbColor outputColor = diffuseColor.add(specularColor);

        if(this.reflectionCoeff != 0){
            outputColor = outputColor.multScalar( this.reflectionCoeff );
        }

        return outputColor;
    }
}
