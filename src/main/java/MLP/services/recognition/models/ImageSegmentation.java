package MLP.services.recognition.models;

import MLP.configuration.DictionaryConfig;
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
