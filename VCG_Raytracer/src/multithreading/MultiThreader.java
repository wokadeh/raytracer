package multithreading;

import raytracer.Raytracer;
import utils.io.Log;

import java.util.ArrayList;

public class MultiThreader{
	private Raytracer mRaytracer;
	private int mIntervalX;
	private int mIntervalY;
	private ArrayList<RenderBlock> mRenderBlockList;

	public MultiThreader(Raytracer raytracer){
		mRaytracer = raytracer;
		//mRenderBlockList = new ArrayList<>();
		//mRenderBlockList.add(new RenderBlock(xMin, yMin, xMax, yMax));
	}

	public void startMultiThreading(int numberOfThreads){
		Log.warn(this, "Start Multithreading with " + numberOfThreads * numberOfThreads + " threads");

		mIntervalX = mRaytracer.getBufferedImage().getWidth() / numberOfThreads;
		mIntervalY = mRaytracer.getBufferedImage().getHeight() / numberOfThreads;

		int counter = 0;

		for(int x = 0; x < mRaytracer.getBufferedImage().getWidth(); x += mIntervalX){
			for(int y = 0; y < mRaytracer.getBufferedImage().getHeight(); y += mIntervalY){
				counter += 1;
				mRenderBlockList = new ArrayList<>();
				mRenderBlockList.add(new RenderBlock(x, y, x  + mIntervalX, y + mIntervalY));
				new RenderThread("RayThread_" + counter, mRaytracer, mRenderBlockList).start();
			}
		}
	}
}


