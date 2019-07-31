'use strict';

angular.module('profinetApp').controller('MessageDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Message', 'User',
        function($scope, $stateParams, $modalInstance, entity, Message, User) {

        $scope.message = entity;
        $scope.users = User.query();
        $scope.load = function(id) {
            Message.get({id : id}, function(result) {
                $scope.message = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('profinetApp:messageUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.message.id != null) {
                Message.update($scope.message, onSaveFinished);
            } else {
                Message.save($scope.message, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
