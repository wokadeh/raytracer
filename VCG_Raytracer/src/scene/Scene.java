package scene;


import scene.cameras.PerspectiveCamera;
import utils.Log;
import utils.RgbColor;
import utils.Vec2;
import utils.Vec3;


public class Scene {

    private PerspectiveCamera mSceneCam;

    public Scene(){
        Log.print(this, "Init");
    }

    public void createPerspCamera(Vec3 camPos, Vec3 viewPoint, Vec3 upVec, float viewAngle, float focalLength, int screenWidth, int screenHeight){
        mSceneCam = new PerspectiveCamera(camPos, viewPoint, upVec, viewAngle, focalLength, screenWidth, screenHeight);
    }

    public Vec3 getCamPos(){
        return mSceneCam.getPosition();
    }
    public Vec3 getCamDirection(){
        return mSceneCam.getLookAt();
    }

    public Vec3 getCamPixelDirection(Vec2 screenVec){
        return mSceneCam.calculateDestPoint(screenVec);
    }
}
