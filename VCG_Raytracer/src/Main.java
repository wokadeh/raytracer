// ************************************************************ //
//                      Hochschule Duesseldorf                  //
//                                                              //
//                     Vertiefung Computergrafik                //
// ************************************************************ //


/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    1. Documentation:    Did you comment your code shortly but clearly?
    2. Structure:        Did you clean up your code and put everything into the right bucket?
    3. Performance:      Are all loops and everything inside really necessary?
    4. Theory:           Are you going the right way?

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

 <<< YOUR TEAM NAME >>>

     Master of Documentation:
     Master of Structure:
     Master of Performance:
     Master of Theory:

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

import raytracer.Raytracer;
import scene.SceneObject;
import scene.lights.AreaLight;
import scene.materials.*;
import scene.shapes.Plane;
import ui.Window;
import scene.Scene;
import utils.io.DataImporter;
import utils.RgbColor;
import utils.algebra.Vec3;

/*
    ISSUES:

    * Fix correct exit ray from refraction
    * Fix light normalisation
    * Add textures
    * Add triangles and polygons
    * Connect importer of OBJ
    * Add depth of field
    * Add SSAO
    * Add GI
    * Add blurry reflections
 */

// Main application class. This is the routine called by the JVM to run the program.
public class Main {

    /** BOX_DIMENSION **/

    static final int IMAGE_WIDTH = 800;
    static final int IMAGE_HEIGHT = 600;

    static final float BOX_DIMENSION = 4f;

    /** RAYTRACER **/

    static final int RECURSIONS = 4;
    static final int ANTI_ALIASING = Raytracer.ANTI_ALIASING_MEDIUM;
    static final boolean USE_SOFT_SHADOWS = true;

    /** LIGHT **/
    static final short LIGHT_DENSITY = 20;
    static final short LIGHT_SAMPLES = 40;

    static final RgbColor BACKGROUND_COLOR = RgbColor.BLACK;

    static final Vec3 LIGHT_POSITION = new Vec3( 0f, BOX_DIMENSION/1.2f - 0.1f, BOX_DIMENSION - 1f );
    static final short AREA_LIGHT_SIZE = 2;

    /** GI **/
    static final boolean USE_GI = false;
    static final int GI_LEVEL = 2;
    static final int GI_SAMPLES = 40;

    static final RgbColor LIGHT_COLOR = (USE_GI) ? RgbColor.GRAY : RgbColor.LIGHT_GRAY;
    static final RgbColor AMBIENT_LIGHT = (USE_GI) ? new RgbColor(0.01f, 0.01f, 0.01f) : new RgbColor(0.7f, 0.7f, 0.7f);

    static final boolean USE_AO = false;
    static final int NUMBER_OF_AO_SAMPLES = 60;
    static final float AO_MAX_DISTANCE = 2f;

    static final boolean USE_POST_FILTERING = false;

    /** CAMERA **/

    static final Vec3 CAM_POS = new Vec3(0, 0, 17);
    static final Vec3 LOOK_AT = new Vec3(0, 0, 0);
    static final Vec3 UP_VECTOR = new Vec3(0, 1, 0);

    static final float VIEW_ANGLE = 35;

    /** DEBUG **/

    static final boolean SHOW_AREA_LIGHT_SAMPLES = false;
    static final boolean SHOW_PRIMARY_RAYS = false;
    static final boolean SHOW_SECONDARY_RAYS = false;
    static final boolean SHOW_PARAM_LABEL = true;

    static final int BLOCK_SIZE = Raytracer.MEDIUM_BLOCK;
    static final int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();

    // Initial method. This is where the show begins.
    public static void main(String[] args){
        //importObject("models/Scooter-smgrps.obj");

        Window renderWindow = new Window(IMAGE_WIDTH, IMAGE_HEIGHT);

        draw(renderWindow);
    }

    private static void importObject(String path){
        DataImporter.loadOBJ(path);
    }


    private static void draw(Window renderWindow){
        Scene renderScene = new Scene();

        setupScene(renderScene);

        raytraceScene(renderWindow, renderScene);
    }

    private static void setupScene(Scene renderScene){
        setupCameras(renderScene);

        setupCornellBox(renderScene);

        setupObjects(renderScene);

        setupLights(renderScene);
    }

