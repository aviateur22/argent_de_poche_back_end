package com.ctoutweb.argenDePoche.infra.config;

import com.ctoutweb.argentDePoche.application.configuration.annotation.CoreService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        basePackages = "com.ctoutweb.argentDePoche.application",
        includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {CoreService.class})}
)
public class CoreAnnotationScan {
}
