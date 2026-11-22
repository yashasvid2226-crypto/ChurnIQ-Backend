
package com.churniq.ml;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import smile.classification.RandomForest;
import smile.data.DataFrame;
import smile.data.formula.Formula;
import smile.data.vector.DoubleVector;
import smile.data.vector.IntVector;

public class ChurnModelTrainer {

    private static final String DATASET_PATH =
            "src/main/resources/ml-data/Telco-Customer-Churn.csv";

    private static final String[] FEATURE_NAMES = {
        "gender",
        "seniorCitizen",
        "partner",
        "dependents",
        "tenure",
        "phoneService",
        "monthlyCharges",
        "totalCharges",
        "contract",
        "internetService"
    };

    public static void main(String[] args) {

        List<double[]> features = new ArrayList<>();
        List<Integer> labels = new ArrayList<>();

        File file = new File(DATASET_PATH);

        if (!file.exists()) {
            System.out.println("Dataset file not found!");
            return;
        }

        // STEP 1: Read and prepare dataset

        try (
            CSVParser parser = CSVParser.parse(
                file,
                StandardCharsets.UTF_8,
                CSVFormat.DEFAULT.builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .get()
            )
        ) {

            for (CSVRecord record : parser) {

                String totalChargesText =
                        record.get("TotalCharges").trim();

                // Skip records with missing TotalCharges
                if (totalChargesText.isEmpty()) {
                    continue;
                }

                double[] row = new double[10];

                row[0] = record.get("gender").equals("Male") ? 1 : 0;
                row[1] = Double.parseDouble(record.get("SeniorCitizen"));
                row[2] = record.get("Partner").equals("Yes") ? 1 : 0;
                row[3] = record.get("Dependents").equals("Yes") ? 1 : 0;
                row[4] = Double.parseDouble(record.get("tenure"));
                row[5] = record.get("PhoneService").equals("Yes") ? 1 : 0;
                row[6] = Double.parseDouble(record.get("MonthlyCharges"));
                row[7] = Double.parseDouble(totalChargesText);

                String contract = record.get("Contract");

                if (contract.equals("Month-to-month")) {
                    row[8] = 0;
                } else if (contract.equals("One year")) {
                    row[8] = 1;
                } else {
                    row[8] = 2;
                }

                String internet = record.get("InternetService");

                if (internet.equals("DSL")) {
                    row[9] = 1;
                } else if (internet.equals("Fiber optic")) {
                    row[9] = 2;
                } else {
                    row[9] = 0;
                }

                features.add(row);

                labels.add(
                    record.get("Churn").equalsIgnoreCase("Yes") ? 1 : 0
                );
            }

        } catch (IOException e) {
            System.out.println("Error reading dataset: " + e.getMessage());
            return;
        }

        System.out.println("Valid records loaded: " + features.size());

        // STEP 2: Shuffle and split the dataset

        List<Integer> indices = new ArrayList<>();

        for (int i = 0; i < features.size(); i++) {
            indices.add(i);
        }

        Collections.shuffle(indices, new Random(42));

        int trainingSize = (int) (features.size() * 0.8);

        List<double[]> trainFeatures = new ArrayList<>();
        List<Integer> trainLabels = new ArrayList<>();

        List<double[]> testFeatures = new ArrayList<>();
        List<Integer> testLabels = new ArrayList<>();

        for (int i = 0; i < indices.size(); i++) {

            int index = indices.get(i);

            if (i < trainingSize) {
                trainFeatures.add(features.get(index));
                trainLabels.add(labels.get(index));
            } else {
                testFeatures.add(features.get(index));
                testLabels.add(labels.get(index));
            }
        }

        // STEP 3: Convert data into Smile DataFrames

        DataFrame trainingData = createDataFrame(
                trainFeatures, trainLabels
        );

        DataFrame testingData = createDataFrame(
                testFeatures, testLabels
        );

        // STEP 4: Train Random Forest

        System.out.println("\nTraining Random Forest model...");

        Properties properties = new Properties();

        properties.setProperty(
                "smile.random_forest.trees", "200"
        );

        properties.setProperty(
                "smile.random_forest.max_depth", "20"
        );

        RandomForest.Options options =
                RandomForest.Options.of(properties);

        RandomForest model = RandomForest.fit(
                Formula.lhs("Churn"),
                trainingData,
                options
        );

        System.out.println("Model training completed!");

        // STEP 5: Evaluate the model

        int truePositive = 0;
        int trueNegative = 0;
        int falsePositive = 0;
        int falseNegative = 0;

        for (int i = 0; i < testingData.size(); i++) {

            int actual = testLabels.get(i);

            int predicted = model.predict(testingData.get(i));

            if (actual == 1 && predicted == 1) {
                truePositive++;
            } else if (actual == 0 && predicted == 0) {
                trueNegative++;
            } else if (actual == 0 && predicted == 1) {
                falsePositive++;
            } else {
                falseNegative++;
            }
        }

        int correct = truePositive + trueNegative;

        double accuracy =
                (correct * 100.0) / testingData.size();

        System.out.println("\n===== CHURNIQ MODEL RESULTS =====");

        System.out.println("Training records: " + trainingData.size());
        System.out.println("Testing records: " + testingData.size());

        System.out.printf("Accuracy: %.2f%%%n", accuracy);

        System.out.println("\nConfusion Matrix:");

        System.out.println("True Positives: " + truePositive);
        System.out.println("True Negatives: " + trueNegative);
        System.out.println("False Positives: " + falsePositive);
        System.out.println("False Negatives: " + falseNegative);

        System.out.println("\nRandom Forest model trained successfully!");

        // STEP 6: Save the trained model

        try {

            Path modelDirectory = Path.of("models");

            Files.createDirectories(modelDirectory);

            Path modelPath =
                    modelDirectory.resolve("churn-random-forest.model");

            try (
                ObjectOutputStream output =
                        new ObjectOutputStream(
                                Files.newOutputStream(modelPath)
                        )
            ) {

                output.writeObject(model);
            }

            System.out.println("\nModel saved successfully!");
            System.out.println("Model location: " + modelPath.toAbsolutePath());

        } catch (IOException e) {

            System.out.println("Error saving model: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // STEP 7: Create Smile DataFrame

    private static DataFrame createDataFrame(
            List<double[]> features,
            List<Integer> labels) {

        int size = features.size();

        double[][] columns = new double[10][size];

        int[] churnLabels = new int[size];

        for (int i = 0; i < size; i++) {

            double[] row = features.get(i);

            for (int j = 0; j < 10; j++) {
                columns[j][i] = row[j];
            }

            churnLabels[i] = labels.get(i);
        }

        return new DataFrame(
            new DoubleVector(FEATURE_NAMES[0], columns[0]),
            new DoubleVector(FEATURE_NAMES[1], columns[1]),
            new DoubleVector(FEATURE_NAMES[2], columns[2]),
            new DoubleVector(FEATURE_NAMES[3], columns[3]),
            new DoubleVector(FEATURE_NAMES[4], columns[4]),
            new DoubleVector(FEATURE_NAMES[5], columns[5]),
            new DoubleVector(FEATURE_NAMES[6], columns[6]),
            new DoubleVector(FEATURE_NAMES[7], columns[7]),
            new DoubleVector(FEATURE_NAMES[8], columns[8]),
            new DoubleVector(FEATURE_NAMES[9], columns[9]),
            new IntVector("Churn", churnLabels)
        );
    }
}