    private static void setupLights(Scene renderScene) {
        // Note: Never put the light source inside a plane
        Vec3 light_pos = new Vec3(LIGHT_POSITION.x, LIGHT_POSITION.y - 0.3f, LIGHT_POSITION.z);

        if(!USE_SOFT_SHADOWS) {
        /* Point Light */
            renderScene.createPointLight(light_pos, LIGHT_COLOR);
        }
        else {
        /* Area Light */
            renderScene.createAreaLight(light_pos, AREA_LIGHT_SIZE, LIGHT_DENSITY, LIGHT_SAMPLES, LIGHT_COLOR.multScalar(0.75f));

            if(SHOW_AREA_LIGHT_SAMPLES) {
                AreaLight light1 = (AreaLight) renderScene.getLightList().get(0);

                for (SceneObject light : light1.getPositionList()) {
                    Material planeMaterial = new AmbientMaterial(RgbColor.GREEN);
                    renderScene.createSquare(light.getPosition(), planeMaterial, 0.06f, Plane.FACING_DOWN, false);
                }
            }
        }
    }

    private static void setupCameras(Scene renderScene) {
        renderScene.createPerspCamera(CAM_POS, LOOK_AT, UP_VECTOR, VIEW_ANGLE, IMAGE_WIDTH, IMAGE_HEIGHT);
    }

    private static void setupObjects(Scene renderScene) {
        setupSpheres(renderScene);
        setupLightShapes(renderScene);
    }

    private static void setupSpheres(Scene renderScene) {
        float sphereRadius = 1f;
        float smallSphereRadius = 0.6f;
        float smallSphereRadius2 = 0.8f;

        // Materials: AmbientMaterial Color, Diffuse Coeeff, Specular Coeff, Shininess, Material
        renderScene.createSphere(new Vec3(-BOX_DIMENSION /4f, -BOX_DIMENSION/1.2f + 1.1f, -BOX_DIMENSION /3f+4), Material.REFLECTIVE_MATERIAL, sphereRadius);
        renderScene.createSphere(new Vec3(BOX_DIMENSION/4f, 1, BOX_DIMENSION /3f+3), Material.REFRACTIVE_MATERIAL, sphereRadius);

        renderScene.createSphere(new Vec3(-BOX_DIMENSION+1f, -BOX_DIMENSION + smallSphereRadius*1.75f, 6), Material.LAMBERT_MATERIAL, smallSphereRadius);
        renderScene.createSphere(new Vec3(-BOX_DIMENSION+2f, -BOX_DIMENSION + smallSphereRadius*1.75f, 6), Material.LAMBERT_MATERIAL, smallSphereRadius);
        renderScene.createSphere(new Vec3(-BOX_DIMENSION+3f, -BOX_DIMENSION + smallSphereRadius*1.75f, 6), Material.LAMBERT_MATERIAL, smallSphereRadius);
        renderScene.createSphere(new Vec3(-BOX_DIMENSION+4f, -BOX_DIMENSION + smallSphereRadius*1.75f, 6), Material.LAMBERT_MATERIAL, smallSphereRadius);
        renderScene.createSphere(new Vec3(-BOX_DIMENSION+5f, -BOX_DIMENSION + smallSphereRadius*1.75f, 6), Material.LAMBERT_MATERIAL, smallSphereRadius);
        renderScene.createSphere(new Vec3(-BOX_DIMENSION+6f, -BOX_DIMENSION + smallSphereRadius*1.75f, 6), Material.LAMBERT_MATERIAL, smallSphereRadius);
        renderScene.createSphere(new Vec3(-BOX_DIMENSION+7f, -BOX_DIMENSION + smallSphereRadius*1.75f, 6), Material.LAMBERT_MATERIAL, smallSphereRadius);

        renderScene.createSphere(new Vec3(-BOX_DIMENSION+7f, -BOX_DIMENSION + smallSphereRadius*1.75f, 5), Material.LAMBERT_MATERIAL, smallSphereRadius);
        renderScene.createSphere(new Vec3(-BOX_DIMENSION+7f, -BOX_DIMENSION + smallSphereRadius*1.75f, 4), Material.LAMBERT_MATERIAL, smallSphereRadius);
        renderScene.createSphere(new Vec3(-BOX_DIMENSION+7f, -BOX_DIMENSION + smallSphereRadius*1.75f, 3), Material.LAMBERT_MATERIAL, smallSphereRadius);
        renderScene.createSphere(new Vec3(-BOX_DIMENSION+7f, -BOX_DIMENSION + smallSphereRadius*1.75f, 2), Material.LAMBERT_MATERIAL, smallSphereRadius);
        renderScene.createSphere(new Vec3(-BOX_DIMENSION+7f, -BOX_DIMENSION + smallSphereRadius*1.75f, 1), Material.LAMBERT_MATERIAL, smallSphereRadius);

        renderScene.createSphere(new Vec3(-BOX_DIMENSION+1f, -BOX_DIMENSION + smallSphereRadius*1.75f, 5), Material.LAMBERT_MATERIAL, smallSphereRadius);
        renderScene.createSphere(new Vec3(-BOX_DIMENSION+1f, -BOX_DIMENSION + smallSphereRadius*1.75f, 4), Material.LAMBERT_MATERIAL, smallSphereRadius);
        renderScene.createSphere(new Vec3(-BOX_DIMENSION+1f, -BOX_DIMENSION + smallSphereRadius*1.75f, 3), Material.LAMBERT_MATERIAL, smallSphereRadius);
        renderScene.createSphere(new Vec3(-BOX_DIMENSION+1f, -BOX_DIMENSION + smallSphereRadius*1.75f, 2), Material.LAMBERT_MATERIAL, smallSphereRadius);
        renderScene.createSphere(new Vec3(-BOX_DIMENSION+1f, -BOX_DIMENSION + smallSphereRadius*1.75f, 1), Material.LAMBERT_MATERIAL, smallSphereRadius);

        renderScene.createSphere(new Vec3(-BOX_DIMENSION+2f, -BOX_DIMENSION + smallSphereRadius*1.75f, 1), Material.LAMBERT_MATERIAL, smallSphereRadius);
        renderScene.createSphere(new Vec3(-BOX_DIMENSION+3f, -BOX_DIMENSION + smallSphereRadius*1.75f, 1), Material.LAMBERT_MATERIAL, smallSphereRadius);
        renderScene.createSphere(new Vec3(-BOX_DIMENSION+4f, -BOX_DIMENSION + smallSphereRadius*1.75f, 1), Material.LAMBERT_MATERIAL, smallSphereRadius);
        renderScene.createSphere(new Vec3(-BOX_DIMENSION+5f, -BOX_DIMENSION + smallSphereRadius*1.75f, 1), Material.LAMBERT_MATERIAL, smallSphereRadius);
        renderScene.createSphere(new Vec3(-BOX_DIMENSION+6f, -BOX_DIMENSION + smallSphereRadius*1.75f, 1), Material.LAMBERT_MATERIAL, smallSphereRadius);

        renderScene.createCube(new Vec3(1.5f,-2.5f,4), Material.LAMBERT_MATERIAL, 1f);
    }

