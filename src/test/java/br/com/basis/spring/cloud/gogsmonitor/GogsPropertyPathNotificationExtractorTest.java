package br.com.basis.spring.cloud.gogsmonitor;

import org.junit.Test;
import org.springframework.cloud.config.monitor.PropertyPathNotification;
import org.springframework.util.LinkedMultiValueMap;

import java.util.HashMap;
import java.util.Map;

import static br.com.basis.spring.cloud.gogsmonitor.GogsPropertyPathNotificationExtractor.X_GOGS_EVENT_HEADER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class GogsPropertyPathNotificationExtractorTest {

    @Test
    public void shouldExtractPathsIfGogsHeaderPresent() {
        final GogsPropertyPathNotificationExtractor propertyPathNotificationExtractor = new GogsPropertyPathNotificationExtractor();
        final Map<String, Object> request = spy(new HashMap<>());
        final LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(X_GOGS_EVENT_HEADER, "push");
        propertyPathNotificationExtractor.extract(headers, request);
        verify(request).get("commits");
    }

    @Test
    public void shouldNotExtractPathsIfNoGogsHeaderPresent() {
        final GogsPropertyPathNotificationExtractor propertyPathNotificationExtractor = new GogsPropertyPathNotificationExtractor();
        final Map<String, Object> request = spy(new HashMap<>());
        final PropertyPathNotification extracted = propertyPathNotificationExtractor.extract(new LinkedMultiValueMap<>(), request);
        verify(request, never()).get(anyObject());
        assertThat(extracted).isNull();
    }

    @Test
    public void shouldNotExtractPathsIfNoGogsPushEventPresent() {
        final GogsPropertyPathNotificationExtractor propertyPathNotificationExtractor = new GogsPropertyPathNotificationExtractor();
        final Map<String, Object> request = spy(new HashMap<>());
        final LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(X_GOGS_EVENT_HEADER, "issues");
        final PropertyPathNotification extracted = propertyPathNotificationExtractor.extract(headers, request);
        verify(request, never()).get(anyObject());
        assertThat(extracted).isNull();
    }
}
