import java.util.*;

class User {
    String username;
    String password;

    User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    boolean login(String inputUsername, String inputPassword) {
        return this.username.equals(inputUsername) && this.password.equals(inputPassword);
    }

    public static void logout() {
        System.out.println("Logged out. Exiting...");
    }

    public String getRole() {
        return "User";
    }

    public String getUsername() {
        return username;
    }
}

class Admin extends User {
    Map<Integer, User> userMap;

    Admin(String username, String password, Map<Integer, User> userMap) {
        super(username, password);
        this.userMap = userMap;
    }

    void accessPanel(Scanner sc) {
        while (true) {
            System.out.println("\n--- Admin Panel ---");
            System.out.println("1. Add User");
            System.out.println("2. Delete User");
            System.out.println("3. View All Users");
            System.out.println("4. Exit Admin Panel");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> addUser(sc);
                case 2 -> deleteUser(sc);
                case 3 -> viewUsers();
                case 4 -> {
                    System.out.println("Exiting Admin Panel.");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    void addUser(Scanner sc) {
        System.out.println("Choose user type to add:");
        System.out.println("1. Teacher\n2. Student\n3. Receptionist");
        int type = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter username: ");
        String uname = sc.nextLine();
        System.out.print("Enter password: ");
        String pass = sc.nextLine();

        User newUser = switch (type) {
            case 1 -> new Teacher(uname, pass);
            case 2 -> new Student(uname, pass);
            case 3 -> new Receptionist(uname, pass);
            default -> null;
        };

        if (newUser != null) {
            int newId = Collections.max(userMap.keySet()) + 1;
            userMap.put(newId, newUser);
            System.out.println("User added successfully!");
        } else {
            System.out.println("Invalid user type.");
        }
    }

    void deleteUser(Scanner sc) {
        viewUsers();
        System.out.print("Enter user ID to delete: ");
        int id = sc.nextInt();
        sc.nextLine();

        if (userMap.containsKey(id) && !(userMap.get(id) instanceof Admin)) {
            userMap.remove(id);
            System.out.println("User deleted successfully!");
        } else {
            System.out.println("Cannot delete this user (may not exist or is an Admin).");
        }
    }

    void viewUsers() {
        System.out.println("\n--- Registered Users ---");
        for (Map.Entry<Integer, User> entry : userMap.entrySet()) {
            System.out.printf("ID: %d | Username: %s | Role: %s\n",
                    entry.getKey(),
                    entry.getValue().getUsername(),
                    entry.getValue().getClass().getSimpleName());
        }
    }
}

class Teacher extends User {
    private List<String> classesTeaching;
    private Map<String, List<String>> studentGrades;

    Teacher(String username, String password) {
        super(username, password);
        this.classesTeaching = new ArrayList<>();
        this.studentGrades = new HashMap<>();
        
        classesTeaching.add("Mathematics");
        classesTeaching.add("Physics");
    }

    void viewClassSchedule() {
        System.out.println("\n--- Your Class Schedule ---");
        for (String className : classesTeaching) {
            System.out.println("- " + className);
        }
    }

    void recordGrades(Scanner sc) {
        System.out.println("\n--- Record Student Grades ---");
        System.out.print("Enter student name: ");
        String studentName = sc.nextLine();
        System.out.print("Enter class name: ");
        String className = sc.nextLine();
        System.out.print("Enter grade: ");
        String grade = sc.nextLine();

        studentGrades.computeIfAbsent(studentName, k -> new ArrayList<>())
                   .add(className + ": " + grade);
        System.out.println("Grade recorded successfully!");
    }

    void viewStudentGrades() {
        System.out.println("\n--- Student Grades ---");
        for (Map.Entry<String, List<String>> entry : studentGrades.entrySet()) {
            System.out.println("Student: " + entry.getKey());
            for (String grade : entry.getValue()) {
                System.out.println("  " + grade);
            }
        }
    }

    public String getRole() {
        return "Teacher";
    }
}

class Student extends User {
    private List<String> enrolledClasses;
    private Map<String, String> grades;

    Student(String username, String password) {
        super(username, password);
        this.enrolledClasses = new ArrayList<>();
        this.grades = new HashMap<>();
        // Initialize with some sample data
        enrolledClasses.add("Mathematics");
        enrolledClasses.add("Physics");
        grades.put("Mathematics", "A");
        grades.put("Physics", "B+");
    }

    void viewTimetable() {
        System.out.println("\n--- Your Timetable ---");
        for (String className : enrolledClasses) {
            System.out.println("- " + className + " (Mon/Wed/Fri 10:00-11:00)");
        }
    }

    void viewGrades() {
        System.out.println("\n--- Your Grades ---");
        for (Map.Entry<String, String> entry : grades.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    void enrollClass(Scanner sc) {
        System.out.print("\nEnter class name to enroll: ");
        String className = sc.nextLine();
        enrolledClasses.add(className);
        System.out.println("Enrolled in " + className + " successfully!");
    }

    public String getRole() {
        return "Student";
    }
}

class Receptionist extends User {
    private List<String> visitorsLog;

    Receptionist(String username, String password) {
        super(username, password);
        this.visitorsLog = new ArrayList<>();
    }

    void generateIDCard() {
        System.out.println("\n--- ID Card Generation ---");
        System.out.println("ID Card for " + getUsername() + " generated.");
        System.out.println("Role: " + getRole());
        System.out.println("Issued on: " + new Date());
    }

    void logVisitor(Scanner sc) {
        System.out.print("\nEnter visitor name: ");
        String name = sc.nextLine();
        System.out.print("Enter purpose: ");
        String purpose = sc.nextLine();
        visitorsLog.add(name + " - " + purpose + " - " + new Date());
        System.out.println("Visitor logged successfully!");
    }

    void viewVisitorsLog() {
        System.out.println("\n--- Visitors Log ---");
        for (String entry : visitorsLog) {
            System.out.println(entry);
        }
    }

    public String getRole() {
        return "Receptionist";
    }
}

public class main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Map<Integer, User> userMap = new HashMap<>();
        Admin adminUser = new Admin("admin", "admin123", userMap);
        userMap.put(1, adminUser);
        userMap.put(2, new Teacher("teacher", "teach123"));
        userMap.put(3, new Student("student", "stud123"));
        userMap.put(4, new Receptionist("reception", "rec123"));

        while (true) {
            System.out.println("\n==== Login Menu ====");
            System.out.println("1. Admin");
            System.out.println("2. Teacher");
            System.out.println("3. Student");
            System.out.println("4. Receptionist");
            System.out.println("99. Exit");
            String credentialsInfo = """
| User Type    | Username     | Password   |
|--------------|--------------|------------|
| Admin        | admin        | admin123   |
| Teacher      | teacher      | teach123   |
| Student      | student      | stud123    |
| Receptionist | reception    | rec123     |
""";
            System.out.println(credentialsInfo);

            System.out.print("Enter user type (1-4 or 99 to exit): ");
            int userType = sc.nextInt();
            sc.nextLine();

            if (userType == 99) {
                User.logout();
                break;
            }

            if (!userMap.containsKey(userType)) {
                System.out.println("Invalid user type. Try again.");
                continue;
            }

            System.out.print("Enter username: ");
            String username = sc.nextLine();
            System.out.print("Enter password: ");
            String password = sc.nextLine();

            User user = userMap.get(userType);
            if (!user.login(username, password)) {
                System.out.println("Incorrect username or password.");
                continue;
            }

            System.out.println("Login successful!");

            switch (userType) {
                case 1 -> ((Admin) user).accessPanel(sc);
                case 2 -> teacherMenu((Teacher) user, sc);
                case 3 -> studentMenu((Student) user, sc);
                case 4 -> receptionistMenu((Receptionist) user, sc);
                default -> System.out.println("Unknown user type.");
            }

            System.out.println("Type 99 to logout or press Enter to continue...");
            String exitCheck = sc.nextLine();
            if (exitCheck.equals("99")) {
                User.logout();
                break;
            }
        }

        sc.close();
    }

    private static void teacherMenu(Teacher teacher, Scanner sc) {
        while (true) {
            System.out.println("\n--- Teacher Menu ---");
            System.out.println("1. View Class Schedule");
            System.out.println("2. Record Student Grades");
            System.out.println("3. View Student Grades");
            System.out.println("4. Logout");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> teacher.viewClassSchedule();
                case 2 -> teacher.recordGrades(sc);
                case 3 -> teacher.viewStudentGrades();
                case 4 -> {
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void studentMenu(Student student, Scanner sc) {
        while (true) {
            System.out.println("\n--- Student Menu ---");
            System.out.println("1. View Timetable");
            System.out.println("2. View Grades");
            System.out.println("3. Enroll in Class");
            System.out.println("4. Logout");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> student.viewTimetable();
                case 2 -> student.viewGrades();
                case 3 -> student.enrollClass(sc);
                case 4 -> {
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void receptionistMenu(Receptionist receptionist, Scanner sc) {
        while (true) {
            System.out.println("\n--- Receptionist Menu ---");
            System.out.println("1. Generate ID Card");
            System.out.println("2. Log Visitor");
            System.out.println("3. View Visitors Log");
            System.out.println("4. Logout");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> receptionist.generateIDCard();
                case 2 -> receptionist.logVisitor(sc);
                case 3 -> receptionist.viewVisitorsLog();
                case 4 -> {
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}
