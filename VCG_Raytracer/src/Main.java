// ************************************************************ //
//                      Hochschule Duesseldorf                  //
//                                                              //
//                     Vertiefung Computergrafik                //
// ************************************************************ //

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    1. Documentation:    Did you comment your code shortly but clearly?
    2. Structure:        Did you clean up your code and put everything into the right bucket?
    3. Simplicity:       Can you shorten your code and gave all variables and methods comprehandable names?
    4. Performance:      Are all loops and everything inside really necessary?
    5. Theory:           Are you going the right way?

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 <<< The Masters of Raytracing >>>

     Master of Documentation:
     Master of Structure:
     Master of Simplicity:
     Master of Performance:
     Master of Theory:

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

import raytracer.Raytracer;
import ui.Window;
import scene.Scene;
import utils.RgbColor;
import utils.Vec3;

import java.util.ArrayList;

// Main application class. This is the routine called by the JVM to run the program.
public class Main {

    static int IMAGE_WIDTH = 800;
    static int IMAGE_HEIGHT = 600;
    static Vec3 CAM_POS = new Vec3(0, 5, -5);
    static Vec3 VIEW_POINT = new Vec3(0, 5, 5);
    static float VIEW_ANGLE = 45f;
    static float FOCAL_LENGTH = 1.3f;
    static int RECURSIONS = 1;
    static RgbColor AMBIENT_COLOR = new RgbColor(0,1,0);

    // Initial method. This is where the show begins.
    public static void main(String[] args){
        long tStart = System.currentTimeMillis();

        Window renderWindow = new Window(IMAGE_WIDTH, IMAGE_HEIGHT);

        raytraceScene(renderWindow);

        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        double elapsedSeconds = tDelta / 1000.0;

        renderWindow.setTimeToLabel(String.valueOf(elapsedSeconds));
    }

    public static void raytraceScene(Window renderWindow){
        Scene renderScene = new Scene();

        renderScene.setCamera(CAM_POS, VIEW_POINT, new Vec3(0,1,0), VIEW_ANGLE, FOCAL_LENGTH, IMAGE_WIDTH, IMAGE_HEIGHT);

        Raytracer raytracer = new Raytracer(renderScene, renderWindow, RECURSIONS, AMBIENT_COLOR);

        raytracer.renderScene();
    }
}
