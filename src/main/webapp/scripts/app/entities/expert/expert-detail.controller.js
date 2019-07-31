'use strict';

angular.module('profinetApp')
    .controller('ExpertDetailController', function ($scope, $rootScope, $stateParams, entity, Expert, Field, Keyword, Language, User) {
        $scope.expert = entity;
        $scope.load = function (id) {
            Expert.get({id: id}, function(result) {
				alert(result.length);
                $scope.expert = result;
            });
        };
        $rootScope.$on('profinetApp:expertUpdate', function(event, result) {
            $scope.expert = result;
        });
    });
