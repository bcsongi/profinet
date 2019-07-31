'use strict';

angular.module('profinetApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('language', {
                parent: 'entity',
                url: '/languages',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'profinetApp.language.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/language/languages.html',
                        controller: 'LanguageController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('language');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('language.detail', {
                parent: 'entity',
                url: '/language/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'profinetApp.language.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/language/language-detail.html',
                        controller: 'LanguageDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('language');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Language', function($stateParams, Language) {
                        return Language.get({id : $stateParams.id});
                    }]
                }
            })
            .state('language.new', {
                parent: 'language',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/language/language-dialog.html',
                        controller: 'LanguageDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {name: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('language', null, { reload: true });
                    }, function() {
                        $state.go('language');
                    })
                }]
            })
            .state('language.edit', {
                parent: 'language',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/language/language-dialog.html',
                        controller: 'LanguageDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Language', function(Language) {
                                return Language.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('language', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
