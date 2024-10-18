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

## **DeepLearning4J (DL4J) Overview**

**Deeplearning4j (DL4J)** is an open-source, distributed deep learning library for Java and the JVM. It is designed for large-scale production environments and provides flexibility in building neural networks. DL4J is a powerful choice for integrating machine learning models into Java applications, especially for use cases like **predictive modeling**, as demonstrated in this project.

### **Why DL4J for This Project?**

DL4J is well-suited for Java-based enterprise applications like this one due to several key reasons:
- **Native Java Support**: DL4J runs on the JVM, allowing seamless integration with Java and Spring Boot. This makes it a natural choice for building AI-powered applications in Java.
- **Distributed Training**: DL4J supports distributed GPU training across multiple machines. Although not utilized in this basic project, it provides scalability for future enhancements if the training data grows.
- **Integration with Other JVM Libraries**: DL4J works well with Java libraries like **ND4J** (N-dimensional arrays for Java), which provides the numerical computing backbone for matrix operations used in training neural networks.
- **Flexibility and Customization**: It allows easy customization of neural network architectures to fit various use cases, including classification tasks like predicting player suitability in this project.

### **DL4J in This Project**

In this application, DL4J is used to build and train a neural network to predict the suitability of a player based on their performance metrics (e.g., batting average, bowling average, strike rate, fielding stats). Here's how it fits into the workflow:

1. **Data Collection**: Player performance data is collected via the REST API and stored in a **MySQL** database. The input features (batting average, strike rate, etc.) are pre-processed to be fed into the neural network.
   
2. **Model Training**: When a new player is added or updated, the application triggers **automatic model retraining**. The player data is pulled from the database and used to retrain the model. Retraining helps ensure the model is always up-to-date with the latest player statistics.
   
3. **Model Architecture**: 
   - Input Layer: Takes in 5 features (e.g., batting average, strike rate).
   - Hidden Layers: The model has two hidden layers with 64 and 32 neurons, respectively, using **ReLU (Rectified Linear Unit)** activation functions, which allow the model to capture non-linear patterns in the data.
   - Output Layer: The output layer consists of one neuron with a **sigmoid activation** function, used to perform binary classification (predicting whether the player is suitable for the team or not).
   
4. **Optimization**: The model uses the **Adam optimizer**, a variant of gradient descent that adjusts the learning rate dynamically, making it well-suited for deep learning applications.
   
5. **Loss Function**: A **binary cross-entropy** loss function is used, which is appropriate for binary classification problems where the output is either 0 or 1.

6. **Prediction**: Once trained, the model can be used to make predictions about a player's suitability based on their performance metrics. The prediction function converts the input data into the expected format and passes it through the trained model.

### **Example of Model Training Code**

Here’s an example of how the neural network is configured in DL4J:

```java
MultiLayerConfiguration config = new NeuralNetConfiguration.Builder()
    .seed(123) // Random seed for reproducibility
    .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
    .updater(new Adam(0.001)) // Adam optimizer
    .list()
    .layer(0, new DenseLayer.Builder().nIn(5).nOut(64).activation(Activation.RELU).build()) // Input layer + first hidden layer
    .layer(1, new DenseLayer.Builder().nIn(64).nOut(32).activation(Activation.RELU).build()) // Second hidden layer
    .layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.XENT)
        .nIn(32).nOut(1)
        .activation(Activation.SIGMOID)
        .build()) // Output layer
    .build();

// Model initialization and training
MultiLayerNetwork model = new MultiLayerNetwork(config);
model.init();
model.fit(trainingData); // Train the model with training data
```

This code demonstrates how the neural network is built with an **input layer**, **two hidden layers**, and an **output layer** for binary classification.

### **Retraining Trigger**

Whenever new player data is added or updated, the application will:
1. Retrieve all player data from MySQL.
2. Preprocess the data and split it into training and testing datasets.
3. Retrain the DL4J model using the updated data.
4. Save the retrained model for future predictions.

This ensures the AI model remains accurate and up-to-date with the latest player statistics.

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
- **Your Name**: [nurajshaminda200@gmail.com](mailto:nurajshaminda200@gmail.com)
- **GitHub**: [https://github.com/Nuraj250](https://github.com/Nuraj250)
