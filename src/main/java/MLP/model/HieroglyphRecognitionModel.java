package MLP.model;

import lombok.*;

import java.awt.image.BufferedImage;

/**
 * author: ElinaValieva on 06.04.2019
 * Model for presenting recognition hieroglyph
 *
 * @value vector - pixels representing
 * @value bufferedImage - image representing
 * @value definition - chiness translation
 * @value width - width image
 * @value height - height image
 */
@Data
@NoArgsConstructor
@Setter(value = AccessLevel.PRIVATE)
public class HieroglyphRecognitionModel {

    private int width;

    private int height;

    private int[][] vector;

    private BufferedImage bufferedImage;

    private String path;

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(HieroglyphRecognitionModel hieroglyphRecognitionModel) {
        return new Builder(hieroglyphRecognitionModel);
    }

    @AllArgsConstructor
    public static class Builder {

        private HieroglyphRecognitionModel hieroglyphRecognitionModel;

        Builder() {
            hieroglyphRecognitionModel = new HieroglyphRecognitionModel();
        }

        public Builder width(int width) {
            hieroglyphRecognitionModel.setWidth(width);
            return this;
        }

        public Builder height(int height) {
            hieroglyphRecognitionModel.setHeight(height);
            return this;
        }

        public Builder vector(int[][] vector) {
            hieroglyphRecognitionModel.setVector(vector);
            return this;
        }

        public Builder bufferedImage(BufferedImage bufferedImage) {
            hieroglyphRecognitionModel.setBufferedImage(bufferedImage);
            return this;
        }

        public Builder path(String path) {
            hieroglyphRecognitionModel.setPath(path);
            return this;
        }

        public HieroglyphRecognitionModel build() {
            return hieroglyphRecognitionModel;
        }
    }
}
