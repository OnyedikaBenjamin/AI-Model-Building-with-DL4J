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

@Getter
@Component
@Slf4j
@AllArgsConstructor
public class PlayerAIModel {

    private MultiLayerNetwork model;
    private static final String MODEL_PATH = "src/main/resources/player_model.zip";

    public PlayerAIModel() {
        File modelFile = new File(MODEL_PATH);
        if (modelFile.exists()) {
            try {
                model = ModelSerializer.restoreMultiLayerNetwork(modelFile);
                System.out.println("Loaded existing model from " + MODEL_PATH);
            } catch (IOException e) {
                System.err.println("Failed to load the model. Creating a new model.");
                createNewModel();
            }
        } else {
            System.out.println("Model file not found. Creating a new model.");
            createNewModel();
        }
    }

    private void createNewModel() {
        model = new MultiLayerNetwork(new NeuralNetConfiguration.Builder()
                .seed(42)
                .updater(new Adam(0.001))
                .list()
                .layer(0, new DenseLayer.Builder().nIn(5).nOut(64).activation(Activation.RELU).build())
                .layer(1, new DenseLayer.Builder().nIn(64).nOut(32).activation(Activation.RELU).build())
                .layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.XENT)
                        .activation(Activation.SIGMOID).nIn(32).nOut(1).build())
                .build());
        model.init();
        try {
            ModelSerializer.writeModel(model, MODEL_PATH, true);
            System.out.println("New model created and saved to " + MODEL_PATH);
        } catch (IOException e) {
            System.err.println("Failed to save the new model.");
        }
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

