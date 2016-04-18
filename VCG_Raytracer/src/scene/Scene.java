package scene;


import scene.cameras.PerspectiveCamera;
import scene.lights.AreaLight;
import scene.lights.Light;
import scene.lights.PointLight;
import scene.materials.Material;
import scene.shapes.Plane;
import scene.shapes.Shape;
import scene.shapes.Sphere;
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
        mShapeList = new ArrayList<>();
        mLightList = new ArrayList<>();
    }

    public void createSphere(Vec3 pos, Material mat, float radius){
        mShapeList.add(new Sphere(pos, mat, radius));
    }

    public void createPlane(Vec3 pos, Material mat, int facingDirection){
        mShapeList.add(new Plane(pos, mat, facingDirection));
    }

    public void createPointLight(Vec3 pos, RgbColor color){
        mLightList.add(new PointLight(pos, color));
    }

    public void createAreaLight(Vec3 pos, float dim, short res, short samples, RgbColor color){
        mLightList.add(new AreaLight(pos, dim, res, samples, color));
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
