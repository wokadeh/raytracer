package scene.lights;

import raytracer.Intersection;
import raytracer.Ray;
import scene.shapes.Plane;
import utils.RgbColor;
import utils.Vec3;

import java.util.ArrayList;

public class AreaLight  extends Light {

	private Plane mPlane;
	private float mDim;
	private short mRes;
	private float mSteps;
	private RgbColor mColor;
	private Vec3 mStartPoint;

	public ArrayList<PointLight> getPositionList() {
		return mPositionList;
	}

	private ArrayList<PointLight> mPositionList;


	public AreaLight(Vec3 pos, float dimension, short resolution, RgbColor color) {
		super(pos, color, "AreaLight");

		mPlane = new Plane(pos, null, Plane.FACING_DOWN);
		mColor = color;
		mDim = dimension;
		mRes = resolution;
		mPositionList = new ArrayList<>();
		mPositionList.add( new PointLight(pos, color) );

		mSteps = mDim / mRes;
		mStartPoint = new Vec3(pos.x - mDim / 2, pos.y, pos.z - mDim / 2);

		calculatePositions();
	}

	private void calculatePositions(){

		if( mRes <= 1 ){
			return;
		}

		float m, n = 0;

		for( short i = 0; i < mRes; i++ ){
			for( short j = 0; j < mRes; j++ ){
				n = mStartPoint.z + j * mSteps;
			}
			m = mStartPoint.x + i * mSteps;

			mPositionList.add(new PointLight( new Vec3(m, getPosition().y, n ), mColor));
		}
	}

	public boolean hitsAreaLight(Ray inputRay){
		// intersects plane?
		Intersection intersection = mPlane.intersect(inputRay);

		boolean hit = intersection.isHit();

		// is within borders?
		if( hit ) {
			if ((intersection.getIntersectionPoint().x < this.getPosition().x + mDim) &&
					(intersection.getIntersectionPoint().x > this.getPosition().x - mDim) &&
					(intersection.getIntersectionPoint().z < this.getPosition().z + mDim) &&
					(intersection.getIntersectionPoint().z > this.getPosition().z - mDim)) {
				return true;
			}
		}

		return false;
	}
}
