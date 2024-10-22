package com.DL4J.player_performance_ai.ai;

import org.deeplearning4j.datasets.iterator.utilty.ListDataSetIterator;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.*;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
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
        return model.output(Nd4j.create(features)).getDouble(0) >= 0.5;
    }
}

