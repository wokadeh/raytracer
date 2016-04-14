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
import ui.Window;
import scene.Scene;
import utils.RgbColor;
import utils.Vec3;

// Main application class. This is the routine called by the JVM to run the program.
public class Main {

    static RgbColor AMBIENT_LIGHT = new RgbColor(0.1f, 0.1f, 0.1f);

    static int IMAGE_WIDTH = 800;
    static int IMAGE_HEIGHT = 600;

    static Vec3 CAM_POS = new Vec3(0, 0, 5);
    static Vec3 LOOK_AT = new Vec3(0, 0, 0);
    static Vec3 UP_VECTOR = new Vec3(0, 1, 0);

    static float VIEW_ANGLE = 160;
    static float FOCAL_LENGTH = 1;

    static float DIMENSION = 3f;

    static int RECURSIONS = 3;

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

    }
    private static void setupCameras(Scene renderScene) {
        renderScene.createPerspCamera(CAM_POS, LOOK_AT, UP_VECTOR, VIEW_ANGLE, FOCAL_LENGTH, IMAGE_WIDTH, IMAGE_HEIGHT);
    }


    private static void raytraceScene(Window renderWindow, Scene renderScene){
        Raytracer raytracer = new Raytracer(renderScene, renderWindow, RECURSIONS, BACKGROUND_COLOR, AMBIENT_LIGHT);

        raytracer.renderScene();
    }

    private static double stopTime(long tStart){
        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        return tDelta / 1000.0;
    }
}


/* Verbesserungen
- Sicherstellen, dass die Normale nur einmal normalisiert wird


*/