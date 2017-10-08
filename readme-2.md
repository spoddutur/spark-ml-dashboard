# Spark ML Dashboard to tweak and test your model
This project shows how to use SPARK MLLib and build a ML Dashboard to:
1. Expose our model via an endpoint for users to play and tweak with the model-params 
2. Quickly test the model with the new params using sample test data.

## 1. Central Idea
Logistic regressions, decision trees, SVMs, neural networks etc have a set of structural choices that one must make before actually fitting the model parameters. For example, within the logistic regression family, you can build separate models using either L1 or L2 regularization penalties. Within the decision tree family, you can have different models with different structural choices such as the depth of the tree, pruning thresholds, or even the splitting criteria. These structural choices are called hyperparameters.

Taditionally, for a datascienctist, building a classification model is an iterative process of coming up with the model, tweak the hyperparameters and test it using test data. If the results are not matching the expectations, this could potentially lead to another iteration of tweaking the model and evaluating it.
In this project, am proposing a solution to ease datascientists off some part of this iteration.

## 2. Proposal:
- Load your trained model using Spark-ML
- Have a dashboard where the hyperparameters like regularization params, thresholds etc are exposed for user to tweak
- Quickly test the model with the new params on test dataset

## 3. ML Dashboard Inputs/Outputs and Demo:

### 3.1 Dashboard Demo:

![out](https://user-images.githubusercontent.com/22542670/31316609-3e301304-ac4e-11e7-9019-196ca0c95f5a.gif)

### 3.2 Dashboard Inputs:
**Model used for testing in this project:**
For the purpose of demo, I've implemented a model using Spark 2.1 ML, to classify news documents into Science or NonScience category. I've done this using K-Fold CrossValidation on a ML Pipeline. Further details on the trained model can be found in the appendix section below.

**Dashboard Inputs submitted by user:**
- Model params: As you can see in the above demo, I have exposed following four parameters of this model for user to play and test:
  1. LinearRegression - Threshold
  2. LinearRegression - RegularizationParam  
  3. LinearRegression - Max Iterations  
  4. HashingTF - Number of Features

- Test Data to evaluate: Folder containing the documents to test

**Initial values of the model params displayed in the dashboard:** These params are initialised with their respective default values that the model was trained to have.

**Dashboard Output:** 
Table with 2 columns: DocumentName and ClassificationResult (whether its a science document or not)

## 4. Usecases tested:
For further clarity, please check below three snapshots, where the LogisticRegression Threshold param was tweaked to 0, 0.5 and 1 values respectively to verify the classification result.

### Case1: Threshold 0.5 - Some documents are classified as true while some as false
<img width="450" alt="screen shot 2017-10-08 at 8 56 44 pm" src="https://user-images.githubusercontent.com/22542670/31318203-055813ce-ac6c-11e7-8eb0-e4da72b6a83a.png">

### Case2: Threshold 1 - All documents are classified as true
<img width="450" alt="screen shot 2017-10-08 at 8 57 18 pm" src="https://user-images.githubusercontent.com/22542670/31318205-0559e87a-ac6c-11e7-9829-7ceecf887064.png">

### Case3: Threshold 0 - All documents are classified as false
<img width="450" alt="screen shot 2017-10-08 at 8 57 38 pm" src="https://user-images.githubusercontent.com/22542670/31318204-0558f5b4-ac6c-11e7-981b-d22e2f7ba45d.png">

## 5. Get Running
- mvn clean install
- spark-submit --class com.spoddutur.MainClass <PATH_TO_20news-bydate.jar_FILE>

## 6. Structure of files in this repository
- ***data:*** Contains training and test news data taken from [scikit](http://scikit-learn.org/stable/datasets/twenty_newsgroups.html).
- ***predictions.json:*** Final output of our trained model predictions on test data.
- ***trained_model:*** Final model we trained
- ***src/main/scala/com/spoddutur/MainApp.scala:*** Main class of this project.

## 7. Requirements
- Spark 2.1 and Spark ML
- Scala 2.11

## 8. Conclusion
This project should be a good starting point on building a ML Dashboard where you can plug and play your Models and quickly verify how your model is classifying any corner test cases.

## 9. References
- [Mastering Apache Spark GitBook](https://jaceklaskowski.gitbooks.io/mastering-apache-spark/content/spark-mllib/spark-mllib-pipelines-example-classification.html)
- [Building, Debugging, and Tuning Spark Machine Learning Pipelines - Joseph Bradley (Databricks)](https://www.youtube.com/watch?v=OednhGRp938&feature=youtu.be)
- [20News Test and Training Data](http://scikit-learn.org/stable/datasets/twenty_newsgroups.html)
- [About Model HyperParameters](https://elitedatascience.com/machine-learning-iteration)

## 10. Appendix:
Model built for Binary Classification of News
-------
It's a Spark 2.1 ML implementation to classify news documents into Science or NonScience category. We've done this using K-Fold CrossValidation on a ML Pipeline.

Input Data Distribution
-----
Label|Training Data Counts| Test Data Counts
---|---|---
Science|8941|5953
NonScience|2373|1579

Pipeline used for training
------
Following diagram depicts the pipeline we used for news classification. Image taken and modified from [here](http://karlrosaen.com/ml/learning-log/2016-06-20/)). 

![pipeline-diagram](https://cloud.githubusercontent.com/assets/22542670/21791030/2c3c0202-d706-11e6-9db4-de24ed14ce98.png)

ParamsGrid used for k-fold
-----
```
val paramGrid = new ParamGridBuilder()
  .addGrid(hashingTF.numFeatures, Array(1000, 10000))
  .addGrid(lr.regParam, Array(0.05, 0.2))
  .addGrid(lr.maxIter, Array(5, 10, 15))
  .build
```

Evaluator used
-----
```
import org.apache.spark.ml.evaluation.BinaryClassificationEvaluator
val evaluator = new BinaryClassificationEvaluator().setMetricName("areaUnderROC")
```

Final accuracy achieved
-----
0.9296967048295749

K-Fold Best Params
---
```
{
	logreg_2be4a9a4df41-maxIter: 10,
	hashingTF_5e435011f5a5-numFeatures: 10000,
	logreg_2be4a9a4df41-regParam: 0.2
}
```

