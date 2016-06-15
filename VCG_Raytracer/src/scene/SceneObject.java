package scene;

import utils.algebra.Vec3;

public class SceneObject {

    private Vec3 mPosition;

    public SceneObject(Vec3 pos){
        mPosition = pos;
    }

    public Vec3 getPosition(){
        return mPosition;
    }


}
