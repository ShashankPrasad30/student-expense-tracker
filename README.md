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
- Input validation
- MySQL database integration using JDBC

## Technologies Used

- Java
- MySQL
- JDBC
- MySQL Connector/J
- Visual Studio Code
- Git and GitHub

## Project Structure

```text
student-expense-tracker/
├── src/                       # Application and DAO classes
├── tests/                     # Manual test programs
├── sql/                       # Database setup scripts
├── lib/                       # MySQL Connector/J library
├── config.properties.example  # Configuration template
├── .gitignore
└── README.md
```

The real `config.properties` file is stored locally and is excluded from Git.

## Prerequisites

- Java JDK installed
- MySQL Server installed and running
- MySQL Workbench (recommended)
- MySQL Connector/J JAR in the `lib/` directory

## Setup Instructions

### 1. Clone the repository

```bash
git clone https://github.com/ShashankPrasad30/student-expense-tracker.git
cd student-expense-tracker
```

### 2. Create the database

Open MySQL Workbench and execute the scripts in this order:

1. `sql/01_create_database.sql`
2. `sql/02_create_tables.sql`

These scripts are intended for an initial database setup. Do not rerun the table-creation or category-insertion statements on an existing database without checking their effects.

### 3. Configure the database connection

Copy the example configuration file:

```bat
copy config.properties.example config.properties
```

Open the new `config.properties` file and enter your local MySQL username and password:

```properties
db.url=jdbc:mysql://localhost:3306/student_expense_tracker
db.username=root
db.password=YOUR_LOCAL_MYSQL_PASSWORD
```

Replace the password placeholder with your actual local MySQL password.

Never upload the real `config.properties` file to GitHub.

### 4. Compile the application

From the project root, run:

```bat
javac -cp "lib\mysql-connector-j-26.7.0.jar;src" -d src src\Main.java
```

### 5. Run the application

```bat
java -cp "src;lib\mysql-connector-j-26.7.0.jar" Main
```

Use the console menu to register, log in, manage expenses, and track monthly budgets.

### Run the application on Windows

After completing the database setup and configuring `config.properties`, you can start the application in either of these ways.

**Option 1: Use the launcher**

Double-click `run.bat` in the project folder.

**Option 2: Use the terminal**

Open a terminal in the project folder and run:

```bat
java -cp "src;lib\mysql-connector-j-26.7.0.jar" Main
```


## Testing

The `tests/` directory contains manual Java test programs for individual features.

For example, compile and run the login test:

```bat
javac -cp "lib\mysql-connector-j-26.7.0.jar;src" -d tests tests\LoginTest.java
java -cp "src;tests;lib\mysql-connector-j-26.7.0.jar" LoginTest
```

Some tests use fixed user IDs, expense IDs, email addresses, or other test data. Review their contents before running them because they may insert, update, or delete database records.

## Security Notes

- Passwords are hashed before being stored.
- Prepared statements are used for database operations.
- Database credentials are kept in a local configuration file.
- The `.gitignore` file excludes local credentials and compiled Java class files.
- Use a unique password for your local database account.

## Future Improvements

- Graphical user interface
- Expense charts and visual reports
- Search and date filtering
- Export reports to CSV
- Automated unit tests
## Application Screenshot

![Student Expense Tracker Dashboard](screenshots/dashboard.png)