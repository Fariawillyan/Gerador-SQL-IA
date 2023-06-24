package com.sqlia.geradorsqlia;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.net.http.HttpClient;

public class GeradorSqlIA {

        public static void main(String[] args) {
                CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                HttpPost request = new HttpPost("https://api.openai.com/v1/engines/davinci/completions");

                // Defina o token de autenticação no cabeçalho da solicitação
                request.addHeader("Authorization", "Bearer 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");
                //criar chave secreta no perfil login openIA

                // Defina o parâmetro do modelo
                request.addHeader("model", "text-davinci-003");

                // Defina o corpo da solicitação
                String requestBody = "{\n" +
                        " \"prompt\": \"select from where \",\n" +
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
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }
}
