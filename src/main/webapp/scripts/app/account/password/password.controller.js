'use strict';

angular.module('profinetApp')
    .controller('PasswordController', function ($scope, Auth, Principal, CheckUserPassword) {
        Principal.identity().then(function(account) {
            $scope.account = account;
        });

        $scope.success = null;
        $scope.error = null;
        $scope.doNotMatch = null;
        
		$scope.changePassword = function () {
			CheckUserPassword.get({login: $scope.account.login, password: $scope.currentPassword}, function(result) {
				
			alert(result);
				if (result == 1) {
					alert("true");
				} else {
					alert("false");					
				}
				//alert(JSON.stringify(result)); 
				if ($scope.password !== $scope.confirmPassword) {
					$scope.doNotMatch = 'ERROR';
				} else {
					$scope.doNotMatch = null;
					Auth.changePassword($scope.password).then(function () {
						$scope.error = null;
						$scope.success = 'OK';
					}).catch(function () {
						$scope.success = null;
						$scope.error = 'ERROR';
					});
				}
			});
		};
    });
