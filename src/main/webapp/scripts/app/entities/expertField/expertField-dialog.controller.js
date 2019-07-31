'use strict';

angular.module('profinetApp').controller('ExpertFieldDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'ExpertField', 'Field', 'Expert',
        function($scope, $stateParams, $modalInstance, entity, ExpertField, Field, Expert) {

        $scope.expertField = entity;
        $scope.fields = Field.query();
        $scope.experts = Expert.query();
        $scope.load = function(id) {
            ExpertField.get({id : id}, function(result) {
                $scope.expertField = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('profinetApp:expertFieldUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.expertField.id != null) {
                ExpertField.update($scope.expertField, onSaveFinished);
            } else {
                ExpertField.save($scope.expertField, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
