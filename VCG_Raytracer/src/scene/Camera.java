package scene;

import utils.Log;
import utils.Vec2;
import utils.Vec3;

public class Camera extends SceneObject {

    private Vec3 v;
    private Vec3 u;
    private Vec3 s;

    private float ratio;

    private float viewPlaneWidth;
    private float viewPlaneHeight;

    private Vec3 center;
    private Vec3 viewVector;

    public Camera(Vec3 pos, Vec3 centerOfInterest, Vec3 upVec, float angleOfView, float focalLength, int screenWidth, int screenHeight) {
        super(pos);
        Log.print(this, "Init");

        this.v = centerOfInterest.sub(pos);
        this.s = v.cross(upVec);
        this.u = s.cross(v);

        this.ratio = (float) screenWidth / (float) screenHeight;
        Log.warn(this, "Test: \t" + this.ratio * Math.tan(angleOfView / 2d) * 2);
        this.viewPlaneHeight = (float) (2f * Math.tan(angleOfView / 2d));
        this.viewPlaneWidth = this.ratio * this.viewPlaneHeight;
        this.viewVector = v.normalize();
        this.center = pos.add(this.viewVector);

        logParameters(centerOfInterest, upVec, angleOfView, screenWidth, screenHeight);
    }

    public Vec3 calculateCoords(Vec2 pixelPos){
        Vec2 screenVec = new Vec2( (int) (2 * ( ((float) pixelPos.x + 0.5)/ (float) this.viewPlaneWidth ) - 1),
                                    (int) (2 * ( ((float) pixelPos.y + 0.5)/ (float) this.viewPlaneHeight ) - 1));

        //screenVec.x *= (float) this.viewPlaneWidth / 2f;
        //screenVec.y *= (float) this.viewPlaneHeight / 2f;

        Vec3 screenSpaceVec = new Vec3(screenVec.x, screenVec.y, this.center.z);
        screenSpaceVec = screenSpaceVec.add(this.u);
        screenSpaceVec = screenSpaceVec.add(this.s);

        return screenSpaceVec;
    }

    private void logParameters(Vec3 centerOfInterest, Vec3 upVec, float angleOfView, int screenWidth, int screenHeight) {
        Log.print(this, "Center of Interest: \t" + centerOfInterest);
        Log.print(this, "Up-Vector: \t\t\t" + upVec);
        Log.print(this, "Angle of View: \t\t" + angleOfView);
        Log.print(this, "Screen Dimensions: \tWidth " + screenWidth + ", Height: " + screenHeight);
        Log.print(this, "Camera Dimensions: \tWidth " + this.viewPlaneWidth + ", Height: " + this.viewPlaneHeight);
        Log.print(this, "Aspect Ratio: \t\t" + this.ratio);
        Log.print(this, "View Vector: \t\t" + this.viewVector);
        Log.print(this, "Center: \t\t\t\t" + this.center);
        Log.print(this, "U: \t\t\t\t\t" + this.u);
        Log.print(this, "V: \t\t\t\t\t" + this.v);
        Log.print(this, "S: \t\t\t\t\t" + this.s);
    }
}
