package utils;

public class Vec3 {
    public float x;
    public float y;
    public float z;

    public Vec3(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public boolean equals(Vec3 inputVec){
        return (this.x == inputVec.x) && (this.y == inputVec.y) && (this.z == inputVec.z);
    }

    public Vec3 normalize(){
        double length = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        return new Vec3( (float) (((double) this.x) / length), (float) (((double) this.y) / length), (float) (((double) this.z) / length));
    }

    public Vec3 add(Vec3 inputVec){
        return new Vec3(this.x + inputVec.x, this.y + inputVec.y, this.z + inputVec.z);
    }

    public Vec3 sub(Vec3 inputVec){
        return new Vec3(this.x - inputVec.x, this.y - inputVec.y, this.z - inputVec.z);
    }

    public float scalar(Vec3 inputVec){
        return this.x * inputVec.x + this.y * inputVec.y + this.z * inputVec.z;
    }

    public Vec3 multScalar(float value){
        return new Vec3(this.x * value, this.y * value, this.z * value);
    }

    public Vec3 cross(Vec3 inputVec){
        return new Vec3(
                this.y * inputVec.z - inputVec.y * this.z,
                this.z * inputVec.x - inputVec.z * this.x,
                this.x * inputVec.y - inputVec.x * this.y
        );
    }

    @Override
    public String toString(){
        return "( " + this.x + ", " + this.y + ", " + this.z + ")";
    }
}
