'use strict';

angular.module('profinetApp').controller('AppointmentListNewController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Appointment', 'ExpertField', 'Principal', 'User',
    function($scope, $stateParams, $modalInstance, entity, Appointment, ExpertField, Principal, User) {
		
		$scope.appointment = entity;
		$scope.appointment.status = "CREATED";
		
		Principal.identity(true).then(function(account) {
			User.get({login: account.login}, function(result) {
				if (result != null) { 	
					$scope.appointment.userappointment = result;
				}
			});
		});

		ExpertField.get({id: $stateParams.expertFieldId}, function(result) {
            $scope.appointment.expertField = result;
        });
		

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
