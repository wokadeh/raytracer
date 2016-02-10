package scene;


import utils.Log;
import utils.RgbColor;
import utils.Vec2;
import utils.Vec3;

import java.util.*;

public class Scene {

    private ArrayList<Shape> mShapeList;
    private ArrayList<Light> mLightList;

    private PerspectiveCamera mSceneCam;

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

    public void createSphere(Vec3 pos, Material mat, float radius){
        mShapeList.add(new Sphere(pos, mat, radius));
    }

    public void createPointLight(Vec3 pos, RgbColor color){
        mLightList.add(new PointLight(pos, color));
    }

    public void createPerspCamera(Vec3 camPos, Vec3 viewPoint, Vec3 upVec, float viewAngle, float focalLength, int screenWidth, int screenHeight){
        mSceneCam = new PerspectiveCamera(camPos, viewPoint, upVec, viewAngle, focalLength, screenWidth, screenHeight);
    }

    public Vec3 getCamPos(){
        return mSceneCam.getPosition();
    }

    public Vec3 getCamPixelDirection(Vec2 screenVec){
        return mSceneCam.calculateDestPoint(screenVec);
    }
}
