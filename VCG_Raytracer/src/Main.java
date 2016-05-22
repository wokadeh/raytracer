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
import scene.materials.AmbientMaterial;
import scene.materials.LambertMaterial;
import scene.materials.Material;
import scene.materials.PhongMaterial;
import scene.shapes.Plane;
import ui.Window;
import scene.Scene;
import utils.ModelImporter;
import utils.Log;
import utils.RgbColor;
import utils.Vec3;

import java.io.File;

// Main application class. This is the routine called by the JVM to run the program.
public class Main {

    static RgbColor AMBIENT_LIGHT = new RgbColor(0.1f, 0.1f, 0.1f);

    static int IMAGE_WIDTH = 800;
    static int IMAGE_HEIGHT = 600;

    static Vec3 CAM_POS = new Vec3(0, 0, 15);
    static Vec3 LOOK_AT = new Vec3(0, 0, 0);
    static Vec3 UP_VECTOR = new Vec3(0, 1, 0);

    static float VIEW_ANGLE = 75;
    static float FOCAL_LENGTH = 1;

    static float DIMENSION = 3f;

    static int RECURSIONS = 3;

    static short LIGHT_DENSITY = 1;// 40;
    static short LIGHT_SAMPLES = 1;//30;

    static int ANTI_ALIASING = Raytracer.ANTI_ALIASING_MEDIUM;

    static RgbColor BACKGROUND_COLOR = RgbColor.BLACK;

    // Initial method. This is where the show begins.
    public static void main(String[] args){
        importObject("models/Scooter-smgrps.obj");

        long tStart = System.currentTimeMillis();

        Window renderWindow = new Window(IMAGE_WIDTH, IMAGE_HEIGHT);

        draw(renderWindow);

        renderWindow.setOutputLabel(String.valueOf(stopTime(tStart)), RECURSIONS, ANTI_ALIASING);
    }

    private static void importObject(String path){
        ModelImporter.load(path);
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

        float lightHeight = DIMENSION - 0.3f;
        float lightDist = DIMENSION;

        /* Middle Light */
        //renderScene.createPointLight(new Vec3( 0, lightHeight, lightDist ), RgbColor.WHITE);

        renderScene.createAreaLight(new Vec3( 0, lightHeight, lightDist ), 2, LIGHT_DENSITY, LIGHT_SAMPLES, RgbColor.SOFT_GRAY);
    }

    private static void setupCameras(Scene renderScene) {
        renderScene.createPerspCamera(CAM_POS, LOOK_AT, UP_VECTOR, VIEW_ANGLE, FOCAL_LENGTH, IMAGE_WIDTH, IMAGE_HEIGHT);
    }

    private static void setupObjects(Scene renderScene) {
        float sphereRadius = 1;
        // Materials: AmbientMaterial Color, Diffuse Coeeff, Specular Coeff, Shininess, Material
        Material sphereMaterial1 = new PhongMaterial(RgbColor.DARK_CUSTOM, RgbColor.DARK_CUSTOM, new RgbColor(0.2f, 0.9f, 0.9f), PhongMaterial.SHINY, Material.MOST_REFLECTION, Material.NO_TRANSMISSION, 1);
        Material sphereMaterial2 = new PhongMaterial(RgbColor.DARK_CUSTOM, RgbColor.DARK_CUSTOM, RgbColor.DARK_CUSTOM, PhongMaterial.VERY_SHINY, Material.NO_REFLECTION, Material.GLASS, 1);

        renderScene.createSphere(new Vec3(-DIMENSION/2f, -DIMENSION + sphereRadius, -DIMENSION/3f+3), sphereMaterial1, sphereRadius);
        renderScene.createSphere(new Vec3(DIMENSION/2f, -DIMENSION + sphereRadius, DIMENSION/3f+3), sphereMaterial2, sphereRadius);

        Material planeMaterial = new AmbientMaterial(RgbColor.WHITE);
        renderScene.createSquare(new Vec3( 0f, DIMENSION - 0.1f, DIMENSION ), planeMaterial, 0.6f, Plane.FACING_DOWN);

    }

    private static void setupCornellBox(Scene renderScene) {
        // Materials: AmbientMaterial Color, Diffuse Coeeff
        Material planeMaterial = new LambertMaterial(RgbColor.DARK_GRAY, RgbColor.WHITE);
        Material planeMaterialLeft = new LambertMaterial(RgbColor.DARK_BLUE, RgbColor.BLUE);
        Material planeMaterialRight = new LambertMaterial(RgbColor.DARK_RED, RgbColor.RED);

        //Material sphereMaterial2 = new PhongMaterial(RgbColor.DARK_GRAY, RgbColor.WHITE, RgbColor.WHITE, PhongMaterial.VERY_SHINY, Material.TOTAL_REFLECTION, Material.NO_TRANSMISSION, 1);

        renderScene.createPlane(new Vec3( DIMENSION, 0f, 0 ), planeMaterialLeft, Plane.FACING_LEFT);
        renderScene.createPlane(new Vec3( -DIMENSION, 0f, 0 ), planeMaterialRight, Plane.FACING_RIGHT);
        renderScene.createPlane(new Vec3( 0f, 0f, 2 * DIMENSION ), planeMaterial, Plane.FACING_FRONT);
        renderScene.createPlane(new Vec3( 0f, 0f, 0f ), planeMaterial, Plane.FACING_BACK);
        renderScene.createPlane(new Vec3( 0f, -DIMENSION, 0 ), planeMaterial, Plane.FACING_UP);
        renderScene.createPlane(new Vec3( 0f, DIMENSION, 0 ), planeMaterial, Plane.FACING_DOWN);
    }

    private static void raytraceScene(Window renderWindow, Scene renderScene){
        Raytracer raytracer = new Raytracer(renderScene, renderWindow, RECURSIONS, BACKGROUND_COLOR, AMBIENT_LIGHT, ANTI_ALIASING);

        raytracer.renderScene();
    }

    private static double stopTime(long tStart){
        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        return tDelta / 1000.0;
    }
}