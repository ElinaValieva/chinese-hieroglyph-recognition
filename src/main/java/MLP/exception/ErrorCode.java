package MLP.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ErrorCode {

    ERROR_CODE_FILE_NOT_FOUND("Segmentation image path not found"),
    ERROR_CODE_FILE_SIZE_LIMIT("File size exceeds the configured maximum"),
    ERROR_CODE_IO("File error"),
    ERROR_CODE_RESPONSE_FAILED("Error, request failed."),
    ERROR_CODE_BODY_MAPPING("Error in response mapping."),
    ERROR_CODE_CONNECTION("Connection error");

    @Getter
    private String message;
}
