package hr.rba.credit_card_client_service.configuration.feign.card_creation_api;

import com.google.gson.Gson;
import feign.okhttp.OkHttpClient;
import org.springframework.context.annotation.Bean;

public class CardCreationApiFeignConfiguration {
    @Bean
    public CardCreationApiErrorDecoder clientErrorDecoder() {
        Gson gson = new Gson();
        return new CardCreationApiErrorDecoder(gson);
    }
    @Bean
    public OkHttpClient feignClient() {
        return new OkHttpClient();
    }
}