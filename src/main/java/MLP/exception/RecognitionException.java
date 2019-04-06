package MLP.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class RecognitionException extends Exception {

    @Getter
    private final String message;
}
