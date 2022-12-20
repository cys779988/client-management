package dev.be.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
	
	@Bean
	public GroupedOpenApi groupedOpenApi() {
		return GroupedOpenApi.builder()
					.group("api")
					.pathsToMatch("/api/**")
					.build();
	}
	
	@Bean
	public GroupedOpenApi groupedUserApi() {
		return GroupedOpenApi.builder()
				.group("user")
				.pathsToMatch("/user/**")
				.build();
	}
	
	@Bean
	public OpenAPI openApi() {
		return new OpenAPI()
				.info(new Info().title("HELP-ME COMPANY API")
						.description("HELP-ME COMPANY API 명세서")
						.version("v1.0.0"));
	}
}
