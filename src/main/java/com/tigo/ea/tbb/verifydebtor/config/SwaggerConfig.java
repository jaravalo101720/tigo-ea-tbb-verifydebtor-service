package com.tigo.ea.tbb.verifydebtor.config;

import java.util.Set;

import org.springframework.boot.actuate.endpoint.mvc.EndpointHandlerMapping;
import org.springframework.boot.actuate.endpoint.mvc.MvcEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicates;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {
	
	public static final String PGK_ROUTE_SWAGGER = "com.tigo.ea.tbb.verifydebtor.rest";

	@Bean
	public Docket swaggerSpringMvcPlugin(final EndpointHandlerMapping actuatorEndpointHandlerMapping) {
		ApiSelectorBuilder builder = new Docket(DocumentationType.SWAGGER_2)
				.useDefaultResponseMessages(false)
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage(PGK_ROUTE_SWAGGER));
		// Ignore the spring-boot-actuator endpoints:
		Set<MvcEndpoint> endpoints = actuatorEndpointHandlerMapping.getEndpoints();
		endpoints.forEach(endpoint -> {
			String path = endpoint.getPath();
			builder.paths(Predicates.not(PathSelectors.regex(path + ".*")));
		});
		return builder.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("TBB API Swagger Documentation")
				.description("Realiza consumos hacia plataforma BSS")
				.termsOfServiceUrl("http://tigo.bo/terms.html")
				.contact(new Contact("HTC CORP", "http://tigo.bo.com","@tigo.net.bo"))
				.license("Apache License Version 2.0")
				//.licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
				.version("1.0")
				.build();
	}
}
