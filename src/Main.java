import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int userType;

        System.out.println("Welcome to the Course Registration System");

        try {
            while (true) {
                System.out.println("Select your user type:");
                System.out.println("1. Student");
                System.out.println("2. Advisor");
                System.out.println("3. Admin");
                System.out.println("4. Exit");
                System.out.print("Enter your choice (1-4): ");
                userType = scanner.nextInt();

                switch (userType) {
                    case 1:
                        Student student=new Student();
                        student.displayStudentMenu();
                        break;
                    case 2:
                        Advisor advisor=new Advisor();
                        advisor.displayAdvisorMenu();
                        break;
                    case 3:
                        Admin admin = new Admin();
                        admin.displayAdminMenu();
                        break;
                    case 4:
                        System.out.println("Exiting the system. Goodbye!");
                        return; 
                    default:
                        System.out.println("Invalid choice. Please select a valid option (1-4).");
                        break;
                }
            }
        } finally {
            scanner.close(); // Close the scanner when done
        }
    }
}
