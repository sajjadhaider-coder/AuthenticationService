package com.spring3.oauth.jwt;

import com.spring3.oauth.jwt.helpers.RefreshableCRUDRepositoryImpl;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(repositoryBaseClass = RefreshableCRUDRepositoryImpl.class)
@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "1024 Technology Co., Ltd.",
                version = "1.0.0",
                description = "The application exclusively belongs to 1024 Technology Co., Ltd. and is designed with the purpose of streamlining business operations.",
                contact = @Contact(
                        name = "Shijina Qin (CEO 1024 Technology Co., Ltd.)",
                        email = "9629523@gmail.com"
                ),
                license = @License(
                        name = "license",
                        url = "license"
                )
        )
)
public class OauthJwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(OauthJwtApplication.class, args);
    }

}
