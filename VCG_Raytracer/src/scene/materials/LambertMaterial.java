package scene.materials;

import scene.lights.Light;
import utils.RgbColor;
import utils.algebra.Vec3;

public class LambertMaterial  extends Material {
    public LambertMaterial(RgbColor ambientCoefficient, RgbColor diffuseCoefficient) {
        super(ambientCoefficient, diffuseCoefficient, RgbColor.BLACK, 0, "LAMBERT");

        this.giOn = true;
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
    public RgbColor getColor(Light light, Vec3 normal, Vec3 vertexPos) {
        Vec3 normalN = normal.normalize();
        Vec3 lightVecN = (light.getPosition().sub(vertexPos)).normalize();

        float angle = this.clampAngle(normalN.scalar(lightVecN));

        return this.calculateDiffuseColor(light.getColor(), angle);
    }

    @Override
    public RgbColor getColor(Light light, Vec3 normal, Vec3 vertexPos, Vec3 camPos) {
        return getColor(light, normal, vertexPos);
    }
}
