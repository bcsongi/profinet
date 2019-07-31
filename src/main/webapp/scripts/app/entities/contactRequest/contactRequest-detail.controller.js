'use strict';

angular.module('profinetApp')
    .controller('ExpertContactRequestDetailController', function ($scope, $rootScope, $stateParams, entity, ExpertContactRequest, User, Expert) {
        $scope.expertContactRequest = entity;
        $scope.load = function (id) {
            ExpertContactRequest.get({id: id}, function(result) {
                $scope.expertContactRequest = result;
            });
        };
        $rootScope.$on('profinetApp:expertContactRequestUpdate', function(event, result) {
            $scope.expertContactRequest = result;
        });
    });
