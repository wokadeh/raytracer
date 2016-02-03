package utils;

public class RgbColor {
    public float red;
    public float green;
    public float blue;

    public RgbColor(float r, float g, float b){
        this.red = r;
        this.green = g;
        this.blue = b;

        clamp();
    }

    public void add(float r, float g, float b){
        this.red += r;
        this.green += g;
        this.blue += g;

        clamp();
    }

    public void add(RgbColor color){
        this.red += color.red;
        this.green += color.green;
        this.blue += color.blue;

        clamp();
    }

    public void mult(RgbColor color){
        this.red *= color.red;
        this.green *= color.green;
        this.blue *= color.blue;
    }

    public void mult(float value){
        this.red *= value;
        this.green *= value;
        this.blue *= value;
    }

    private void clamp(){
        if( this.red > 1 ) this.red = 1;
        if( this.green > 1 ) this.green = 1;
        if( this.blue > 1 ) this.blue = 1;
    }
}
