package app;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import java.util.Scanner;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class Main {
    private static final String API_KEY = "ENTER-YOUR-API-KEY-HERE";
    private static final String API_URL = "https://api.openai.com/v1/completions";

    public static void main(String[] args) throws Exception {

        Scanner in = new Scanner(System.in, StandardCharsets.UTF_8);

        System.out.println("Hello! i'm ChatGPTConsole.");
        System.out.println("Enter the prompt or Enter for Exit.");

        while (true)
        {
            System.out.print("> ");
            String message = in.nextLine();

            if (message.length() == 0) {
                System.out.println("Good Bye!");
                break;
            }

            System.out.println("Get response...");
            String response = getResponse(message);
            System.out.println(response);
        }
    }

    private static String getResponse(String prompt) throws Exception {
        URL url = new URL(API_URL);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + API_KEY);

        String message = "{\"prompt\": \"" + prompt + "\", \"model\": \"text-davinci-003\", \"temperature\": 0.7, \"max_tokens\": 1000}";
        String result = "Try again please...";

        try {
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            byte[] input = message.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();

            reader.close();
            connection.disconnect();

            //Parse response
            JsonReader jsonReader = Json.createReader(new StringReader(response));
            JsonObject responseObj = jsonReader.readObject();
            result =  responseObj.getJsonArray("choices").getJsonObject(0).getString("text");

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

        return result;
    }

}
