'use strict';

angular.module('profinetApp').controller('FieldDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Field',
        function($scope, $stateParams, $modalInstance, entity, Field) {

        $scope.field = entity;
        $scope.load = function(id) {
            Field.get({id : id}, function(result) {
                $scope.field = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('profinetApp:fieldUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.field.id != null) {
                Field.update($scope.field, onSaveFinished);
            } else {
                Field.save($scope.field, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
