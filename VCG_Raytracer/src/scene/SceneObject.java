package scene;

import utils.Vec4;

public class SceneObject {

    private Vec4 mPosition;

    public SceneObject(Vec4 pos){
        mPosition = pos;
    }

    public Vec4 getPosition(){
        return mPosition;
    }


}
