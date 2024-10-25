package com.DL4J.player_performance_ai.ai;

import com.DL4J.player_performance_ai.dto.PlayerPerformanceDto;
import com.DL4J.player_performance_ai.model.PlayerPerformance;
import com.DL4J.player_performance_ai.repository.PlayerPerformanceRepository;
import com.DL4J.player_performance_ai.service.PlayerPerformanceService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.deeplearning4j.datasets.iterator.utilty.ListDataSetIterator;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component  // Register as a Spring bean
@Slf4j
public class PlayerAIModel {

    private final PlayerPerformanceRepository playerPerformanceRepository;
    private final MultiLayerNetwork model;
    private static final String MODEL_PATH = "src/main/resources/player_model.zip";

    // Use constructor injection for the repository
    public PlayerAIModel(PlayerPerformanceRepository repository) {
        this.playerPerformanceRepository = repository;
        this.model = initializeModel();  // Initialize or load the model
    }

    private MultiLayerNetwork initializeModel() {
        File modelFile = new File(MODEL_PATH);
        if (modelFile.exists()) {
            try {
                System.out.println("Loaded existing model from " + MODEL_PATH);
                return ModelSerializer.restoreMultiLayerNetwork(modelFile);
            } catch (IOException e) {
                System.err.println("Failed to load the model. Creating a new one.");
            }
        }
        return createNewModel();
    }

    private MultiLayerNetwork createNewModel() {
        MultiLayerNetwork newModel = new MultiLayerNetwork(new NeuralNetConfiguration.Builder()
                .seed(42)
                .updater(new Adam(0.001))
                .list()
                .layer(0, new DenseLayer.Builder().nIn(5).nOut(64).activation(Activation.RELU).build())
                .layer(1, new DenseLayer.Builder().nIn(64).nOut(32).activation(Activation.RELU).build())
                .layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.XENT)
                        .activation(Activation.SIGMOID).nIn(32).nOut(1).build())
                .build());
        newModel.init();
        saveModel(newModel);
        return newModel;
    }

    private void saveModel(MultiLayerNetwork model) {
        try {
            ModelSerializer.writeModel(model, MODEL_PATH, true);
            System.out.println("Model saved to " + MODEL_PATH);
        } catch (IOException e) {
            System.err.println("Failed to save the model.");
        }
    }

    public void trainModel() {
        List<PlayerPerformance> players = playerPerformanceRepository.findAll();  // Fetch all player data
        List<DataSet> dataSets = new ArrayList<>();

        for (PlayerPerformance player : players) {
            // Convert player data to a 2D array (1 row, 5 columns)
            INDArray features = Nd4j.create(new float[][]{
                    {
                            (float) player.getAverage(),
                            (float) player.getStrikeRate(),
                            (float) player.getBowlingAverage(),
                            (float) player.getEconomyRate(),
                            player.getFieldingStats()
                    }
            });

            // Convert the label to a 2D array (1 row, 1 column)
            INDArray label = Nd4j.create(new float[][]{
                    {player.getLabel()}
            });

            // Create a DataSet with the correctly shaped features and label
            dataSets.add(new DataSet(features, label));
        }

        // Create a ListDataSetIterator from the DataSet list
        ListDataSetIterator<DataSet> iterator = new ListDataSetIterator<>(dataSets);

        // Train the model for 50 epochs
        model.fit(iterator, 50);

        // Save the retrained model
        saveModel(model);
    }


    public boolean predict(float[] features) {
        // Convert the input array to a 2D matrix (1 row, 5 columns)
        INDArray input = Nd4j.create(features).reshape(1, features.length);

        // Get the output from the model
        INDArray output = model.output(input);

        // Assuming the model outputs a probability or classification
        return output.getFloat(0) > 0.5;
    }
}

