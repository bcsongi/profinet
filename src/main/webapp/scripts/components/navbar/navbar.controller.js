'use strict';

angular.module('profinetApp')
    .controller('NavbarController', function ($scope, $location, $state, Auth, Principal, ExpertByLogin) {
        $scope.isAuthenticated = Principal.isAuthenticated;
        $scope.$state = $state;

        $scope.logout = function () {
            Auth.logout();
            $state.go('home');
        };
		
//		$scope.checkRole = function(){
//			$scope.isExpert = false;
//			Principal.identity(true).then(function(account){
//				ExpertByLogin.get({login: account.login}, function(result){
//					if (result.id !== undefined){
//						$scope.isExpert = true;
//					}
//				});
//			});
//		}

//		$scope.checkRole();
    });
