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
import scene.materials.LambertMaterial;
import scene.materials.Material;
import scene.materials.PhongMaterial;
import scene.shapes.Plane;
import ui.Window;
import scene.Scene;
import utils.RgbColor;
import utils.Vec3;

// Main application class. This is the routine called by the JVM to run the program.
public class Main {

    static int IMAGE_WIDTH = 800;
    static int IMAGE_HEIGHT = 600;

    static Vec3 CAM_POS = new Vec3(0, 0, 5);
    static Vec3 LOOK_AT = new Vec3(0, 0, 0);
    static Vec3 UP_VECTOR = new Vec3(0, 1, 0);

    static float VIEW_ANGLE = 35;
    static float FOCAL_LENGTH = 10;

    static int RECURSIONS = 2;

    static RgbColor AMBIENT_COLOR = RgbColor.LIGHT_GRAY;
    static RgbColor BACKGROUND_COLOR = RgbColor.BLACK;

    // Initial method. This is where the show begins.
    public static void main(String[] args){
        long tStart = System.currentTimeMillis();

        Window renderWindow = new Window(IMAGE_WIDTH, IMAGE_HEIGHT);

        draw(renderWindow);

        renderWindow.setTimeToLabel(String.valueOf(stopTime(tStart)));
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
        renderScene.createPointLight(new Vec3( 0, 1, 0 ), RgbColor.WHITE);
       //renderScene.createPointLight(new Vec3( 0, -1, 0 ), RgbColor.WHITE);
       // renderScene.createPointLight(new Vec3( -200, -200, 0 ), RgbColor.WHITE);
    }

    private static void setupCameras(Scene renderScene) {
        renderScene.createPerspCamera(CAM_POS, LOOK_AT, UP_VECTOR, VIEW_ANGLE, FOCAL_LENGTH, IMAGE_WIDTH, IMAGE_HEIGHT);
    }

    private static void setupObjects(Scene renderScene) {
        // Materials: Ambient Color, Diffuse Coeeff, Specular Coeff, Shininess, Material
        Material sphereMaterial1 = new PhongMaterial(AMBIENT_COLOR, RgbColor.MAGENTA, RgbColor.GRAY, 10);
        Material sphereMaterial2 = new PhongMaterial(AMBIENT_COLOR, RgbColor.BLUE, RgbColor.GRAY, 10);

        renderScene.createSphere(new Vec3(-0.8f, 0.5f, 0), sphereMaterial1, 0.1f);
        renderScene.createSphere(new Vec3(0f, -0.5f, 0f), sphereMaterial2, 0.1f);
    }

    private static void setupCornellBox(Scene renderScene) {
        // Materials: Ambient Color, Diffuse Coeeff
        Material planeMaterial = new LambertMaterial(RgbColor.BLACK, RgbColor.WHITE);
        Material planeMaterialLeft = new LambertMaterial(RgbColor.BLACK, RgbColor.RED);
        Material planeMaterialRight = new LambertMaterial(RgbColor.BLACK, RgbColor.GREEN);

        renderScene.createPlane(new Vec3( 1f, 0f, 0 ), planeMaterialLeft, Plane.FACING_LEFT);
        renderScene.createPlane(new Vec3( -1f, 0f, 0 ), planeMaterialRight, Plane.FACING_RIGHT);
        renderScene.createPlane(new Vec3( 0f, 0f, -1f ), planeMaterial, Plane.FACING_FRONT);
        renderScene.createPlane(new Vec3( 0f, -1f, 0 ), planeMaterial, Plane.FACING_UP);
        renderScene.createPlane(new Vec3( 0f, 1f, 0 ), planeMaterial, Plane.FACING_DOWN);
    }

    private static void raytraceScene(Window renderWindow, Scene renderScene){
        Raytracer raytracer = new Raytracer(renderScene, renderWindow, RECURSIONS, AMBIENT_COLOR, BACKGROUND_COLOR);

        raytracer.renderScene();
    }

    private static double stopTime(long tStart){
        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        return tDelta / 1000.0;
    }
}
