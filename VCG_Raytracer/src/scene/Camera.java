package scene;

import utils.Log;
import utils.Vec2;
import utils.Vec3;

public class Camera extends SceneObject {

    private Vec3 v;
    private Vec3 u;
    private Vec3 s;

    private float ratio;

    private int viewPlaneWidth;
    private int viewPlaneHeight;

    private Vec3 center;
    private Vec3 viewVector;

    public Camera(Vec3 pos, Vec3 centerOfInterest, Vec3 upVec, float angleOfView, float focalLength, int screenWidth, int screenHeight) {
        super(pos);
        Log.print(this, "Init");

        this.v = centerOfInterest.sub(pos);
        this.s = v.cross(upVec);
        this.u = s.cross(v);

        this.ratio = (float) screenWidth / (float) screenHeight;

        this.viewPlaneHeight = (int) (2 * Math.tan(angleOfView / 2d));
        this.viewPlaneWidth = (int) this.ratio * this.viewPlaneHeight;
        this.viewVector = v.normalize();
        this.center = pos.add(this.viewVector);
    }

    public Vec3 calculateCoords(Vec2 pixelPos){
        Vec2 screenVec = new Vec2( (int) (2 * ( ((float) pixelPos.x + 0.5)/ (float) this.viewPlaneWidth ) - 1),
                         (int) (2 * ( ((float) pixelPos.y + 0.5)/ (float) this.viewPlaneHeight ) - 1));

        screenVec.x *= this.viewPlaneWidth / 2;
        screenVec.y *= this.viewPlaneHeight / 2;

        Vec3 screenSpaceVec = new Vec3(screenVec.x, screenVec.y, this.center.z);
        screenSpaceVec = screenSpaceVec.add(this.u);
        screenSpaceVec = screenSpaceVec.add(this.s);

        return screenSpaceVec;
    }

    // Auflösung des Bildes ist Center in v,u,s + normalisiertes x bzw y

    public int getViewWidth(){
        return this.viewPlaneWidth;
    }

    public int getViewPlaneHeight(){
        return this.viewPlaneHeight;
    }

    public Vec3 getCenter(){
        return this.center;
    }
}
