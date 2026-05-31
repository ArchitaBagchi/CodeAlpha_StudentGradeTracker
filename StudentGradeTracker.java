import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class StudentGradeTracker {
    private final List<Student> students = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        new StudentGradeTracker().run();
    }

    private void run() {
        System.out.println("=== Student Grade Tracker ===");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose an option: ");
            switch (choice) {
                case 1 -> addStudent();
                case 2 -> addGrade();
                case 3 -> showSummary();
                case 4 -> showStudentReport();
                case 5 -> running = false;
                default -> System.out.println("Invalid choice. Try again.");
            }
        }

        System.out.println("Exiting Student Grade Tracker.");
    }

    private void printMenu() {
        System.out.println();
        System.out.println("1. Add student");
        System.out.println("2. Add grade to student");
        System.out.println("3. Show class summary");
        System.out.println("4. Show individual student report");
        System.out.println("5. Exit");
    }

    private void addStudent() {
        String name = readNonEmptyString("Enter student name: ");
        students.add(new Student(name));
        System.out.println("Student added successfully.");
    }

    private void addGrade() {
        if (students.isEmpty()) {
            System.out.println("No students available. Add a student first.");
            return;
        }

        Student student = selectStudent();
        if (student == null) {
            return;
        }

        double grade = readDouble("Enter grade (0-100): ", 0, 100);
        student.grades.add(grade);
        System.out.println("Grade added successfully.");
    }

    private void showSummary() {
        if (students.isEmpty()) {
            System.out.println("No students to display.");
            return;
        }

        double totalAverage = 0;
        double highest = Double.MIN_VALUE;
        double lowest = Double.MAX_VALUE;
        int gradedStudents = 0;

        System.out.println();
        System.out.println("--- Class Summary ---");
        for (Student student : students) {
            String averageText = student.grades.isEmpty()
                ? "No grades yet"
                : String.format("%.2f", student.average());
            System.out.printf("%s | Grades: %d | Average: %s%n",
                student.name,
                student.grades.size(),
                averageText);

            if (!student.grades.isEmpty()) {
                double avg = student.average();
                totalAverage += avg;
                highest = Math.max(highest, student.highest());
                lowest = Math.min(lowest, student.lowest());
                gradedStudents++;
            }
        }

        if (gradedStudents > 0) {
            System.out.printf("Overall average: %.2f%n", totalAverage / gradedStudents);
            System.out.printf("Highest score: %.2f%n", highest);
            System.out.printf("Lowest score: %.2f%n", lowest);
        } else {
            System.out.println("No grades have been recorded yet.");
        }
    }

    private void showStudentReport() {
        if (students.isEmpty()) {
            System.out.println("No students available.");
            return;
        }

        Student student = selectStudent();
        if (student == null) {
            return;
        }

        System.out.println();
        System.out.println("--- Student Report ---");
        System.out.println("Name: " + student.name);
        if (student.grades.isEmpty()) {
            System.out.println("No grades recorded.");
            return;
        }

        System.out.println("Grades: " + student.grades);
        System.out.printf("Average: %.2f%n", student.average());
        System.out.printf("Highest: %.2f%n", student.highest());
        System.out.printf("Lowest: %.2f%n", student.lowest());
    }

    private Student selectStudent() {
        System.out.println("Available students:");
        for (int i = 0; i < students.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, students.get(i).name);
        }

        int choice = readInt("Select a student number: ");
        if (choice < 1 || choice > students.size()) {
            System.out.println("Invalid student selection.");
            return null;
        }
        return students.get(choice - 1);
    }

    private String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be empty.");
        }
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    private double readDouble(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                double value = Double.parseDouble(input);
                if (value < min || value > max) {
                    System.out.printf("Value must be between %.0f and %.0f.%n", min, max);
                    continue;
                }
                return value;
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static class Student {
        private final String name;
        private final List<Double> grades = new ArrayList<>();

        private Student(String name) {
            this.name = name;
        }

        private double average() {
            return grades.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        }

        private double highest() {
            return grades.stream().max(Comparator.naturalOrder()).orElse(0.0);
        }

        private double lowest() {
            return grades.stream().min(Comparator.naturalOrder()).orElse(0.0);
        }
    }
}
