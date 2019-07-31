'use strict';

angular.module('profinetApp')
    .controller('UserFeedbackController', function ($scope, UserFeedback, UserFeedbackSearch, ParseLinks) {
        $scope.userFeedbacks = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            UserFeedback.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.userFeedbacks = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            UserFeedback.get({id: id}, function(result) {
                $scope.userFeedback = result;
                $('#deleteUserFeedbackConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            UserFeedback.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteUserFeedbackConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            UserFeedbackSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.userFeedbacks = result;
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
            $scope.userFeedback = {comment: null, rating: null, date: null, id: null};
        };
    });
