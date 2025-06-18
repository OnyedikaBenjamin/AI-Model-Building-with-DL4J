# **AI Model Building with DL4J**

This project is a **Java Spring Boot application** that uses **DL4J (Deeplearning4j)** to train a neural network for predicting if a player is **suitable or not** for the team. The application supports **CRUD operations** on player performance data stored in a **MySQL** database and retrains the AI model automatically whenever the data changes.


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
git clone https://github.com/Nuraj250/ai-model-building-dl4j.git
cd ai-model-building-dl4j
```

---

### **Step 2: Set Up MySQL Database**
1. Start your MySQL server.
2. Create a new database:
   ```sql
   CREATE DATABASE player_ai_db;
   ```
3. Configure the database credentials in **`src/main/resources/application.properties`**:
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

---

## **How the AI Model Works**

The project uses **DL4J** to build a neural network that predicts player suitability based on five performance metrics:  
**batting average, strike rate, bowling average, economy rate, and fielding stats**.

### **Workflow**

1. **Data Collection**: Player data is collected through the API and saved to **MySQL**.
2. **Model Training**: When data is added or updated, the **neural network** retrains to incorporate the changes.
3. **Prediction**: The trained model predicts if a player is **suitable** based on performance metrics.
4. **Model Storage**: The model is saved as `player_model.zip` and reloaded whenever required.

### **Model Configuration Example**
```java
MultiLayerConfiguration config = new NeuralNetConfiguration.Builder()
    .seed(123)
    .updater(new Adam(0.001))
    .list()
    .layer(0, new DenseLayer.Builder().nIn(5).nOut(64).activation(Activation.RELU).build())
    .layer(1, new DenseLayer.Builder().nIn(64).nOut(32).activation(Activation.RELU).build())
    .layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.XENT)
        .nIn(32).nOut(1)
        .activation(Activation.SIGMOID)
        .build())
    .build();

MultiLayerNetwork model = new MultiLayerNetwork(config);
model.init();
```

---

## **Retraining Process**
1. When a player is **added, updated, or deleted**, the system retrieves the latest data.
2. The model **re-trains** using the new dataset.
3. The **new model** replaces the old one by saving it as `player_model.zip`.

---

## **Making Predictions**
You can use the trained model to predict player suitability programmatically:

```java
float[] newPlayer = {50.0f, 120.0f, 25.0f, 4.2f, 14};
boolean isGoodPlayer = aiModel.predict(newPlayer);
System.out.println("Is the player suitable? " + (isGoodPlayer ? "Yes" : "No"));
```

---

## **Contributing**
1. Fork the repository.
2. Create a new branch:
   ```bash
   git checkout -b feature-branch
   ```
3. Make your changes and commit them:
   ```bash
   git commit -m "Add new feature"
   ```
4. Push your changes:
   ```bash
   git push origin feature-branch
   ```
5. Create a pull request.

---

## **License**
This project is licensed under the MIT License. See the **LICENSE** file for details.

---

## **Contact**
For questions or support, contact:
[nurajshaminda200@gamil.com](mailto:nurajshaminda200@gamil.com)
---
