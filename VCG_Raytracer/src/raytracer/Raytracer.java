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
    public static final int ANTI_ALIASING_LOW = 2;
    public static final int ANTI_ALIASING_MEDIUM = 4;
    public static final int ANTI_ALIASING_HIGH = 8;
    public static final int ANTI_ALIASING_INSANE = 16;

    public static final int MULTI_THREADING_NONE = 1;
    public static final int MULTI_THREADING_LOW = 2;
    public static final int MULTI_THREADING_MEDIUM = 4;
    public static final int MULTI_THREADING_HIGH = 8;
    public static final int MULTI_THREADING_INSANE = 16;
    public static final int MULTI_THREADING_GODLIKE = 64;

    private static final float AA_THRESHOLD = 0.015f;

    private static float GI_FACTOR = (float) (1f / Math.PI);
    private BufferedImage mBufferedImage;
    private ArrayList<Shape> mShapeList;
    private ArrayList<Light> mLightList;

    private Scene mScene;
    private Window mRenderWindow;
    private Vec3[][] mPrimaryColorMap;
    private Vec3[][] mAntiAliasingMap;

    private Ray[][] mPrimaryRayMap;

    private int mMaxRecursions;
    private int mGiLevel;
    private int mGiSamples;

    private float mAntiAliasingCounter;
    private int mAntiAliasingSamples;

    private int mBlockSize;
    private int mNumberOfThreads;

    private boolean mUseGI;
    private float mPDFFactor;

    private RgbColor mBackgroundColor;
    private RgbColor mAmbientLight;

    private boolean mDebug;
    private long tStart;

    public Raytracer(Scene scene, Window renderWindow, int recursions, boolean useGi, int giLevel, int giSamples, RgbColor backColor, RgbColor ambientLight, int antiAliasingSamples, int blockSize, int numberOfThreads, boolean debugOn){
        Log.print(this, "Init");
        mMaxRecursions = recursions;
        mUseGI = useGi;

        if(mUseGI) {
            mGiLevel = giLevel;
            mGiSamples = giSamples;
        }

        mBufferedImage = renderWindow.getBufferedImage();
        mPrimaryColorMap = new Vec3[mBufferedImage.getWidth()][mBufferedImage.getHeight()];
        mAntiAliasingMap = new Vec3[mBufferedImage.getWidth()][mBufferedImage.getHeight()];
        mPrimaryRayMap = new Ray[mBufferedImage.getHeight()][mBufferedImage.getWidth()];
        mBackgroundColor = backColor;
        mAmbientLight = ambientLight;
        mScene = scene;
        mRenderWindow = renderWindow;
        mShapeList = scene.getShapeList();
        mLightList = scene.getLightList();
        mAntiAliasingCounter = 1f / antiAliasingSamples;
        mDebug = debugOn;
        tStart = System.currentTimeMillis();
        mAntiAliasingSamples = antiAliasingSamples;
        mBlockSize = blockSize;
        mNumberOfThreads = numberOfThreads;

        this.exportRendering();

        mPDFFactor = (float) (1f / (2f* Math.PI));

        this.createPrimaryColorMap();
        this.createAntiAliasingMap();
    }

    public BufferedImage getBufferedImage() {
        return mBufferedImage;
    }

    public Window getRenderWindow() {
        return mRenderWindow;
    }

    public void exportRendering(){
        mRenderWindow.exportRendering(String.valueOf(stopTime(tStart)), mMaxRecursions, mAntiAliasingSamples, mDebug, mGiLevel, mGiSamples);
    }

    private static double stopTime(long tStart){
        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        return tDelta / 1000.0;
    }

    public void renderScene(){
        Log.print(this, "Prepare rendering at " + String.valueOf(stopTime(tStart)));

        MultiThreader rayMultiThreader = new MultiThreader(this, mBlockSize, mNumberOfThreads);
        rayMultiThreader.prepareMultiThreading();

        Log.print(this, "Start rendering at " + String.valueOf(stopTime(tStart)));
        rayMultiThreader.startMultiThreading();
    }

    public void renderBlock(RenderBlock renderBlock){
        // Rows
        for (int y = renderBlock.yMin; y < renderBlock.yMax; y++) {
            // Columns
            for (int x = renderBlock.xMin; x < renderBlock.xMax; x++) {

                RgbColor renderColor;

//                if(mAntiAliasingMap[x][y].equals(RgbColor.WHITE.colors)){
//                    renderColor = this.calculateAntiAliasedColor(y, x);
//                }
//                else{
//                    renderColor = this.sendPrimaryRay( new Vec2( x, y ) );
//                }

                this.getRenderWindow().setPixel(this.getBufferedImage(), new RgbColor(mAntiAliasingMap[x][y]), new Vec2(x, y));
            }
        }
    }

    public RgbColor calculateAntiAliasedColor(int y, int x) {

        Vec2 screenPosition;
        RgbColor antiAlisedColor = RgbColor.BLACK;

        Random antiAliasingRandom = new Random();

        // Start super sampling
        for(float i = 0; i < mAntiAliasingSamples; i ++){
                float m = antiAliasingRandom.nextFloat();
                float n = antiAliasingRandom.nextFloat();

                screenPosition = new Vec2(x + m, y + n);
                antiAlisedColor = antiAlisedColor.add(this.sendPrimaryRay(screenPosition).multScalar(mAntiAliasingCounter));
        }

        return antiAlisedColor;
    }

    private void createPrimaryColorMap(){
        Log.print(this, "Create Primary Color Map");
        for(int x = 0; x < mBufferedImage.getWidth(); x++){
            for(int y = 0; y < mBufferedImage.getHeight(); y++) {
                mPrimaryRayMap[y][x] = createPrimaryRay(x,y);
                mAntiAliasingMap[x][y] = new Vec3();

                Intersection intersection = RaytracerMethods.getIntersectionOnShapes(mPrimaryRayMap[y][x], null, mShapeList);

                if (intersection.isHit()) {
                    mPrimaryColorMap[x][y] = shade(intersection).colors;
                }
            }
        }
        Log.print(this, "Primary Color Map finished!");
    }

    private Ray createPrimaryRay(float x, float y){
        Vec3 startPoint = mScene.getCamPos();
        Vec3 destinationDir = mScene.getCamPixelDirection(new Vec2(x, y));
        return new Ray(startPoint, destinationDir, 1f);
    }

    private void createAntiAliasingMap() {
        Log.print(this, "Create AntiAliased Map");
        for(int x = 1; x < mBufferedImage.getWidth() - 1; x++){
            for(int y = 1; y < mBufferedImage.getHeight() - 1; y++) {
                if(this.hasToBeAntiAliased(x,y)){
                    mAntiAliasingMap[x][y] = RgbColor.WHITE.colors;
                }
            }
        }
        Log.print(this, "AntiAliased Map finished!");
    }

    private boolean hasToBeAntiAliased(int x, int y){

        float value = mPrimaryColorMap[x][y].length();

        if ((value - mPrimaryColorMap[x+1][y].length()) > AA_THRESHOLD ||
            (value - mPrimaryColorMap[x-1][y].length()) > AA_THRESHOLD ||
            (value - mPrimaryColorMap[x][y+1].length()) > AA_THRESHOLD ||
            (value - mPrimaryColorMap[x][y-1].length()) > AA_THRESHOLD) {
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

                directLight = getGIColor(giLevelCounter, directLight, intersection);
            }

            // Add ambient term
            RgbColor ambientTerm = intersection.getShape().getMaterial().getAmbientCoeff().multRGB( this.mAmbientLight );
            directLight = directLight.add( ambientTerm );
        }

        return directLight;
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
