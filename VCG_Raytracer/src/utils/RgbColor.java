package utils;

public class RgbColor {
    public float red;
    public float green;
    public float blue;

    public RgbColor(float r, float g, float b){
        red = r;
        green = g;
        blue = b;

        clamp();
    }

    public void add(float r, float g, float b){
        red += r;
        green += g;
        blue += g;

        clamp();
    }

    public void add(RgbColor color){
        red += color.red;
        green += color.green;
        blue += color.blue;

        clamp();
    }

    private void clamp(){
        if( red > 1 ) red = 1;
        if( green > 1 ) green = 1;
        if( blue > 1 ) blue = 1;
    }
}
