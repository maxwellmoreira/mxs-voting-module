package com.mxs.voting;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Voting Module", version = "0.0.1-SNAPSHOT", description = "Module responsible for a voting system"))
public class MxsVotingModuleApplication {
    public static void main(String[] args) {
        SpringApplication.run(MxsVotingModuleApplication.class, args);
    }
}
