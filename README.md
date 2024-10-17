# AI Model Building with DL4J

This project is a **Java Spring Boot application** that uses **DL4J (Deeplearning4j)** to train a neural network for predicting if a player is **suitable or not** for the team. The application supports **CRUD operations** on player performance data stored in a **MySQL** database and retrains the AI model automatically whenever the data changes.

---

## **Features**
- **CRUD Operations**: Add, update, delete, and retrieve player performance data.
- **AI Model**: Uses a **Neural Network** (DL4J) for player suitability prediction.
- **Automatic Model Retraining**: The AI model retrains every time the performance data is modified.
- **MySQL Integration**: Stores player data in a relational database.
- **REST API**: Exposes endpoints for managing player data.

---

## **Technologies Used**
- **Java 17**
- **Spring Boot** (Web, Data JPA)
- **DL4J (Deeplearning4j)** for neural network implementation
- **MySQL** for data storage
- **Lombok** for reducing boilerplate code
- **Maven** for dependency management

---

## **Project Structure**
```
src/main/java/com/ictss
    ├── ai              # DL4J model logic
    ├── controller      # REST controllers
    ├── dto             # Data Transfer Objects (DTO)
    ├── model           # JPA entities
    ├── repository      # Spring Data JPA repositories
    └── service         # Service layer for business logic
```

---

## **Setup and Installation**

### **Prerequisites**
- **Java 17** or higher installed
- **Maven** installed
- **MySQL** installed and running
- **Git** installed (for cloning the project)

---

### **Step 1: Clone the Repository**
```bash
git clone https://github.com/yourusername/ai-model-building-dl4j.git
cd ai-model-building-dl4j
```

---

### **Step 2: Set Up MySQL Database**
1. Start your MySQL server.
2. Create a new database:
   ```sql
   CREATE DATABASE player_ai_db;
   ```
3. Open the project’s **`src/main/resources/application.properties`** file and configure the database credentials:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/player_ai_db?useSSL=false
   spring.datasource.username=root
   spring.datasource.password=yourpassword
   ```

---

### **Step 3: Build and Run the Application**
Use Maven to build and run the application:
```bash
mvn clean install
mvn spring-boot:run
```

---

### **Step 4: Test the API Endpoints**

Use **Postman** or another tool to test the API.

#### **1. Add Player Performance Data**
- **POST**: `http://localhost:8080/api/performance`
- **Body (JSON)**:
    ```json
    {
        "average": 55.0,
        "strikeRate": 130.0,
        "bowlingAverage": 20.0,
        "economyRate": 4.0,
        "fieldingStats": 15,
        "label": 1
    }
    ```

#### **2. Retrieve All Player Data**
- **GET**: `http://localhost:8080/api/performance`

#### **3. Predict Player Suitability**
Once the model is trained, you can predict player suitability using the **DL4J model logic**:
```java
float[] playerData = {48.0f, 120.0f, 23.0f, 4.1f, 12};
boolean isSuitable = aiModel.predict(playerData);
System.out.println("Is the player suitable? " + (isSuitable ? "Yes" : "No"));
```

---

## **How It Works**
1. **Add or update player performance data** using the API.
2. **The AI model retrains automatically** with the new data.
3. The **trained model is saved** and ready to make predictions.
4. **Predictions** can be made using the player’s performance metrics.

---

## **Model Overview**
- **Neural Network Architecture**:
  - Input: 5 features (average, strike rate, etc.)
  - Hidden Layers: 2 layers with 64 and 32 neurons (ReLU activation)
  - Output Layer: 1 neuron with **sigmoid activation** (binary classification)
- **Optimizer**: Adam
- **Loss Function**: Binary cross-entropy

---

## **Contributing**
1. Fork the project.
2. Create a new branch (`git checkout -b feature-branch`).
3. Commit your changes (`git commit -am 'Add new feature'`).
4. Push to the branch (`git push origin feature-branch`).
5. Open a pull request.

---

## **License**
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## **Contact**
For questions or support, contact:
- **Your Name**: [your.email@example.com](mailto:your.nurajshaminda200@gmail.com)
- **GitHub**: [https://github.com/yourusername](https://github.com/Nuraj250)

---

## **Final Notes**
This project demonstrates how to integrate **machine learning models** into a **Spring Boot backend** using **DL4J** and **MySQL**. You can expand it by adding new metrics or experimenting with different neural network architectures.
