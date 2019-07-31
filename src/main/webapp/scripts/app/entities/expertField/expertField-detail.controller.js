'use strict';

angular.module('profinetApp')
    .controller('ExpertFieldDetailController', function ($scope, $rootScope, $stateParams, entity, ExpertField, Expert, Field) {
        $scope.expertField = entity;
        $scope.load = function (id) {
            ExpertField.get({id: id}, function(result) {
                $scope.expertField = result;
            });
        };
        $rootScope.$on('profinetApp:expertFieldUpdate', function(event, result) {
            $scope.expertField = result;
        });
    });
