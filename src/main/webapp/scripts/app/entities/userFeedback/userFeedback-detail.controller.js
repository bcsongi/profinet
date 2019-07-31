'use strict';

angular.module('profinetApp')
    .controller('UserFeedbackDetailController', function ($scope, $rootScope, $stateParams, entity, UserFeedback, Appointment) {
        $scope.userFeedback = entity;
        $scope.load = function (id) {
            UserFeedback.get({id: id}, function(result) {
                $scope.userFeedback = result;
            });
        };
        $rootScope.$on('profinetApp:userFeedbackUpdate', function(event, result) {
            $scope.userFeedback = result;
        });
    });
