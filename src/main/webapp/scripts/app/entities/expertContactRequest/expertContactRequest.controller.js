'use strict';

angular.module('profinetApp')
    .controller('ExpertContactRequestController', function ($scope, ExpertContactRequest, ExpertContactRequestSearch, ParseLinks) {
        $scope.expertContactRequests = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            ExpertContactRequest.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.expertContactRequests = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            ExpertContactRequest.get({id: id}, function(result) {
                $scope.expertContactRequest = result;
                $('#deleteExpertContactRequestConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            ExpertContactRequest.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteExpertContactRequestConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            ExpertContactRequestSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.expertContactRequests = result;
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

        $scope.clear = function () {
            $scope.expertContactRequest = {approved: null, id: null};
        };
		
    });
