package MLP.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RImage implements Cloneable {

    private int sizeX;
    private int sizeY;
    private List<Integer> pixels;

    public void refresh() {
        sizeX = sizeY = 0;
        pixels = new ArrayList<>();
    }

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
