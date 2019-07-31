'use strict';

angular.module('profinetApp')
    .factory('ExpertContactRequest', function ($resource, DateUtils) {
        return $resource('api/expertContactRequests/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    })
	.factory('ExpertContactRequestBetween', function($resource){
		return $resource('api/_search/expertContactRequestsStatusBetween/:expertid&:userid',{},{
			'query': {method: 'GET', isArray: true}
		});
    })
	.factory('ExpertContactRequestTo', function($resource){
		return $resource('api/_search/expertContactRequestsTo/:expertid',{},{
			'query': {method: 'GET', isArray: true}
		});
    });
