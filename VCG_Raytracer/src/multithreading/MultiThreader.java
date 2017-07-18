package multithreading;

import raytracer.Raytracer;
import utils.io.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultiThreader{
	private Raytracer mRaytracer;
	private int mIntervalX;
	private int mIntervalY;
	private ArrayList<RenderBlock> mRenderBlockList;
	private List<RenderBlock> mSyncRenderBlockList;
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
		Collections.shuffle(mRenderBlockList);
		mSyncRenderBlockList = Collections.synchronizedList(mRenderBlockList);
	}

	public void startMultiThreading(){
		for(int i = 0; i < mNumberOfThreads; i++){
			new RenderThread("RayThread_" + i, mRaytracer, mSyncRenderBlockList).start();
		}
	}
}


