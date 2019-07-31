'use strict';

angular.module('profinetApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('appointmentListForExpert', {
                parent: 'entity',
                url: '/appointmentListForExpert',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'profinetApp.appointment.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/appointmentListForExpert/appointmentsListForExpert.html',
                        controller: 'AppointmentListForExpertController'
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
            .state('appointmentListForExpert.detail', {
                parent: 'entity',
                url: '/appointmentListForExpert/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'profinetApp.appointment.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/appointmentListForExpert/appointmentListForExpert-detail.html',
                        controller: 'AppointmentListForExpertDetailController'
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
            .state('appointmentListForExpert.new', {
                parent: 'appointmentListForExpert',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/appointmentListForExpert/appointmentListForExpert-dialog.html',
                        controller: 'AppointmentListForExpertDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {startingDate: null, endingDate: null, description: null, status: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('appointmentListForExpert', null, { reload: true });
                    }, function() {
                        $state.go('appointmentListForExpert');
                    })
                }]
            })
            .state('appointmentListForExpert.edit', {
                parent: 'appointmentListForExpert',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/appointmentListForExpert/appointmentListForExpert-dialog.html',
                        controller: 'AppointmentListForExpertDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Appointment', function(Appointment) {
                                return Appointment.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('appointmentListForExpert', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
