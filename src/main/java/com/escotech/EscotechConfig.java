package com.escotech;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import java.util.Locale;

@Configuration
public class EscotechConfig implements WebMvcConfigurer {

    /**
     * https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-servlet
     The RequestMappingHandlerAdapter provides a flag called ignoreDefaultModelOnRedirect, which you can use to
     indicate that the content of the default Model should never be used if a controller method redirects. Instead,
     the controller method should declare an attribute of type RedirectAttributes or, if it does not do so, no
     attributes should be passed on to RedirectView
     */

    @Autowired
    private ApplicationContext applicationContext;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");

        // added this for scsss
        registry.addResourceHandler("/scss/**").addResourceLocations("classpath:/static/scss/");
    }

    // Adds internationalization:
    // Ties our current session to our locale.
    // This bean creates an instance of the LocaleResolver and puts it in the spring registry.
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.US);
        return slr;
    }

    // adds internationalization:
    // Looks for a parameter either through a hidden element or on our url string as a query parameter -
    // however we want to pass that in, but it looks for that to see if it should intercept that change.
    // We are looking for the param name of lang.
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    // Adds internationalization:
    // This is a feature we override from the WebMvcConfigurer. It registers the LocaleChangeInterceptor
    // run and go to http://localhost:8080/music_pusher/registration?lang=es
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    // Use thymeleaf templates
    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");
        return templateResolver;
    }

    // Use thymeleaf templates:
    // We didn't have to create an engine of any sort when we were using jsp pages. So this is a little bit
    // unique to thymeleaf. We have to create a spring template engine that will process the pages and
    // substitute in the model values from spring into our pages to be displayed. Noticed that this code makes
    // a call to that template resolver method.
    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addDialect(new Java8TimeDialect());
        templateEngine.addDialect(new SpringSecurityDialect());
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }

    // Use thymeleaf templates:
    // Finally we have a view resolver the view Resolver is a little bit different because the template
    // resolver looked up the actual template. The view resolver just takes whichever template was loaded
    // and returns that based off the name. So they kind of work in conjunction. I will tell you now, though,
    // that the viewResolver.setOrder has to be before the jsp page in this example for it to work. So we're
    // going to change the order of our jsp resolver to one and this to zero. If not, it'll look for jsp's
    // name this way.
    @Bean
    public ViewResolver thymeleafResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setOrder(0);
        return viewResolver;
    }
}
