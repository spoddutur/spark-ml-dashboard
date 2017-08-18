var app = angular.module("TestSparkMlModel", [])
app.config(function($locationProvider){
    $locationProvider.html5Mode({
        enabled: true,
        requireBase: false
    })
});

app.controller("SparkMlModelController", function($scope, $http, $location) {
    $scope.helloTo = {};
    console.log("in test.js -", $location.search.id)
    $scope.helloTo.title = "Some blabla..";

    var modelParams = [
        {label:"LinearRegression - Threshold", name: "lrThreshold", default: "0.5"},
        {label:"LinearRegression - RegularizationParam", name: "lrRegParam", default:"0.2"},
        {label:"LinearRegression - Max Iterations", name: "lrMaxIter", default:"5"},
        {label:"HashingTF - Number of Features", name:"htfNumFeatures", default:"5000"}
    ];

    $scope.modelParams = modelParams
    $scope.formData = {query: "", modelParams: modelParams};

     $scope.submitTheForm = function() {
        var query = $scope.formData.query
        console.log(query)
        $scope.testModelWithQuery($scope.formData)
    }

    $scope.testModelWithQuery = function(formData) {

        var modelParams = formData.modelParams;
        var len = modelParams.length

        var data = {}
        data.query = formData.query
        for(i=0; i< len; i++) {
            data[modelParams[i].name] = modelParams[i].default
        }

        // Call the services
        // $http.post('http://localhost:8003/testmodelpost', JSON.stringify(data)).then(
        $http.post('http://localhost:8003/testmodelpost', data ,{
          headers: {
             'Content-Type': 'application/json; charset=utf-8'
          }
         }).then(
            function (response) {
                if (response.data) {
                    console.log("posted data");
                    $scope.helloTo.title = response.data;
                    $scope.msg = "Post Data Submitted Successfully!";
                }
            },
            function (response) {
                $scope.msg = "Service not Exists";
                console.log("service doesnt exist");
                $scope.statusval = response.status;
                $scope.statustext = response.statusText;
                $scope.headers = response.headers();
            }
        );

        console.log("inside testModelWithQuery", data);
//        $http.get('http://localhost:8003/test?query=' + query)
//            .then(function(response) {
//                console.log(response.data)
//                $scope.helloTo.title = response.data;
//            });
    }
});
