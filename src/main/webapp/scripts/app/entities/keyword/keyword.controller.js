'use strict';

angular.module('profinetApp')
    .controller('KeywordController', function ($scope, Keyword, KeywordSearch, ParseLinks) {
        $scope.keywords = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Keyword.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.keywords = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Keyword.get({id: id}, function(result) {
                $scope.keyword = result;
                $('#deleteKeywordConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Keyword.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteKeywordConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            KeywordSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.keywords = result;
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
            $scope.keyword = {name: null, id: null};
        };
    });
