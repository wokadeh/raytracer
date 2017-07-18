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
	private ArrayList<RenderBlock> mRenderBlockList1;
	private ArrayList<RenderBlock> mRenderBlockList2;
	private List<RenderBlock> mSyncRenderBlockList_1;
	private List<RenderBlock> mSyncRenderBlockList_2;
	private int mBlockSize;
	private int mNumberOfThreads;

	public MultiThreader(Raytracer raytracer, int blockSize, int numberOfThreads){
		mRaytracer = raytracer;
		mRenderBlockList1 = new ArrayList<>();
		mRenderBlockList2 = new ArrayList<>();
		mBlockSize = blockSize;
		mNumberOfThreads = numberOfThreads;
	}

	public void prepareMultiThreading(){
		Log.warn(this, "Start Multithreading with block size of " + mBlockSize  + " and " + mNumberOfThreads + " threads");

		mIntervalX = mRaytracer.getBufferedImage().getWidth() / mBlockSize;
		mIntervalY = mRaytracer.getBufferedImage().getHeight() / mBlockSize;

		for(int x = 0; x < mRaytracer.getBufferedImage().getWidth(); x += mIntervalX){
			for(int y = 0; y < mRaytracer.getBufferedImage().getHeight(); y += mIntervalY){
				mRenderBlockList1.add(new RenderBlock(x, y, x  + mIntervalX, y + mIntervalY));
				mRenderBlockList2.add(new RenderBlock(x, y, x  + mIntervalX, y + mIntervalY));
			}
		}
		Collections.shuffle(mRenderBlockList1);
		Collections.shuffle(mRenderBlockList2);
		mSyncRenderBlockList_1 = Collections.synchronizedList(mRenderBlockList1);
		mSyncRenderBlockList_2 = Collections.synchronizedList(mRenderBlockList2);
	}

	public void startMultiThreading( boolean withAA ){
		String prequence = withAA ? "SECOND_" : "FIRST_";

		for(int i = 0; i < mNumberOfThreads; i++){
			new RenderThread(prequence + "RayThread_" + i, mRaytracer, withAA ? mSyncRenderBlockList_2 : mSyncRenderBlockList_1, withAA).start();
		}
	}
}


