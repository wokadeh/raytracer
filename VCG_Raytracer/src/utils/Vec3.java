package utils;

public class Vec3 {
    public int x;
    public int y;
    public int z;

    public Vec3(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public boolean equals(Vec4 inputVec){
        return (this.x == inputVec.x) && (this.y == inputVec.y) && (this.z == inputVec.z);
    }
}
