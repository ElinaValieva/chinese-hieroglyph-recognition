package MLP.model;

import lombok.Builder;
import lombok.Getter;

import java.awt.image.BufferedImage;

/**
 * author: ElinaValieva on 06.04.2019
 * Model for presenting recognition hieroglyph
 * @value vector - pixels representing
 * @value bufferedImage - image representing
 * @value definition - chiness translation
 * @value width - width image
 * @value height - height image
 */
@Getter
@Builder
public class HieroglyphRecognitionModel {

    private int width;

    private int height;

    private int[][] vector;

    private BufferedImage bufferedImage;

    private String definition;
}
