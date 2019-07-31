'use strict';

angular.module('profinetApp')
    .controller('ProfileDetailController', function ($scope, $rootScope, $stateParams, entity, Principal) {
        $scope.profile = entity;
		
		$scope.loadAll = function() {
            Expert.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.experts = result;
            });
        };
		
        $scope.load = function (id) {
            Profile.get({id: id}, function(result) {
                $scope.profile = result;
				$scope.loadAll();
            });
        };
		
		$scope.loadAll();
		
        $rootScope.$on('profinetApp:profileUpdate', function(event, result) {
            $scope.profile = result;
        });
		Principal.identity().then(function(account) {
            alert();
			$scope.account = account;
			
            $scope.isAuthenticated = Principal.isAuthenticated;
        });
    });