package com.smart.watchboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WebConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

//    @Autowired
//    CloseableHttpClient httpClient;
//
//    @Value("${api.host.baseurl}")
//    private String apiHost;
//
//    @Bean
//    public RestTemplate restTemplate() {
//
//        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
//        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(apiHost));
//        return restTemplate;
//    }
//
//    @Bean
//    @ConditionalOnMissingBean
//    public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
//
//        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory
//                = new HttpComponentsClientHttpRequestFactory();
//        clientHttpRequestFactory.setHttpClient(httpClient);
//        return clientHttpRequestFactory;
//    }
}
