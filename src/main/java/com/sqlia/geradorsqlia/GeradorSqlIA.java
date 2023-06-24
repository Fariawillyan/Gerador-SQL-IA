package com.sqlia.geradorsqlia;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GeradorSqlIA {
        public static void main(String[] args) throws IOException {
                // Parâmetros da solicitação
                String url = "https://api.openai.com/v1/engines/davinci-codex/completions";
                String apiKey = "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";  // Substitua YOUR_API_KEY pelo seu token de autenticação da API

                // Construir a solicitação
                URL apiUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "Bearer " + apiKey);
                connection.setDoOutput(true);

                // Definir os parâmetros da solicitação
                String requestData = "{\"prompt\": \"Query de exemplo\", \"max_tokens\": 100}";

                // Enviar a solicitação
                try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                        outputStream.writeBytes(requestData);
                        outputStream.flush();
                }

                // Ler a resposta da solicitação
                StringBuilder response = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                                response.append(line);
                        }
                }

                // Processar a resposta
                String jsonResponse = response.toString();
                System.out.println("Resposta: " + jsonResponse);
        }
}
