'use strict';

angular.module('profinetApp')
    .controller('OutsideExpertDetailController', function ($scope, $rootScope, $stateParams, entity, Expert, Field, Keyword, Language, User, Principal, ExpertContactRequestBetween) {
        $scope.expert = entity;
        $scope.load = function (id) {
            Expert.get({id: id}, function(result) {
                $scope.expert = result;
            });
			
        };

		$scope.load($stateParams.id);
		
        $rootScope.$on('profinetApp:expertUpdate', function(event, result) {
            $scope.expert = result;
        });
    });
