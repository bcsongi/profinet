'use strict';

angular.module('profinetApp')
    .controller('FieldDetailController', function ($scope, $rootScope, $stateParams, entity, Field) {
        $scope.field = entity;
        $scope.load = function (id) {
            Field.get({id: id}, function(result) {
                $scope.field = result;
            });
        };
        $rootScope.$on('profinetApp:fieldUpdate', function(event, result) {
            $scope.field = result;
        });
    });
