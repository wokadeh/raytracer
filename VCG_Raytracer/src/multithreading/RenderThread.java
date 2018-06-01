package multithreading;

import raytracer.Raytracer;
import utils.io.Log;

import java.util.List;

class RenderThread implements Runnable{
	private Thread mThread;
	private String mThreadName;
	private Raytracer mRaytracer;
	private List<RenderBlock> mSyncRenderBlockList;
	private boolean mWithAA = false;

	RenderThread(String name, Raytracer raytracer, List<RenderBlock> renderBlockArrayList, boolean withAA){
		mThreadName = name;
		mRaytracer = raytracer;
		mSyncRenderBlockList = renderBlockArrayList;
		mWithAA = withAA;

		Log.warn(this, "Create Thread " + name);
	}

	@Override
	public void run() {
		Log.warn(this, "Running Thread " + mThreadName + " with " + mSyncRenderBlockList.size() + " items ");
		try {
			while (!mSyncRenderBlockList.isEmpty()){
				mRaytracer.renderBlock(mSyncRenderBlockList.remove(0), mWithAA);
			}

		}catch (Exception e) {
			Log.error(this,"Thread " +  mThreadName + " interrupted: " + e);
		}

		mRaytracer.exportFinalRendering();
		Log.warn(this, mThreadName + " ... finished!");

		if(mSyncRenderBlockList.isEmpty()){
			mRaytracer.callback();
		}
	}

	public void start () {
		Log.warn(this,"Starting " + mThreadName);
		if (mThread == null) {
			mThread = new Thread(this, mThreadName);
			mThread.start();
		}
	}
}