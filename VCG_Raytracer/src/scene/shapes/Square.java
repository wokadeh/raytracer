package scene.shapes;

import raytracer.Intersection;
import raytracer.Ray;
import scene.materials.Material;
import utils.algebra.Matrix4x4;
import utils.algebra.Vec3;

public class Square extends Shape {

	private Plane mPlane;
	private float mDim;

	public Square(Vec3 pos, Material mat, float dimension, int facingDirection) {
		super(pos, mat, new Matrix4x4().translateXYZ(pos), "SQUARE" + facingDirection);

		mPlane = new Plane(pos, mat, facingDirection);
		mDim = dimension;
	}

	public Square(Vec3 pos, Material mat, float dimension, int facingDirection, boolean raytraced) {
		this(pos, mat, dimension, facingDirection);

		this.raytraced = raytraced;
	}

	public Square(Vec3 pos, Material mat, float dimension, int facingDirection, boolean raytraced, boolean giOff) {
		this(pos, mat, dimension, facingDirection, raytraced);

		this.raytraced = raytraced;
	}

	@Override
	public Intersection intersect(Ray ray) {
		return mPlane.intersect(ray, mDim, mDim);
	}
}
