package com.DL4J.player_performance_ai.ai;

import org.deeplearning4j.datasets.iterator.utilty.ListDataSetIterator;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.*;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PlayerAIModel {

    private final MultiLayerNetwork model;

    public PlayerAIModel() {
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
    }

    public void train(List<DataSet> data) {
        DataSetIterator iterator = new ListDataSetIterator<>(data);
        model.fit(iterator, 50);
    }

    public void save(String path) throws IOException {
        model.save(new File(path), true);
    }

    public boolean predict(float[] features) {
        return model.output(Nd4j.create(features)).getDouble(0) >= 0.5;
    }
}

