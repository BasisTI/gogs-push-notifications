package br.com.basis.spring.cloud.gogsmonitor;

import org.springframework.cloud.config.monitor.PropertyPathNotification;
import org.springframework.cloud.config.monitor.PropertyPathNotificationExtractor;
import org.springframework.util.MultiValueMap;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * PropertyPathNotificationExtractor for Gogs Webhooks
 * Will extract which files have been changed for push in the Git config repository
 */
public class GogsPropertyPathNotificationExtractor implements PropertyPathNotificationExtractor {

    static final String X_GOGS_EVENT_HEADER = "x-gogs-event";

    @Override
    public PropertyPathNotification extract(MultiValueMap<String, String> headers, Map<String, Object> request) {
        if ("push".equals(headers.getFirst(X_GOGS_EVENT_HEADER))) {
            if (request.get("commits") instanceof Collection) {
                Set<String> paths = new HashSet<>();
                @SuppressWarnings("unchecked")
                Collection<Map<String, Object>> commits = (Collection<Map<String, Object>>) request.get("commits");
                for (Map<String, Object> commit : commits) {
                    addAllPaths(paths, commit, "added");
                    addAllPaths(paths, commit, "removed");
                    addAllPaths(paths, commit, "modified");
                }
                if (!paths.isEmpty()) {
                    return new PropertyPathNotification(paths.toArray(new String[0]));
                }
            }
        }
        return null;
    }

    private void addAllPaths(Set<String> paths, Map<String, Object> commit, String name) {
        @SuppressWarnings("unchecked")
        Collection<String> files = (Collection<String>) commit.get(name);
        if (files != null) {
            paths.addAll(files);
        }
    }
}
