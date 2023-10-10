//package com.smdb.spatialmeta.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//@Configuration
//@EnableSwagger2
//public class SwaggerConfig {
//
//    //http://127.0.0.1:8001/v2/api-docs
//    //http://localhost:8001/v2/api-docs
//    //http://192.168.1.67:8001/v2/api-docs
//    //http://39.105.104.226:8001/v2/api-docs
//    @Bean
//    public Docket docket() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo()).select()
//                .apis(RequestHandlerSelectors.basePackage("com.smdb.spatialmeta.controller"))
//                .paths(PathSelectors.any())
//                .build();
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("郭郭api文档")
//                .description("管理后台api")
//                .version("v1")
//                .build();
//
//    }
//}
