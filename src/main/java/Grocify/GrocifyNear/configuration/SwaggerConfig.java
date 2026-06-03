package Grocify.GrocifyNear.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Grocify API")
                        .version("1.0")
                        .description("""
                                A location-aware grocery backend that helps users find nearby shops,
                                compare product prices across stores, and place orders with real payment
                                integration — powered by Spring Boot 4, JWT security, Razorpay, and an
                                AI recommendation engine
                                """)
                        .contact(new Contact()
                                .name("Satyendra More")
                                .email("satyendramore25@gmail.com")
                        )
                );
    }
}