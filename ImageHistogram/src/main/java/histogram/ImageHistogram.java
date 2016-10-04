package histogram;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * Prints brightness histogram.
 */
public class ImageHistogram extends Application{
    private static String imageInputString;
    private static File imageOutputPath;
    private static String extentionOfOutputFile;

    public static void main(String[] args) throws IOException {
        imageInputString = "";
        imageOutputPath = null;

        // Get data from input arguments array
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-i") && i + 1 != args.length) {
                imageInputString = args[i + 1];
                i++;
            } else if (args[i].equals("-o") && i + 1 != args.length) {
                imageOutputPath = new File(args[i + 1]);
                i++;
            }
        }

        // Check for not empty or null data
        if (Objects.equals(imageInputString, "") || imageOutputPath == null) return;

        // Get extention
        extentionOfOutputFile = "";
        int dotIndex = imageOutputPath.getName().lastIndexOf('.');
        if (dotIndex > 0) {
            extentionOfOutputFile = imageOutputPath.getName().substring(dotIndex + 1);
        }

        launch();
    }

    private static LineChart<String, Number> getHistogram(int[] brightness) {
        // Initialize variables
        CategoryAxis xAxis_brightness = new CategoryAxis();
        NumberAxis yAxis_brightness = new NumberAxis();
        LineChart<String, Number> brightnessHistogram = new LineChart<>(xAxis_brightness, yAxis_brightness);
        brightnessHistogram.setCreateSymbols(false);

        // Get histogram data
        brightnessHistogram.getData().add( getHistogramData(brightness) );

        return brightnessHistogram;
    }

    private static XYChart.Series<String, Number> getHistogramData(int[] brightness) {
        XYChart.Series<String, Number> seriesBrightness = new XYChart.Series<>();
        seriesBrightness.setName("Brightness");

        for (int i = 0; i < 256; i++) {

            // Add every value from array to XYChart
            // xAxis = String.valueOf(i)
            // yAxis = brightness[i]
            seriesBrightness.getData().add(new XYChart.Data<>(String.valueOf(i), brightness[i]));

        }

        return seriesBrightness;
    }

    private static void writeImageToFile(WritableImage imageInput, String extentionOfOutputFile, File imageOutputPath) {
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(imageInput, null), extentionOfOutputFile, imageOutputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int[] getBrightness(String imageSrcString) {
        // Initialization
        Image image = getImage(imageSrcString);
        PixelReader pixelReader = image.getPixelReader();
        int brightness[] = new int[256];

        // Fill brightness array with value of each pixel's brightness
        for (int y = 0; y < image.getHeight(); y++) {

            for (int x = 0; x < image.getWidth(); x++) {

                double currentPixelBrightness = pixelReader.getColor(x, y).getBrightness();
                brightness[(int) (currentPixelBrightness * 255)]++;

            }
        }
        return brightness;
    }

    private static Image getImage(String imageSrcString) {
        Image image = null;
        try {

            BufferedImage tempCard = ImageIO.read(new File(imageSrcString));
            image = SwingFXUtils.toFXImage(tempCard, null);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Get histogram data
        int[] brightness = getBrightness(imageInputString);
        LineChart<String, Number> histogram = getHistogram(brightness);

        // Get image of histogram
        Scene scene = new Scene(histogram);
        WritableImage out = scene.snapshot(null);

        // Write histogram to file
        writeImageToFile(out, extentionOfOutputFile, imageOutputPath);

        // Close application
        primaryStage.close();
        Platform.exit();
    }
}
