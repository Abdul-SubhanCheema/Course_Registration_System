# Course_Registration_System

# Requirements for Students:
●	View Available Courses:
Students should have the ability to view a list of available courses along with details such as course code, name, description, and available seats.
●	Register for Courses:
Students should be able to register for courses from the list of available courses.
The system should ensure that a student cannot register for a course if it has reached its maximum capacity (no available seats), if it does not fit the student's schedule, or if the student has not already all of its prerequisite courses.
●	View Registered Courses:
Students should be able to view a list of courses they are currently registered for
This list should include details such as course code, name, and instructor.
●	Drop Course:
Students should have the option to drop a course they have previously registered for if needed.
●	View All Validated Courses:
Students should be able to view the list of courses they have previously validated.
●	View Schedule:
Students should be able to view their schedule.
●	Update Personal Information:
Students should be able to update their personal information such as their phone number or their address.
●	Submit and receive feedback for special registration requests:
Students should be able to submit a special registration request. This request is typically sent if an open course has no more available seats but is needed by the student. If the request is approved by the advisor, the student is added to the list of enrolled students.
●	List All Prerequisite Courses for a given Course:
Students should have the ability to view a comprehensive list of all prerequisite courses required for enrollment in a given course, including prerequisites of prerequisites and subsequent dependencies.

# Requirements for Advisors:
●	View Registered Students:
Advisors should be able to view a list of registered students along with their details.
●	View Students' Registered Courses:
Advisors should be able to view the courses registered by individual students.
●	Approve/Deny Special Course Registration Requests:
Advisors should have the authority to approve or deny special course registration requests submitted by students. If the request is approved by the advisor, the student is added to the list of enrolled students.

# Requirements for Administrators:
●	Manage Courses:
Administrators should have the ability to add new courses to the system, including details such as course code, name, description, and available seats.
Administrators should also be able to remove/update existing courses if necessary.
●	Manage Users:
Administrators should be able to add new users (students, advisors) to the system.
Administrators should have the authority to remove users from the system if needed.
●	View System Statistics:
Administrators should be able to view statistics about the system, such as the total number of courses, the total number of students, etc.

# System-Level Requirement:
●	Data Consistency Across Sessions:
The system should ensure that all data modifications made by users during their sessions are preserved and remain consistent across multiple sessions. (data are read from file(s) at the beginning of the session and written back by the end).
