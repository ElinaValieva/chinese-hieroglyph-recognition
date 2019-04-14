package MLP.exception;


import lombok.Getter;

public class RecognitionException extends Exception {

    @Getter
    private final String message;

    public RecognitionException(String message) {
        super(message);
        this.message = message;
    }
}
