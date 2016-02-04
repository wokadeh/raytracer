package utils;


public class Vec2 {
    public int x;
    public int y;

    public Vec2(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString(){
        return "( " + this.x + ", " + this.y + ")";
    }
}
