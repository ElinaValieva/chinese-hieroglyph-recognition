package MLP.services.rest_client;

import MLP.configuration.AppProperty;
import MLP.exception.ErrorCode;
import MLP.exception.RecognitionException;
import lombok.extern.log4j.Log4j2;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for REST-calls
 */
@Log4j2
@Service
public class RestClient {

    private final RestManager restManager;

    private final AppProperty appProperty;

    @Autowired
    public RestClient(RestManager restManager, AppProperty appProperty) {
        this.restManager = restManager;
        this.appProperty = appProperty;
    }

    /**
     * Send segments images to neural-network service
     * @param segments - segments images
     * @return list predicted chinese codes
     * @throws RecognitionException - connection refused
     */
    public List<Integer> sendSegments(List<String> segments) throws  RecognitionException {
        List<Integer> commandResults = new ArrayList<>();
        segments.forEach(segment-> {
            try {
                String URL = appProperty.neuralNetworkURl;
                RequestBody requestBody = restManager.createBody(segment);
                Request request = restManager.createRequest(URL, RequestMethod.POST, requestBody);
                Response response = restManager.execute(request);

                if (restManager.isSuccessFulResponse(response)) {
                    int result = restManager.mapFromJson(response, Integer.class);
                    commandResults.add(result);
                    log.info("Send commands instructions for creation with with STATUS: {}", HttpStatus.OK);
                }
            } catch (Exception e) {
                log.error("", e);
            }
        });

        if (commandResults.isEmpty())
            throw new RecognitionException(ErrorCode.ERROR_CODE_CONNECTION.getMessage());

        return commandResults;
    }
}
