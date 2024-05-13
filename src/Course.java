import java.util.ArrayList;

public class Course {
	private String courseCode;
	private String courseName;
	private String courseDescription;
	private String instructor;
	private int availableSeats;
	private String schedule;
	private ArrayList<String> prerequisites;
	private int credits;
	private ArrayList<String> enrolledStudents;

	public Course(String courseCode, String courseName, String courseDescription, String instructor, int availableSeats,
			String schedule, ArrayList<String> prerequisites, int credits) {
		this.courseCode = courseCode;
		this.courseName = courseName;
		this.courseDescription = courseDescription;
		this.instructor = instructor;
		this.availableSeats = availableSeats;
		this.schedule = schedule;
		this.prerequisites = prerequisites;
		this.credits = credits;
		this.enrolledStudents = new ArrayList<String>();
	}

	public Course(String courseCode, String courseName, String courseDescription, String instructor, int availableSeats,
			String schedule, ArrayList<String> prerequisites, ArrayList<String> std, int credits) {
		this.courseCode = courseCode;
		this.courseName = courseName;
		this.courseDescription = courseDescription;
		this.instructor = instructor;
		this.availableSeats = availableSeats;
		this.schedule = schedule;
		this.prerequisites = prerequisites;
		this.credits = credits;
		this.enrolledStudents = std;
	}

	// Getters and setters for Course attributes

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCourseDescription() {
		return courseDescription;
	}

	public void setCourseDescription(String courseDescription) {
		this.courseDescription = courseDescription;
	}

	public String getInstructor() {
		return instructor;
	}

	public void setInstructor(String instructor) {
		this.instructor = instructor;
	}

	public int getAvailableSeats() {
		return availableSeats;
	}

	public void setAvailableSeats(int availableSeats) {
		this.availableSeats = availableSeats;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	public ArrayList<String> getPrerequisites() {
		return prerequisites;
	}

	public void setPrerequisites(ArrayList<String> prerequisites) {
		this.prerequisites = prerequisites;
	}

	public int getCredits() {
		return credits;
	}

	public void setCredits(int credits) {
		this.credits = credits;
	}

	public ArrayList<String> getEnrolledStudents() {
		return enrolledStudents;
	}

	public void setEnrolledStudents(ArrayList<String> enrolledStudents) {
		this.enrolledStudents = enrolledStudents;
	}



}
