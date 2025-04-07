# FinTrack

## Project Overview
FinTrack is a backend application for managing personal finances. It allows users to:
- Register and authenticate securely.
- Track transactions, monitor balances, and analyze income/expenses.
- Set financial goals and manage progress.
- Utilize an API for frontend integration and report generation.

## Features

### User Management
- User registration with a unique email and password.
- Secure login authentication.
- Editing user profiles (name, email, password).
- Deleting an account.

### Financial Transactions
- Adding income and expenses with amount, category, date, and description.
- Modifying transaction details.
- Deleting transactions from the record.
- Viewing transaction history with filtering by date, category, and type (income/expense).

### Budget Management
- Setting a monthly budget.
- Tracking budget limits and receiving notifications for overspending.

### Financial Goals
- Setting savings goals.
- Monitoring progress towards financial objectives.

### Statistics & Reports
- Calculating the current balance.
- Summarizing income and expenses over a selected period.
- Analyzing expenses by category.
- Generating financial reports for users.

### Notifications
- API for spending limit reminders.
- Support for email notifications.

### Administration
- Admins can view user lists and their transactions.
- Admins can block or delete users.

## Installation & Setup

### Prerequisites
- Java 17
- Gradle
- Git
- Docker & Docker Compose (for database setup)


### Clone the Repository
```sh
git clone https://github.com/pseudomonoral/FinTrack.git
cd FinTrack
```

### Configure Environment Variables  
- To modify **Docker Compose settings** and **database connection settings**, simply edit the `.env` file.  
- No need to update `config.yaml`—values are automatically loaded from `.env`.  


### Run with IntelliJ IDEA  
Open the `com.financeTracker.ApplicationRunner` class and run the `main()` method.

### Default Credentials

#### Admin Access:
- **Login:** admin@gmail.com
- **Password:** admin

#### User Access:
- **Login:** user@gmail.com
- **Password:** user  
