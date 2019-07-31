'use strict';

angular.module('profinetApp').controller('LanguageDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Language',
        function($scope, $stateParams, $modalInstance, entity, Language) {

        $scope.language = entity;
        $scope.load = function(id) {
            Language.get({id : id}, function(result) {
                $scope.language = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('profinetApp:languageUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.language.id != null) {
                Language.update($scope.language, onSaveFinished);
            } else {
                Language.save($scope.language, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
