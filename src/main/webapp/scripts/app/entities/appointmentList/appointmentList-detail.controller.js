'use strict';

angular.module('profinetApp')
    .controller('AppointmentListDetailController', function ($scope, $rootScope, $stateParams, entity, Appointment,
			Expert, User, UserFeedback, UserFeedbackByAppointment) {
		
		$scope.getUFByAppointmentId = function(appointmentId) {
			UserFeedbackByAppointment.get({id: appointmentId}, function(result) {
				if (typeof result.id === 'undefined') {
					$scope.userFeedback = new UserFeedback();
				} else {
					$scope.userFeedback = result;
				}
			});
		}
		
		$scope.appointment = entity;
		setTimeout(function() { 
			$scope.getUFByAppointmentId($scope.appointment.id);
			}, 1);

		
		$scope.load = function (id) {
            Appointment.get({id: id}, function(result) {
                $scope.appointment = result;
            });
        };
		
		$rootScope.$on('profinetApp:appointmentUpdate', function(event, result) {
            $scope.appointment = result;
        });
    });
