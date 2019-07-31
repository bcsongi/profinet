'use strict';

angular.module('profinetApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('expertFields', {
                parent: 'entity',
                url: '/',
                data: {
                    pageTitle: 'profinetApp.expertField.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/expertList/experts.html',
                        controller: 'OutsideExpertFieldController'
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
			.state('expertFields.detail', {
                parent: 'entity',
                url: '/expertDetailed/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'profinetApp.expert.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/expertList/expert-detail.html',
                        controller: 'OutsideExpertDetailController'
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
    });
