
# Student Expense Tracker

A Java console application for students to record expenses, manage monthly budgets, and monitor their spending using MySQL.

## Features

- User registration and login
- Password hashing using PBKDF2
- Add, view, update, and delete expenses
- Category-wise spending summaries
- Total expense calculation
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

## Project Structure

```text
Project Student Expense Tracker/
├── src/
├── sql/
├── lib/
├── config.properties.example
├── config.properties
├── .gitignore
└── README.md
```

## Setup Instructions

1. Install the Java JDK and MySQL Server.
2. Create the `student_expense_tracker` database and tables using the SQL scripts in the `sql/` folder.
3. Obtain MySQL Connector/J and place its JAR file in the `lib/` folder.
4. Copy `config.properties.example` to a new file named `config.properties`.
5. Update `config.properties` with your local MySQL username and password.
6. Open a terminal in the project root.

Compile the application:

```bat
javac -cp "lib\mysql-connector-j-26.7.0.jar;src" -d src src\Main.java
```

Run the application:

```bat
java -cp "src;lib\mysql-connector-j-26.7.0.jar" Main
```

## Security Notes

- Never commit `config.properties`.
- Use a unique password for your local database account.
- Passwords are hashed before storage.
- The application uses prepared statements for database operations.

## Future Improvements

- Graphical user interface
- Expense charts and reports
- Search and filtering
- Export expense reports
