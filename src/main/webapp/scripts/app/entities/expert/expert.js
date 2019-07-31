'use strict';

angular.module('profinetApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('expert', {
                parent: 'entity',
                url: '/experts',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'profinetApp.expert.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/expert/experts.html',
                        controller: 'ExpertController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('expert');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('expert.detail', {
                parent: 'entity',
                url: '/expert/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'profinetApp.expert.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/expert/expert-detail.html',
                        controller: 'ExpertDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('expert');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Expert', function($stateParams, Expert) {
                        return Expert.get({id : $stateParams.id});
                    }]
                }
            })
            .state('expert.new', {
                parent: 'expert',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/expert/expert-dialog.html',
                        controller: 'ExpertDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {expertuser: null, timetable: null, phoneNumber: null, lastActive: null, latitude: null, longitude: null, active: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('expert', null, { reload: true });
                    }, function() {
                        $state.go('expert');
                    })
                }]
            })
            .state('expert.edit', {
                parent: 'expert',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/expert/expert-dialog.html',
                        controller: 'ExpertDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Expert', function(Expert) {
                                return Expert.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('expert', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
