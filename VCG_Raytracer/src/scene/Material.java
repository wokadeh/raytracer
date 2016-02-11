package scene;

import utils.Log;
import utils.RgbColor;
import utils.Vec3;

public class Material {

    public RgbColor ambient;
    private RgbColor mDiffuse;
    private RgbColor mSpecular;
    private RgbColor mDiffCoeff;
    private RgbColor mSpecCoeff;

    private float mShininess;

    public static int PHONG = 0;
    public static int BLINN = 1;

    private int mType;

    public Material(RgbColor ambientColor, RgbColor diffuseCoefficient, RgbColor specularCoefficient, float shininess, int type){
        Log.print(this, "Init " + type);

        this.ambient = ambientColor;
        mShininess = shininess;

        mDiffCoeff = diffuseCoefficient;
        mSpecCoeff = specularCoefficient;

        mDiffuse = new RgbColor(0,0,0);
        mSpecular = new RgbColor(0,0,0);

        mType = type;
    }

    public RgbColor getColor(Light light, Vec3 normal, Vec3 vertexPos, Vec3 camPos) {

        if(mType == PHONG){
            return getPhongColor(light, normal, camPos, vertexPos);
        }
        if(mType == BLINN){
            return getBlinnColor(light, normal, camPos, vertexPos);
        }
        return new RgbColor(0,0,0);
    }

    private RgbColor getBlinnColor(Light light, Vec3 normal, Vec3 camPos, Vec3 vertexPos) {
        return this.ambient;
    }

    private float clampAngle(float angle){
        if(angle > 1f){
            return 1f;
        }
        if(angle < 0){
            return 0;
        }
        return angle;
    }

    private RgbColor getPhongColor(Light light, Vec3 normal, Vec3 camPos, Vec3 vertexPos){
        Vec3 normalN = normal.normalize();

        Vec3 lightVecN = (light.getPosition().sub(vertexPos)).normalize();
        float angle = clampAngle(normalN.scalar(lightVecN));

        //if(angle >= 0) {
            mDiffuse = calculateDiffuseColor(light.getColor(), angle);
            mSpecular = calculateSpecularColor(normalN, lightVecN, light.getColor(), vertexPos, camPos, angle);

            return mDiffuse.add(mSpecular);
        //}

//        Log.warn(this, mDiffuse + ", " + mSpecular);

        //return mDiffuse;
    }

    private RgbColor calculateSpecularColor(Vec3 normalN, Vec3 lightVecN, RgbColor lightColor, Vec3 position, Vec3 camPosition, float angle) {
        Vec3 viewVecN = camPosition.sub(position).normalize();

        Vec3 reflectN = normalN.multScalar(2 * angle);
        reflectN = reflectN.sub(lightVecN);

        float specAngle = reflectN.scalar(viewVecN);
        float specFactor = (float) Math.pow(specAngle, mShininess);

        return mSpecCoeff
                .multScalar(lightColor)
                .multScalar(specFactor);
    }

    private RgbColor calculateDiffuseColor(RgbColor lightColor, float angle){
        return mDiffCoeff
                .multScalar(lightColor)
                .multScalar(angle);
    }
}
