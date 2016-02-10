package scene;

import utils.Log;
import utils.RgbColor;
import utils.Vec3;

import java.util.ArrayList;

public class Material {

    private RgbColor mAmbient;
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

        mAmbient = ambientColor;
        mShininess = shininess;

        mDiffCoeff = diffuseCoefficient;
        mSpecCoeff = specularCoefficient;

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
        return mAmbient;
    }

    private RgbColor getPhongColor(Light light, Vec3 normal, Vec3 camPos, Vec3 vertexPos){
        RgbColor outColor = mAmbient;

        Vec3 normalN = normal.normalize();

        Vec3 lightVecN = (vertexPos.sub(light.getPosition())).normalize();
        float angle = normalN.scalar(lightVecN);

        mDiffuse = calculateDiffuseColor(light.getColor(), angle);
        mSpecular = calculateSpecularColor(normalN, lightVecN, light.getColor(), vertexPos, camPos, angle);

        outColor.add(mDiffuse);
        outColor.add(mSpecular);

        return outColor;
    }

    private RgbColor calculateSpecularColor(Vec3 normalN, Vec3 lightVecN, RgbColor lightColor, Vec3 position, Vec3 camPosition, float angle) {
        RgbColor outColor = mSpecCoeff;

        Vec3 viewVecN = position.sub(camPosition).normalize();

        Vec3 reflectN = normalN.multScalar(2 * angle);
        reflectN = reflectN.sub(lightVecN);

        float specAngle = reflectN.scalar(viewVecN);
        float specFactor = (float) Math.pow(specAngle, mShininess);

        outColor.mult(lightColor);
        outColor.mult(specFactor);

        return outColor;
    }

    private RgbColor calculateDiffuseColor(RgbColor lightColor, float angle){

        RgbColor outColor = mDiffCoeff;
        outColor.mult(lightColor);
        outColor.mult(angle);

        return outColor;
    }
}
