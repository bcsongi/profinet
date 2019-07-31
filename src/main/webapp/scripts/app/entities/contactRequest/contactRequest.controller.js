'use strict';

angular.module('profinetApp')
    .controller('ContactRequestController', function ($scope, ExpertContactRequest, ExpertContactRequestTo, ExpertContactRequestSearch, ParseLinks, Auth, Principal, User, ExpertByLogin) {
        $scope.contactRequests = [];
        $scope.page = 1;
		$scope.user = new User();
		
		$scope.loadAll = function(){
			Principal.identity(true).then(function(account) {    
				$scope.account = account;
				$scope.id = $scope.account.login;	
				// get actual login expert info
				ExpertByLogin.get({login: account.login}, function(result) {
					if (result != null) { 	
						$scope.expert = result;
						ExpertContactRequestTo.query({page: $scope.page, per_page: 20, expertid: $scope.expert.id}, function(result, headers) {
							$scope.links = ParseLinks.parse(headers('link'));
							$scope.contactRequests = result;
						});
					} 
				});		
			});
		};
		
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };

        $scope.loadAll();

        $scope.search = function () {
            ExpertContactRequestSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.expertContactRequests = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

		$scope.save = function(expertContactRequest){
			ExpertContactRequest.update(expertContactRequest);
		};
		
		$scope.approve = function(expertContactRequest){
			expertContactRequest.approved = true;
			$scope.save(expertContactRequest);
		};
		
		$scope.refuse = function(expertContactRequest){
			expertContactRequest.approved = false;
			$scope.save(expertContactRequest);
		};
		
    });
