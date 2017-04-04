package scene.cameras;

import scene.SceneObject;
import utils.io.Log;
import utils.algebra.Vec2;
import utils.algebra.Vec3;

public class PerspectiveCamera extends SceneObject {

    private Vec3 v;
    private Vec3 u;
    private Vec3 s;

    private float ratio;

    private float viewPlaneWidth;
    private float viewPlaneHeight;

    private int screenWidth;
    private int screenHeight;

    private float angleRad;

    private Vec3 center;

    public PerspectiveCamera(Vec3 pos, Vec3 centerOfInterest, Vec3 upVec, float angleOfView, int screenWidth, int screenHeight) {
        super(pos);
        Log.print(this, "Init");
        this.center = centerOfInterest;

        // subtract 1 to be in range of 0 - (width - 1)
        this.screenWidth = screenWidth - 1;
        this.screenHeight = screenHeight - 1;

        this.calculateCameraCoord(pos, centerOfInterest, upVec);
        this.calculateViewplane(angleOfView);

        logParameters(centerOfInterest, pos, upVec, angleOfView);
    }

    private void calculateViewplane(float angleOfView){
        this.ratio = (float) screenWidth / (float) screenHeight;
        this.angleRad = (float) (((angleOfView/2) * Math.PI) / 180f);
        this.viewPlaneHeight = (float) Math.tan(angleRad);
        this.viewPlaneWidth = (this.ratio * this.viewPlaneHeight);

        // Take the half for the coming adjustment to the viewplane
        this.viewPlaneWidth  *= 0.5f;
        this.viewPlaneHeight *= 0.5f;
    }

    private void calculateCameraCoord(Vec3 pos, Vec3 centerOfInterest, Vec3 upVec){
        this.v = centerOfInterest.sub( pos ).normalize();
        this.s = v.cross( upVec ).normalize();
        this.u = s.cross( v ).normalize();
    }

    public Vec3 calculateDestPoint(Vec2 pixelPos){
        // Calculate normalized pixel coordinates
        float pNormX = 2f * ( pixelPos.x + 0.5f ) / ( (float) this.screenWidth ) - 1f ;
        float pNormY = 2f * ( pixelPos.y + 0.5f ) / ( (float) this.screenHeight ) - 1f;

        // Calculate normalized pixels with world scale and add to camera vectors
        float x = this.viewPlaneWidth * pNormX;
        float y = this.viewPlaneHeight * pNormY;

        Vec3 destPoint = new Vec3()
                .add( this.v )
                .add( this.s.multScalar( x ) )
                .add( this.u.multScalar( y ) )
                .normalize();

        // Attention: Coordinates are flipped because the positive y axis is going down
        destPoint.y *= -1;

        return destPoint;
    }

    private void logParameters(Vec3 centerOfInterest, Vec3 camPos, Vec3 upVec, float angleOfView) {
        Log.print(this, "Position: \t\t" + camPos);
        Log.print(this, "Center of Interest: \t" + centerOfInterest);
        Log.print(this, "Up-Vector: \t\t\t" + upVec);
        Log.print(this, "Angle of View: \t\t" + angleOfView);
        Log.print(this, "Calculated angle: \t" + this.angleRad);
        Log.print(this, "Screen Dimensions: \tWidth " + this.screenWidth + ", Height: " + this.screenHeight);
        Log.print(this, "PerspectiveCamera Dimensions: \tWidth " + this.viewPlaneWidth + ", Height: " + this.viewPlaneHeight);
        Log.print(this, "Aspect Ratio: \t\t" + this.ratio);
        Log.print(this, "Center: \t\t\t\t" + this.center);
        Log.print(this, "U: \t\t\t\t\t" + this.u);
        Log.print(this, "V: \t\t\t\t\t" + this.v);
        Log.print(this, "S: \t\t\t\t\t" + this.s);
    }
}
