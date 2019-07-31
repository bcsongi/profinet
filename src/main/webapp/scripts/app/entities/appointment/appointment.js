'use strict';

angular.module('profinetApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('appointment', {
                parent: 'entity',
                url: '/appointments',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'profinetApp.appointment.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/appointment/appointments.html',
                        controller: 'AppointmentController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('appointment');
						$translatePartialLoader.addPart('appointmentStatus');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('appointment.detail', {
                parent: 'entity',
                url: '/appointment/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'profinetApp.appointment.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/appointment/appointment-detail.html',
                        controller: 'AppointmentDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('appointment');
						$translatePartialLoader.addPart('appointmentStatus');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Appointment', function($stateParams, Appointment) {
                        return Appointment.get({id : $stateParams.id});
                    }]
                }
            })
            .state('appointment.new', {
                parent: 'appointment',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/appointment/appointment-dialog.html',
                        controller: 'AppointmentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {startingDate: null, endingDate: null, description: null, status: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('appointment', null, { reload: true });
                    }, function() {
                        $state.go('appointment');
                    })
                }]
            })
            .state('appointment.edit', {
                parent: 'appointment',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/appointment/appointment-dialog.html',
                        controller: 'AppointmentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Appointment', function(Appointment) {
                                return Appointment.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('appointment', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
