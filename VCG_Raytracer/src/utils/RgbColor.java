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

    public int getRGB(){
        return ((int) (this.red * 255f) << 16) + ((int) (this.green * 255f) << 8) + ((int) (this.blue * 255f));
    }

    private void clamp(){
        if( this.red > 1 ) this.red = 1f;
        if( this.green > 1 ) this.green = 1f;
        if( this.blue > 1 ) this.blue = 1f;

        if( this.red < 0 ) this.red = 0f;
        if( this.green < 0 ) this.green = 0f;
        if( this.blue < 0 ) this.blue = 0f;
    }

    @Override
    public String toString(){
        return "( " + this.red + ", " + this.green + ", " + this.blue + " )";
    }

    public boolean equals(RgbColor inColor){
        return inColor.red == this.red && inColor.green == this.green && inColor.blue == this.blue;
    }
}
