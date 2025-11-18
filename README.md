<div align="center">

# 🎓 Course Registration System

![Java](https://img.shields.io/badge/Java-OOP-red?style=for-the-badge&logo=java&logoColor=white)
![Console](https://img.shields.io/badge/Console-Application-darkgreen?style=for-the-badge&logo=terminal&logoColor=white)
![File](https://img.shields.io/badge/File-System-blue?style=for-the-badge&logo=files&logoColor=white)
![Academic](https://img.shields.io/badge/Academic-System-purple?style=for-the-badge&logo=graduation-cap&logoColor=white)

<p align="center">
  <strong>🏫 Comprehensive university course management and registration platform</strong>
</p>

<p align="center">
  <em>Object-oriented Java application for students, advisors, and administrators</em>
</p>

</div>

## 📋 Overview

**Course Registration System** is a robust Java console application that manages university course enrollment, student records, and academic administration. Built with object-oriented principles, the system provides role-based access for students, advisors, and administrators with comprehensive file-based data persistence.

## 🏗️ System Architecture

```
🎓 Course Registration System
├── 👨‍🎓 Student Module (Course enrollment & management)
├── 👨‍🏫 Advisor Module (Student oversight & approvals)
├── 👨‍💼 Admin Module (System & user management)
├── 📚 Course Management (Catalog & prerequisites)
├── 💾 File System (Data persistence)
└── 🔐 Role-Based Access (Multi-user support)
```

## ✨ Core Features

### 👨‍🎓 **Student Functions**

| Feature | Description | Capabilities |
|---------|-------------|--------------|
| **📚 Course Viewing** | Browse available courses | Code, name, description, seats |
| **✅ Course Registration** | Enroll in courses | Prerequisite & schedule validation |
| **👀 View Registered** | See current enrollments | Course details & instructors |
| **❌ Drop Courses** | Withdraw from courses | Enrollment management |
| **🎯 View Completed** | See validated courses | Academic history |
| **📅 Schedule View** | Personal timetable | Time conflict resolution |
| **📝 Update Profile** | Personal information | Phone, address updates |
| **🙏 Special Requests** | Capacity override requests | Advisor approval system |

### 👨‍🏫 **Advisor Functions**

| Feature | Description | Access Level |
|---------|-------------|--------------|
| **👥 View Students** | Registered student list | Complete student database |
| **📊 Student Courses** | Individual course tracking | Per-student enrollment view |
| **✅/❌ Request Approval** | Special registration decisions | Approve/deny capacity overrides |

### 👨‍💼 **Administrator Functions**

| Feature | Description | System Control |
|---------|-------------|----------------|
| **📚 Course Management** | Add/remove/update courses | Complete catalog control |
| **👥 User Management** | Add/remove users | Student & advisor accounts |
| **📊 System Statistics** | Usage analytics | Course & student metrics |

## 🔧 Technical Implementation

### **Object-Oriented Design**
```java
// Class Hierarchy
User (Base Class)
├── Student (Course enrollment, profile management)
├── Advisor (Student oversight, approvals)
└── Admin (System administration)

Course (Entity Class)
├── Course details & metadata
├── Prerequisite management
├── Enrollment tracking
└── Schedule management
```

### **File-Based Persistence**
```java
// Data Storage Structure
courses.txt         // Course catalog & enrollment data
students.txt        // Student profiles & registrations  
advisors.txt        // Advisor accounts & permissions
special_request.txt // Pending approval requests
```

### **Core Business Logic**
- **Prerequisite Validation**: Recursive dependency checking
- **Schedule Conflict Detection**: Time slot overlap prevention
- **Capacity Management**: Seat availability tracking
- **Data Consistency**: File I/O synchronization across sessions

## 🚀 Getting Started

### **Prerequisites**
- Java Development Kit (JDK 8 or higher)
- Java Runtime Environment (JRE)
- Command line or IDE (Eclipse, IntelliJ, VS Code)

### **Installation & Setup**

```bash
# Clone the repository
git clone <repository-url>
cd Course_Registration_System

# Compile the Java files
javac src/*.java

# Run the application
java -cp src Main
```

### **Using an IDE**
1. **Import Project**: Open in Eclipse/IntelliJ as existing project
2. **Build Path**: Ensure src folder is in build path
3. **Run Configuration**: Set Main.java as entry point
4. **Execute**: Run as Java application

## 📁 Project Structure

```
Course_Registration_System/
├── src/                      # Java source files
│   ├── Main.java            # Application entry point
│   ├── User.java            # Base user class
│   ├── Student.java         # Student functionality (846 lines)
│   ├── Advisor.java         # Advisor functionality
│   ├── Admin.java           # Admin functionality (966 lines)
│   └── Course.java          # Course entity & logic
├── Data Files/              # Persistent storage
│   ├── courses.txt          # Course catalog
│   ├── students.txt         # Student records
│   ├── advisors.txt         # Advisor accounts
│   └── special_request.txt  # Pending requests
├── bin/                     # Compiled class files
├── .settings/               # IDE configuration
└── README.md               # Project documentation
```

## 🎯 Key System Features

### **Academic Workflow**
1. **Course Discovery**: Students browse available courses with detailed information
2. **Smart Registration**: Automatic validation of prerequisites and schedules
3. **Conflict Resolution**: Prevention of time conflicts and capacity issues
4. **Approval Process**: Special requests for full courses via advisor approval
5. **Progress Tracking**: Complete academic history and current enrollment view

### **Data Management**
- **Persistent Storage**: All data preserved across application sessions
- **File Synchronization**: Read on startup, write on termination
- **Data Integrity**: Consistent state maintenance across operations
- **Backup Ready**: Text-based storage for easy backup and recovery

### **Business Rules**
- **Prerequisite Enforcement**: Cannot register without completed prerequisites
- **Capacity Limits**: Automatic seat availability checking
- **Schedule Conflicts**: Prevents overlapping course times
- **Role Permissions**: Appropriate access control per user type

## 🔐 User Roles & Permissions

| Role | Login Required | Capabilities | Data Access |
|------|---------------|--------------|-------------|
| **👨‍🎓 Student** | Yes | Course registration, profile updates | Own data + course catalog |
| **👨‍🏫 Advisor** | Yes | Student oversight, request approvals | Student data + courses |
| **👨‍💼 Admin** | Yes | Full system management | All system data |

## 📊 System Statistics

The system tracks and provides:
- **Total Courses**: Complete catalog count
- **Active Students**: Registered user count
- **Enrollment Metrics**: Course capacity utilization
- **Request Analytics**: Approval/denial statistics

## 🛠️ Development Features

### **Design Patterns**
- **Inheritance**: User hierarchy with specialized roles
- **Encapsulation**: Private data with public interfaces
- **File I/O**: Persistent data storage implementation
- **Menu Systems**: Interactive console navigation

### **Error Handling**
- **Input Validation**: Robust user input checking
- **File Operations**: Safe file read/write with error recovery
- **Business Logic**: Comprehensive validation of academic rules
- **User Feedback**: Clear error messages and guidance

## 📈 Academic Benefits

- **Streamlined Registration**: Efficient course enrollment process
- **Conflict Prevention**: Automatic schedule and prerequisite validation
- **Administrative Efficiency**: Centralized course and user management
- **Data Persistence**: Reliable academic record keeping
- **Scalable Design**: Easily extensible for additional features

## 🤝 Contributing

We welcome contributions to enhance the Course Registration System:

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/AcademicFeature`)
3. **Commit** your changes (`git commit -m 'Add course waitlist feature'`)
4. **Push** to the branch (`git push origin feature/AcademicFeature`)
5. **Open** a Pull Request

### **Enhancement Ideas**
- 🔍 Add course search and filtering
- 📱 GUI interface development
- 🗄️ Database integration (MySQL/PostgreSQL)
- 📊 Advanced reporting and analytics
- 📧 Email notification system
- 🌐 Web-based interface

## 📄 License

This project is licensed under the **MIT License** - see the LICENSE file for details.

## 📞 Contact

<div align="center">

[![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/Abdul-SubhanCheema)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://linkedin.com/in/abdulsubhan303)

<img src="https://user-images.githubusercontent.com/74038190/213910845-af37a709-8995-40d6-be59-724526e3c3d7.gif" width="100">

### 🎓 *"Bringing academic administration into the digital era, one enrollment at a time!"* ✨

**⭐ Enjoyed the project? Give it a star!**

</div>








