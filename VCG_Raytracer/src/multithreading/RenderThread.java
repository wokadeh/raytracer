package multithreading;

import raytracer.Raytracer;
import utils.io.Log;

class RenderThread implements Runnable{
	private Thread mThread;
	private String mThreadName;
	private Raytracer mRaytracer;
	private RenderBlock mRenderBlock;

	RenderThread(String name, Raytracer raytracer, int xMin, int yMin, int xMax, int yMax){
		mThreadName = name;
		mRaytracer = raytracer;
		mRenderBlock = new RenderBlock(xMin, yMin, xMax, yMax);

		Log.warn(this, "Create Thread " + name);
	}

	@Override
	public void run() {
		Log.warn(this, "Running Thread " + mThreadName);

		try {
			mRaytracer.renderBlock(mRenderBlock);
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