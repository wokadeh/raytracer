package multithreading;

import raytracer.Raytracer;
import utils.RgbColor;
import utils.algebra.Vec2;
import utils.io.Log;

public class MultiThreader{
	private Thread mThread;
	private String mThreadName;
	private Raytracer mRaytracer;

	public MultiThreader(Raytracer raytracer){
		mRaytracer = raytracer;
	}

	public void startMultiThreading(int numberOfThreads){
		Log.warn(this, "Start Multithreading with " + numberOfThreads + " threads");

		RayThread rayThread0 = new RayThread("RayThread_" + "0", mRaytracer, 0, 0, mRaytracer.getBufferedImage().getWidth(), (int)(mRaytracer.getBufferedImage().getHeight() / 2f));
		RayThread rayThread1 = new RayThread("RayThread_" + "1", mRaytracer, 0, (int)(mRaytracer.getBufferedImage().getHeight() / 2f), mRaytracer.getBufferedImage().getWidth(), mRaytracer.getBufferedImage().getHeight());

		rayThread0.run();
		rayThread1.run();

//		// Rows
//		for (int y = 0; y < mRaytracer.getBufferedImage().getHeight(); y++) {
//			// Columns
//			for (int x = 0; x < mRaytracer.getBufferedImage().getWidth(); x++) {
//
//
//			}
//		}


//		for(int i = 0; i < numberOfThreads; i++){
//
//			}
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

		// Rows
		for (int y = mYMin; y < mYMax; y++) {
			// Columns
			for (int x = mXMin; x < mXMax; x++) {
				RgbColor antiAlisedColor = mRaytracer.calculateAntiAliasedColor(y, x);
				mRaytracer.getRenderWindow().setPixel(mRaytracer.getBufferedImage(), antiAlisedColor, new Vec2(x, y));
			}
		}

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
