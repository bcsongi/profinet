'use strict';

angular.module('profinetApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('expertContactRequest', {
                parent: 'entity',
                url: '/expertContactRequests',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'profinetApp.expertContactRequest.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/expertContactRequest/expertContactRequests.html',
                        controller: 'ExpertContactRequestController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('expertContactRequest');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('expertContactRequest.detail', {
                parent: 'entity',
                url: '/expertContactRequest/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'profinetApp.expertContactRequest.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/expertContactRequest/expertContactRequest-detail.html',
                        controller: 'ExpertContactRequestDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('expertContactRequest');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'ExpertContactRequest', function($stateParams, ExpertContactRequest) {
                        return ExpertContactRequest.get({id : $stateParams.id});
                    }]
                }
            })
            .state('expertContactRequest.new', {
                parent: 'expertContactRequest',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/expertContactRequest/expertContactRequest-dialog.html',
                        controller: 'ExpertContactRequestDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {approved: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('expertContactRequest', null, { reload: true });
                    }, function() {
                        $state.go('expertContactRequest');
                    })
                }]
            })
            .state('expertContactRequest.edit', {
                parent: 'expertContactRequest',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/expertContactRequest/expertContactRequest-dialog.html',
                        controller: 'ExpertContactRequestDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ExpertContactRequest', function(ExpertContactRequest) {
                                return ExpertContactRequest.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('expertContactRequest', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
