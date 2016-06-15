package utils.io;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageExporter {
    public static void saveImageToPng(BufferedImage image, String fileName){
        File outFile = new File(fileName);
        try {
            ImageIO.write(image, "png", outFile);
        } catch (Exception e) {
            System.err.println(e.getMessage()); // print any ImageExporter errors to stderr.
        }
    }
}
