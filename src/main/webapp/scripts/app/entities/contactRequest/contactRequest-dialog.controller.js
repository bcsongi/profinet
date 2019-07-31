'use strict';

angular.module('profinetApp').controller('ExpertContactRequestDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'ExpertContactRequest', 'User', 'Expert',
        function($scope, $stateParams, $modalInstance, entity, ExpertContactRequest, User, Expert) {

        $scope.expertContactRequest = entity;
        $scope.users = User.query();
        $scope.experts = Expert.query();
        $scope.load = function(id) {
            ExpertContactRequest.get({id : id}, function(result) {
                $scope.expertContactRequest = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('profinetApp:expertContactRequestUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.expertContactRequest.id != null) {
                ExpertContactRequest.update($scope.expertContactRequest, onSaveFinished);
            } else {
                ExpertContactRequest.save($scope.expertContactRequest, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
