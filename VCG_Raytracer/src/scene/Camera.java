package scene;

import utils.Vec4;

public class Camera extends SceneObject {

    private Vec4 v;
    private Vec4 u;
    private Vec4 s;

    public Camera(Vec4 pos, Vec4 centerOfInterest, Vec4 upVec, float angleOfView, float focalLength, float aspectRatio) {
        super(pos);

        this.v = centerOfInterest.sub(pos);
        this.s = v.cross(upVec);
        this.u = s.cross(v);
    }
}
