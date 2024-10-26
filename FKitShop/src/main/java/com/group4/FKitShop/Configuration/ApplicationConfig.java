package com.group4.FKitShop.Configuration;

import com.group4.FKitShop.Entity.Accounts;
import com.group4.FKitShop.Repository.AccountsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;

@Slf4j
@Configuration
public class ApplicationConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner runner(AccountsRepository accountsRepository) {
        return args -> {
            if (accountsRepository.findByrole("admin").isEmpty()) {
                Accounts accounts = Accounts.builder()
                        .accountID("A00001")
                        .dob(new Date(new java.util.Date().getTime()))
                        .createDate(new Date(new java.util.Date().getTime()))
                        .email("admin@gmail.com")
                        .password(passwordEncoder.encode("admin@gmail.com"))
                        .fullName("Admin")
                        .phoneNumber("0123456789")
                        .image("")
                        .role("admin")
                        .status(1)
                        .build();
                accountsRepository.save(accounts);
                log.warn("admin accounts has been created with default email: admin@gmail.com");
                log.warn("default admin password: 'admin@gmail.com', please change it later ");
            }
        };
    }
}
