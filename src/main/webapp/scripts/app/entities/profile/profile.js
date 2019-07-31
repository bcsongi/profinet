'use strict';

angular.module('profinetApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('profile', {
                parent: 'entity',
                url: '/profiles',
                data: {
                    roles: ['ROLE_USER'],
					
                },
                views:{
					'content@':{
                        templateUrl: 'scripts/app/entities/profile/profiles.html',
                        controller: 'ProfileDialogController'
					}
				},
				resolve: {
					translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
						$translatePartialLoader.addPart('expert');
						$translatePartialLoader.addPart('global');
						return $translate.refresh();
					}],
                }
            }
            
            );
    });
