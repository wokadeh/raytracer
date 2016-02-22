package scene.materials;

import scene.lights.Light;
import utils.RgbColor;
import utils.Vec3;

public class LambertMaterial  extends Material {
    public LambertMaterial(RgbColor ambientColor, RgbColor diffuseCoefficient) {
        super(ambientColor, diffuseCoefficient, null, 0, "LAMBERT");
    }

    @Override
    public boolean isReflective() {
        return false;
    }

    @Override
    public RgbColor getColor(Light light, Vec3 normal, Vec3 vertexPos, Vec3 camPos) {
        Vec3 normalN = normal.normalize();

        Vec3 lightVecN = (light.getPosition().sub(vertexPos)).normalize();
        float angle = clampAngle(normalN.scalar(lightVecN));

        return this.calculateDiffuseColor(light.getColor(), angle);
    }
}
