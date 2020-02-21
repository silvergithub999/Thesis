package com.example.thesis;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.example.thesis.Coordinates.ScreenCoordinates;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class ImageService {
    private Process process;
    private Context context;
    private Map<Integer, Mat> numpadImages;

    public ImageService(Context context) {
        this.context = context;
        this.process = ProcessManagerService.getRootProcess();
        this.numpadImages = getNumpadImages();
    }

    public void mainCode() {
        getButtonLocationsFromScreenshot();
    }


    /**
     * Helpful:
     * https://riptutorial.com/opencv/example/22915/template-matching-with-java
     */
    private void getButtonLocationsFromScreenshot() {
        Mat screenshot = getScreenshot();
        Set<Integer> numImgSet = numpadImages.keySet();

        for (Integer imgValue : numImgSet) {
            Mat button = null;

            // Template matching.
            int machMethod=Imgproc.TM_CCOEFF;
            Mat outputImage=new Mat();
            Imgproc.matchTemplate(screenshot, button, outputImage, machMethod);


            Core.MinMaxLocResult mmr = Core.minMaxLoc(outputImage);
            Point matchLoc = mmr.maxLoc;
            ScreenCoordinates screenCoordinates = new ScreenCoordinates((int) matchLoc.x, (int) matchLoc.y);

            Log.i("Found Button", "Button " + imgValue + " is in Coordinates: " + screenCoordinates);
        }
    }


    private Mat getScreenshot() {
        String savePath = "/mnt/sdcard/Download/test.png"; // TODO: maybe use the hint
        takeScreenshot(savePath);
        Mat screenshot = loadImage(savePath);
        return screenshot;
    }


    private Map<Integer, Mat> getNumpadImages() {
        Map<Integer, Mat> pinImages = getPinImages();
        Map<Integer, Mat> otherImages = getOtherImages();
        pinImages.putAll(otherImages);
        return pinImages;
    }

    private Map<Integer, Mat> getPinImages() {
        Map<Integer, Mat> pinImages = new HashMap<>();

        Resources resources = context.getResources();
        // for (int i = 0; i < 10; i++) {
        for (int i = 1; i < 7; i++) {   // TODO: this is for testing, upper row is correct.
            try {
                int resourceId = resources.getIdentifier("button_" + i + ".png", "drawable", context.getPackageName());
                Mat img = Utils.loadResource(context, resourceId);
                pinImages.put(i, img);
            } catch (IOException e) {
                Log.e("ImageService", "Error, could not get image: " + e.getMessage());
            }
        }
        return pinImages;
    }

    private Map<Integer, Mat> getOtherImages() {
        // TODO
        return new HashMap<>();
    }



    private Mat loadImage(String path) {
        return Imgcodecs.imread(path);
    }


    private void takeScreenshot(String savePath) {
        ProcessManagerService.sendCommand(process, "screencap " + savePath);
    }
}
