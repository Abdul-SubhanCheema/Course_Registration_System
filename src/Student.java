import java.util.ArrayList;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Student extends User {
	private int studentId;
	private String major;
	private HashMap<String, Course> courses;
	private ArrayList<String> registercourses;
	private ArrayList<String> passcourses;
	private Scanner scanner;
	private HashMap<String, User> users;

	public Student() {
		super();
		this.users = new HashMap<>();
		this.courses = new HashMap<>();
		loadDataFromFile();
		this.scanner = new Scanner(System.in);
		this.registercourses = new ArrayList<String>();
		this.passcourses = new ArrayList<String>();
		loadDataForStudents();

	}

	public Student(String username, String password, int studentId, String major) {
		super(username, password);
		this.studentId = studentId;
		this.major = major;
		this.registercourses = new ArrayList<String>();
		this.passcourses = new ArrayList<String>();
	}

	public Student(String username, String password, int studentId, String major, ArrayList<String> registercourses,
			ArrayList<String> passcourses) {
		super(username, password);
		this.studentId = studentId;
		this.major = major;
		this.registercourses = registercourses;
		this.passcourses = passcourses;
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

	public void displayStudentMenu() {
		int stdChoice;

		while (true) {
			System.out.println("\nStudent Options:");
			System.out.println("1. Available Courses");
			System.out.println("2. Register Course");
			System.out.println("3. View Registered Course");
			System.out.println("4. Drop Course");
			System.out.println("5. Validated Course");
			System.out.println("6. View Schedule");
			System.out.println("7. Update Profile");
			System.out.println("8. View Prerequisite Courses");
			System.out.println("9. Special Request Course");
			System.out.println("10. Back to Main Page");

			System.out.print("Enter your choice (1-9): ");
			stdChoice = scanner.nextInt();

			switch (stdChoice) {
			case 1:
				viewAvailableCourses();
				break;
			case 2:
				registerForCourses();
				break;
			case 3:
				ViewRegisterCourses();
				break;
			case 4:
				DropCourse();
				break;
			case 5:
				ViewValidatedCourses();
				break;
			case 6:
				viewSchedule();
				break;
			case 7:
				update();
				break;
			case 8:
				Prereq();
				break;
			case 9:
				SpecialRequest();
				break;
			case 10:
				return;
			default:
				System.out.println("Invalid choice. Please select a valid option (1-9).");
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

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public ArrayList<String> getRegistercourses() {
		return registercourses;
	}

	public ArrayList<String> getPassedcourses() {
		return passcourses;
	}

	public void setRegistercourses(ArrayList<String> Registercourses) {
		this.registercourses = Registercourses;
	}

	public void setPassedcourses(ArrayList<String> Registercourses) {
		this.passcourses = Registercourses;
	}

	public void viewAvailableCourses() {
		System.out.println("\nAvailable Courses:");
		for (Course course : courses.values()) {
			System.out.println("Course Code: " + course.getCourseCode());
			System.out.println("Course Name: " + course.getCourseName());
			System.out.println("Description: " + course.getCourseDescription());
			System.out.println("Available Seats: " + course.getAvailableSeats());
			System.out.println("----------------------------");
		}
	}

	public void registerForCourses() {
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
		if (selectedCourse.getAvailableSeats() <= 0) {
			System.out.println("This course has no available seats.");
			return;
		}
		updateCourseDataregister(selectedCourse, Integer.toString(student.getStudentId()));
		updateStudentRegister(student, selectedCourse);

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

	private boolean studentHasPrerequisites(Student student, Course course) {
		List<String> prerequisites = course.getPrerequisites();
		ArrayList<String> passedCourses = student.getPassedcourses();
		return passedCourses.containsAll(prerequisites);
	}

	public void viewdata() {
		System.out.println("\nStudent Data");
		System.out.println(this.getUsername());
		System.out.println(this.getPassword());
		System.out.println(this.getMajor());
		System.out.println(this.getStudentId());
		System.out.println(this.getRegistercourses());
		System.out.println(this.getPassedcourses());

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
					int availableSeats = course.getAvailableSeats() - 1; // Decrease available seats by 1
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

	private void ViewRegisterCourses() {
		System.out.println("Enter student ID:");
		int studentId;
		studentId = scanner.nextInt();
		scanner.nextLine();

		// Load student data from file
		Student student = loadStudentById(studentId);

		for (String course : student.getRegistercourses()) {
			CourseInfo(course);
		}
	}

	private void ViewValidatedCourses() {
		System.out.println("Enter student ID:");
		int studentId;
		studentId = scanner.nextInt();
		scanner.nextLine();

		// Load student data from file
		Student student = loadStudentById(studentId);

		for (String course : student.getPassedcourses()) {
			CourseInfo1(course);
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

	private void CourseInfo1(String coursecode) {
		System.out.println("\nValidated Courses");
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

	private void DropCourse() {

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
		System.out.println("Enter course code for Drop:");
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
		updateCourseDatadrop(selectedCourse, Integer.toString(student.getStudentId()));
		updateStudentdrop(student, selectedCourse);

	}

	private void updateCourseDatadrop(Course course, String studentId) {
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
					int availableSeats = course.getAvailableSeats() + 1;
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

	private void updateStudentdrop(Student student, Course course) {
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

	private void viewSchedule() {
		System.out.println("Enter student ID:");
		int studentId;
		studentId = scanner.nextInt();
		scanner.nextLine();

		// Load student data from file
		Student student = loadStudentById(studentId);
		for (String course : student.getRegistercourses()) {
			CourseInfo3(course);
		}
	}

	private void CourseInfo3(String coursecode) {
		System.out.println("\nSchedule");
		if (courses.containsKey(coursecode)) {
			Course course = courses.get(coursecode);
			System.out.println("Course Code: " + course.getCourseCode());
			System.out.println("Course Name: " + course.getCourseName());
			System.out.println("Schedule: " + course.getSchedule());
			System.out.println();
		} else {
			System.out.println("Course not found.");
		}
	}

	private void update() {
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
		System.out.println("\nUpdate Profile:");
		System.out.print("Enter New username: ");
		String username = scanner.nextLine();
		System.out.print("Enter New password: ");
		String password = scanner.nextLine();
		student.setUsername(username);
		student.setPassword(password);
		updateStudentProfile(student);
	}

	private void updateStudentProfile(Student student) {
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

					writer.write(student.getRegistercourses() + "\n");
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

	private void Prereq() {
		System.out.println("\nList of Prerequisite:");
		System.out.print("Enter Course Code: ");
		String code;
		code = scanner.next();
		listAllPrerequisites(code);

	}

	public void listAllPrerequisites(String courseCode) {
		if (!courses.containsKey(courseCode)) {
			System.out.println("Course not found.");
			return;
		}

		Course course = courses.get(courseCode);
		ArrayList<String> prereqs = course.getPrerequisites();
		System.out.println("Prerequisite courses for " + courseCode + ":");
		listPrerequisitesRecursive(courses, prereqs);
	}

	private void listPrerequisitesRecursive(HashMap<String, Course> courses, ArrayList<String> prereqs) {
		for (String prereq : prereqs) {
			if (courses.containsKey(prereq)) {
				Course prereqCourse = courses.get(prereq);
				ArrayList<String> nestedPrereqs = prereqCourse.getPrerequisites();
				listPrerequisitesRecursive(courses, nestedPrereqs);
				System.out.println(" - " + prereq + ": " + prereqCourse.getCourseName());
			}
		}
	}
	private void SpecialRequest()
	{
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
		System.out.println("Enter course code for Request:");
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
		if (selectedCourse.getEnrolledStudents().contains(Integer.toString(student.getStudentId()))) {
			System.out.println("Already Register for this course.");
			return;
		}
		if(selectedCourse.getAvailableSeats()>0)
		{
			System.out.println("Already Seats Available.");
			return;
		}
		addspecialrequest(student, selectedCourse);
		
	}
	private void addspecialrequest(Student student,Course course) {
		try (FileWriter writer = new FileWriter("special_request.txt", true)) {
			writer.write(student.getStudentId() + "\n");
			writer.write(course.getCourseCode() + "\n");
			writer.write("=====\n");
			System.out.println("Request added to file successfully.");
		} catch (IOException e) {
			System.out.println("Error adding request to file: " + e.getMessage());
		}
	}

}