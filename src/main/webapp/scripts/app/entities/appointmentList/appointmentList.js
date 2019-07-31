'use strict';

angular.module('profinetApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('appointmentList', {
                parent: 'entity',
                url: '/appointmentList',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'profinetApp.appointment.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/appointmentList/appointmentsList.html',
                        controller: 'AppointmentListController'
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
            .state('appointmentList.detail', {
                parent: 'entity',
                url: '/appointmentList/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'profinetApp.appointment.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/appointmentList/appointmentList-detail.html',
                        controller: 'AppointmentListDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('appointment');
						$translatePartialLoader.addPart('appointmentStatus');
						$translatePartialLoader.addPart('userFeedback');
						return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Appointment', function($stateParams, Appointment) {
                        return Appointment.get({id : $stateParams.id});
                    }]
                }
            })
            .state('appointmentList.new', {
                parent: 'expertFields',
                url: '/newAppointment/{expertFieldId}',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/appointmentList/appointmentList-new.html',
                        controller: 'AppointmentListNewController',
                        size: 'lg',
						resolve: {
							translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
								$translatePartialLoader.addPart('appointment');
								$translatePartialLoader.addPart('appointmentStatus');
								return $translate.refresh();
							}],
                            entity: function () {
                                return {expertField: null, startingDate: null, endingDate: null, description: null, status: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('expertFields', null, { reload: true });
                    }, function() {
                        $state.go('expertFields');
                    })
                }]
            })
            .state('appointmentList.edit', {
                parent: 'appointmentList',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/appointmentList/appointmentList-dialog.html',
                        controller: 'AppointmentListDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Appointment', function(Appointment) {
                                return Appointment.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('appointmentList', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
			.state('appointmentList.rating', {
                parent: 'appointmentList',
                url: '/rating/{id}',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/appointmentList/appointmentRating.html',
                        controller: 'AppointmentRatingController',
                        size: 'lg',
                        resolve: {
                            entity: ['Appointment', function(Appointment) {
                                return Appointment.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('appointmentRating', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }],
				resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('userFeedback');
                        return $translate.refresh();
                    }]
                }
            });
    });
