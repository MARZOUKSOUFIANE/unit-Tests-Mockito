package com.octo;

import com.octo.model.VirementRequest;
import com.octo.service.VirementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class DemoApplication {

    @Autowired
    VirementService virementService;

    public static void main(String[] args) {

        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    CommandLineRunner start(){
        return args -> {
            VirementRequest virementRequest=new VirementRequest("12345","123456", BigDecimal.valueOf(3000));
            try {
                virementService.performTransfer(virementRequest);
            }catch (Exception e){
                e.getMessage();
            }
        };
    }

}
