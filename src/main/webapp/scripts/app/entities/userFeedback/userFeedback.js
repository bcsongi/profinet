'use strict';

angular.module('profinetApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('userFeedback', {
                parent: 'entity',
                url: '/userFeedbacks',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'profinetApp.userFeedback.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/userFeedback/userFeedbacks.html',
                        controller: 'UserFeedbackController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('userFeedback');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('userFeedback.detail', {
                parent: 'entity',
                url: '/userFeedback/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'profinetApp.userFeedback.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/userFeedback/userFeedback-detail.html',
                        controller: 'UserFeedbackDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('userFeedback');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'UserFeedback', function($stateParams, UserFeedback) {
                        return UserFeedback.get({id : $stateParams.id});
                    }]
                }
            })
            .state('userFeedback.new', {
                parent: 'userFeedback',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/userFeedback/userFeedback-dialog.html',
                        controller: 'UserFeedbackDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {comment: null, rating: null, date: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('userFeedback', null, { reload: true });
                    }, function() {
                        $state.go('userFeedback');
                    })
                }]
            })
            .state('userFeedback.edit', {
                parent: 'userFeedback',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/userFeedback/userFeedback-dialog.html',
                        controller: 'UserFeedbackDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['UserFeedback', function(UserFeedback) {
                                return UserFeedback.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('userFeedback', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
