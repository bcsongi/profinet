'use strict';

angular.module('profinetApp').controller('AppointmentListDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Appointment', 'Expert', 'User',
        function($scope, $stateParams, $modalInstance, entity, Appointment, Expert, User) {

        $scope.appointment = entity;
        $scope.experts = Expert.query();
        $scope.users = User.query();
        $scope.load = function(id) {
            Appointment.get({id : id}, function(result) {
                $scope.appointment = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('profinetApp:appointmentUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.appointment.id != null) {
                Appointment.update($scope.appointment, onSaveFinished);
            } else {
                Appointment.save($scope.appointment, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel'); 
        };
}]);
