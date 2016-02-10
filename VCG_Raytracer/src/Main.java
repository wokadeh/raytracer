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
import scene.Material;
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

    static float VIEW_ANGLE = 54.43f;
    static float FOCAL_LENGTH = 35f;

    static int RECURSIONS = 1;

    static RgbColor AMBIENT_COLOR = new RgbColor(0, 1, 0);
    static RgbColor BACKGROUND_COLOR = new RgbColor(0.2f, 0.2f, 0.3f);

    // Initial method. This is where the show begins.
    public static void main(String[] args){
        long tStart = System.currentTimeMillis();

        Window renderWindow = new Window(IMAGE_WIDTH, IMAGE_HEIGHT);

        Scene renderScene = new Scene();

        setupScene(renderScene);

        raytraceScene(renderWindow, renderScene);

        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        double elapsedSeconds = tDelta / 1000.0;

        renderWindow.setTimeToLabel(String.valueOf(elapsedSeconds));
    }

    public static void setupScene(Scene renderScene){
        renderScene.createPerspCamera(CAM_POS, LOOK_AT, UP_VECTOR, VIEW_ANGLE, FOCAL_LENGTH, IMAGE_WIDTH, IMAGE_HEIGHT);

        Material sphereMaterial = new Material(AMBIENT_COLOR, new RgbColor(0.4f,0.4f,0.4f), new RgbColor(0.8f,0.8f,0.8f), 10, Material.PHONG);

        renderScene.createSphere(new Vec3(0, -1, 0), sphereMaterial, 1f);
        renderScene.createPointLight(new Vec3( 0, 20, 0 ), new RgbColor(0.8f, 0.8f, 0.6f));
    }

    public static void raytraceScene(Window renderWindow, Scene renderScene){
        Raytracer raytracer = new Raytracer(renderScene, renderWindow, RECURSIONS, AMBIENT_COLOR, BACKGROUND_COLOR);

        raytracer.renderScene();
    }
}
