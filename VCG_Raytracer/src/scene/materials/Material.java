package scene.materials;

import scene.lights.Light;
import utils.Log;
import utils.RgbColor;
import utils.Vec3;

public abstract class Material {

    public static float GLASS = 1.0f;
    public static float MIRROR = -1.0f;
    public static float SILVER = -0.75f;

    protected RgbColor diffCoeff;
    protected RgbColor specCoeff;

    protected float shininess;
    protected float reflectivity;

    private String mType;

    protected Material(RgbColor diffuseCoefficient, RgbColor specularCoefficient, float shininess, String type){
        Log.print(this, "Init " + type);

        this.diffCoeff = diffuseCoefficient;
        this.specCoeff = specularCoefficient;
        this.shininess = shininess;

        mType = type;
    }

    public String getType(){
        return mType;
    }

    public boolean isType(String type){
        return mType.equals(type);
    }

    public boolean isReflective() {
        return this.reflectivity < 0;
    }

    public boolean isTransparent(){
        return this.reflectivity > 0;
    }

    public abstract RgbColor getColor(Light light, Vec3 normal, Vec3 vertexPos, Vec3 camPos);

    protected float clampAngle(float angle){
        if(angle > 1f){
            return 1f;
        }
        if(angle < 0){
            return 0;
        }
        return angle;
    }

    protected RgbColor calculateSpecularColor(Vec3 normalN, Vec3 lightVecN, RgbColor lightColor, Vec3 position, Vec3 camPosition, float angle) {
        Vec3 viewVecN = camPosition.sub(position).normalize();

        Vec3 reflectN = normalN.multScalar(2 * angle);
        reflectN = reflectN.sub(lightVecN);

        float specAngle = reflectN.scalar(viewVecN);
        float specFactor = (float) Math.pow(specAngle, shininess);

        return this.specCoeff
                .multRGB(lightColor)
                .multScalar(specFactor);
    }

    protected RgbColor calculateDiffuseColor(RgbColor lightColor, float angle){
        return this.diffCoeff
                .multRGB(lightColor)
                .multScalar(angle);
    }
}
