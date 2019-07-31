'use strict';

angular.module('profinetApp')
    .controller('LanguageDetailController', function ($scope, $rootScope, $stateParams, entity, Language) {
        $scope.language = entity;
        $scope.load = function (id) {
            Language.get({id: id}, function(result) {
                $scope.language = result;
            });
        };
        $rootScope.$on('profinetApp:languageUpdate', function(event, result) {
            $scope.language = result;
        });
    });
