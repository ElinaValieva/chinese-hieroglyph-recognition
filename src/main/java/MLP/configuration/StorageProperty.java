package MLP.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * author: ElinaValieva on 15.12.2018
 * Property for storage settings
 */
@Component
public class StorageProperty {

    @Value("${storage.location}")
    public String location;
}
