package scene;

import utils.Vec3;
import utils.Vec4;

public class PointLight implements ILight {

    private Vec4 mPosition;

    public PointLight(Vec4 position){

    }

    @Override
    public Vec4 getPosition() {
        return mPosition;
    }
}
