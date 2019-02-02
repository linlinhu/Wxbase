package com.emin.platform.wxbase.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import com.emin.platform.wxbase.filter.UserFilter;

import freemarker.template.TemplateExceptionHandler;

@Configuration
@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter{
	
	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    registry.addResourceHandler("/js/**")
	            .addResourceLocations("classpath:/static/js/");
	    registry.addResourceHandler("/css/**")
	    		.addResourceLocations("classpath:/static/css/");
	    registry.addResourceHandler("/img/**")
				.addResourceLocations("classpath:/static/img/");
	    registry.addResourceHandler("/fonts/**")
				.addResourceLocations("classpath:/static/fonts/");
	    
	    
	}
	
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(userFilter()).addPathPatterns("/**").excludePathPatterns("/login","/getValidImg","/loginIn");
	}
	
	@Bean
	public UserFilter userFilter() {
		return new UserFilter();
	}
	
	@Bean
	public CommandLineRunner customFreemarker(FreeMarkerViewResolver resolver) {
		return new CommandLineRunner() {
			@Autowired
			private freemarker.template.Configuration configuration;

			@Override
			public void run(String... strings) throws Exception {
				configuration.setLogTemplateExceptions(false);
				configuration.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
				configuration.setNumberFormat("#");
				resolver.setViewClass(CustomFreeMarkerView.class);
			}
		};
	}
}
