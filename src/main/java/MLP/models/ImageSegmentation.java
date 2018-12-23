package MLP.models;

import MLP.configs.DictionaryConfig;
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

    public void setCodeResult(Integer codeResult) {
        this.codeResult = codeResult;
        this.result = DictionaryConfig.getDictionaryConfig().getResult(codeResult);
    }
}
