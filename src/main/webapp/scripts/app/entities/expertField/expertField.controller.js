'use strict';

angular.module('profinetApp')
    .controller('ExpertFieldController', function ($scope, ExpertField, ExpertFieldSearch, ParseLinks) {
        $scope.expertFields = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            ExpertField.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.expertFields = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            ExpertField.get({id: id}, function(result) {
                $scope.expertField = result;
                $('#deleteExpertFieldConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            ExpertField.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteExpertFieldConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            ExpertFieldSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.expertFields = result;
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
            $scope.expertField = {description: null, id: null};
        };
    });
