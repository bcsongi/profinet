'use strict';

angular.module('profinetApp')
    .controller('KeywordDetailController', function ($scope, $rootScope, $stateParams, entity, Keyword) {
        $scope.keyword = entity;
        $scope.load = function (id) {
            Keyword.get({id: id}, function(result) {
                $scope.keyword = result;
            });
        };
        $rootScope.$on('profinetApp:keywordUpdate', function(event, result) {
            $scope.keyword = result;
        });
    });
