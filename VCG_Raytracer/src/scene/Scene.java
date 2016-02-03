package scene;


import java.util.*;

public class Scene {

    private ArrayList mShapeList;
    private ArrayList mLightList;

    public ArrayList getShapeList() {
        return mShapeList;
    }
    public ArrayList getLightList() {
        return mLightList;
    }

    public Scene(){
        mShapeList = new ArrayList();
    }
}
