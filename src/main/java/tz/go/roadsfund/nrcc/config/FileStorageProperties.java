package tz.go.roadsfund.nrcc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * File storage configuration properties
 */
@Configuration
@ConfigurationProperties(prefix = "file")
@Data
public class FileStorageProperties {
    private String uploadDir;
    private String[] allowedExtensions;
}
