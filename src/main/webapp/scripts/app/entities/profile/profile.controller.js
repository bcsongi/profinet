 'use strict';

angular.module('profinetApp')
    .controller('ProfileController', function ($scope, Expert, ExpertSearch, ParseLinks) {
        $scope.experts = [];
        $scope.page = 1;
		$scope.getExpert = [];
        $scope.loadAll = function() {
            Expert.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.experts = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Expert.get({id: id}, function(result) {
                $scope.expert = result;
                $('#deleteExpertConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Expert.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteExpertConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            ExpertSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.experts = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };
		
    });

