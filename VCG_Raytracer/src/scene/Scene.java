package scene;


import utils.Vec3;
import utils.Vec4;

import java.util.*;

public class Scene {

    private ArrayList<Shape> mShapeList;
    private ArrayList<Light> mLightList;

    private Camera mSceneCam;

    public ArrayList<Shape> getShapeList() {
        return mShapeList;
    }
    public ArrayList<Light> getLightList() {
        return mLightList;
    }

    public Scene(){
        mShapeList = new ArrayList<Shape>();

    }

    public void setCamera(Vec3 camPos, Vec3 viewPoint, Vec3 upVec, float viewAngle, float focalLength, int screenWidth, int screenHeight){
        mSceneCam = new Camera(camPos, viewPoint, upVec, viewAngle, focalLength, screenWidth, screenHeight);
    }
}
