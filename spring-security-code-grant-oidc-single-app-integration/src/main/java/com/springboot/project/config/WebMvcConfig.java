package com.springboot.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final String BASE_PATH_SUFFIX = "/**";
    private static final String DELIMITER = "/";
    private static final String INDEX_HTML = "index.html";

    private final PathResourcePrivateAppResolverConfig pathResourcePrivateAppResolverConfig;
    private final PathResourcePublicAppResolverConfig pathResourcePublicAppResolverConfig;
    private final ApplicationProperty appConfig;

    @Autowired
    public WebMvcConfig(
            PathResourcePrivateAppResolverConfig pathResourcePrivateAppResolverConfig,
            PathResourcePublicAppResolverConfig pathResourcePublicAppResolverConfig,
            ApplicationProperty appConfig) {
        this.pathResourcePrivateAppResolverConfig = pathResourcePrivateAppResolverConfig;
        this.pathResourcePublicAppResolverConfig = pathResourcePublicAppResolverConfig;
        this.appConfig = appConfig;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(
                        appConfig.getFrontEnd().getPrivateAngularApp().getResourceHandlerPath())
                .addResourceLocations(
                        appConfig.getFrontEnd().getPrivateAngularApp().getResourceLocations())
                .setCacheControl(CacheControl.noCache())
                .resourceChain(true)
                .addResolver(this.pathResourcePrivateAppResolverConfig);
        registry.addResourceHandler(
                        appConfig.getFrontEnd().getPublicAngularApp().getResourceHandlerPath())
                .addResourceLocations(
                        appConfig.getFrontEnd().getPublicAngularApp().getResourceLocations())
                .setCacheControl(CacheControl.noCache())
                .resourceChain(true)
                .addResolver(this.pathResourcePublicAppResolverConfig);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        String privateAppViewController =
                appConfig.getFrontEnd().getPrivateAngularApp().getViewController();
        String privateAppViewName = privateAppViewController + DELIMITER + INDEX_HTML;
        String publicAppViewController =
                appConfig.getFrontEnd().getPublicAngularApp().getViewController();
        String publicAppViewName = publicAppViewController + DELIMITER + INDEX_HTML;

        registry.addViewController(privateAppViewController)
                .setViewName(this.getRedirectViewName(privateAppViewController));
        registry.addViewController(publicAppViewController)
                .setViewName(this.getRedirectViewName(publicAppViewController));
        registry.addViewController(privateAppViewController + DELIMITER)
                .setViewName(this.getForwardViewName(privateAppViewName));
        registry.addViewController(publicAppViewController + DELIMITER)
                .setViewName(this.getForwardViewName(publicAppViewName));
        registry.addViewController(DELIMITER)
                .setViewName(this.getRedirectViewName(privateAppViewName));
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix(
                appConfig.getBackEnd().getApiBasePath() + BASE_PATH_SUFFIX,
                HandlerTypePredicate.forAnnotation(RestController.class));
    }

    private String getRedirectViewName(String viewController) {
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX
                + appConfig.getBackEnd().getServerBasePath()
                + viewController
                + DELIMITER;
    }

    private String getForwardViewName(String viewName) {
        return UrlBasedViewResolver.FORWARD_URL_PREFIX + viewName;
    }
}
