package MLP.services.recognition.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RImage implements Cloneable {

    private int sizeX;
    private int sizeY;
    private int[][] pixels;

    public RImage clone() throws CloneNotSupportedException {
        return (RImage) super.clone();
    }

    public RImage changeSize() {
        int SIZE_X = sizeX;
        int SIZE_Y = sizeY;
        RImage rImage = null;
        try {
            rImage = clone();
            rImage.setSizeY(SIZE_X);
            rImage.setSizeX(SIZE_Y);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return rImage;
    }
}
