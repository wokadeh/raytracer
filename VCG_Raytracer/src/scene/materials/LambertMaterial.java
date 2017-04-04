package scene.materials;

import scene.lights.Light;
import utils.RgbColor;
import utils.algebra.Vec3;

public class LambertMaterial  extends Material {
    public LambertMaterial(RgbColor ambientCoefficient, RgbColor diffuseCoefficient) {
        super(ambientCoefficient, diffuseCoefficient, RgbColor.BLACK, 0, "LAMBERT");
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
        Vec3 normalN = normal.normalize();

        Vec3 lightVecN = (light.getPosition().sub(vertexPos)).normalize();
        float angle = clampAngle(normalN.scalar(lightVecN));

        RgbColor diffuseColor = this.calculateDiffuseColor(light.getColor(), angle);

        RgbColor specularColor = this.calculateSpecularColor(normalN, lightVecN, light.getColor(), vertexPos, camPos, angle);

        RgbColor outputColor = diffuseColor.add(specularColor);

        if(this.reflectionCoeff != 0){
            outputColor = outputColor.multScalar( this.reflectionCoeff );
        }

        return outputColor;
    }
}
