package scene;

import utils.RgbColor;
import utils.Vec4;

public class Light extends SceneObject {

    public RgbColor getColor() {
        return mColor;
    }

    private RgbColor mColor;

    public Light(Vec4 pos, RgbColor color) {
        super(pos);
        mColor = color;
    }


}
