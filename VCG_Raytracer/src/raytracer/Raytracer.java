/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    1. Send primary ray
    2. intersection test with all shapes
    3. if hit:
    3a: send secondary ray to the light source
    3b: 2
        3b.i: if hit:
            - Shape is in the shade
            - Pixel color = ambient value
        3b.ii: in NO hit:
            - calculate local illumination
    4. if NO hit:
        - set background color

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package raytracer;

import multithreading.MultiThreader;
import multithreading.RenderBlock;
import scene.lights.Light;
import scene.Scene;
import scene.shapes.Shape;
import ui.Window;
import utils.*;
import utils.algebra.Vec2;
import utils.algebra.Vec3;
import utils.io.Log;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class Raytracer {

    public static final int TINY_BLOCK = 5;
    public static final int MEDIUM_BLOCK = 10;
    public static final int LARGE_BLOCK = 20;
    public static final int HUGE_BLOCK = 40;
    public static final int GIANT_BLOCK = 100;

    public static final int ANTI_ALIASING_NONE = 1;
    public static final int ANTI_ALIASING_MEDIUM = 4;
    public static final int ANTI_ALIASING_HIGH = 9;
    public static final int ANTI_ALIASING_INSANE = 16;
    public static final int ANTI_ALIASING_GODLIKE = 25;

    public static final int MULTI_THREADING_NONE = 1;
    public static final int MULTI_THREADING_LOW = 2;
    public static final int MULTI_THREADING_MEDIUM = 4;
    public static final int MULTI_THREADING_HIGH = 8;
    public static final int MULTI_THREADING_INSANE = 16;
    public static final int MULTI_THREADING_GODLIKE = 64;

    private static final float AA_THRESHOLD = 0.05f;

    private static float GI_FACTOR = (float) (1f / Math.PI);
    private BufferedImage mBufferedImage;
    private ArrayList<Shape> mShapeList;
    private ArrayList<Light> mLightList;

    private Scene mScene;
    private Window mRenderWindow;
    private Vec3[][] mAliasedColorMap;
    private Vec3[][] mEdgesMap;

    private MultiThreader mRayMultiThreader;

    private int mMaxRecursions;
    private int mGiLevel;
    private int mGiSamples;

    private float mAntiAliasingCounter;
    private float mAntiAliasingSamples;
    private int mAntiAliasingDim;

    private int mBlockSize;
    private int mNumberOfThreads;

    private boolean mUseGI;
    private float mPDFFactor;
    private boolean mUseAO;
    private int mNumberOfAOSamples;
    private float mAoMaxDistance;

    private RgbColor mBackgroundColor;
    private RgbColor mAmbientLight;

    private boolean mDebug;
    private long tStart;

    private int mThreadsRendered = 0;

    public Raytracer(Scene scene, Window renderWindow, int recursions, boolean useGi, int giLevel, int giSamples, boolean useAo, int numberOfAoSamples, float maxDistance, RgbColor backColor, RgbColor ambientLight, int antiAliasingSamples, int blockSize, int numberOfThreads, boolean debugOn){
        Log.print(this, "Init");
        mMaxRecursions = recursions;
        mUseGI = useGi;
        mUseAO = useAo;
        mNumberOfAOSamples = numberOfAoSamples;
        mAoMaxDistance = maxDistance;

        if(mUseGI) {
            mGiLevel = giLevel;
            mGiSamples = giSamples;
        }

        mAntiAliasingSamples = antiAliasingSamples;
        mAntiAliasingDim = (int) Math.round(Math.sqrt(mAntiAliasingSamples));
        mAntiAliasingCounter = 1f / (mAntiAliasingDim);

        mBufferedImage = renderWindow.getBufferedImage();
        mAliasedColorMap = new Vec3[mBufferedImage.getWidth() * mAntiAliasingDim][mBufferedImage.getHeight() * mAntiAliasingDim];
        mEdgesMap = new Vec3[mBufferedImage.getWidth()][mBufferedImage.getHeight()];

        mBackgroundColor = backColor;
        mAmbientLight = ambientLight;
        mScene = scene;
        mRenderWindow = renderWindow;
        mShapeList = scene.getShapeList();
        mLightList = scene.getLightList();
        mDebug = debugOn;
        tStart = System.currentTimeMillis();
        mBlockSize = blockSize;
        mNumberOfThreads = numberOfThreads;

        this.exportRendering();

        mPDFFactor = (float) (1f / (2f* Math.PI));
    }

    public void callback(){
        mThreadsRendered++;

        Log.print(this, "Thread rendered " + mThreadsRendered);

        if(mThreadsRendered == mNumberOfThreads) {
            Log.print(this, "Finished rendering!");

            this.createEdgeMap();

            Log.print(this, "Start SECOND rendering at " + String.valueOf(stopTime(tStart)));
            mRayMultiThreader.startMultiThreading(true);
        }
    }

    public BufferedImage getBufferedImage() {
        return mBufferedImage;
    }

    public Window getRenderWindow() {
        return mRenderWindow;
    }

    public void exportRendering(){
        mRenderWindow.exportRendering(String.valueOf(stopTime(tStart)), mMaxRecursions, (int )mAntiAliasingSamples, mDebug, mGiLevel, mGiSamples);
    }

    private static double stopTime(long tStart){
        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        return tDelta / 1000.0;
    }

    public void renderScene(){
        Log.print(this, "Prepare rendering at " + String.valueOf(stopTime(tStart)));

        mRayMultiThreader = new MultiThreader(this, mBlockSize, mNumberOfThreads);
        mRayMultiThreader.prepareMultiThreading();

        Log.print(this, "Start FIRST rendering at " + String.valueOf(stopTime(tStart)));
        mRayMultiThreader.startMultiThreading(false);
    }

    public void renderBlock(RenderBlock renderBlock, boolean withAA){
     // Rows
        for (int y = renderBlock.yMin; y < renderBlock.yMax; y++) {
            // Columns
            for (int x = renderBlock.xMin; x < renderBlock.xMax; x++) {
                if(!withAA) {
                    this.renderAliased(y, x);
                }
                else {
                    this.renderAntiAliased(y, x);
                }
            }
        }
    }

    private void renderAntiAliased(int y, int x) {
        RgbColor renderColor;
        try {
            if(mEdgesMap[x][y].equals(RgbColor.WHITE.colors)){
                renderColor = this.calculateAntiAliasedColor(y, x);
            }
            else{
                renderColor = new RgbColor( mAliasedColorMap[x * mAntiAliasingDim][y * mAntiAliasingDim] );
            }

            this.getRenderWindow().setPixel(this.getBufferedImage(), renderColor, new Vec2(x, y));

        }catch (Exception e){
            Log.error(this, e.getMessage());
        }
    }

    private void renderAliased(int y, int x) {
        mAliasedColorMap[x * mAntiAliasingDim][y * mAntiAliasingDim] = this.sendPrimaryRay(new Vec2(x, y)).colors;

        // Fill already Edges Map black
        mEdgesMap[x][y] = new Vec3(0, 0, 0);

        // DEBUG ONLY
        //this.getRenderWindow().setPixel(this.getBufferedImage(), new RgbColor(mAliasedColorMap[x][y]), new Vec2(x, y));
    }

    private void createEdgeMap() {
            Log.print(this, "Create Edge Map");

            for (int x = 1; x < mBufferedImage.getWidth() - 1; x++) {
                for (int y = 1; y < mBufferedImage.getHeight() - 1; y++) {
                    if (this.hasToBeAntiAliased(x, y)) {
                        mEdgesMap[x][y] = RgbColor.WHITE.colors;
                        mEdgesMap[x + 1][y + 1] = RgbColor.WHITE.colors;
                        mEdgesMap[x - 1][y - 1] = RgbColor.WHITE.colors;
                        mEdgesMap[x + 1][y - 1] = RgbColor.WHITE.colors;
                        mEdgesMap[x - 1][y + 1] = RgbColor.WHITE.colors;
                    }
                    // DEBUG ONLY
                    //this.getRenderWindow().setPixel(this.getBufferedImage(), new RgbColor(mEdgesMap[x][y]), new Vec2(x, y));
                }
            }
            Log.print(this, "Edge Map finished!");
    }


    public RgbColor calculateAntiAliasedColor(int y, int x) {
        Vec2 screenPosition;
        Vec3 antiAliasedColor = new Vec3();

        int preX = (y * mAntiAliasingDim) - mAntiAliasingDim + 1;
        int preY = (x * mAntiAliasingDim) - mAntiAliasingDim + 1;

        // Don't use random, because it produces artifacts!
        // Avoid calculating the same color twice, so use the mAliasedColorMap
        for(float n = -1f; n < 0f; n += mAntiAliasingCounter){
            for(float m = -1f; m < 0f; m += mAntiAliasingCounter){
                float xc = (x + m);
                float yc = (y + n);

                int yA = preX + (int) (n * mAntiAliasingDim);
                int xA = preY + (int) (m * mAntiAliasingDim);

                if(mAliasedColorMap[xA][yA] == null) {
                    screenPosition = new Vec2(xc, yc);
                    mAliasedColorMap[xA][yA] = this.sendPrimaryRay(screenPosition).colors;
                }

                antiAliasedColor = antiAliasedColor.add(mAliasedColorMap[xA][yA].multScalar(1f / (mAntiAliasingSamples)));
            }
        }

        return new RgbColor(antiAliasedColor);
    }

    private Ray createPrimaryRay(float x, float y){
        Vec3 startPoint = mScene.getCamPos();
        Vec3 destinationDir = mScene.getCamPixelDirection(new Vec2(x, y));
        return new Ray(startPoint, destinationDir, 1f);
    }

    private boolean hasToBeAntiAliased(int x, int y){

        int xA = x * mAntiAliasingDim;
        int yA = y * mAntiAliasingDim;

        float value = mAliasedColorMap[xA][yA].length();

        if ((value - mAliasedColorMap[xA+mAntiAliasingDim][yA+mAntiAliasingDim].length()) > AA_THRESHOLD ||
            (value - mAliasedColorMap[xA-mAntiAliasingDim][yA-mAntiAliasingDim].length()) > AA_THRESHOLD ||
            (value - mAliasedColorMap[xA-mAntiAliasingDim][yA+mAntiAliasingDim].length()) > AA_THRESHOLD ||
            (value - mAliasedColorMap[xA+mAntiAliasingDim][yA-mAntiAliasingDim].length()) > AA_THRESHOLD) {
            return true;
        }
        return false;
    }

    private RgbColor sendPrimaryRay(Vec2 pixelPoint){
        return traceRay(mMaxRecursions, mGiLevel, createPrimaryRay(pixelPoint.x, pixelPoint.y), mBackgroundColor, null);
    }

    private RgbColor traceRay(int recursionCounter, int giLevelCounter, Ray inRay, RgbColor localColor, Intersection prevIntersec){
        RgbColor directLight = localColor;

        // For each pixel testing each shape to get nearest intersection; the range of the Ray is this time unlimited
        Intersection intersection = RaytracerMethods.getIntersectionOnShapes(inRay, prevIntersec, mShapeList);

        if( intersection.isHit() ){
            // Stop! Enter, if the last recursion level is reached, but it is not the final ray to the light
            if( recursionCounter <= 0 ) {
                // If recursion is done and it is not the last ray then trace the ray to all lights to see if any obstacle exists
                return directLight;
            }

            RgbColor shadedColor = shade( intersection );

            // Calculate the color of every object, that was hit in between, depending on recursive level
            directLight = directLight.add( shadedColor );

            // Further recursions through objects, if the recursion is not finished and object is not diffuse
            if ( intersection.getShape().isReflective() ) {
                recursionCounter -= 1;
                directLight = this.getReflectiveColor(recursionCounter, giLevelCounter, directLight, intersection);
            }
            if ( intersection.getShape().isRefractive() ) {
                recursionCounter -= 1;
                directLight = this.getRefractionColor(recursionCounter, giLevelCounter, directLight, intersection);
            }
            if ( mUseGI && intersection.getShape().getMaterial().isGiOn()){
                // direct illumination + indirect illumination
                directLight = this.getGIColor(giLevelCounter, directLight, intersection);
            }
            if ( mUseAO && intersection.getShape().getMaterial().isGiOn()){
                // direct illumination + indirect illumination
                // directLight = directLight.add( this.getAmbientOcclusionColor(intersection) );
                directLight = this.getAmbientOcclusionColor(directLight, intersection);
            }

            // Add ambient term
            RgbColor ambientTerm = intersection.getShape().getMaterial().getAmbientCoeff().multRGB( this.mAmbientLight );
            directLight = directLight.add( ambientTerm );
        }

        return directLight;
    }

    private RgbColor getAmbientOcclusionColor(RgbColor directLight, Intersection intersection){
        Vec3 aoColor = RgbColor.BLACK.colors;

        for(int i = 0; i < mNumberOfAOSamples; i++){
            aoColor = aoColor.add(aoTraceRay(intersection));
        }
        aoColor = aoColor.multScalar(1f / mNumberOfAOSamples);

        return new RgbColor(aoColor);
        //return directLight.add(new RgbColor(aoColor));
    }

    private Vec3 aoTraceRay(Intersection prevIntersec){
        Ray randomRay = prevIntersec.calculateRandomRay(true);

        Intersection intersection = RaytracerMethods.getIntersectionOnShapes(randomRay, prevIntersec, mShapeList);

        if( intersection.isHit() && intersection.isOutOfDistance(mAoMaxDistance) ){
            return RgbColor.DARK_GRAY.colors;
        }

        return RgbColor.BLACK.colors;
    }

    private RgbColor getGIColor(int giLevelCounter, RgbColor directLight, Intersection intersection) {
        Vec3 indirectLight = this.calculateGiIntersections(giLevelCounter, new Vec3(), intersection);

        //Vec3 colorVec = directLight.colors.multScalar(GI_FACTOR).add(indirectLight.multScalar(2f)).multScalar(0.058f);
        Vec3 colorVec = (directLight.colors.add(indirectLight.multScalar(GI_FACTOR * 0.25f)));


        directLight = new RgbColor(colorVec);
        return directLight;
    }

    private RgbColor getRefractionColor(int recursionCounter, int giLevelCounter, RgbColor directLight, Intersection intersection) {
        RgbColor transmissionColor = this.traceRay(recursionCounter, giLevelCounter, intersection.calculateRefractionRay(), directLight, intersection);
        directLight = directLight.add( transmissionColor.multScalar(0.5f) );
        return directLight;
    }

    private RgbColor getReflectiveColor(int recursionCounter, int giLevelCounter, RgbColor directLight, Intersection intersection) {
        Vec3 reflectionColorVec = new Vec3();

        for(int i = 0; i < intersection.getShape().getMaterial().getReflection().blurryLevel; i++) {
            //float reflectivity = intersection.getShape().getMaterial().getReflection().reflectivity;
            float reflectivity = intersection.calculateReflectivity();
            RgbColor reflectionColor = this.traceRay(recursionCounter, giLevelCounter, intersection.calculateReflectionRay(), directLight, intersection).multScalar(reflectivity);
            reflectionColorVec = reflectionColorVec.add(reflectionColor.colors);
        }

        reflectionColorVec = reflectionColorVec.multScalar(1f / intersection.getShape().getMaterial().getReflection().blurryLevel);

        directLight = directLight.add(new RgbColor(reflectionColorVec));
        return directLight;
    }

    private Vec3 giTraceRay(int giLevelCounter, Vec3 outColor, Intersection prevIntersec){
        Ray randomRay = prevIntersec.calculateRandomRay();

        Intersection intersection = RaytracerMethods.getIntersectionOnShapes(randomRay, prevIntersec, mShapeList);

        if( intersection.isHit() && intersection.getShape().getMaterial().isGiOn()){

            // calculate the shaded color at one point
            for(Light light : mLightList){
                Vec3 giColor = intersection.getShape().getColor(light, intersection).colors;

                // start a new GI iteration
                outColor = outColor.add(this.calculateGiIntersections(giLevelCounter, giColor, intersection));
            }
            outColor = outColor.multScalar(randomRay.getDirection().scalar(intersection.getNormal()));
        }

        return outColor;
    }

    private Vec3 calculateGiIntersections(int giLevelCounter, Vec3 outColor, Intersection intersection){
        if( giLevelCounter > 0 ) {
            Vec3 indirectLight = new Vec3();

            giLevelCounter -= 1;

            // object is diffuse; send additional rays
            for (int i = 0; i < mGiSamples; i++) {

                Vec3 giColor = this.giTraceRay(giLevelCounter, outColor, intersection).multScalar(1f / mPDFFactor).multScalar(intersection.getCosTheta());
                indirectLight = indirectLight.add(giColor);
            }

            if(mGiSamples != 0) {
                outColor = indirectLight.multScalar( 1f / (float) mGiSamples);
            }
        }

        return outColor;
    }

    private RgbColor shade(Intersection finalIntersection) {
        RgbColor illuColor = RgbColor.BLACK;

        // Check the ray from the intersection point to any light source
        for( Light light : mLightList ) {
            illuColor = illuColor.add( RaytracerMethods.traceIllumination( illuColor, light, finalIntersection, mShapeList, mScene ) );
        }
        // If illuColor is BLACK nothing was hit the position is in shadow - do nothing
        return illuColor;
    }
}
