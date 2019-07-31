'use strict';

angular.module('profinetApp').controller('UserFeedbackDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'UserFeedback', 'Appointment',
        function($scope, $stateParams, $modalInstance, entity, UserFeedback, Appointment) {

        $scope.userFeedback = entity;
        $scope.appointments = Appointment.query();
        $scope.load = function(id) {
            UserFeedback.get({id : id}, function(result) {
                $scope.userFeedback = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('profinetApp:userFeedbackUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.userFeedback.id != null) {
                UserFeedback.update($scope.userFeedback, onSaveFinished);
            } else {
                UserFeedback.save($scope.userFeedback, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
