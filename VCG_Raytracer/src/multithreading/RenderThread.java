package multithreading;

import raytracer.Raytracer;
import utils.io.Log;

import java.util.List;

class RenderThread implements Runnable{
	private Thread mThread;
	private String mThreadName;
	private Raytracer mRaytracer;
	private List<RenderBlock> mSyncRenderBlockList;

	RenderThread(String name, Raytracer raytracer, List<RenderBlock> renderBlockArrayList){
		mThreadName = name;
		mRaytracer = raytracer;
		mSyncRenderBlockList = renderBlockArrayList;

		Log.warn(this, "Create Thread " + name);
	}

	@Override
	public void run() {
		Log.warn(this, "Running Thread " + mThreadName);
		try {
			while (!mSyncRenderBlockList.isEmpty()){
				mRaytracer.renderBlock(mSyncRenderBlockList.remove(0), false);
			}


		}catch (Exception e) {
			Log.error(this,"Thread " +  mThreadName + " interrupted: " + e);
		}

		mRaytracer.exportRendering();
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