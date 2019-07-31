'use strict';

angular.module('profinetApp').controller('ExpertDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Expert', 'Field', 'Keyword', 'Language', 'User',
        function($scope, $stateParams, $modalInstance, entity, Expert, Field, Keyword, Language, User) {

        $scope.expert = entity;
        $scope.fields = Field.query();
        $scope.keywords = Keyword.query();
        $scope.languages = Language.query();
        $scope.users = User.query();
        $scope.load = function(id) {
            Expert.get({id : id}, function(result) {
                $scope.expert = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('profinetApp:expertUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.expert.id != null) {
                Expert.update($scope.expert, onSaveFinished);
            } else {
                Expert.save($scope.expert, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
