package scene;

import utils.RgbColor;
import utils.Vec3;

public class Light extends SceneObject {

    public RgbColor getColor() {
        return mColor;
    }

    private RgbColor mColor;

    public Light(Vec3 pos, RgbColor color) {
        super(pos);
        mColor = color;
    }


}
