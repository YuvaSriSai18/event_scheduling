package events;

import java.io.IOException;
import java.util.Base64;
import java.util.Scanner;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Auth {
    private static final OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = "http://localhost:5500";
    private static String token = "";
    private static JSONObject user = null; // Store decoded user data
    private static final Scanner scanner = new Scanner(System.in);

    public static String getToken() {
        return token;
    }

    public static JSONObject getUser() {
        return user;
    }

    public static void signup() throws IOException {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        JSONObject json = new JSONObject()
                .put("name", name)
                .put("email", email)
                .put("password", password);

        RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(BASE_URL + "/auth/signup")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println(response.body().string());
        }
    }

    public static void login() throws IOException {
        System.out.print("Enter email: ");  
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        JSONObject json = new JSONObject()
                .put("email", email)
                .put("password", password);

        RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(BASE_URL + "/auth/login")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                JSONObject jsonResponse = new JSONObject(response.body().string());
                token = jsonResponse.getString("token");
                decodeToken(token); // Decode token to extract user details
                System.out.println("Login Successful!");
            } else {
                System.out.println("Login failed: " + (response.body() != null ? response.body().string() : "No response"));
            }
        }
    }

    private static void decodeToken(String token) {
        try {
            String[] parts = token.split("\\."); // JWT has 3 parts separated by "."
            if (parts.length < 2) {
                System.out.println("Invalid token format");
                return;
            }

            String payload = new String(Base64.getUrlDecoder().decode(parts[1])); // Decode payload
            user = new JSONObject(payload);
            System.out.println("User Info: " + user.toString(2)); // Pretty print user data
            System.out.println("User uid: " + user.toString(2));
        } catch (Exception e) {
            System.out.println("Error decoding token: " + e.getMessage());
        }
    }
}
