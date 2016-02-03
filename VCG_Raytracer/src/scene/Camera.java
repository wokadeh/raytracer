package scene;

import utils.Vec3;

public class Camera extends SceneObject {

    private Vec3 v;
    private Vec3 u;
    private Vec3 s;

    private float ratio;

    private int viewPlaneWidth;
    private int viewPlaneHeight;

    private Vec3 center;

    public Camera(Vec3 pos, Vec3 centerOfInterest, Vec3 upVec, float angleOfView, float focalLength, int screenWidth, int screenHeight) {
        super(pos);

        this.v = centerOfInterest.sub(pos);
        this.s = v.cross(upVec);
        this.u = s.cross(v);

        this.ratio = (float) screenWidth / (float) screenHeight;

        this.viewPlaneHeight = (int) (2 * Math.tan(angleOfView / 2d));
        this.viewPlaneWidth = (int) this.ratio * this.viewPlaneHeight;
        this.center = pos.add(this.v);
    }

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
