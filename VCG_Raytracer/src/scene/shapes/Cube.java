package scene.shapes;


import raytracer.Intersection;
import raytracer.Ray;
import raytracer.RaytracerMethods;
import scene.materials.Material;
import utils.algebra.Matrix4x4;
import utils.algebra.Vec3;

import java.util.ArrayList;

public class Cube extends Shape{

	private ArrayList<Shape> mPlaneList;

	private float mDim;

	public Cube(Vec3 pos, Material mat, float dimension){
		super(pos, mat, new Matrix4x4().translateXYZ(pos), "CUBE");

		mPlaneList = new ArrayList<>();

		mDim = dimension;

		mPlaneList.add( new Square(new Vec3(0, 0, mDim).add(pos), mat, mDim, Plane.FACING_BACK));
		mPlaneList.add( new Square(new Vec3(0, 0, -mDim).add(pos), mat, mDim, Plane.FACING_FRONT));
		mPlaneList.add( new Square(new Vec3(0, mDim, 0).add(pos), mat, mDim, Plane.FACING_UP));
		mPlaneList.add( new Square(new Vec3(0, -mDim, 0).add(pos), mat, mDim, Plane.FACING_DOWN));
		mPlaneList.add( new Square(new Vec3(mDim, 0, 0).add(pos), mat, mDim, Plane.FACING_RIGHT));
		mPlaneList.add( new Square(new Vec3(-mDim, 0, 0).add(pos), mat, mDim, Plane.FACING_LEFT));
	}

	@Override
	public Intersection intersect(Ray ray) {
		return RaytracerMethods.getIntersectionOnShapes(ray, null, mPlaneList);
	}
}

