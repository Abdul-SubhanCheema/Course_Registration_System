import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.io.*;

public class Admin {
	private HashMap<String, Course> courses;
	private HashMap<String, User> users;
	private HashMap<String, User> users1;
	private Scanner scanner;

	public Admin() {
		this.courses = new HashMap<>();
		this.users = new HashMap<>();
		this.users1 = new HashMap<>();
		this.scanner = new Scanner(System.in);
		loadDataFromFile();
		loadDataForStudents();
		loadDataForAdvisors();
	}

	public void displayAdminMenu() {
		int adminChoice;

		while (true) {
			System.out.println("\nAdmin Options:");
			System.out.println("1. Manage Courses");
			System.out.println("2. Manage Users");
			System.out.println("3. View System Statistics");
			System.out.println("4. Back to Main Page");

			System.out.print("Enter your choice (1-4): ");
			adminChoice = scanner.nextInt();

			switch (adminChoice) {
			case 1:
				manageCourses();
				break;
			case 2:
				manageUsers();
				break;
			case 3:
				viewStatistics();
				break;
			case 4:
				return;
			default:
				System.out.println("Invalid choice. Please select a valid option (1-4).");
				break;
			}
		}
	}

	public void viewStatistics() {
		System.out.println("\nView Statistics:");

		// Display statistics for courses
		System.out.println("Courses:");
		for (Course course : courses.values()) {
			System.out.println(course.getCourseCode() + " - " + course.getCourseName());
		}
		System.out.println("Total Courses: " + courses.size());

		// Display statistics for students
		System.out.println("\nStudents:");
		int totalStudents = 0;
		for (User user : users.values()) {
			if (user instanceof Student) {
				System.out.println(user.getUsername() + " - " + user.getPassword());
				totalStudents++;
			}
		}
		System.out.println("Total Students: " + totalStudents);

		// Display statistics for advisors
		System.out.println("\nAdvisors:");
		int totalAdvisors = 0;
		for (User user : users1.values()) {
			if (user instanceof Advisor) {
				System.out.println(user.getUsername() + " - " + user.getPassword());
				totalAdvisors++;
			}
		}
		System.out.println("Total Advisors: " + totalAdvisors);
	}

	public void manageUsers() {

		int choice;
		do {
			System.out.println("\nManage Users Menu:");
			System.out.println("1. Add Student");
			System.out.println("2. Add Advisor");
			System.out.println("3. Remove Student");
			System.out.println("4. Remove Advisor");
			System.out.println("5. Validate Course");
			System.out.println("6. Back to Main Menu");
			System.out.print("Enter your choice: ");
			choice = scanner.nextInt();
			scanner.nextLine();

			switch (choice) {
			case 1:
				addStudent();
				break;
			case 2:
				addAdvisor();
				break;
			case 3:
				removeStudent();
				break;
			case 4:
				removeAdvisor();
				break;
			case 5:
				ValidateCourse();
				break;
			case 6:
				System.out.println("Returning to Main Menu...");
				return;
			default:
				System.out.println("Invalid choice. Please try again.");
			}
		} while (choice != 4);

	}

	private void addStudent() {

		System.out.println("\nAdd Student:");
		System.out.print("Enter username: ");
		String username = scanner.nextLine();
		System.out.print("Enter password: ");
		String password = scanner.nextLine();

		int studentId;
		do {
			System.out.print("Enter Student ID: ");
			studentId = scanner.nextInt();
			if (users.containsKey(Integer.toString(studentId))) {
				System.out.println("Student ID already exists. Please enter a different Student ID.");
			}
		} while (users.containsKey(Integer.toString(studentId)));
		scanner.nextLine();
		System.out.print("Enter major: ");
		String major = scanner.nextLine();

		Student student = new Student(username, password, studentId, major);
		users.put(Integer.toString(studentId), student);
		addStudentToFile(student);

	}

