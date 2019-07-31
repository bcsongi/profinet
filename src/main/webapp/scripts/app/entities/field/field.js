'use strict';

angular.module('profinetApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('field', {
                parent: 'entity',
                url: '/fields',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'profinetApp.field.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/field/fields.html',
                        controller: 'FieldController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('field');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('field.detail', {
                parent: 'entity',
                url: '/field/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'profinetApp.field.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/field/field-detail.html',
                        controller: 'FieldDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('field');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Field', function($stateParams, Field) {
                        return Field.get({id : $stateParams.id});
                    }]
                }
            })
            .state('field.new', {
                parent: 'field',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/field/field-dialog.html',
                        controller: 'FieldDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {name: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('field', null, { reload: true });
                    }, function() {
                        $state.go('field');
                    })
                }]
            })
            .state('field.edit', {
                parent: 'field',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/field/field-dialog.html',
                        controller: 'FieldDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Field', function(Field) {
                                return Field.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('field', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
