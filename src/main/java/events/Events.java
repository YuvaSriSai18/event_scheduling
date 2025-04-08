package events;

import java.io.IOException;
import java.util.Scanner;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Events {
    private static final OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = "http://localhost:5500";
    private static final Scanner scanner = new Scanner(System.in);

    public static void manageEvents() throws IOException {
        while (true) {
            System.out.println("\n1. Create Event");
            // System.out.println("2. View All Events");
            System.out.println("2. Update Event");
            System.out.println("3. Delete Event");
            System.out.println("4. View Ongoing Events");
            System.out.println("5. View Upcoming Events");
            System.out.println("6. View Completed Events");
            System.out.println("7. Go Back");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: createEvent(); break;
                // case 2: getEvents("/all"); break;
                case 2: updateEvent(); break;
                case 3: deleteEvent(); break;
                case 4: getEvents("/ongoing"); break;
                case 5: getEvents("/upcoming"); break;
                case 6: getEvents("/completed"); break;
                case 7: return;
                default: System.out.println("Invalid option!");
            }
        }
    }

    private static void createEvent() throws IOException {
        if (Auth.getToken().isEmpty()) {
            System.out.println("Please login first.");
            return;
        }

        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        System.out.print("Enter date (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        System.out.print("Enter time (HH:MM): ");
        String time = scanner.nextLine();
        String uid = Auth.getUser().getString("uid") ;

        JSONObject json = new JSONObject()
                .put("title", title)
                .put("description", description)
                .put("date", date)
                .put("time", time)
                .put("createdBy" , uid) ;

        RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(BASE_URL + "/event/create")
                .post(body)
                .addHeader("Authorization", "Bearer " + Auth.getToken())
                .build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println(response.body().string());
        }
    }

    private static void getEvents(String type) throws IOException {
        if (Auth.getToken().isEmpty()) {
            System.out.println("Please login first.");
            return;
        }

        if (Auth.getUser() == null) {
            System.out.println("User data not available. Please log in again.");
            return;
        }
        String uid = Auth.getUser().getString("uid");
        String url = BASE_URL + "/event/" + uid + type;

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + Auth.getToken())
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                System.out.println("Events: " + response.body().string());
            } else {
                System.out.println("Failed to fetch events: " + response.code());
            }
        }
    }


    private static void updateEvent() throws IOException {
        if (Auth.getToken().isEmpty()) {
            System.out.println("Please login first.");
            return;
        }

        System.out.print("Enter Event ID to update: ");
        String eventId = scanner.nextLine();
        System.out.print("Enter new title (leave blank to keep current): ");
        String title = scanner.nextLine();
        System.out.print("Enter new description (leave blank to keep current): ");
        String description = scanner.nextLine();
        System.out.print("Enter new date (leave blank to keep current): ");
        String date = scanner.nextLine();
        System.out.print("Enter new time (leave blank to keep current): ");
        String time = scanner.nextLine();

        JSONObject json = new JSONObject();
        if (!title.isEmpty()) json.put("title", title);
        if (!description.isEmpty()) json.put("description", description);
        if (!date.isEmpty()) json.put("date", date);
        if (!time.isEmpty()) json.put("time", time);

        RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(BASE_URL + "/event/" + eventId)
                .put(body)
                .addHeader("Authorization", "Bearer " + Auth.getToken())
                .build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println(response.body().string());
        }
    }

    private static void deleteEvent() throws IOException {
        if (Auth.getToken().isEmpty()) {
            System.out.println("Please login first.");
            return;
        }

        System.out.print("Enter Event ID to delete: ");
        String eventId = scanner.nextLine();

        Request request = new Request.Builder()
                .url(BASE_URL + "/event/" + eventId)
                .delete()
                .addHeader("Authorization", "Bearer " + Auth.getToken())
                .build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println(response.body().string());
        }
    }
}
