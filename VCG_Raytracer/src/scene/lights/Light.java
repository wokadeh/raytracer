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
    protected String type;

    public Light(Vec3 pos, RgbColor color, String type) {
        super(pos);
        mColor = color;
        this.type = type;
        Log.print(this, "Init with color " + mColor);
    }

    public String isType() {
        return this.type;
    }
}
