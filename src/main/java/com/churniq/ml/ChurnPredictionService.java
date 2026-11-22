
package com.churniq.ml;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import smile.classification.RandomForest;
import smile.data.DataFrame;
import smile.data.vector.DoubleVector;

@Service
public class ChurnPredictionService {

    private RandomForest model;

    private static final String MODEL_PATH =
            "models/churn-random-forest.model";

    @PostConstruct
    public void loadModel() {

        Path path = Path.of(MODEL_PATH);

        if (!Files.exists(path)) {
            throw new RuntimeException(
                    "Churn prediction model not found: "
                    + path.toAbsolutePath()
            );
        }

        try (ObjectInputStream input = new ObjectInputStream(
                Files.newInputStream(path))) {

            model = (RandomForest) input.readObject();

            System.out.println(
                    "Churn prediction model loaded successfully!"
            );

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(
                    "Failed to load churn prediction model.", e
            );
        }
    }

    public int predict(PredictionRequest request) {

        DataFrame customerData = new DataFrame(
            new DoubleVector("gender",
                    new double[]{request.getGender()}),
            new DoubleVector("seniorCitizen",
                    new double[]{request.getSeniorCitizen()}),
            new DoubleVector("partner",
                    new double[]{request.getPartner()}),
            new DoubleVector("dependents",
                    new double[]{request.getDependents()}),
            new DoubleVector("tenure",
                    new double[]{request.getTenure()}),
            new DoubleVector("phoneService",
                    new double[]{request.getPhoneService()}),
            new DoubleVector("monthlyCharges",
                    new double[]{request.getMonthlyCharges()}),
            new DoubleVector("totalCharges",
                    new double[]{request.getTotalCharges()}),
            new DoubleVector("contract",
                    new double[]{request.getContract()}),
            new DoubleVector("internetService",
                    new double[]{request.getInternetService()})
        );

        return model.predict(customerData.get(0));
    }
}
