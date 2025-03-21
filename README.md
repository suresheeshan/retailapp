# RetailApp - Spring Boot Application

##  Overview
The **Retail Application** is a Spring Boot-based system designed to calculate reward points for customers based on their transactions. Customers earn points based on their spending, and the system tracks their rewards over time. The application follows a **RESTful API** structure and includes functionalities for **customer management, transactions, and reward calculations**.

---

##  Project Structure
```
retailapp/
│── src/main/java/com/example/rewards/
│   ├── controller/
│   │   ├── TransactionController.java
│   │   ├── CustomerController.java
│   ├── service/
│   │   ├── CustomerService.java
│   │   ├── RewardService.java
│   │   ├── TransactionService.java
│   ├── repository/
│   │   ├── CustomerRepository.java
│   │   ├── TransactionRepository.java
│   ├── model/
│   │   ├── Customer.java
│   │   ├── Transaction.java
│   ├── dto/
│   │   ├── RewardPoints.java
│   ├── exception/
│   │   ├── CustomerNotFoundException.java
│   │   ├── ExceptionHandlerMapper.java
│   │   ├── TransactionNotFoundException.java
│── src/main/resources/
│   ├── application.properties
│── src/main/java/com/example/rewards/
│   ├── controller/
│   │   ├── TransactionControllerTest.java
│   │   ├── CustomerControllerTest.java
│   ├── service/
│   │   ├── CustomerServiceTest.java
│   │   ├── RewardServiceTest.java
│   │   ├── TransactionServiceTest.java
│── README.md
│── build.gradle
```

---

## Technologies Used
- **Java 21**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **H2 Database** (In-memory database for testing)
- **JUnit & Mockito** (for unit testing)

---

## Database Schema
### **Customer Table (`customers`)**
| Column  | Type   | Description          |
|---------|--------|----------------------|
| id      | Long   | Primary Key          |
| name    | String | Customer's full name |
| email   | String | Unique email ID      |

### **Transaction Table (`transactions`)**
| Column          | Type      | Description                  |
|----------------|----------|------------------------------|
| id             | Long     | Primary Key                  |
| customer_id    | Long     | Foreign Key (Customer)       |
| amount         | Double   | Transaction amount           |
| transactionDate| LocalDate| Date of the transaction      |

---

## API Endpoints
### **Customer API**
| Method | Endpoint | Description |
|--------|---------|-------------|
| `POST` | `/create-customer` | Register a new customer |
| `GET`  | `/customers` | Get all customers |

### **Transaction API**
| Method | Endpoint | Description |
|--------|---------|-------------|
| `POST` | `/api/v1/create-transaction` | Create a new transaction |
| `GET`  | `/api/v1/transactions` | Get all transactions |
| `GET`  | `/api/v1/transactions?customerId=1` | Get transactions for a customer |

---

## Business Logic: Reward Points Calculation
Customers earn reward points based on the following criteria:
- **$50 - $100** → **1 point per dollar spent**
- **Above $100** → **2 points per dollar spent over $100**

### **Example Calculation:**
- **Transaction Amount: $120**
  - Remaining **$100 - $120** → `20 * 2 = 40 points`
  - First **$50** → `50 * 1 = 50 points`
  - **Total: 90 points**

- **Transaction Amount: $200**
  - Remaining **$100 - $200** → `100 * 2 = 200 points`
  - First **$50** → `50 * 1 = 50 points`
  - **Total: 250 points**

---

## Setup & Installation
### **1. Clone the repository**
```sh
git clone https://github.com/suresheeshan/retailapp.git
cd retailapp
```

### **2. Configure the database**
Modify `src/main/resources/application.properties`:
```properties
# H2 Database Configuration
spring.datasource.url=jdbc:h2:mem:rewarddb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
```

### **3. Build & Run the Application**
```sh
./gradlew bootRun
```

---

## Contributors
- **Suresh**
  - 📧 Contact: [suresh.paruchuri@capgemini.com](mailto:suresh.paruchuri@capgemini.com)

Feel free to contribute! 🚀

