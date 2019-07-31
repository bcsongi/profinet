'use strict';

angular.module('profinetApp')
    .controller('FieldController', function ($scope, Field, FieldSearch, ParseLinks) {
        $scope.fields = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Field.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.fields = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Field.get({id: id}, function(result) {
                $scope.field = result;
                $('#deleteFieldConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Field.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteFieldConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            FieldSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.fields = result;
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
            $scope.field = {name: null, id: null};
        };
    });
