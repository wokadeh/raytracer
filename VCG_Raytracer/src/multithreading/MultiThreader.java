package multithreading;

import raytracer.Raytracer;
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
				new RenderThread("RayThread_" + counter, mRaytracer, x, y, x  + mIntervalX, y + mIntervalY).start();
			}
		}
	}
}


