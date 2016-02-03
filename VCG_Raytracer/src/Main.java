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

import java.util.ArrayList;

// Main application class. This is the routine called by the JVM to run the program.
public class Main {

    static int IMAGE_WIDTH = 800;
    static int IMAGE_HEIGHT = 600;
    static int RECURSIONS = 1;

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

        Raytracer raytracer = new Raytracer(renderScene, new ArrayList(), renderWindow.getBufferedImage(), RECURSIONS);

        renderWindow.renderFrame(raytracer.getBufferedImage());
    }
}
