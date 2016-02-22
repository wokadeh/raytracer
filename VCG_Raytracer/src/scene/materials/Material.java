package scene.materials;

import scene.lights.Light;
import utils.Log;
import utils.RgbColor;
import utils.Vec3;

public abstract class Material {

    public RgbColor ambient;
    protected RgbColor diffCoeff;
    protected RgbColor specCoeff;

    protected float shininess;

    private String mType;

    protected Material(RgbColor ambientColor, RgbColor diffuseCoefficient, RgbColor specularCoefficient, float shininess, String type){
        Log.print(this, "Init " + type);

        this.ambient = ambientColor;
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

    public abstract boolean isReflective();

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