	private void addAdvisor() {

		System.out.println("\nAdd Advisor:");
		System.out.print("Enter username: ");
		String username = scanner.nextLine();
		System.out.print("Enter password: ");
		String password = scanner.nextLine();

		int advisorId;
		do {
			System.out.print("Enter Advisor ID: ");
			advisorId = scanner.nextInt();
			if (users1.containsKey(Integer.toString(advisorId))) {
				System.out.println("Advisor ID already exists. Please enter a different Advisor ID.");
			}
		} while (users1.containsKey(Integer.toString(advisorId)));
		scanner.nextLine();
		System.out.print("Enter Department: ");
		String dep = scanner.nextLine();

		Advisor advisor = new Advisor(username, password, dep, advisorId);
		users1.put(Integer.toString(advisorId), advisor);
		addAdvisorToFile(advisor);

	}

	private void addAdvisorToFile(Advisor advisor) {
		try (FileWriter writer = new FileWriter("advisors.txt", true)) {
			writer.write(advisor.getAdvisorId() + "\n");
			writer.write(advisor.getUsername() + "\n");
			writer.write(advisor.getPassword() + "\n");
			writer.write(advisor.getDepartment() + "\n");
			writer.write("=====\n");
			System.out.println("Advisor added to file successfully.");
		} catch (IOException e) {
			System.out.println("Error adding advisor to file: " + e.getMessage());
		}
	}

	public void removeAdvisor() {
		System.out.println("\nRemove Advisor:");
		System.out.print("Enter Advisor ID to remove: ");
		int advisorIdToRemove = scanner.nextInt();
		scanner.nextLine(); // Consume newline

		boolean removed = false;
		Iterator<Map.Entry<String, User>> iterator = users1.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, User> entry = iterator.next();
			if (entry.getValue() instanceof Advisor) {
				Advisor advisor = (Advisor) entry.getValue();
				if (advisor.getAdvisorId() == advisorIdToRemove) {
					iterator.remove(); // Remove the entry from the HashMap
					removeAdvisorFromFile(advisor);
					removed = true;
					System.out.println("Advisor removed successfully.");
					break;
				}
			}
		}

