package com.sqlia.geradorsqlia;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@RestController
@RequestMapping("/api/")
public class GeradorSqlIA {

        private static final Logger logger = LoggerFactory.getLogger(GeradorSqlIA.class);
        private static final String OPENAI_API_URL = "https://api.openai.com/v1/engines/davinci/completions";
        private static final String OPENAI_API_TOKEN = "Passar CHAVE aqui"; //TESTAR PROJETO VIA POSTMAN. JSON Exemplo no resourses

        @PostMapping("/generate-sql")
        public ResponseEntity<String> generateSqlQuery(@RequestBody String requestBody) {
                try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
                        HttpPost request = new HttpPost(OPENAI_API_URL);

                        // Define o token de autenticação no cabeçalho da solicitação
                        request.addHeader("Authorization", "Bearer " + OPENAI_API_TOKEN);
                        request.addHeader("model", "text-curie-001");

                        // Define o corpo da solicitação
                        StringEntity entity = new StringEntity(requestBody, ContentType.APPLICATION_JSON);
                        request.setEntity(entity);

                        HttpResponse response = httpClient.execute(request);
                        int statusCode = response.getStatusLine().getStatusCode();
                        logger.info("Response Code: {}", statusCode);

                        HttpEntity responseEntity = response.getEntity();
                        if (responseEntity != null) {
                                String responseBody = EntityUtils.toString(responseEntity);
                                logger.info("Response Body: {}", responseBody);

                                // Converter a resposta em JSON
                                ObjectMapper objectMapper = new ObjectMapper();
                                JsonNode responseJson = objectMapper.readTree(responseBody);

                                // Obter o texto da resposta
                                String text = responseJson.get("choices").get(0).get("text").asText();

                                return ResponseEntity.ok(text);
                        }
                } catch (IOException e) {
                        logger.error("Falha ao gerar SQL", e);
                }

                return ResponseEntity.status(500).body("Falha ao gerar SQL");
        }
}
