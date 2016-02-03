package raytracer;

import scene.IShape;

import java.awt.image.BufferedImage;
import java.util.List;

public class Raytracer {

    BufferedImage mBufferedImage;

    public Raytracer(List<IShape> shapeList, BufferedImage bufferedImage){
        mBufferedImage = bufferedImage;
    }

    public BufferedImage getBufferedImage(){
        return mBufferedImage;
    }
}
