package scene.lights;

import scene.lights.Light;
import utils.RgbColor;
import utils.Vec3;

public class PointLight extends Light {

    public PointLight(Vec3 pos, RgbColor color) {
        super(pos, color);
    }
}
