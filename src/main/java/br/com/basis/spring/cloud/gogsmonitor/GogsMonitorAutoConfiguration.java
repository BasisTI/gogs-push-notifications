package br.com.basis.spring.cloud.gogsmonitor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnWebApplication
public class GogsMonitorAutoConfiguration {

    private final Log log = LogFactory.getLog(GogsMonitorAutoConfiguration.class);

    @Bean
    @ConditionalOnProperty(value = "spring.cloud.config.server.monitor.gogs.enabled",
        havingValue = "true", matchIfMissing = true)
    public GogsPropertyPathNotificationExtractor gogsPropertyPathNotificationExtractor() {
        log.debug("Configuring GogsPropertyPathNotificationExtractor");
        return new GogsPropertyPathNotificationExtractor();
    }
}
