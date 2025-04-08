package events;

import java.io.IOException;
import java.util.Scanner;

public class App {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        while (true) {
            System.out.println("\n1. Login");
            System.out.println("2. Signup");
            System.out.println("3. Manage Events");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: Auth.login(); break;
                case 2: Auth.signup(); break;
                case 3: Events.manageEvents(); break;
                case 4: System.out.println("Exiting..."); System.exit(0); break;
                default: System.out.println("Invalid option! Please try again.");
            }
        }
    }
}
