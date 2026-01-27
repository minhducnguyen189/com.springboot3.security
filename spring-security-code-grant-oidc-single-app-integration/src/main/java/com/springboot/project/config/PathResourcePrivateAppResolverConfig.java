package com.springboot.project.config;

import jakarta.servlet.ServletContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.TransformedResource;

@Component
public class PathResourcePrivateAppResolverConfig extends PathResourceResolver {

    private static final String INDEX_HTML = "index.html";
    private static final String DELIMITER = "/";
    private static final String BASE_TAG_PATTERN = "<base[^>]*>";
    private static final String HTML_BASE_HREF = "<base href=\"%s\"/>";

    private final ApplicationProperty appConfig;
    private final ServletContext servletContext;

    @Autowired
    public PathResourcePrivateAppResolverConfig(
            ApplicationProperty appConfig, ServletContext servletContext) {
        this.appConfig = appConfig;
        this.servletContext = servletContext;
    }

    @Override
    public Resource getResource(@NonNull String resourcePath, Resource location)
            throws IOException {
        Resource requestedResource = location.createRelative(resourcePath);
        if (requestedResource.exists()
                && requestedResource.isReadable()
                && !resourcePath.equals(INDEX_HTML)) {
            return requestedResource;
        } else {
            Resource resource = location.createRelative(INDEX_HTML);
            try (BufferedReader bufferedReader =
                    new BufferedReader(
                            new InputStreamReader(
                                    resource.getInputStream(), StandardCharsets.UTF_8))) {
                String indexContent = bufferedReader.lines().collect(Collectors.joining("\n"));
                String adjustIndexContent =
                        indexContent.replaceAll(
                                BASE_TAG_PATTERN,
                                String.format(
                                        HTML_BASE_HREF,
                                        this.servletContext.getContextPath()
                                                + this.appConfig
                                                        .getFrontEnd()
                                                        .getPrivateAngularApp()
                                                        .getViewController()
                                                + DELIMITER));
                return new TransformedResource(resource, adjustIndexContent.getBytes());
            }
        }
    }
}
