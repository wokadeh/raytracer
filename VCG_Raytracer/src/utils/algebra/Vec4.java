package utils.algebra;

public class Vec4 {
    public float x;
    public float y;
    public float z;
    public float w;

    public Vec4(float x, float y, float z, float w){
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vec4(){
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.w = 0;
    }

    public boolean equals(Vec4 inputVec){
        return (this.x == inputVec.x) && (this.y == inputVec.y) && (this.z == inputVec.z) && (this.w == inputVec.w);
    }

    public Vec4 normalize(){
        double length = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w);
        return new Vec4( (float) (((double) this.x) / length), (float) (((double) this.y) / length), (float) (((double) this.z) / length), (float) (((double) this.w) / length));
    }

    public float length(){
        return (float) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w);
    }

    public Vec4 add(Vec4 inputVec){
        return new Vec4(this.x + inputVec.x, this.y + inputVec.y, this.z + inputVec.z, this.w + inputVec.w);
    }

    public Vec4 sub(Vec4 inputVec){
        return new Vec4(this.x - inputVec.x, this.y - inputVec.y, this.z - inputVec.z, this.w - inputVec.w);
    }

    public Vec4 negate(){
        return new Vec4(-this.x, -this.y, -this.z, -this.w);
    }

    public float scalar(Vec4 inputVec){
        return this.x * inputVec.x + this.y * inputVec.y + this.z * inputVec.z + this.w * inputVec.w;
    }

    public Vec4 multScalar(float value){
        return new Vec4(this.x * value, this.y * value, this.z * value, this.w * value);
    }

    public Vec4 cross(Vec4 inputVec){
        return new Vec4(
                this.y * inputVec.z - inputVec.y * this.z,
                this.z * inputVec.w - inputVec.z * this.w,
                this.w * inputVec.x - inputVec.w * this.x,
                this.x * inputVec.y - inputVec.y * this.x
        );
    }

    @Override
    public String toString(){
        return "( " + this.x + ", " + this.y + ", " + this.z + ")";
    }
}
