'use strict';

angular.module('profinetApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('contactRequests', {
                parent: 'entity',
                url: '/contactRequest',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'profinetApp.expertContactRequest.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/contactRequest/contactRequest.html',
                        controller: 'ContactRequestController'
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
            .state('contactRequest.detail', {
                parent: 'entity',
                url: '/contactRequest/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'profinetApp.expertContactRequest.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/contactRequest/contactRequest-detail.html',
                        controller: 'ContactRequestDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('expertContactRequest');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'ContactRequest', function($stateParams, ContactRequest) {
                        return ContactRequest.get({id : $stateParams.id});
                    }]
                }
            })
            .state('contactRequest.edit', {
                parent: 'contactRequest',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/contactRequest/contactRequest-dialog.html',
                        controller: 'ContactRequestDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ExpertContactRequest', function(ExpertContactRequest) {
                                return ExpertContactRequest.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('contactRequest', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
