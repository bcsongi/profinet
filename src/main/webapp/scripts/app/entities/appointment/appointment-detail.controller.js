'use strict';

angular.module('profinetApp')
    .controller('AppointmentDetailController', function ($scope, $rootScope, $stateParams, entity, Appointment, Expert, User) {
        $scope.appointment = entity;
        $scope.load = function (id) {
            Appointment.get({id: id}, function(result) {
                $scope.appointment = result;
            });
        };
        $rootScope.$on('profinetApp:appointmentUpdate', function(event, result) {
            $scope.appointment = result;
        });
    });
