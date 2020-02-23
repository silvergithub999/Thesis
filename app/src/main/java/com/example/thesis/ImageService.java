package com.example.thesis;

import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;

import com.example.thesis.Buttons.Button;
import com.example.thesis.Buttons.PinButton;
import com.example.thesis.Coordinates.ScreenCoordinates;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class ImageService {
    // TODO: add success and failure images aswell.
    // TODO: add PIN1 and PIN2 text images and check error text.

    private Process process;
    private Context context;


    public ImageService(Context context) {
        this.context = context;
        this.process = ProcessManagerService.getRootProcess();
    }


    public void onClose() {
        process.destroy();
    }


    public void mainCode() {
        // Taking screenshot.
        Mat screenshot = getScreenshot();

        int b = 1;

        // Getting pin number buttons.
        Map<Integer, Mat> pinImages = getPinButtonImages();
        Map<Integer, Button> pinButtons = getPinButtonLocationsFromScreenshot(pinImages, screenshot);

        // Getting other buttons.
        Map<Integer, Mat> otherImages = getOtherButtonImages();
        Map<Integer, Button> otherButtons = getOtherButtonLocationsFromScreenshot(otherImages, screenshot);

        Log.i("PIN buttons", pinButtons.toString());
        Log.i("Other buttons", otherButtons.toString());
    }



    public Map<Integer, Button> getPinButtonLocationsFromScreenshot(Map<Integer, Mat> pinImages, Mat screenShot) {
        Map<Integer, Button> buttonLocations = new HashMap<>();
        int heightWidth = 280;

        Set<Integer> numImgSet = pinImages.keySet();
        for (Integer imgValue : numImgSet) {
            Mat button = pinImages.get(imgValue);

            // Resizing button.
            Mat buttonResize = new Mat();
            Size size = new Size(heightWidth, heightWidth);
            Imgproc.resize(button, buttonResize, size);

            // Getting the location.
            ScreenCoordinates screenCoordinates = findButtonLocationOnImage(buttonResize, screenShot);
            buttonLocations.put(imgValue, new PinButton(imgValue, heightWidth, heightWidth, screenCoordinates.getScreenX(), screenCoordinates.getScreenY()));
        }
        return buttonLocations;
    }


    private Map<Integer, Button> getOtherButtonLocationsFromScreenshot(Map<Integer, Mat> otherImages, Mat screenShot) {
        Map<Integer, Button> buttonLocations = new HashMap<>();

        Set<Integer> numImgSet = otherImages.keySet();
        for (Integer imgValue : numImgSet) {
            Mat button = otherImages.get(imgValue);
            ScreenCoordinates screenCoordinates = findButtonLocationOnImage(button, screenShot);
            buttonLocations.put(imgValue, new PinButton(imgValue, button.cols(), button.rows(), screenCoordinates.getScreenX(), screenCoordinates.getScreenY()));
        }
        return buttonLocations;
    }


    /**
     * Helpful:
     * https://riptutorial.com/opencv/example/22915/template-matching-with-java
     * https://www.programcreek.com/java-api-examples/?class=org.opencv.imgproc.Imgproc&method=matchTemplate
     * https://docs.opencv.org/3.4/de/da9/tutorial_template_matching.html
     */
    private ScreenCoordinates findButtonLocationOnImage(Mat button, Mat image) {
        Imgproc.cvtColor(button, button, CvType.channels(3));
        Imgproc.cvtColor(image, image, CvType.channels(3));
        button.convertTo(button, CvType.CV_8UC3);
        image.convertTo(image, CvType.CV_8UC3);

        // Template matching.
        int result_cols = image.cols() - button.cols() + 1;
        int result_rows = image.rows() - button.rows() + 1;
        Mat outputImage = new Mat(result_rows, result_cols, CvType.CV_8UC3);

        Imgproc.matchTemplate(image, button, outputImage, Imgproc.TM_CCOEFF);

        Core.MinMaxLocResult mmr = Core.minMaxLoc(outputImage);
        Point matchLoc = mmr.maxLoc;

        return new ScreenCoordinates((int) matchLoc.x, (int) matchLoc.y);
    }



    private Mat getScreenshot() {
        String savePath = Environment.getExternalStorageDirectory().getPath();
        String name = "success.png";
        takeScreenshot(savePath + " " + name);
        Mat img = loadImageFromDownloads(name);
        return img;
    }


    private Map<Integer, Mat> getPinButtonImages() {
        Map<Integer, Mat> pinImages = new HashMap<>();
        for (int i = 1; i < 10; i++) {
            Mat img = getButtonImage(Integer.toString(i));
            pinImages.put(i, img);
        }
        return pinImages;
    }


    private Map<Integer, Mat> getOtherButtonImages() {
        Map<Integer, Mat> otherButtonImages = new HashMap<>();
        otherButtonImages.put(-1000, getButtonImage("cancel"));
        otherButtonImages.put(-500, getButtonImage("delete"));
        return otherButtonImages;
    }


    /**
     * Gets an image out of the drawable folder with the given name.
     * @param name - name of the image.
     * @return the image with the given name.
     */
    private Mat getImage(String name) {
        Mat img = null;
        try {
            Resources resources = context.getResources();
            int resourceId = resources.getIdentifier(name, "drawable", context.getPackageName());
            img = Utils.loadResource(context, resourceId);

        } catch (IOException error) {
            Log.e("ImageService", "Error, could not get image: " + error.getMessage());
        }
        return img;
    }

    /**
     * Runs getImage with name: "button_" + name.
     * @param name - unique value at the end of "button_".
     * @return image that with the name "button_name".
     */
    private Mat getButtonImage(String name) {
        return getImage("button_" + name);
    }



    private Mat loadImageFromDownloads(String name) {
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        return Imgcodecs.imread(file.getAbsolutePath() + "/" + name);
    }


    private void takeScreenshot(String savePath) {
        ProcessManagerService.sendCommand(process, "screencap " + savePath);
    }
}
