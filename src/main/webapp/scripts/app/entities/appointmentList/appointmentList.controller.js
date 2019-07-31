'use strict';

angular.module('profinetApp')
    .controller('AppointmentListController', function ($scope, Principal, Appointment, AppointmentsForUser, AppointmentSearch, AppointmentSearchForUser, ParseLinks) {
        $scope.appointments = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            AppointmentsForUser.query({}, function(result) {//({page: $scope.page, per_page: 20}, function(result, headers) {
                //$scope.links = ParseLinks.parse(headers('link'));
                $scope.appointments = result;
            });
        };
		
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Appointment.get({id: id}, function(result) {
                $scope.appointment = result;
                $('#deleteAppointmentConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Appointment.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteAppointmentConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            AppointmentSearchForUser.query({query: $scope.searchQuery}, function(result) {
                $scope.appointments = result;
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
            $scope.appointment = {startingDate: null, endingDate: null, description: null, status: null, id: null};
        };
    });
