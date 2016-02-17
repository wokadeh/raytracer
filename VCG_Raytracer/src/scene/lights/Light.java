package scene.lights;

import scene.SceneObject;
import utils.Log;
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
        Log.print(this, "Init with color " + mColor);
    }


}
