package utils;

public class Vec4 {
    public int x;
    public int y;
    public int z;
    public int w;

    public Vec4(int x, int y, int z, int w){
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public boolean equals(Vec4 inputVec){
        return (this.x == inputVec.x) && (this.y == inputVec.y) && (this.z == inputVec.z) && (this.w == inputVec.w);
    }
}
