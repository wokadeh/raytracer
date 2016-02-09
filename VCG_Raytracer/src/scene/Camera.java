package scene;

import utils.Log;
import utils.Vec2;
import utils.Vec3;

public class Camera extends SceneObject {

    private Vec3 v;
    private Vec3 u;
    private Vec3 s;

    private float ratio;

    private float focalLength;

    private float viewPlaneWidth;
    private float viewPlaneHeight;

    private int screenWidth;
    private int screenHeight;

    private Vec3 center;

    public Camera(Vec3 pos, Vec3 centerOfInterest, Vec3 upVec, float angleOfView, float focalLength, int screenWidth, int screenHeight) {
        super(pos);
        Log.print(this, "Init");

        this.v = centerOfInterest.sub(pos).normalize();
        this.s = v.cross(upVec).normalize();
        this.u = s.cross(v).normalize();

        this.focalLength = focalLength;

        this.ratio = (float) screenWidth / (float) screenHeight;
        float a = (float) (angleOfView * Math.PI / 180f / 2f);
        this.viewPlaneHeight = (float) (focalLength * Math.tan(a));
        //this.viewPlaneHeight = (float) (2f * Math.tan(angleOfView / 2d));
        this.viewPlaneWidth = this.ratio * this.viewPlaneHeight;
        this.center = centerOfInterest;

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        logParameters(centerOfInterest, pos, upVec, angleOfView);
    }

    public float getFocalLength() {
        return focalLength;
    }

    public Vec3 calculateDestPoint(Vec2 pixelPos){

        float x = ((2f * ( (pixelPos.x + 0.5f) / (float) this.screenWidth ) - 1f) - 1f ) * this.viewPlaneWidth;
        float y = ((-2f * ( (pixelPos.y + 0.5f) / (float) this.screenHeight ) - 1f) + 1f ) * this.screenHeight;

        Vec3 screenVec = new Vec3( x, y, 1f );

        Vec3 destPoint = screenVec.add(this.v.multScalar(this.focalLength));
        destPoint.add(this.s.multScalar(x));
        destPoint.add(this.u.multScalar(y));
        destPoint.normalize();

        //Vec3 screenSpaceVec = new Vec3(screenVec.x, screenVec.y, this.center.z);
        //screenSpaceVec.x = (screenSpaceVec.x * this.u.x + screenSpaceVec.x * this.s.x) * this.viewPlaneWidth / 2f;
        //screenSpaceVec.y = (screenSpaceVec.y * this.u.y + screenSpaceVec.y * this.s.y) * this.viewPlaneHeight / 2f;
        //double sFactor = 0.005f * (pixelPos.x - 0.5f * (this.screenWidth - 1.0f));
        //double uFactor = 0.005f * (pixelPos.y - 0.5f * (this.screenHeight - 1.0f));
        //Vec3 screenVec = this.getPosition().add(this.v.multScalar(5f)).add(this.s.multScalar((float) sFactor).add(this.u.multScalar((float) uFactor)));

        return screenVec;
    }

    private void logParameters(Vec3 centerOfInterest, Vec3 camPos, Vec3 upVec, float angleOfView) {
        Log.print(this, "Camera Position: \t" + camPos);
        Log.print(this, "Center of Interest: \t" + centerOfInterest);
        Log.print(this, "Up-Vector: \t\t\t" + upVec);
        Log.print(this, "Angle of View: \t\t" + angleOfView);
        Log.print(this, "Screen Dimensions: \tWidth " + this.screenWidth + ", Height: " + this.screenHeight);
        Log.print(this, "Camera Dimensions: \tWidth " + this.viewPlaneWidth + ", Height: " + this.viewPlaneHeight);
        Log.print(this, "Aspect Ratio: \t\t" + this.ratio);
        Log.print(this, "Center: \t\t\t\t" + this.center);
        Log.print(this, "U: \t\t\t\t\t" + this.u);
        Log.print(this, "V: \t\t\t\t\t" + this.v);
        Log.print(this, "S: \t\t\t\t\t" + this.s);
    }
}
