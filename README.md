# Student Expense Tracker

A Java console application that helps students record daily expenses, manage monthly budgets, and understand their spending habits using MySQL.

## Features

- User registration and login
- Password hashing using PBKDF2
- Add, view, update, and delete expenses
- Category-wise spending summaries
- Total spending calculation
- Monthly budget management
- Remaining budget calculation
- Input validation for user entries
- Database integration using JDBC
- Local configuration for database credentials

## Technologies Used

- **Java** — application logic
- **MySQL** — database management
- **JDBC** — Java database connectivity
- **MySQL Connector/J** — MySQL JDBC driver
- **Visual Studio Code** — development environment
- **Git and GitHub** — version control and project hosting

## Project Structure

```text
student-expense-tracker/
├── src/                       # Main Java application and DAO classes
├── tests/                     # Manual Java test programs
├── sql/                       # Database setup scripts
├── lib/                       # MySQL Connector/J library
├── screenshots/
│   └── dashboard.png          # Application dashboard screenshot
├── config.properties.example  # Safe configuration template
├── .gitignore                 # Files excluded from Git
├── README.md                  # Project documentation
└── run.bat                    # Windows application launcher
```

The real `config.properties` file is stored locally and excluded from Git.

## Prerequisites

Before running the application, install or prepare the following:

- Java JDK 17 or later
- MySQL Server
- MySQL Workbench (recommended)
- MySQL Connector/J JAR file in the `lib/` directory
- Git (optional, for version control)

## Setup Instructions

### 1. Clone the Repository

Open a terminal and run:

```bash
git clone https://github.com/ShashankPrasad30/student-expense-tracker.git
cd student-expense-tracker
```

Alternatively, download the repository as a ZIP file from GitHub and extract it.

### 2. Create the Database

Open MySQL Workbench and execute the SQL scripts in this order:

1. `sql/01_create_database.sql`
2. `sql/02_create_tables.sql`

These scripts are intended for initial database setup. Before rerunning them on an existing database, check their contents to avoid recreating tables or duplicating category records.

### 3. Configure the Database Connection

Create your local configuration file by copying the example:

```bat
copy config.properties.example config.properties
```

Open `config.properties` and configure your local MySQL connection:

```properties
db.url=jdbc:mysql://localhost:3306/student_expense_tracker
db.username=root
db.password=YOUR_LOCAL_MYSQL_PASSWORD
```

Replace `YOUR_LOCAL_MYSQL_PASSWORD` with your own MySQL password. Do not include the example placeholder as the actual password.

**Security:** Never commit or upload your real `config.properties` file to GitHub.

### 4. Check the MySQL Connector

Make sure the following file exists:

```text
lib/mysql-connector-j-26.7.0.jar
```

The commands below assume this exact filename. If your JAR has a different name, update the commands accordingly.

### 5. Compile the Application

Open a terminal in the project root and run:

```bat
javac -cp "lib\mysql-connector-j-26.7.0.jar;src" -d src src\Main.java
```

### 6. Run the Application

**Option 1: Use the Windows launcher**

Double-click `run.bat` in the project folder.

The launcher checks for the local configuration and MySQL Connector/J, compiles the application, and starts it.

**Option 2: Use the terminal**

Run:

```bat
java -cp "src;lib\mysql-connector-j-26.7.0.jar" Main
```

Follow the console menu to register, log in, manage expenses, and track monthly budgets.

## Application Screenshot

The following screenshot shows the Student Expense Tracker dashboard.

![Student Expense Tracker Dashboard](screenshots/dashboard.png)

## Testing

The `tests/` directory contains manual Java test programs for individual features.

For example, to compile and run the login test on Windows:

```bat
javac -cp "lib\mysql-connector-j-26.7.0.jar;src" -d tests tests\LoginTest.java
java -cp "src;tests;lib\mysql-connector-j-26.7.0.jar" LoginTest
```

Some test programs use fixed email addresses, user IDs, expense IDs, or other test data. Review a test before running it because it may register a user or insert, update, or delete database records.

Use a test database or disposable test records when testing operations that modify data.

## Security Notes

- Passwords are hashed before being stored in the database.
- PBKDF2 is used for the current password-hashing format.
- Older SHA-256 password hashes are supported for migration when the corresponding user logs in successfully.
- Prepared statements are used for database operations.
- Database credentials are kept in a local configuration file.
- `.gitignore` excludes the real configuration file and compiled Java class files.
- Never publish database passwords, access tokens, or other secrets.
- Use a unique, strong password for your local database account.

## Future Improvements

- Graphical user interface
- Expense charts and visual reports
- Search and date filtering
- Export expense reports to CSV
- Automated unit tests
- Improved reporting and dashboard features
