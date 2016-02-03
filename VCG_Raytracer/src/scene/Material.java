package scene;

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

    public int PHONG = 0;
    public int BLINN = 1;

    public Material(RgbColor ambientColor, RgbColor diffuseCoefficient, RgbColor specularCoefficient, float shininess){
        mAmbient = ambientColor;
        mShininess = shininess;

        mDiffCoeff = diffuseCoefficient;
        mSpecCoeff = specularCoefficient;
    }

    public RgbColor getColor(ArrayList<Light> lightList, Vec3 normal, Vec3 position, Camera camera, int type) {

        if(type == PHONG){
            return getPhongColor(lightList, normal, camera, position);
        }
        if(type == BLINN){
            return getBlinnColor(lightList, normal, camera, position);
        }
        return new RgbColor(0,0,0);
    }

    private RgbColor getBlinnColor(ArrayList<Light> lightList, Vec3 normal, Camera camera, Vec3 position) {
        return mAmbient;
    }

    private RgbColor getPhongColor(ArrayList<Light> lightList, Vec3 normal, Camera camera, Vec3 position){
        RgbColor outColor = mAmbient;

        Vec3 normalN = normal.normalize();

        for(Light light : lightList){
            Vec3 lightVecN = (position.sub(light.getPosition())).normalize();
            float angle = normalN.scalar(lightVecN);

            mDiffuse = calculateDiffuseColor(light.getColor(), angle);
            mSpecular = calculateSpecularColor(normalN, lightVecN, light.getColor(), position, camera.getPosition(), angle);

            outColor.add(mDiffuse);
            outColor.add(mSpecular);
        }

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