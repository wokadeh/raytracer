package scene.lights;

import utils.RgbColor;
import utils.algebra.Vec3;

public class PointLight extends Light {

    public PointLight(Vec3 pos, RgbColor color) {
        super(pos, color, "PointLight");
    }
}
