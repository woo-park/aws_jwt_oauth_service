package com.awsjwtservice;


import com.awsjwtservice.domain.Site;
import com.awsjwtservice.service.SiteService;
import com.awsjwtservice.storage.StorageProperties;
import com.awsjwtservice.storage.StorageService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

//@EnableResourceServer
//@EnableJpaAuditing  // JPA Auditing 활성화
@EnableConfigurationProperties(StorageProperties.class)
@SpringBootApplication
public class AuthServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }

    @Bean
    public ApplicationRunner siteInitializer(SiteService siteService) {
        return args -> {
            siteService.deleteAll();
          siteService.create(Site.builder().siteUrl("tempUrl").userSeq((long) 99).title("test title").build());
        };
    }
}

