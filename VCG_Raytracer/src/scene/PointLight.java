package scene;

import utils.RgbColor;
import utils.Vec4;

public class PointLight extends Light {

    public PointLight(Vec4 pos, RgbColor color) {
        super(pos, color);
    }
}
