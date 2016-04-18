package scene.materials;

import scene.lights.Light;
import utils.RgbColor;
import utils.Vec3;

public class AmbientMaterial extends Material {
	public AmbientMaterial(RgbColor ambientCoefficient) {
		super(ambientCoefficient, null, null, 0, "AMBIENT");
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
		return this.getAmbientCoeff();
	}
}
