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

// Main application class. This is the routine called by the JVM to run the program.
public class Main {

    static int IMAGE_WIDTH = 800;
    static int IMAGE_HEIGHT = 600;

    // Initial method. This is where the show begins.
    public static void main(String[] args){

        Window renderWindow = new Window(IMAGE_WIDTH, IMAGE_HEIGHT);

        Scene renderScene = new Scene();

        long tStart = System.currentTimeMillis();

        Raytracer raytracer = new Raytracer(renderScene.getShapeList(), renderWindow.getBufferedImage());

        renderWindow.renderFrame(raytracer.getBufferedImage());

        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        double elapsedSeconds = tDelta / 1000.0;

        renderWindow.setTimeToLabel(String.valueOf(elapsedSeconds));
    }
}
