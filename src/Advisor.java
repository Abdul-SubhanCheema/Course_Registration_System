import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Advisor extends User {
	private String department;
	private int advisorID;
	private Scanner scanner;
	private HashMap<String, Course> courses;
	private HashMap<String, User> users;
	private HashMap<Student, Course> request;

	public Advisor() {
		super();
		this.users = new HashMap<>();
		this.courses = new HashMap<>();
		this.request = new HashMap<>();
		this.scanner = new Scanner(System.in);
		loadDataForStudents();
		loadDataFromFile();
		loadDataFromRequest();
	}

	public Advisor(String username, String password, String department, int id) {
		super(username, password);
		this.department = department;
		this.advisorID = id;
	}

	public String getDepartment() {
		return department;
	}

	public int getAdvisorId() {
		return advisorID;
	}

	public void setAdvisorId(int Id) {
		this.advisorID = Id;
	}

	public void setDepartment(String department) {
		this.department = department;
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

	public void loadDataFromRequest() {
		try (BufferedReader reader = new BufferedReader(new FileReader("special_request.txt"))) {
			String line;
			ArrayList<String> courseDetails = new ArrayList<>();
			while ((line = reader.readLine()) != null) {
				if (line.equals("=====")) {
					
					processrequest(courseDetails);
					courseDetails.clear(); // Clear list for next course
				} else {
					courseDetails.add(line);
				}
			}
			// Process the last course after reading all lines
			if (!courseDetails.isEmpty()) {

				processCourse(courseDetails);
			}
			System.out.println("Requests loaded from file successfully.");
		} catch (IOException e) {
			System.out.println("Error loading requests from file: " + e.getMessage());
		}
	}

	private void processrequest(ArrayList<String> details) {

		if (details.size() == 2) {
			String studentid = details.get(0);
			String coursecode = details.get(1);

			
			Student student = loadStudentById(Integer.valueOf(studentid));
			if (student == null) {
				System.out.println("Student not found.");
				return;
			}
			Course selectedCourse = courses.get(coursecode);
			if (selectedCourse == null) {
				System.out.println("Course not found.");
				return;
			}

			Course course = new Course(selectedCourse.getCourseCode(), selectedCourse.getCourseName(),
					selectedCourse.getCourseDescription(), selectedCourse.getInstructor(),
					selectedCourse.getAvailableSeats(), selectedCourse.getSchedule(), selectedCourse.getPrerequisites(),
					selectedCourse.getEnrolledStudents(), selectedCourse.getCredits());
			Student std = new Student(student.getUsername(), student.getPassword(), student.getStudentId(),
					student.getMajor(), student.getRegistercourses(), student.getPassedcourses());
			courses.put(coursecode, course);
			users.put(studentid, std);
			request.put(std, course);
		} else {
			System.out.println("Invalid data format for Request: " + details);
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
		ArrayList<String> prerequisites = new ArrayList<>();
		if (!prerequisitesString.equals("[]")) {
			String[] parts = prerequisitesString.substring(1, prerequisitesString.length() - 1).split(", ");
			for (String part : parts) {
				prerequisites.add(part);
			}
		}
		return prerequisites;
	}

	private ArrayList<String> parseEnrolledStudents(String enrolledString) {

		ArrayList<String> enrolledStudents = new ArrayList<>();
		if (!enrolledString.equals("[]")) { // Check if enrolled students array is not empty
			String[] parts = enrolledString.substring(1, enrolledString.length() - 1).split(",");
			for (String part : parts) {
				enrolledStudents.add(part);
			}
		}
		return enrolledStudents;
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
					studentInfo.clear();
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

	public void displayAdvisorMenu() {
		int stdChoice;

		while (true) {
			System.out.println("\nAdvisor Options:");
			System.out.println("1. View Students");
			System.out.println("2. View Students Registered Courses");
			System.out.println("3. View Requests");
			System.out.println("4. Handle Requests");
			System.out.println("5. Back to Main Page");

			System.out.print("Enter your choice (1-5): ");
			stdChoice = scanner.nextInt();

			switch (stdChoice) {
			case 1:
				viewAvailableStudents();
				break;
			case 2:
				ViewRegisterCourses();
				break;
			case 3:
				printRequestData();
				break;
			case 4:
				handle();
				break;
			case 5:
				return;
			default:
				System.out.println("Invalid choice. Please select a valid option (1-5).");
				break;
			}
		}
	}

	public void viewAvailableStudents() {
		System.out.println("\nStudents:");
		int totalStudents = 0;
		for (User user : users.values()) {
			if (user instanceof Student) {
				System.out.println(user.getUsername() + " - " + user.getPassword());
				totalStudents++;
			}
		}
		System.out.println("Total Students: " + totalStudents);
	}

	private void ViewRegisterCourses() {
		for (HashMap.Entry<String, User> entry : users.entrySet()) {
			User user = entry.getValue();
			if (user instanceof Student) {
				Student students = (Student) user; // Cast to Student
				System.out.println("Student ID: " + students.getStudentId());
				for (String course : students.getRegistercourses()) {
					CourseInfo(course);
				}
			}
		}

	}

	private void CourseInfo(String coursecode) {
		System.out.println("\nRegistered Courses");
		if (courses.containsKey(coursecode)) {
			Course course = courses.get(coursecode);
			System.out.println("Course Code: " + course.getCourseCode());
			System.out.println("Course Name: " + course.getCourseName());
			System.out.println("Instructor: " + course.getInstructor());
			System.out.println();
		} else {
			System.out.println("Course not found.");
		}
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

	public void printRequestData() {
		System.out.println("Student-Course Requests:");

		for (Map.Entry<Student, Course> entry : request.entrySet()) {
			Student student = entry.getKey();
			Course course = entry.getValue();

			System.out.println("Student Name: " + student.getUsername());
			System.out.println("Student Password: " + student.getPassword());
			System.out.println("Student Major: " + student.getMajor());
			System.out.println("Course Code: " + course.getCourseCode());
			System.out.println("Course Name: " + course.getCourseName());
			System.out.println("--------------------");
		}
	}

	private void handle() {
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
		System.out.println("Enter course code to register:");
		String courseCode = scanner.nextLine();
		Course selectedCourse = courses.get(courseCode);
		if (selectedCourse == null) {
			System.out.println("Course not found.");
			return;
		}
		if (selectedCourse.getEnrolledStudents().contains(Integer.toString(student.getStudentId()))) {
			System.out.println("Already Register for this course.");
			return;
		}
		if (!studentHasPrerequisites(student, selectedCourse)) {
			System.out.println("You do not meet the prerequisites for this course.");
			return;
		}
		updateCourseDataregister(selectedCourse, Integer.toString(student.getStudentId()));
		updateStudentRegister(student, selectedCourse);
		removeRequestFromFile(Integer.toString(student.getStudentId()),selectedCourse.getCourseCode());

	}

	private boolean studentHasPrerequisites(Student student, Course course) {
		List<String> prerequisites = course.getPrerequisites();
		ArrayList<String> passedCourses = student.getPassedcourses();
		return passedCourses.containsAll(prerequisites);
	}

	private void updateCourseDataregister(Course course, String studentId) {
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
					int availableSeats = course.getAvailableSeats();
					writer.write(availableSeats + "\n");
					writer.write(course.getSchedule() + "\n");
					writer.write(course.getPrerequisites().toString() + "\n");
					writer.write(course.getCredits() + "\n");

					ArrayList<String> enrolledStudents = course.getEnrolledStudents();
					enrolledStudents.add(studentId);
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

	private void updateStudentRegister(Student student, Course course) {
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

					// Check the size of the registerCourses list
					List<String> enrolledStudents = student.getRegistercourses();
					enrolledStudents.add(course.getCourseCode());
					writer.write(enrolledStudents.toString() + "\n");
					writer.write(student.getPassedcourses() + "\n");
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

	private  void removeRequestFromFile(String studentId, String courseCode) {
		try {
			File inputFile = new File("special_request.txt");
			File tempFile = new File("temp.txt");

			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

			String line;
			boolean requestFound = false;
			while ((line = reader.readLine()) != null) {
				if (line.equals(studentId)) {
					// Read the course code
					String code = reader.readLine();
					// Check if the course code matches
					if (code.equals(courseCode)) {
						// Skip the ===== separator
						reader.readLine();
						requestFound = true;
					} else {
						// Write the student ID and course code to temp file
						writer.write(line + "\n");
						writer.write(code + "\n");
					}
				} else {
					// Write the line to temp file
					writer.write(line + "\n");
				}
			}

			reader.close();
			writer.close();

			if (!requestFound) {
				System.out.println("Request not found in the file.");
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
				System.out.println("Request removed from file.");
			}
		} catch (IOException e) {
			System.out.println("Error removing request from file: " + e.getMessage());
		}
	}

}
