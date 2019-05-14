package scene.cameras;

import utils.Log;
import utils.Vec2;
import utils.Vec3;

public class PerspectiveCamera {

    private Vec3 v;
    private Vec3 u;
    private Vec3 s;

    private float ratio;
    private Vec3 position;

    private float focalLength;

    private float viewPlaneWidth;
    private float viewPlaneHeight;

    private int screenWidth;
    private int screenHeight;

    private Vec3 center;

    public PerspectiveCamera(Vec3 pos, Vec3 centerOfInterest, Vec3 upVec, float angleOfView, float focalLength, int screenWidth, int screenHeight) {
        Log.print(this, "Init");
        this.position = pos;
        this.focalLength = focalLength;
        this.center = centerOfInterest;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        this.v = centerOfInterest.sub(pos).normalize();
        this.s = v.cross(upVec).normalize();
        this.u = s.cross(v).normalize();

        this.ratio = (float) screenWidth / (float) screenHeight;
        float angle = (float) Math.toRadians(angleOfView / 2f);
        this.viewPlaneHeight = (float) (focalLength * Math.tan(angle));
        this.viewPlaneWidth = this.ratio * this.viewPlaneHeight;

        logParameters(centerOfInterest, pos, upVec, angleOfView);
    }

    public Vec3 getPosition(){
        return this.position;
    }

    public Vec3 getLookAt(){
        return this.center;
    }

    public Vec3 calculateDestPoint(Vec2 pixelPos){
        float pNormX = 2f * (pixelPos.x + 0.5f) / ((float) this.screenWidth - 1f) - 1f ;
        float pNormY = -2f * (pixelPos.y + 0.5f) / ((float) this.screenHeight - 1f) + 1f;

        // Attention: Coordinates are flipped because the positive y axis is going down
        float x = pNormX * this.viewPlaneWidth;
        float y = pNormY * this.viewPlaneHeight;

        // Destination points are on viewplane 1 unit in front of the camera
        Vec3 destPoint = new Vec3(x, y, position.z - 1f);

//        Vec3 destPoint = new Vec3()
//                .add( this.v.multScalar( this.focalLength ) )
//                .add( this.s.multScalar( x ) )
//                .add( this.u.multScalar( y ) )
//                .normalize();

        return destPoint;
    }

    private void logParameters(Vec3 centerOfInterest, Vec3 camPos, Vec3 upVec, float angleOfView) {
        Log.print(this, "PerspectiveCamera Position: \t" + camPos);
        Log.print(this, "Center of Interest: \t" + centerOfInterest);
        Log.print(this, "Up-Vector: \t\t\t" + upVec);
        Log.print(this, "Angle of View: \t\t" + angleOfView);
        Log.print(this, "Screen Dimensions: \tWidth " + this.screenWidth + ", Height: " + this.screenHeight);
        Log.print(this, "PerspectiveCamera Dimensions: \tWidth " + this.viewPlaneWidth + ", Height: " + this.viewPlaneHeight);
        Log.print(this, "Aspect Ratio: \t\t" + this.ratio);
        Log.print(this, "Center: \t\t\t\t" + this.center);
        Log.print(this, "U: \t\t\t\t\t" + this.u);
        Log.print(this, "V: \t\t\t\t\t" + this.v);
        Log.print(this, "S: \t\t\t\t\t" + this.s);
    }
}
