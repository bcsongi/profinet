'use strict';

angular.module('profinetApp').controller('AppointmentDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Appointment', 'ExpertField', 'User',
        function($scope, $stateParams, $modalInstance, entity, Appointment, ExpertField, User) {

        $scope.appointment = entity;
        $scope.expertfields = ExpertField.query();
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
			$scope.refresh;
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel'); 
        };
}]);
