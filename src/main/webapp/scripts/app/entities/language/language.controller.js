'use strict';

angular.module('profinetApp')
    .controller('LanguageController', function ($scope, Language, LanguageSearch, ParseLinks) {
        $scope.languages = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Language.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.languages = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Language.get({id: id}, function(result) {
                $scope.language = result;
                $('#deleteLanguageConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Language.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteLanguageConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            LanguageSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.languages = result;
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
            $scope.language = {name: null, id: null};
        };
    });