    private static void setupLightShapes(Scene renderScene) {
        Material planeMaterial = new AmbientMaterial(RgbColor.WHITE);
        renderScene.createSquare(LIGHT_POSITION, planeMaterial, 0.6f, Plane.FACING_UP, true);
        renderScene.createSquare(LIGHT_POSITION, planeMaterial, 0.6f, Plane.FACING_DOWN, false);
    }

    private static void setupCornellBox(Scene renderScene) {
        // Materials: AmbientMaterial Color, Diffuse Coeeff

        Material planeMaterialLeft = new LambertMaterial(RgbColor.DARK_GRAY, RgbColor.BLUE);
        Material planeMaterialRight = new LambertMaterial(RgbColor.DARK_GRAY, RgbColor.RED);


        // Plane 0 LEFT
        renderScene.createPlane(new Vec3(BOX_DIMENSION, 0f, 0 ), planeMaterialLeft, Plane.FACING_LEFT);
        // Plane 1 RIGHT
        renderScene.createPlane(new Vec3( -BOX_DIMENSION, 0f, 0 ), planeMaterialRight, Plane.FACING_RIGHT);
        // Plane 2 FRONT
        renderScene.createPlane(new Vec3( 0f, 0f, 2 * BOX_DIMENSION ), Material.LAMBERT_MATERIAL, Plane.FACING_FRONT);
        // Plane 3 BACK
        renderScene.createPlane(new Vec3( 0f, 0f, -BOX_DIMENSION/2 ), Material.LAMBERT_MATERIAL, Plane.FACING_BACK);
        // Plane 4 UP
        renderScene.createPlane(new Vec3( 0f, -BOX_DIMENSION/1.2f, 0 ), Material.LAMBERT_MATERIAL, Plane.FACING_UP);
        // Plane 5 DOWN
        renderScene.createPlane(new Vec3( 0f, BOX_DIMENSION/1.2f, 0 ), Material.LAMBERT_MATERIAL, Plane.FACING_DOWN);
    }

    private static void raytraceScene(Window renderWindow, Scene renderScene){
        Raytracer raytracer = new Raytracer(
                renderScene,
                renderWindow,
                RECURSIONS,
                USE_GI,
                GI_LEVEL,
                GI_SAMPLES,
                USE_AO,
                NUMBER_OF_AO_SAMPLES,
                AO_MAX_DISTANCE,
                BACKGROUND_COLOR,
                AMBIENT_LIGHT,
                ANTI_ALIASING,
                BLOCK_SIZE,
                NUMBER_OF_THREADS,
                USE_POST_FILTERING,
                SHOW_PARAM_LABEL);

        raytracer.renderScene();
    }
}