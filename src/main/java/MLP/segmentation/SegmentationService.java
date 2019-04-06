package MLP.segmentation;


import marvin.color.MarvinColorModelConverter;
import marvin.image.MarvinImage;
import marvin.image.MarvinSegment;
import marvin.io.MarvinImageIO;
import marvin.math.MarvinMath;

import java.awt.*;

import static marvinplugins.MarvinPluginCollection.floodfillSegmentation;
import static marvinplugins.MarvinPluginCollection.morphologicalClosing;

/**
 * author: ElinaValieva on 06.04.2019
 */
public class SegmentationService {

    public static void simpleSegmentation() {
        MarvinImage original = MarvinImageIO.loadImage("C:/Users/Elina/Desktop/4.png");
        MarvinImage image = original.clone();
        // 2. Change green pixels to white
        filterGreen(image);
        // 3. Use threshold to separate foreground and background.
        MarvinImage bin = MarvinColorModelConverter.rgbToBinary(image, 127);
        // 4. Morphological closing to group separated parts of the same object
        morphologicalClosing(bin.clone(), bin, MarvinMath.getTrueMatrix(10, 10));
        // 5. Use Floodfill segmention to get image segments
        image = MarvinColorModelConverter.binaryToRgb(bin);
        MarvinSegment[] segments = floodfillSegmentation(image);
        // 6. Show the segments in the original image
        for (int i = 1; i < segments.length; i++) {
            MarvinSegment seg = segments[i];
            original.drawRect(seg.x1, seg.y1, seg.width, seg.height, Color.GREEN);
        }
        MarvinImageIO.saveImage(original, "C:/Users/Elina/Desktop/result.png");
    }

    private static void filterGreen(MarvinImage image) {
        int r, g, b;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                r = image.getIntComponent0(x, y);
                g = image.getIntComponent1(x, y);
                b = image.getIntComponent2(x, y);
                if (g > r * 1.5 && g > b * 1.5) {
                    image.setIntColor(x, y, 255, 255, 255);
                }
            }
        }
    }

    public static void main(String[] args) {
        simpleSegmentation();
    }
}
