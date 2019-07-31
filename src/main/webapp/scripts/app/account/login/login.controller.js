'use strict';

angular.module('profinetApp')
    .controller('LoginController', function ($rootScope, $scope, $state, $timeout, Auth, ExpertByLogin, Expert, User, ExpertUpdateLastDate) {
        $scope.user = {};
        $scope.errors = {};

        $scope.rememberMe = true;
        $timeout(function () {
			angular.element('[ng-model="username"]').focus();
		});
        
		$scope.setLastActive = function() {
			ExpertByLogin.get({login: $scope.username}, function(expertResult) {
			// if user exist in the expert table
				if (expertResult.id != null) {
					ExpertUpdateLastDate.update(expertResult.id);
				}
			});
		};
		
		$scope.login = function (event) {
            event.preventDefault();
            Auth.login({
                username: $scope.username,
                password: $scope.password,
                rememberMe: $scope.rememberMe
            }).then(function () {
				// setLastActive if the user already a expert
				$scope.setLastActive();
                $scope.authenticationError = false;
                if ($rootScope.previousStateName === 'register') {
                    $state.go('home');
                } else {
                    $rootScope.back();
                }
            }).catch(function () {
                $scope.authenticationError = true;
            });
        };
    });
