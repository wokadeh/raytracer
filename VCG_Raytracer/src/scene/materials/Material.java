package scene.materials;

import scene.lights.Light;
import utils.Log;
import utils.RgbColor;
import utils.Vec3;

public abstract class Material {

    /* TRANSMISSION INDICES */
    public static float DIAMOND = 2.417f;
    public static float WATER = 2.00f;
    public static float CUSTOM = 1.46f;
    public static float GLASS = 1.54f;
    public static float AIR = 1.0003f;
    public static float NO_TRANSMISSION = 0f;

    /* REFLECTION INDICES */
    public static float TOTAL_REFLECTION = 1.0f;
    public static float MOST_REFLECTION = 0.75f;
    public static float HALF_REFLECTION = 0.5f;
    public static float TINY_REFLECTION = 0.1f;
    public static float NO_REFLECTION = 0f;

    protected RgbColor ambiCoeff;
    protected RgbColor diffCoeff;
    protected RgbColor specCoeff;

    protected float shininess = 0;

    protected float fractionCoeff = 0;
    protected float switchedFractionCoeff = 0;
    protected float transparency = 0;

    private String mType;

    protected Material(RgbColor ambientCoefficient, RgbColor diffuseCoefficient, RgbColor specularCoefficient, float shininess, String type){
        Log.print(this, "Init " + type);

        this.ambiCoeff = ambientCoefficient;
        this.diffCoeff = diffuseCoefficient;
        this.specCoeff = specularCoefficient;
        this.shininess = shininess;

        mType = type;
    }

    protected void calculateMaterialCoeff(float fractionCoeff){
        if (fractionCoeff != 0) {
            this.fractionCoeff = fractionCoeff / Material.AIR ;
            this.switchedFractionCoeff = Material.AIR / fractionCoeff;
        }
    }

    public boolean isType(String type){
        return mType.equals(type);
    }

    public boolean isReflective() {
        return this.reflectionCoeff != 0;
    }

    public boolean isTransparent(){
        return this.fractionCoeff != 0;
    }

    public String getType(){
        return mType;
    }
    public abstract RgbColor getColor(Light light, Vec3 normal, Vec3 vertexPos, Vec3 camPos);

    public float getFractionCoeff() {
        return this.fractionCoeff;
    }

    public RgbColor getAmbientCoeff() {
        return this.ambiCoeff;
    }

    public float getSwitchedFractionCoeff() {
        return this.switchedFractionCoeff;
    }

    public float getReflectivity() {
        return reflectionCoeff;
    }

    protected float reflectionCoeff = 0;

    public float getTransparency() {
        return transparency;
    }

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
        float specFactor = (float) Math.pow(specAngle, this.shininess);

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
