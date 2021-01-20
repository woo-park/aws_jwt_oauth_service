package com.awsjwtservice.config;

import com.awsjwtservice.config.annotation.LoginUserHandlerMethodArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

// @Slf4j
@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final LoginUserHandlerMethodArgumentResolver loginUserHandlerMethodArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(loginUserHandlerMethodArgumentResolver);
    }


//     @Bean
//     public MappingJackson2HttpMessageConverter jsonEscapeConverter() {
//         ObjectMapper copy = objectMapper.copy();
//         copy.getFactory().setCharacterEscapes(new HtmlCharacterEscapes());
//         return new MappingJackson2HttpMessageConverter(copy);
//     }
}


