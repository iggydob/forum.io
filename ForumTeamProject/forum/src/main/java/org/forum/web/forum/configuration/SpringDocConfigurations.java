package org.forum.web.forum.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@SecurityScheme(
//        name = "Authorization",
//        type = SecuritySchemeType.HTTP,
//        bearerFormat = "JWT",
//        scheme = "bearer"
//)
public class SpringDocConfigurations {


    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("Authorization",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("basic")));
    }

//    @Bean
//    public OpenAPI customOpenAPI() {
//        return new OpenAPI()
//                .components(new Components()
//                        .addSecuritySchemes("bearer-key",
//                                new io.swagger.v3.oas.models.security.SecurityScheme()
//                                        .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
//                                        .scheme("bearer")
//                                        .bearerFormat("JWT")));
//    }
//
//    @PostConstruct
//    public void printMessage() {
//        System.out.println("Bearer configuration is working");
//    }

//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("your.base.package.controller"))
//                .build()
//                .securitySchemes(List.of(new ApiKey("Authorization", HttpHeaders.AUTHORIZATION, "header")));
//    }
//
//    @Bean
//    public SecurityConfiguration security() {
//        return SecurityConfigurationBuilder.builder()
//                .scopeSeparator(",")
//                .additionalQueryStringParams(null)
//                .useBasicAuthenticationWithAccessCodeGrant(false)
//                .build();
//    }

}
