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
        private static final String OPENAI_API_TOKEN = "XXXXXXX";

        @GetMapping("/generate-sql")
        public ResponseEntity<String> generateSqlQuery() {
                CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                HttpPost request = new HttpPost(OPENAI_API_URL);

                // Defina o token de autenticação no cabeçalho da solicitação
                request.addHeader("Authorization", "Bearer " + OPENAI_API_TOKEN);
                request.addHeader("model", "text-curie-001");

                // Defina o corpo da solicitação
                String requestBody = """
                {
                  "prompt": "### Postgres SQL tables, with their properties:\\n#\\n# Employee(id, name, department_id)\\n# Department(id, name, address)\\n# Salary_Payments(id, employee_id, amount, date)\\n#\\n### A query to list the names of the departments which employed more than 10 employees in the last 3 months\\nSELECT",
                  "temperature": 0,
                  "max_tokens": 150,
                  "top_p": 1.0,
                  "frequency_penalty": 0.0,
                  "presence_penalty": 0.0,
                  "stop": ["#", ";"]
                }
                """;
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
