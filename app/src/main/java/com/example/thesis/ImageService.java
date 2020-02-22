package com.example.thesis;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.thesis.Coordinates.ScreenCoordinates;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.opencv.core.CvType.CV_32FC1;


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
     * https://www.programcreek.com/java-api-examples/?class=org.opencv.imgproc.Imgproc&method=matchTemplate
     * https://docs.opencv.org/3.4/de/da9/tutorial_template_matching.html
     */
    private void getButtonLocationsFromScreenshot() {
        Mat screenshot = getScreenshot();
        Set<Integer> numImgSet = numpadImages.keySet();

        for (Integer imgValue : numImgSet) {
            Mat button = numpadImages.get(imgValue);

            // Resizing button.
            // TODO: maybe add programatically resizing.
            Mat buttonResize = new Mat();
            Size size = new Size(280,280);
            Imgproc.resize(button, buttonResize, size);
            button = buttonResize;


            // Template matching.
            int result_cols = screenshot.cols() - button.cols() + 1;
            int result_rows = screenshot.rows() - button.rows() + 1;
            Mat outputImage = new Mat(result_rows, result_cols, CvType.CV_32FC1);

            Imgproc.matchTemplate(screenshot, button, outputImage, Imgproc.TM_CCOEFF);


            Core.MinMaxLocResult mmr = Core.minMaxLoc(outputImage);
            Point matchLoc = mmr.maxLoc;
            ScreenCoordinates screenCoordinates = new ScreenCoordinates((int) matchLoc.x, (int) matchLoc.y);

            Log.i("Found Button", "Button " + imgValue + " is in Coordinates: " + screenCoordinates);
        }
    }


    private Mat getScreenshot() {
        /*
        String savePath = "/mnt/sdcard/Download/test.png"; // TODO: maybe use the hint
        takeScreenshot(savePath);
        Mat screenshot = loadImage(savePath);
        return screenshot;
        */

        Mat img = null;
        try {
            Resources resources = context.getResources();
            int resourceId = resources.getIdentifier("screenshot", "drawable", context.getPackageName());
            img = Utils.loadResource(context, resourceId);
        } catch (IOException error) {
            Log.e("ImageService", "Error, could not get image: " + error.getMessage());
        }
        return img;
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
        for (int i = 1; i < 10; i++) {   // TODO: this is for testing, upper row is correct.
            try {
                int resourceId = resources.getIdentifier("button_" + i, "drawable", context.getPackageName());
                Mat img = Utils.loadResource(context, resourceId);
                pinImages.put(i, img);
            } catch (IOException error) {
                Log.e("ImageService", "Error, could not get image: " + error.getMessage());
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
