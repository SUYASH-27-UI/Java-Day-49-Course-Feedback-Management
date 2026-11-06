import java.util.*;
import java.io.*;

class Feedback {

    int id;
    String studentName;
    String course;
    int rating;
    String comment;

    Feedback(int id, String studentName,
             String course, int rating, String comment) {

        this.id = id;
        this.studentName = studentName;
        this.course = course;
        this.rating = rating;
        this.comment = comment;
    }

    void display() {
        System.out.println("\nFeedback ID: " + id);
        System.out.println("Student: " + studentName);
        System.out.println("Course: " + course);
        System.out.println("Rating: " + rating + "/5");
        System.out.println("Comment: " + comment);
    }
}

public class Main {

    static ArrayList<Feedback> feedbackList = new ArrayList<>();

    static Scanner sc = new Scanner(System.in);

    static final String FILE_NAME = "feedback.txt";

    static void loadFeedback() {

        try (BufferedReader br =
                     new BufferedReader(new FileReader(FILE_NAME))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split("\\|", 5);

                if (data.length == 5) {
                    feedbackList.add(new Feedback(
                        Integer.parseInt(data[0]),
                        data[1],
                        data[2],
                        Integer.parseInt(data[3]),
                        data[4]
                    ));
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("New feedback database created.");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading feedback file.");
        }
    }

    static void saveFeedback() {

        try (BufferedWriter bw =
                     new BufferedWriter(new FileWriter(FILE_NAME))) {

            for (Feedback f : feedbackList) {

                bw.write(
                    f.id + "|" +
                    f.studentName + "|" +
                    f.course + "|" +
                    f.rating + "|" +
                    f.comment
                );

                bw.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error saving feedback.");
        }
    }

    static void addFeedback() {

        int id = 1001;

        for (Feedback f : feedbackList) {
            if (f.id >= id) {
                id = f.id + 1;
            }
        }

        System.out.print("Enter Student Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Course Name: ");
        String course = sc.nextLine();

        System.out.print("Enter Rating (1-5): ");
        int rating = Integer.parseInt(sc.nextLine());

        if (rating < 1 || rating > 5) {
            System.out.println("Rating must be between 1 and 5!");
            return;
        }

        System.out.print("Enter Feedback Comment: ");
        String comment = sc.nextLine();

        if (name.contains("|") || course.contains("|")
                || comment.contains("|")) {
            System.out.println("The | character is not allowed.");
            return;
        }

        Feedback f = new Feedback(
            id, name, course, rating, comment
        );

        feedbackList.add(f);

        saveFeedback();

        System.out.println("Feedback submitted successfully!");
        System.out.println("Feedback ID: " + id);
    }

    static void displayFeedback() {

        if (feedbackList.isEmpty()) {
            System.out.println("No feedback available.");
            return;
        }

        for (Feedback f : feedbackList) {
            f.display();
        }
    }

    static void calculateAverage() {

        if (feedbackList.isEmpty()) {
            System.out.println("No feedback available.");
            return;
        }

        HashMap<String, int[]> courseRatings = new HashMap<>();

        for (Feedback f : feedbackList) {

            int[] values = courseRatings.getOrDefault(
                f.course, new int[2]
            );

            values[0] += f.rating;
            values[1]++;

            courseRatings.put(f.course, values);
        }

        System.out.println("\n===== COURSE RATING REPORT =====");

        for (String course : courseRatings.keySet()) {

            int[] values = courseRatings.get(course);

            double average = (double) values[0] / values[1];

            System.out.printf(
                "%s: %.2f/5 (%d feedbacks)%n",
                course, average, values[1]
            );
        }
    }

    static void searchFeedback() {

        System.out.print("Enter Course Name: ");
        String course = sc.nextLine();

        boolean found = false;

        for (Feedback f : feedbackList) {

            if (f.course.equalsIgnoreCase(course)) {
                f.display();
                found = true;
            }
        }

        if (!found) {
            System.out.println("No feedback found for this course.");
        }
    }

    public static void main(String[] args) {

        loadFeedback();

        int choice = 0;

        do {

            System.out.println("\n===== COURSE FEEDBACK SYSTEM =====");
            System.out.println("1. Submit Feedback");
            System.out.println("2. Display Feedback");
            System.out.println("3. Course Rating Report");
            System.out.println("4. Search Course Feedback");
            System.out.println("5. Exit");

            System.out.print("Enter Choice: ");

            try {

                choice = Integer.parseInt(sc.nextLine());

                switch (choice) {

                    case 1:
                        addFeedback();
                        break;

                    case 2:
                        displayFeedback();
                        break;

                    case 3:
                        calculateAverage();
                        break;

                    case 4:
                        searchFeedback();
                        break;

                    case 5:
                        System.out.println("Thank you!");
                        break;

                    default:
                        System.out.println("Invalid choice!");
                }

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }

        } while (choice != 5);

        sc.close();
    }
}
