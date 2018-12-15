package MLP.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * author: ElinaValieva on 15.12.2018
 */
@ConfigurationProperties(prefix = "storage")
public class StorageConfig {

    @Getter
    @Setter
    private String location;
}
