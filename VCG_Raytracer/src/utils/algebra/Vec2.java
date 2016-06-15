package utils.algebra;


public class Vec2 {
    public float x;
    public float y;

    public Vec2(float x, float y){
        this.x = x;
        this.y = y;
    }

    public Vec2(){
        this.x = 0;
        this.y = 0;
    }

    public boolean equals(Vec2 inputVec){
        return (this.x == inputVec.x) && (this.y == inputVec.y);
    }

    public Vec2 normalize(){
        float length = this.length();
        return new Vec2(this.x / length, this.y / length);
    }

    public float length(){
        return (float) Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public Vec2 add(Vec2 inputVec){
        return new Vec2(this.x + inputVec.x, this.y + inputVec.y);
    }

    public Vec2 sub(Vec2 inputVec){
        return new Vec2(this.x - inputVec.x, this.y - inputVec.y);
    }

    public Vec2 negate(){
        return new Vec2(-this.x, -this.y);
    }

    public float scalar(Vec2 inputVec){
        return this.x * inputVec.x + this.y * inputVec.y;
    }

    public Vec2 multScalar(float value){
        return new Vec2(this.x * value, this.y * value);
    }

    public Vec2 cross(Vec2 inputVec){
        return new Vec2(
                this.y * inputVec.x - inputVec.y * this.x,
                this.x * inputVec.y - inputVec.x * this.y
        );
    }

    @Override
    public String toString(){
        return "( " + this.x + ", " + this.y + ")";
    }
}
