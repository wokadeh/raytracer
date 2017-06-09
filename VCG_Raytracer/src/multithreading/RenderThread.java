package multithreading;

import raytracer.Raytracer;
import utils.io.Log;
import java.util.ArrayList;

class RenderThread implements Runnable{
	private Thread mThread;
	private String mThreadName;
	private Raytracer mRaytracer;
	private ArrayList<RenderBlock> mRenderBlockList;

	RenderThread(String name, Raytracer raytracer, ArrayList<RenderBlock> renderBlockArrayList){
		mThreadName = name;
		mRaytracer = raytracer;
		mRenderBlockList = renderBlockArrayList;

		Log.warn(this, "Create Thread " + name);
	}

	@Override
	public void run() {
		Log.warn(this, "Running Thread " + mThreadName);
		try {
			for (RenderBlock renderBlock : mRenderBlockList
			     ) {
				mRaytracer.renderBlock(renderBlock);
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