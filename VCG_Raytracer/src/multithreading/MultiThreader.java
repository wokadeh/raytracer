package multithreading;

import raytracer.Raytracer;
import utils.io.Log;

import java.util.ArrayList;
import java.util.Collections;

public class MultiThreader{
	private Raytracer mRaytracer;
	private int mIntervalX;
	private int mIntervalY;
	private ArrayList<RenderBlock> mRenderBlockList;
	private int mBlockSize;
	private int mNumberOfThreads;

	public MultiThreader(Raytracer raytracer, int blockSize, int numberOfThreads){
		mRaytracer = raytracer;
		mRenderBlockList = new ArrayList<>();
		mBlockSize = blockSize;
		mNumberOfThreads = numberOfThreads;
	}

	public void prepareMultiThreading(){
		Log.warn(this, "Start Multithreading with block size of " + mBlockSize  + " and " + mNumberOfThreads + " threads");

		mIntervalX = mRaytracer.getBufferedImage().getWidth() / mBlockSize;
		mIntervalY = mRaytracer.getBufferedImage().getHeight() / mBlockSize;

		for(int x = 0; x < mRaytracer.getBufferedImage().getWidth(); x += mIntervalX){
			for(int y = 0; y < mRaytracer.getBufferedImage().getHeight(); y += mIntervalY){
				mRenderBlockList.add(new RenderBlock(x, y, x  + mIntervalX, y + mIntervalY));
			}
		}
	}

	public void startMultiThreading(){
		int intervals = mRenderBlockList.size() / mNumberOfThreads;
		int counter = 0;
		for(int i = 0; i < mRenderBlockList.size() - intervals + 1; i+= intervals){
			ArrayList<RenderBlock> renderSubList = new ArrayList<>();
			counter ++;
			int start = i;

			for(int j = start; j < start + intervals; j++) {
				renderSubList.add(mRenderBlockList.get(j));
			}
			//Collections.shuffle(renderSubList);
			new RenderThread("RayThread_" + counter, mRaytracer, renderSubList).start();
		}
	}
}


