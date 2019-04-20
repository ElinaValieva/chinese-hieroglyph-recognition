package MLP.services.rest_client;

import MLP.exception.ErrorCode;
import MLP.exception.RecognitionException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import okhttp3.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * RestManager servive for REST-calls
 */
@Log4j2
@Service
public class RestManager {

    private OkHttpClient client;

    private OkHttpClient getOkHttpClient() {
        log.debug("Creating HttpClient");
        if (client == null)
            client = new OkHttpClient().newBuilder()
                    .connectTimeout(100, TimeUnit.MINUTES)
                    .writeTimeout(100, TimeUnit.MINUTES)
                    .readTimeout(100, TimeUnit.MINUTES)
                    .build();
        return client;
    }

    RequestBody createBody(String value) {
        return RequestBody.create(MediaType.parse(org.springframework.http.MediaType.APPLICATION_JSON_VALUE), value);
    }

    Request createRequest(String URL, RequestMethod requestMethod, RequestBody requestBody) throws  RecognitionException {
        Request.Builder builder = new Request.Builder().url(URL);

        switch (requestMethod) {
            case GET:
                return builder.get().build();
            case DELETE:
                return builder.delete().build();
            case POST:
                return builder.post(requestBody).build();
            default:
                throw new RecognitionException(ErrorCode.ERROR_CODE_RESPONSE_FAILED.getMessage());
        }
    }

    Response execute(Request request) throws IOException {
        return getOkHttpClient().newCall(request).execute();
    }

    boolean isSuccessFulResponse(Response response) throws RecognitionException {
        if (!response.isSuccessful()) {
            log.error("Failed : HTTP error [code : {}], error message: {} ",
                    response.code(),
                    response.body());
            throw new RecognitionException(ErrorCode.ERROR_CODE_RESPONSE_FAILED.getMessage());
        }
        return response.isSuccessful();
    }

    <T> T mapFromJson(Response response, Class<T> tClass) throws RecognitionException {
        T object = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            object = objectMapper.readValue(getResponseBody(response), tClass);

        } catch (IOException e) {
            log.error("", e);
        }
        return object;
    }

    <T> List<T> mapFromJsonList(Response response, Class<T> tClass) throws RecognitionException {
        List<T> object = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            object = objectMapper.readValue(getResponseBody(response), objectMapper.getTypeFactory().constructCollectionType(List.class, tClass));

        } catch (IOException e) {
            log.error("", e);
        }
        return object;
    }

    private String getResponseBody(Response response) throws IOException, RecognitionException {
        if (response.body() != null)
            return response.body().string();
        else
            throw new RecognitionException(ErrorCode.ERROR_CODE_BODY_MAPPING.getMessage());
    }
}