		if (!removed) {
			System.out.println("Advisor not found.");
		}
	}

	private void removeStudentFromFile(Student student) {
		try {
			File inputFile = new File("students.txt");
			File tempFile = new File("temp.txt");

			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

			String line;
			boolean studentFound = false;
			while ((line = reader.readLine()) != null) {
				if (line.equals(Integer.toString(student.getStudentId()))) {
					studentFound = true;
					// Skip lines for the course to be removed
					for (int i = 0; i < 4; i++) {
						reader.readLine();
					}
					// Skip the separator line "====="
					reader.readLine();
					reader.readLine();
					continue;
				}
				writer.write(line + System.getProperty("line.separator"));
			}

			reader.close();
			writer.close();

			if (!studentFound) {
				System.out.println("Student not found.");
			} else {
				// Rename the temporary file to the original file
				if (!inputFile.delete()) {
					System.out.println("Error deleting file.");
					return;
				}
				if (!tempFile.renameTo(inputFile)) {
					System.out.println("Error renaming file.");
				} else {
					System.out.println("Student removed successfully.");
				}
			}
		} catch (IOException e) {
			System.out.println("Error removing student data: " + e.getMessage());
		}
	}

	private void removeAdvisorFromFile(Advisor advisor) {
		try {
			File inputFile = new File("advisors.txt");
			File tempFile = new File("temp.txt");

			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

			String line;
			boolean studentFound = false;
			while ((line = reader.readLine()) != null) {
				if (line.equals(Integer.toString(advisor.getAdvisorId()))) {
					studentFound = true;
					// Skip lines for the course to be removed
					for (int i = 0; i < 2; i++) {
						reader.readLine();
					}

					reader.readLine();
					reader.readLine();
					continue;
				}
				writer.write(line + System.getProperty("line.separator"));
			}

			reader.close();
			writer.close();

			if (!studentFound) {
				System.out.println("Advisor not found.");
			} else {
				// Rename the temporary file to the original file
				if (!inputFile.delete()) {
					System.out.println("Error deleting file.");
					return;
				}
				if (!tempFile.renameTo(inputFile)) {
					System.out.println("Error renaming file.");
				} else {
					System.out.println("Advisor removed successfully.");
				}
			}
		} catch (IOException e) {
			System.out.println("Error removing Advisor: " + e.getMessage());
		}
	}

	public void removeStudent() {
		System.out.println("\nRemove Student:");
		System.out.print("Enter Student ID to remove: ");
		int studentIdToRemove = scanner.nextInt();
		scanner.nextLine(); // Consume newline

		boolean removed = false;
		Iterator<Map.Entry<String, User>> iterator = users.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, User> entry = iterator.next();
			if (entry.getValue() instanceof Student) {
				Student student = (Student) entry.getValue();
				if (student.getStudentId() == studentIdToRemove) {
					iterator.remove(); // Remove the entry from the HashMap
					removeStudentFromFile(student);
					removed = true;
					System.out.println("Student removed successfully.");
					break;
				}
			}
		}

		if (!removed) {
			System.out.println("Student not found.");
		}
	}

	private void addStudentToFile(Student student) {
		try (FileWriter writer = new FileWriter("students.txt", true)) {
			writer.write(student.getStudentId() + "\n");
			writer.write(student.getUsername() + "\n");
			writer.write(student.getPassword() + "\n");

			writer.write(student.getMajor() + "\n");
			writer.write(student.getRegistercourses() + "\n");
			writer.write(student.getPassedcourses() + "\n");
			writer.write("=====\n"); // Add separator line
			System.out.println("Student added to file successfully.");
		} catch (IOException e) {
			System.out.println("Error adding student to file: " + e.getMessage());
		}
	}

	private void loadDataForStudents() {

		try (BufferedReader reader = new BufferedReader(new FileReader("students.txt"))) {
			String line;
			List<String> studentInfo = new ArrayList<>();
			while ((line = reader.readLine()) != null) {
				if (line.equals("=====")) {
					if (studentInfo.size() >= 5) {
						String username = studentInfo.get(1);
						String password = studentInfo.get(2);
						String studentId = studentInfo.get(0);
						String major = studentInfo.get(3);
						String enrolledCoursesString = studentInfo.get(4);
						String passcourse = studentInfo.get(5);
						ArrayList<String> enrolledCourses = parseEnrolledCourses(enrolledCoursesString);
						ArrayList<String> passCourses = parseEnrolledCourses(passcourse);
						Student student = new Student(username, password, Integer.parseInt(studentId), major,
								enrolledCourses, passCourses);
						users.put(studentId, student);
					} else {
						System.out.println("Invalid data format for student.");
					}
					studentInfo.clear(); // Clear the list for the next student
				} else {
					studentInfo.add(line);
				}
			}
		} catch (IOException e) {
			System.out.println("Error loading student data from file: " + e.getMessage());
		}
	}

	private static ArrayList<String> parseEnrolledCourses(String enrolledCoursesString) {
		ArrayList<String> enrolledCourses = new ArrayList<>();
		// Check if the string represents an empty list
		if (enrolledCoursesString.equals("[]")) {
			return enrolledCourses; // Return empty list if string is empty list representation
		}

		// If not empty list, proceed with parsing
		enrolledCoursesString = enrolledCoursesString.substring(1, enrolledCoursesString.length() - 1);
		String[] coursesArray = enrolledCoursesString.split(",");
		for (String course : coursesArray) {
			enrolledCourses.add(course.trim());
		}
		return enrolledCourses;
	}

	private void loadDataForAdvisors() {
		try (BufferedReader reader = new BufferedReader(new FileReader("advisors.txt"))) {
			String line;
			List<String> studentInfo = new ArrayList<>();
			while ((line = reader.readLine()) != null) {
				if (line.equals("=====")) {
					if (studentInfo.size() >= 3) {
						String username = studentInfo.get(1);
						String password = studentInfo.get(2);
						String advisortId = studentInfo.get(0);
						String department = studentInfo.get(3);
						Advisor advisor = new Advisor(username, password, department, Integer.parseInt(advisortId));
						users1.put(advisortId, advisor);
					} else {
						System.out.println("Invalid data format for Advisor.");
					}
					studentInfo.clear(); // Clear the list for the next student
				} else {
					studentInfo.add(line);
				}
			}
		} catch (IOException e) {
			System.out.println("Error loading Advisor data from file: " + e.getMessage());
		}
	}

	private void manageCourses() {
		int courseOption;

		while (true) {
			System.out.println("\nCourse Management:");
			System.out.println("1. Add Course");
			System.out.println("2. Remove Course");
			System.out.println("3. Update Course");
			System.out.println("4. Display Courses");
			System.out.println("5. Back to Admin Options");
			System.out.print("Enter your choice (1-5): ");
			courseOption = scanner.nextInt();

			switch (courseOption) {
			case 1:
				addCourse();
				break;
			case 2:
				removeCourse();
				break;
			case 3:
				updateCourse();
				break;
			case 4:
				displayCourses();
				break;
			case 5:
				return;
			default:
				System.out.println("Invalid choice. Please select a valid option (1-5).");
				break;
			}
		}
	}

	public void loadDataFromFile() {
		try (BufferedReader reader = new BufferedReader(new FileReader("courses.txt"))) {
			String line;
			ArrayList<String> courseDetails = new ArrayList<>();
			while ((line = reader.readLine()) != null) {
				if (line.equals("=====")) {
					// Process course details when separator is encountered
					processCourse(courseDetails);
					courseDetails.clear(); // Clear list for next course
				} else {
					courseDetails.add(line);
				}
			}
			// Process the last course after reading all lines
			if (!courseDetails.isEmpty()) {

				processCourse(courseDetails);
			}
			System.out.println("Courses loaded from file successfully.");
		} catch (IOException e) {
			System.out.println("Error loading courses from file: " + e.getMessage());
		}
	}

	private void processCourse(ArrayList<String> details) {

		if (details.size() == 9) {
			String courseCode = details.get(0);
			String courseName = details.get(1);
			String courseDescription = details.get(2);
			String instructor = details.get(3);
			int availableSeats = Integer.parseInt(details.get(4));
			String schedule = details.get(5);
			ArrayList<String> prerequisites = parsePrerequisites(details.get(6));
			int credits = Integer.parseInt(details.get(7));
			ArrayList<String> enrolledStudents = parseEnrolledStudents(details.get(8));

			Course course = new Course(courseCode, courseName, courseDescription, instructor, availableSeats, schedule,
					prerequisites, enrolledStudents, credits);
			courses.put(courseCode, course);
		} else {
			System.out.println("Invalid data format for course: " + details);
		}
	}

	private ArrayList<String> parsePrerequisites(String prerequisitesString) {

		// Example implementation:
		ArrayList<String> prerequisites = new ArrayList<>();

		if (!prerequisitesString.equals("[]")) { // Check if enrolled students array is not empty
			String[] parts = prerequisitesString.substring(1, prerequisitesString.length() - 1).split(",");
			for (String part : parts) {
				prerequisites.add(part);
			}
		}
		return prerequisites;
	}

	private ArrayList<String> parseEnrolledStudents(String enrolledString) {
		// Logic to parse enrolled students from string, assuming format [ALi,PAK]
		// Example implementation:
		ArrayList<String> enrolledStudents = new ArrayList<>();
		if (!enrolledString.equals("[]")) { // Check if enrolled students array is not empty
			String[] parts = enrolledString.substring(1, enrolledString.length() - 1).split(",");
			for (String part : parts) {
				enrolledStudents.add(part);
			}
		}
		return enrolledStudents;
	}

	private void addCourseToFile(Course course) {
		try (FileWriter writer = new FileWriter("courses.txt", true)) {

			writer.write(course.getCourseCode() + "\n");
			writer.write(course.getCourseName() + "\n");
			writer.write(course.getCourseDescription() + "\n");
			writer.write(course.getInstructor() + "\n");
			writer.write(course.getAvailableSeats() + "\n");
			writer.write(course.getSchedule() + "\n");
			writer.write(course.getPrerequisites() + "\n");
			writer.write(course.getCredits() + "\n");
			writer.write(course.getEnrolledStudents() + "\n");
			writer.write("=====\n"); // Empty line as separator between courses
			System.out.println("Course details written to file successfully.");
		} catch (IOException e) {
			System.out.println("Error writing course details to file: " + e.getMessage());
		}
	}

	public void addCourse() {
		System.out.println("\nAdd Course:");
		String courseCode;
		do {
			System.out.print("Enter course code: ");
			courseCode = scanner.next();
			if (courses.containsKey(courseCode)) {
				System.out.println("Course code already exists. Please enter a different course code.");
			}
		} while (courses.containsKey(courseCode));
		scanner.nextLine();
		System.out.print("Enter course name: ");
		String courseName = scanner.nextLine(); // Read course name using nextLine()

		System.out.print("Enter course description: ");
		String courseDescription = scanner.nextLine();

		System.out.print("Enter instructor: ");
		String instructor = scanner.nextLine();

		System.out.print("Enter available seats: ");
		int availableSeats = scanner.nextInt(); // Read available seats using nextInt()
		scanner.nextLine(); // Consume newline character

		System.out.print("Enter schedule: ");
		String schedule = scanner.nextLine();

		System.out.print("Enter prerequisites course code (comma-separated) If no just Enter: ");
		String[] prerequisitesArray = scanner.nextLine().split(",");
		ArrayList<String> prerequisites = new ArrayList<>();
		for (String prerequisite : prerequisitesArray) {
			prerequisites.add(prerequisite.trim());
		}

		System.out.print("Enter credits: ");
		int credits = scanner.nextInt(); // Read credits using nextInt()
		scanner.nextLine(); // Consume newline character

		Course newCourse = new Course(courseCode, courseName, courseDescription, instructor, availableSeats, schedule,
				prerequisites, credits);
		courses.put(courseCode, newCourse);
		addCourseToFile(newCourse);
	}

	private void removeCourseFromFile(String courseCodeToRemove) {
		try {
			File inputFile = new File("courses.txt");
			File tempFile = new File("temp.txt");

			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

			String line;
			boolean found = false;
			while ((line = reader.readLine()) != null) {
				if (line.equals(courseCodeToRemove)) {
					found = true;
					// Skip lines for the course to be removed
					for (int i = 0; i < 8; i++) {
						reader.readLine();
					}
					// Skip the separator line "====="
					reader.readLine();
					continue;
				}
				writer.write(line + System.getProperty("line.separator"));
			}

			reader.close();
			writer.close();

			if (!found) {
				System.out.println("Course code not found.");
			} else {
				// Rename the temporary file to the original file
				if (!inputFile.delete()) {
					System.out.println("Error deleting file.");
					return;
				}
				if (!tempFile.renameTo(inputFile)) {
					System.out.println("Error renaming file.");
				} else {
					System.out.println("Course data removed successfully.");
				}
			}
		} catch (IOException e) {
			System.out.println("Error removing course data: " + e.getMessage());
		}

	}

	private void removeCourse() {
		System.out.println("\nRemove Course:");
		System.out.print("Enter course code to remove: ");
		String courseCode = scanner.next();
		scanner.nextLine();

		if (courses.containsKey(courseCode)) {
			courses.remove(courseCode);
			removeCourseFromFile(courseCode);
			System.out.println("Course removed successfully.");
		} else {
			System.out.println("Course not found.");
		}
	}

	private void updateCourse() {
		System.out.print("Enter course code to update: ");
		String courseCode = scanner.next();
		updateCourse(courseCode);
	}

	private void updateCourse(String courseCode) {
		if (courses.containsKey(courseCode)) {
			Course courseToUpdate = courses.get(courseCode);
			System.out.println("\nUpdate Course:");
			System.out.print("Enter new course name: ");
			String newName = scanner.next();
			scanner.nextLine();
			System.out.print("Enter new course description: ");
			String newDescription = scanner.nextLine();
			System.out.print("Enter new instructor: ");
			String newInstructor = scanner.nextLine();
			System.out.print("Enter new available seats: ");
			int newSeats = Integer.parseInt(scanner.nextLine());
			System.out.print("Enter new schedule: ");
			String newSchedule = scanner.nextLine();
			System.out.print("Enter new prerequisites (comma-separated): ");
			String[] prerequisitesArray = scanner.nextLine().split(",");
			ArrayList<String> newPrerequisites = new ArrayList<>();
			for (String prerequisite : prerequisitesArray) {
				newPrerequisites.add(prerequisite.trim());
			}
			System.out.print("Enter new credits: ");
			int newCredits = Integer.parseInt(scanner.nextLine());

			courseToUpdate.setCourseName(newName);
			courseToUpdate.setCourseDescription(newDescription);
			courseToUpdate.setInstructor(newInstructor);
			courseToUpdate.setAvailableSeats(newSeats);
			courseToUpdate.setSchedule(newSchedule);
			courseToUpdate.setPrerequisites(newPrerequisites);
			courseToUpdate.setCredits(newCredits);

			updateCourseInFile(courseCode, courseToUpdate);

			System.out.println("Course updated successfully.");
		} else {
			System.out.println("Course not found.");
		}
	}

	private void updateCourseInFile(String courseCodeToUpdate, Course updatedCourse) {
		try {
			File inputFile = new File("courses.txt");
			File tempFile = new File("temp.txt");

			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

			String line;
			boolean found = false;
			while ((line = reader.readLine()) != null) {
				if (line.equals(courseCodeToUpdate)) {
					found = true;
					// Skip lines for the course to be updated
					for (int i = 0; i < 8; i++) {
						reader.readLine();
					}
					// Skip the separator line "====="
					reader.readLine();
					// Write the updated course details
					writer.write(updatedCourse.getCourseCode() + "\n");
					writer.write(updatedCourse.getCourseName() + "\n");
					writer.write(updatedCourse.getCourseDescription() + "\n");
					writer.write(updatedCourse.getInstructor() + "\n");
					writer.write(updatedCourse.getAvailableSeats() + "\n");
					writer.write(updatedCourse.getSchedule() + "\n");
					writer.write(updatedCourse.getPrerequisites() + "\n");
					writer.write(updatedCourse.getCredits() + "\n");
					writer.write(updatedCourse.getEnrolledStudents() + "\n");
					writer.write("=====\n"); // Add separator line
					continue;
				}
				writer.write(line + "\n");
			}

			reader.close();
			writer.close();

			if (!found) {
				System.out.println("Course code not found.");
				return;
			}

			// Rename the temporary file to replace the original file
			if (!inputFile.delete()) {
				System.out.println("Error deleting file.");
				return;
			}
			if (!tempFile.renameTo(inputFile)) {
				System.out.println("Error renaming file.");
			} else {
				System.out.println("Course data updated successfully.");
			}
		} catch (IOException e) {
			System.out.println("Error updating course data: " + e.getMessage());
		}
	}

	private void ValidateCourse() {
		System.out.println("Enter student ID:");
		int studentId;
		studentId = scanner.nextInt();
		scanner.nextLine();
		// Load student data from file
		Student student = loadStudentById(studentId);
		if (student == null) {
			System.out.println("Student not found.");
			return;
		}
		System.out.println("Enter course code for validation:");
		String courseCode = scanner.nextLine();
		Course selectedCourse = courses.get(courseCode);
		if (selectedCourse == null) {
			System.out.println("Course not found.");
			return;
		}
		if (student.getPassedcourses().contains(courseCode)) {
			System.out.println("Already Passed course for student.");
			return;
		}
		if (!selectedCourse.getEnrolledStudents().contains(Integer.toString(student.getStudentId()))) {
			System.out.println("Not Register for this course.");
			return;
		}
		updateCourseDatavalidate(selectedCourse, Integer.toString(student.getStudentId()));
		updateStudentvalidate(student, selectedCourse);

	}

	private Student loadStudentById(int studentId) {
		try (BufferedReader reader = new BufferedReader(new FileReader("students.txt"))) {
			String line;
			List<String> studentInfo = new ArrayList<>();
			while ((line = reader.readLine()) != null) {
				if (line.equals("=====")) {
					if (studentInfo.size() >= 5) {
						String username = studentInfo.get(1);
						String password = studentInfo.get(2);
						String Id = studentInfo.get(0);
						String major = studentInfo.get(3);
						String enrolledCoursesString = studentInfo.get(4);
						String passcourse = studentInfo.get(5);
						ArrayList<String> enrolledCourses = parseEnrolledCourses(enrolledCoursesString);
						ArrayList<String> passCourses = parseEnrolledCourses(passcourse);

						if (Integer.parseInt(Id) == studentId) {
							return new Student(username, password, studentId, major, enrolledCourses, passCourses);

						}

					} else {
						System.out.println("Invalid data format for student.");
					}
					studentInfo.clear(); // Clear the list for the next student
				} else {
					studentInfo.add(line);
				}
			}
		} catch (IOException | NumberFormatException e) {
			System.out.println("Error loading student data: " + e.getMessage());
		}
		return null;
	}

	private void updateCourseDatavalidate(Course course, String studentId) {
		try {
			File inputFile = new File("courses.txt");
			File tempFile = new File("temp.txt");

			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

			String line;
			boolean courseFound = false;
			while ((line = reader.readLine()) != null) {
				if (line.equals(course.getCourseCode())) {
					courseFound = true;
					for (int i = 0; i < 8; i++) {
						reader.readLine();
					}
					reader.readLine();
					// Write updated course details with decreased available seats
					writer.write(course.getCourseCode() + "\n");
					writer.write(course.getCourseName() + "\n");
					writer.write(course.getCourseDescription() + "\n");
					writer.write(course.getInstructor() + "\n");
					int availableSeats = course.getAvailableSeats() + 1; // Decrease available seats by 1
					writer.write(availableSeats + "\n");
					writer.write(course.getSchedule() + "\n");
					writer.write(course.getPrerequisites().toString() + "\n");
					writer.write(course.getCredits() + "\n");

					ArrayList<String> enrolledStudents = course.getEnrolledStudents();
					enrolledStudents.remove(studentId);

					writer.write(enrolledStudents.toString() + "\n");
					writer.write("=====\n"); // Add separator line
					continue;
				} else {
					writer.write(line + "\n");
				}
			}

			reader.close();
			writer.close();

			if (!courseFound) {
				System.out.println("Course not found.");
				return;
			}

			// Rename the temporary file to replace the original file
			if (!inputFile.delete()) {
				System.out.println("Error deleting file.");
				return;
			}
			if (!tempFile.renameTo(inputFile)) {
				System.out.println("Error renaming file.");
			} else {
				System.out.println("Course data updated successfully.");
			}
		} catch (IOException e) {
			System.out.println("Error updating course data: " + e.getMessage());
		}
	}

	private void updateStudentvalidate(Student student, Course course) {
		try {
			File inputFile = new File("students.txt");
			File tempFile = new File("temp.txt");

			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

			String line;
			boolean studentFound = false;
			while ((line = reader.readLine()) != null) {
				if (line.equals(Integer.toString(student.getStudentId()))) {
					studentFound = true;
					for (int i = 0; i < 4; i++) {
						reader.readLine();
					}
					reader.readLine();

					writer.write(student.getStudentId() + "\n");
					writer.write(student.getUsername() + "\n");
					writer.write(student.getPassword() + "\n");
					writer.write(student.getMajor() + "\n");

					List<String> enrolledStudents = student.getRegistercourses();
					enrolledStudents.remove(course.getCourseCode());
					writer.write(enrolledStudents.toString() + "\n");
					List<String> pass = student.getPassedcourses();
					pass.add(course.getCourseCode());
					writer.write(pass + "\n");
				} else {
					writer.write(line + "\n");
				}
			}

			reader.close();
			writer.close();

			if (!studentFound) {
				System.out.println("Student not found.");
				return;
			}

			// Rename the temporary file to replace the original file
			if (!inputFile.delete()) {
				System.out.println("Error deleting file.");
				return;
			}
			if (!tempFile.renameTo(inputFile)) {
				System.out.println("Error renaming file.");
			} else {
				System.out.println("Student data updated successfully.");
			}
		} catch (IOException e) {
			System.out.println("Error updating student data: " + e.getMessage());
		}
	}

	private void displayCourses() {
		if (courses.isEmpty()) {
			System.out.println("No courses available.");
		} else {
			System.out.println("List of Courses:");
			for (Course course : courses.values()) {
				System.out.println(course.getCourseCode() + " - " + course.getCourseName());
			}
		}
	}
}
