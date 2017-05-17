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
import scene.materials.AmbientMaterial;
import scene.materials.LambertMaterial;
import scene.materials.Material;
import scene.materials.PhongMaterial;
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

    static int IMAGE_WIDTH = 800;
    static int IMAGE_HEIGHT = 600;

    static float BOX_DIMENSION = 4f;

    /** LIGHT **/

    static RgbColor AMBIENT_LIGHT = new RgbColor(0.01f, 0.01f, 0.01f);

    static short LIGHT_DENSITY = 30;
    static short LIGHT_SAMPLES = 40;
    static RgbColor LIGHT_COLOR = RgbColor.GRAY;

    static RgbColor BACKGROUND_COLOR = RgbColor.BLACK;

    static Vec3 LIGHT_POSITION = new Vec3( 0f, BOX_DIMENSION/1.2f - 0.3f, BOX_DIMENSION - 1f );
    static short AREA_LIGHT_SIZE = 2;

    /** KAMERA **/

    static Vec3 CAM_POS = new Vec3(0, 0, 17);
    static Vec3 LOOK_AT = new Vec3(0, 0, 0);
    static Vec3 UP_VECTOR = new Vec3(0, 1, 0);

    static float VIEW_ANGLE = 70;

    /** RAYTRACER **/

    static int RECURSIONS = 8;
    static int ANTI_ALIASING = Raytracer.ANTI_ALIASING_LOW; //Raytracer.ANTI_ALIASING_MEDIUM;
    static boolean USE_SOFT_SHADOWS = false;

    static boolean USE_GI = true;
    static int GI_LEVEL = 2;
    static int GI_SAMPLES = 10;

    /** DEBUG **/

    static boolean SHOW_AREA_LIGHT_SAMPLES = false;
    static boolean SHOW_PRIMARY_RAYS = false;
    static boolean SHOW_SECONDARY_RAYS = false;
    static boolean SHOW_PARAM_LABEL = true;

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
            renderScene.createAreaLight(light_pos, AREA_LIGHT_SIZE, LIGHT_DENSITY, LIGHT_SAMPLES, LIGHT_COLOR);

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


        Material lambert1 = new LambertMaterial(RgbColor.RED, RgbColor.BLUE);
        renderScene.createSquare(new Vec3(0, 0, LIGHT_POSITION.z), lambert1, 0.6f, Plane.FACING_UP, true);
    }

    private static void setupSpheres(Scene renderScene) {
        float sphereRadius = 1f;
        float smallSphereRadius = 0.6f;
        float smallSphereRadius2 = 0.9f;

        // Materials: AmbientMaterial Color, Diffuse Coeeff, Specular Coeff, Shininess, Material
        Material sphereMaterial1 = new PhongMaterial(RgbColor.DARK_GRAY, RgbColor.DARK_GRAY, RgbColor.WHITE, PhongMaterial.VERY_SHINY, Material.MOST_REFLECTION, Material.NO_TRANSMISSION, 1);
        Material sphereMaterial2 = new PhongMaterial(RgbColor.BLACK, RgbColor.DARK_GRAY, RgbColor.WHITE, PhongMaterial.SHINY, Material.NO_REFLECTION, Material.AIR, 1);
        Material sphereMaterial3 = new PhongMaterial(RgbColor.GRAY, RgbColor.DARK_GRAY, RgbColor.WHITE, PhongMaterial.VERY_SHINY, Material.NO_REFLECTION, Material.AIR, 1);

        Material boringMaterial1 = new PhongMaterial(RgbColor.DARK_GRAY, RgbColor.GRAY, RgbColor.WHITE, PhongMaterial.SHINY, Material.MOST_REFLECTION, Material.NO_TRANSMISSION, 1);
        Material boringMaterial2 = new PhongMaterial(RgbColor.DARK_GRAY, RgbColor.GRAY, RgbColor.WHITE, PhongMaterial.SHINY, Material.MOST_REFLECTION, Material.NO_TRANSMISSION, 1);

        Material lambert1 = new LambertMaterial(RgbColor.LIGHT_GRAY, RgbColor.WHITE);

        renderScene.createSphere(new Vec3(-BOX_DIMENSION /4f, -BOX_DIMENSION/1.2f + 1.1f, -BOX_DIMENSION /3f+4), sphereMaterial1, sphereRadius);
        renderScene.createSphere(new Vec3(BOX_DIMENSION/4f, -BOX_DIMENSION/1.2f + sphereRadius, BOX_DIMENSION /3f+3), sphereMaterial2, sphereRadius);
        //renderScene.createSphere(new Vec3(BOX_DIMENSION/4f, -BOX_DIMENSION/1.2f + sphereRadius, BOX_DIMENSION /3f+3), sphereMaterial3, smallSphereRadius2);

        renderScene.createSphere(new Vec3(-BOX_DIMENSION+1f, -BOX_DIMENSION + smallSphereRadius*1.75f, 6), lambert1, smallSphereRadius);
        renderScene.createSphere(new Vec3(-BOX_DIMENSION+2f, -BOX_DIMENSION + smallSphereRadius*1.75f, 6), lambert1, smallSphereRadius);
        renderScene.createSphere(new Vec3(-BOX_DIMENSION+3f, -BOX_DIMENSION + smallSphereRadius*1.75f, 6), lambert1, smallSphereRadius);
        renderScene.createSphere(new Vec3(-BOX_DIMENSION+4f, -BOX_DIMENSION + smallSphereRadius*1.75f, 6), lambert1, smallSphereRadius);
        renderScene.createSphere(new Vec3(-BOX_DIMENSION+5f, -BOX_DIMENSION + smallSphereRadius*1.75f, 6), lambert1, smallSphereRadius);
        renderScene.createSphere(new Vec3(-BOX_DIMENSION+6f, -BOX_DIMENSION + smallSphereRadius*1.75f, 6), lambert1, smallSphereRadius);
        renderScene.createSphere(new Vec3(-BOX_DIMENSION+7f, -BOX_DIMENSION + smallSphereRadius*1.75f, 6), lambert1, smallSphereRadius);

        renderScene.createSphere(new Vec3(-BOX_DIMENSION+7f, -BOX_DIMENSION + smallSphereRadius*1.75f, 5), lambert1, smallSphereRadius);
        renderScene.createSphere(new Vec3(-BOX_DIMENSION+7f, -BOX_DIMENSION + smallSphereRadius*1.75f, 4), lambert1, smallSphereRadius);
        renderScene.createSphere(new Vec3(-BOX_DIMENSION+7f, -BOX_DIMENSION + smallSphereRadius*1.75f, 3), lambert1, smallSphereRadius);
        renderScene.createSphere(new Vec3(-BOX_DIMENSION+7f, -BOX_DIMENSION + smallSphereRadius*1.75f, 2), lambert1, smallSphereRadius);
        renderScene.createSphere(new Vec3(-BOX_DIMENSION+7f, -BOX_DIMENSION + smallSphereRadius*1.75f, 1), lambert1, smallSphereRadius);

        renderScene.createSphere(new Vec3(-BOX_DIMENSION+1f, -BOX_DIMENSION + smallSphereRadius*1.75f, 5), lambert1, smallSphereRadius);
        renderScene.createSphere(new Vec3(-BOX_DIMENSION+1f, -BOX_DIMENSION + smallSphereRadius*1.75f, 4), lambert1, smallSphereRadius);
        renderScene.createSphere(new Vec3(-BOX_DIMENSION+1f, -BOX_DIMENSION + smallSphereRadius*1.75f, 3), lambert1, smallSphereRadius);
        renderScene.createSphere(new Vec3(-BOX_DIMENSION+1f, -BOX_DIMENSION + smallSphereRadius*1.75f, 2), lambert1, smallSphereRadius);
        renderScene.createSphere(new Vec3(-BOX_DIMENSION+1f, -BOX_DIMENSION + smallSphereRadius*1.75f, 1), lambert1, smallSphereRadius);

        renderScene.createSphere(new Vec3(-BOX_DIMENSION+2f, -BOX_DIMENSION + smallSphereRadius*1.75f, 1), lambert1, smallSphereRadius);
        renderScene.createSphere(new Vec3(-BOX_DIMENSION+3f, -BOX_DIMENSION + smallSphereRadius*1.75f, 1), lambert1, smallSphereRadius);
        renderScene.createSphere(new Vec3(-BOX_DIMENSION+4f, -BOX_DIMENSION + smallSphereRadius*1.75f, 1), lambert1, smallSphereRadius);
        renderScene.createSphere(new Vec3(-BOX_DIMENSION+5f, -BOX_DIMENSION + smallSphereRadius*1.75f, 1), lambert1, smallSphereRadius);
        renderScene.createSphere(new Vec3(-BOX_DIMENSION+6f, -BOX_DIMENSION + smallSphereRadius*1.75f, 1), lambert1, smallSphereRadius);
    }

    private static void setupLightShapes(Scene renderScene) {
        Material planeMaterial = new AmbientMaterial(RgbColor.WHITE);
        renderScene.createSquare(LIGHT_POSITION, planeMaterial, 0.6f, Plane.FACING_UP, false);
        renderScene.createSquare(LIGHT_POSITION, planeMaterial, 0.6f, Plane.FACING_DOWN, false);
    }

    private static void setupCornellBox(Scene renderScene) {
        // Materials: AmbientMaterial Color, Diffuse Coeeff

        Material planeMaterial = new LambertMaterial(RgbColor.LIGHT_GRAY, RgbColor.LIGHT_GRAY);
        Material darkCeilingMaterial = new LambertMaterial(RgbColor.LIGHT_GRAY, RgbColor.BLACK);
        Material planeMaterial2 = new LambertMaterial(RgbColor.BLACK, RgbColor.BLACK);
        Material planeMaterialLeft = new LambertMaterial(RgbColor.DARK_BLUE, RgbColor.BLUE);
        Material planeMaterialRight = new LambertMaterial(RgbColor.DARK_RED, RgbColor.RED);

        //Material sphereMaterial2 = new PhongMaterial(RgbColor.DARK_GRAY, RgbColor.WHITE, RgbColor.WHITE, PhongMaterial.VERY_SHINY, Material.TOTAL_REF
        // LECTION, Material.NO_TRANSMISSION, 1);

        // Plane 0 LEFT
        renderScene.createPlane(new Vec3(BOX_DIMENSION, 0f, 0 ), planeMaterialLeft, Plane.FACING_LEFT);
        // Plane 1 RIGHT
        renderScene.createPlane(new Vec3( -BOX_DIMENSION, 0f, 0 ), planeMaterialRight, Plane.FACING_RIGHT);
        // Plane 2 FRONT
        //renderScene.createPlane(new Vec3( 0f, 0f, 2 * BOX_DIMENSION ), planeMaterial2, Plane.FACING_FRONT);
        // Plane 3 BACK
        renderScene.createPlane(new Vec3( 0f, 0f, -BOX_DIMENSION/2 ), planeMaterial, Plane.FACING_BACK);
        // Plane 4 UP
        renderScene.createPlane(new Vec3( 0f, -BOX_DIMENSION/1.2f, 0 ), planeMaterial, Plane.FACING_UP);
        // Plane 5 DOWN
        renderScene.createPlane(new Vec3( 0f, BOX_DIMENSION/1.2f, 0 ), darkCeilingMaterial, Plane.FACING_DOWN);
    }

    private static void raytraceScene(Window renderWindow, Scene renderScene){
        Raytracer raytracer = new Raytracer(renderScene, renderWindow, RECURSIONS, USE_GI, GI_LEVEL, GI_SAMPLES, BACKGROUND_COLOR, AMBIENT_LIGHT, ANTI_ALIASING, SHOW_PARAM_LABEL);

        raytracer.renderScene();
    }
}