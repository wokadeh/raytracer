package scene.lights;

import scene.shapes.Plane;
import utils.RgbColor;
import utils.algebra.Vec3;

import java.util.ArrayList;
import java.util.Random;

public class AreaLight  extends Light {

	private Plane mPlane;
	private float mDim;
	private short mRes;
	private short mSamples;
	private float mSteps;
	private RgbColor mColor;
	private Vec3 mStartPoint;
	private PointLight mCenterLight;

	private ArrayList<PointLight> mTempPositionList;

	public AreaLight(Vec3 pos, float dimension, short resolution, short samples, RgbColor color) {
		super(pos, color, "AreaLight");

		mPlane = new Plane(pos, null, Plane.FACING_DOWN);
		mColor = color;
		mDim = dimension;
		mRes = resolution;
		mSamples = samples;

		mCenterLight = new PointLight(getPosition(), mColor);

		mTempPositionList = new ArrayList<>();

		mSteps = mDim / mRes;
		mStartPoint = new Vec3(pos.x - mDim / 2, pos.y, pos.z - mDim / 2);

		fillTempPointLightList();
	}

	public ArrayList<PointLight> getPositionList() {

		ArrayList<PointLight> positionList = new ArrayList<>();
		positionList.add( mCenterLight );

		if( mRes <= 1 ){
			return positionList;
		}

		ArrayList<Integer> sampleNumberList = fillSampleNumberList();

		for( Integer i : sampleNumberList ){
			positionList.add( mTempPositionList.get( i - 1 ) );
		}

		return positionList;
	}

	private void fillTempPointLightList() {
		float m, n = 0;

		for( short i = 0; i < mRes; i++ ){
			for( short j = 0; j < mRes; j++ ){
				n = mStartPoint.z + j * mSteps;
			}
			m = mStartPoint.x + i * mSteps;

			mTempPositionList.add( new PointLight( new Vec3(m, getPosition().y, n ), mColor) );
		}
	}

	private ArrayList<Integer> fillSampleNumberList(){
		Random rand = new Random();

		ArrayList<Integer> sampleNumberList = new ArrayList<>();

		for(int i = 0; i < mSamples; i++){
			sampleNumberList.add( rand.nextInt( mRes ) + 1 );
			// same multiple values possible!!
		}
		return sampleNumberList;
	}


}
