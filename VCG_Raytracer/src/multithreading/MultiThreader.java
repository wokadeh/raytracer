package multithreading;

import raytracer.Raytracer;
import utils.RgbColor;
import utils.algebra.Vec2;
import utils.io.Log;

public class MultiThreader{
	private Raytracer mRaytracer;
	private int mIntervalX;
	private int mIntervalY;

	public MultiThreader(Raytracer raytracer){
		mRaytracer = raytracer;
	}

	public void startMultiThreading(int numberOfThreads){
		Log.warn(this, "Start Multithreading with " + numberOfThreads * numberOfThreads + " threads");

		mIntervalX = mRaytracer.getBufferedImage().getWidth() / numberOfThreads;
		mIntervalY = mRaytracer.getBufferedImage().getHeight() / numberOfThreads;

		int counter = 0;

		for(int x = 0; x < mRaytracer.getBufferedImage().getWidth(); x += mIntervalX){
			for(int y = 0; y < mRaytracer.getBufferedImage().getHeight(); y += mIntervalY){
				counter += 1;
				new RayThread("RayThread_" + counter, mRaytracer, x, y, x  + mIntervalX, y + mIntervalY).start();
			}
		}
	}
}

class RayThread implements Runnable{
	private Thread mThread;
	private String mThreadName;
	private Raytracer mRaytracer;
	private int mXMin;
	private int mYMin;
	private int mXMax;
	private int mYMax;

	RayThread(String name, Raytracer raytracer, int xMin, int yMin, int xMax, int yMax){
		mThreadName = name;
		mRaytracer = raytracer;
		mXMin = xMin;
		mYMin = yMin;
		mXMax = xMax;
		mYMax = yMax;

		Log.warn(this, "Create Thread " + name);
	}

	@Override
	public void run() {
		Log.warn(this, "Running Thread " + mThreadName);

		try {
			// Rows
			for (int y = mYMin; y < mYMax; y++) {
				// Columns
				for (int x = mXMin; x < mXMax; x++) {
					RgbColor antiAlisedColor = mRaytracer.calculateAntiAliasedColor(y, x);
					mRaytracer.getRenderWindow().setPixel(mRaytracer.getBufferedImage(), antiAlisedColor, new Vec2(x, y));
				}
			}
		}catch (Exception e) {
			Log.error(this,"Thread " +  mThreadName + " interrupted: " + e);
		}

		mRaytracer.exportRendering();
		Log.warn(this, mThreadName + " ... finished!");
	}

	public void start () {
		Log.warn(this,"Starting " + mThreadName);
		if (mThread == null) {
			mThread = new Thread(this, mThreadName);
			mThread.start();
		}
	}
}
