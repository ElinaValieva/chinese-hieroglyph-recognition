package MLP.models;

import lombok.Data;

/**
 * author: ElinaValieva on 22.12.2018
 */
@Data
public class ImageSegmentation {
    private String pathImage;
    private Integer codeResult;
    private Double error;
    private String result;
}
