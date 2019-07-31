'use strict';

angular.module('profinetApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('expertField', {
                parent: 'entity',
                url: '/expertFields',
                data: {
                    //roles: ['ROLE_USER'],
                    pageTitle: 'profinetApp.expertField.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/expertField/expertFields.html',
                        controller: 'ExpertFieldController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('expertField');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('expertField.detail', {
                parent: 'entity',
                url: '/expertField/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'profinetApp.expertField.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/expertField/expertField-detail.html',
                        controller: 'ExpertFieldDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('expertField');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'ExpertField', function($stateParams, ExpertField) {
                        return ExpertField.get({id : $stateParams.id});
                    }]
                }
            })
            .state('expertField.new', {
                parent: 'expertField',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/expertField/expertField-dialog.html',
                        controller: 'ExpertFieldDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {description: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('expertField', null, { reload: true });
                    }, function() {
                        $state.go('expertField');
                    })
                }]
            })
            .state('expertField.edit', {
                parent: 'expertField',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/expertField/expertField-dialog.html',
                        controller: 'ExpertFieldDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ExpertField', function(ExpertField) {
                                return ExpertField.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('expertField', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
