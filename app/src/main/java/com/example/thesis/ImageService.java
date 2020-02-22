package com.example.thesis;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.example.thesis.Buttons.Button;
import com.example.thesis.Buttons.ButtonValue;
import com.example.thesis.Buttons.ButtonValueConverter;
import com.example.thesis.Buttons.PinButton;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class ImageService {
    // TODO: add success and failure images aswell.
    // TODO: there seems to be no OK image.
    private Process process;
    private Context context;
    private Map<Integer, Mat> numpadImages;

    public ImageService(Context context) {
        this.context = context;
        this.process = ProcessManagerService.getRootProcess();
        this.numpadImages = getNumpadImages();
    }

    public void mainCode() {
        getPinButtonLocationsFromScreenshot();
        getOtherButtonLocationsFromScreenshot();
    }


    /**
     * Helpful:
     * https://riptutorial.com/opencv/example/22915/template-matching-with-java
     * https://www.programcreek.com/java-api-examples/?class=org.opencv.imgproc.Imgproc&method=matchTemplate
     * https://docs.opencv.org/3.4/de/da9/tutorial_template_matching.html
     */
    public Map<Integer, Button> getPinButtonLocationsFromScreenshot() {
        Map<Integer, Button> buttonLocations = new HashMap<>();

        Mat screenshot = getScreenshot();
        Set<Integer> numImgSet = numpadImages.keySet();

        int heightWidth = 280;

        for (Integer imgValue : numImgSet) {
            Mat button = numpadImages.get(imgValue);

            // Resizing button.
            // TODO: maybe add programatically resizing.
            Mat buttonResize = new Mat();
            Size size = new Size(heightWidth, heightWidth);
            Imgproc.resize(button, buttonResize, size);
            button = buttonResize;


            // Template matching.
            int result_cols = screenshot.cols() - button.cols() + 1;
            int result_rows = screenshot.rows() - button.rows() + 1;
            Mat outputImage = new Mat(result_rows, result_cols, CvType.CV_32FC1);

            Imgproc.matchTemplate(screenshot, button, outputImage, Imgproc.TM_CCOEFF);


            Core.MinMaxLocResult mmr = Core.minMaxLoc(outputImage);
            Point matchLoc = mmr.maxLoc;

            buttonLocations.put(imgValue, new PinButton(imgValue, heightWidth, heightWidth, (int) matchLoc.x, (int) matchLoc.y));
        }

        return buttonLocations;
    }


    private void getOtherButtonLocationsFromScreenshot() {

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


    private Map<ButtonValue, Mat> getPinButtonImages() {
        Map<ButtonValue, Mat> pinImages = new HashMap<>();
        for (int i = 1; i < 10; i++) {
            Mat img = getButtonImage(Integer.toString(i));
            ButtonValue buttonValue = ButtonValueConverter.convertPinIntToButtonValue(i);
            pinImages.put(buttonValue, img);
        }
        return pinImages;
    }


    private Map<ButtonValue, Mat> getOtherButtonImages() {
        Map<ButtonValue, Mat> otherButtonImages = new HashMap<>();
        otherButtonImages.put(ButtonValue.CANCEL, getButtonImage("cancel"));
        otherButtonImages.put(ButtonValue.OK, getButtonImage("ok"));
        otherButtonImages.put(ButtonValue.DELETE, getButtonImage("delete"));      // TODO: this shows up later.
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



    private Mat loadImage(String path) {
        return Imgcodecs.imread(path);
    }


    private void takeScreenshot(String savePath) {
        ProcessManagerService.sendCommand(process, "screencap " + savePath);
    }
}
