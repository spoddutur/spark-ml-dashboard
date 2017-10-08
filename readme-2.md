# Spark ML Dashboard to tweak and test your model
This project shows how to use SPARK MLLib and build a ML Dashboard to:
1. Expose our model via an endpoint for users to play and tweak with the model-params 
2. Quickly test the model with the new params using sample test data.

## 1. Central Idea
Taditionally, for a datascienctist, building a classification model is an iterative process of coming up with the model, tweak the hyperparameters or model-coeficients and test it using test data.
If the results are not matching the expectations, this could potentially lead to another iteration of tweaking the model and evaluating it.
In this project, am proposing a solution to ease datascientists off some part of this iteration.

## 2. Proposal:
- Load your trained model using Spark-ML
- Have a dashboard where the hyperparameters like regularization params, thresholds etc are exposed for user to tweak
- Quickly test the model with the new params on test dataset

## 3. Demo:
For the purpose of demo, I've implemented a model using Spark 2.1 ML, to classify news documents into Science or NonScience category. 
I've done this using K-Fold CrossValidation on a ML Pipeline. Further details on the trained model can be found here.

I have exposed following four parameters of this model for user to play and test:
1. LinearRegression - Threshold
2. LinearRegression - RegularizationParam  
3. LinearRegression - Max Iterations  
4. HashingTF - Number of Features

**Initial values:** These params are displayed with their respective default values that the model was trained to have.

** Testing inputs:**
- Model params
- Folder containing the test data

** Test output:** 
Table with 2 columns: DocumentName and ClassificationResult (whether its a science document or not)

Below video illustrates the same:
![out](https://user-images.githubusercontent.com/22542670/31316609-3e301304-ac4e-11e7-9019-196ca0c95f5a.gif)

## 4. Snapshots of results with different values of Threshold
For further clarity, please check below three snapshots, where the LogisticRegression Threshold param was tweaked to 0, 0.5 and 1
- Case1: Threshold 0.5 - Some documents are classified as true while some as false
![image](https://user-images.githubusercontent.com/22542670/31317769-585b38b0-ac64-11e7-81cf-9e95cabeba9c.png)
- Case2: Threshold 1 - All documents are classified as true
![image](https://user-images.githubusercontent.com/22542670/31317787-9f182d80-ac64-11e7-8512-8af785ab7f04.png)
- Case3: Threshold 0 - All documents are classified as false
![image](https://user-images.githubusercontent.com/22542670/31317789-b45624d6-ac64-11e7-899f-76b5a622c1ef.png)


### 1.1 What are hyperparameters?
Logistic regressions, decision trees, SVMs, neural networks etc have a set of structural choices that one must make before actually fitting the model parameters. For example, within the logistic regression family, you can build separate models using either L1 or L2 regularization penalties. Within the decision tree family, you can have different models with different structural choices such as the depth of the tree, pruning thresholds, or even the splitting criteria.
These structural choices are called hyperparameters.

### 1.2 Iteration in building a model
For instance, training a logistic regression model actually is a two-stage process: 
1. Decide hyperparameters for the model. Example: Should the model have an L1 or L2 penalty to prevent overfitting?
2. Then, fit the model parameters to the data. Example: What are the model coefficients that minimize the loss function?

Once the model is ready, to evaluate its performance, there would be some edge cases that we might want to test the model against.
