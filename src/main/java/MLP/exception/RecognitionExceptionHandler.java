package MLP.exception;


import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartException;

import java.io.IOException;


@Log4j2
@ControllerAdvice
public class RecognitionExceptionHandler {

    @ExceptionHandler(RecognitionException.class)
    public ResponseEntity<String> handleMonitoringException(RecognitionException ex) {
        log.error("", ex);
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(FileUploadBase.SizeLimitExceededException.class)
    public ResponseEntity<String> handleSizeLimitExceededException(FileUploadBase.SizeLimitExceededException ex) {
        log.error("", ex);
        return ResponseEntity.badRequest().body(ErrorCode.ERROR_CODE_FILE_SIZE_LIMIT.getMessage());
    }

    @ExceptionHandler(MultipartException.class)
    @ResponseStatus(value = HttpStatus.PAYLOAD_TOO_LARGE)
    public ResponseEntity<String> handleMultipartException(MultipartException ex) {
        log.error("", ex);
        return ResponseEntity.badRequest().body(ErrorCode.ERROR_CODE_FILE_SIZE_LIMIT.getMessage());
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(value = HttpStatus.PAYLOAD_TOO_LARGE)
    public ResponseEntity<String> handleIOException(IOException ex) {
        log.error("", ex);
        return ResponseEntity.badRequest().body(ErrorCode.ERROR_CODE_IO.getMessage());
    }
}
