package scene;


import utils.Log;
import utils.Vec2;
import utils.Vec3;

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
        Log.print(this, "Init");
        mShapeList = new ArrayList<Shape>();
        mLightList = new ArrayList<Light>();
    }

    public void setCamera(Vec3 camPos, Vec3 viewPoint, Vec3 upVec, float viewAngle, float focalLength, int screenWidth, int screenHeight){
        mSceneCam = new Camera(camPos, viewPoint, upVec, viewAngle, focalLength, screenWidth, screenHeight);
    }

    public Vec3 getCamPos(){
        return mSceneCam.getPosition();
    }

    public Vec3 getCamCoords(Vec2 screenVec){
        return mSceneCam.calculateCoords(screenVec);
    }
}
