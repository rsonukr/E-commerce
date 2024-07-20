package com.example.ecommerce.config;

//import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.License;*/


import io.swagger.v3.oas.models.OpenAPI;



@Configuration
/*@OpenAPIDefinition(
		info=@Info(
			title="E-commerce API",
			version="1.0",
			description="API documentation for E-coomerce application",
			contact=@Contact(name="SONU KUMAR",email="rkumarsonu96@gmail.com"),
			license=@License(name="Apache 2.0",url="http://springdoc.org")
			)
)*/

public class swaggerConfig{
	
	@Bean
	OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new io.swagger.v3.oas.models.info.Info()
				.title("E-commerce API")
				.version("1.0")
				.description("API documentation for E-commmerce application")
				.contact(new io.swagger.v3.oas.models.info.Contact()
						.name("SONU KUMAR")
						.email("rkumarsonu96@gmail.com"))
				.license(new io.swagger.v3.oas.models.info.License()
						.name("Apache 2.0")
						.url("http://localhost:8080/swagger-ui/index.html")));
	}
	/*@Bean
	public GroupedOpenApi publicApi() {
		return GroupedOpenApi.builder()
				.group("public")
				.pathsToMatch("/product/**")
				//.packagesToScan("com.example.ecommerce")
				.build();
	}*/
}

