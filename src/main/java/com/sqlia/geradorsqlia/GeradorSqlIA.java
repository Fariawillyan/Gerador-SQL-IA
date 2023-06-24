package com.sqlia.geradorsqlia;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/")
public class GeradorSqlIA {

        private static final String OPENAI_API_URL = "https://api.openai.com/v1/engines/davinci/completions";
        private static final String OPENAI_API_TOKEN = "xxx";

        @GetMapping("/generate-sql")
        public ResponseEntity<String> generateSqlQuery() {
                CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                HttpPost request = new HttpPost(OPENAI_API_URL);

                // Defina o token de autenticação no cabeçalho da solicitação
                request.addHeader("Authorization", "Bearer " + OPENAI_API_TOKEN);

                // Defina o parâmetro do modelo
                request.addHeader("model", "text-davinci-003");

                // Defina o corpo da solicitação
                String requestBody = "{\n" +
                        " \"prompt\": \"era uma vez \",\n" +
                        "  \"max_tokens\": 50\n" +
                        "}";
                StringEntity entity = new StringEntity(requestBody, ContentType.APPLICATION_JSON);
                request.setEntity(entity);

                try {
                        HttpResponse response = httpClient.execute(request);
                        int statusCode = response.getStatusLine().getStatusCode();
                        System.out.println("Response Code: " + statusCode);

                        HttpEntity responseEntity = response.getEntity();
                        if (responseEntity != null) {
                                String responseBody = EntityUtils.toString(responseEntity);
                                System.out.println("Response Body: " + responseBody);

                                // Converter a resposta em JSON
                                ObjectMapper objectMapper = new ObjectMapper();
                                Object json = objectMapper.readValue(responseBody, Object.class);
                                String jsonResponse = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);

                                return ResponseEntity.ok(jsonResponse);
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }

                return ResponseEntity.status(500).body("Failed to generate SQL query");
        }
}
