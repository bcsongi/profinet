'use strict';

angular.module('profinetApp')
    .factory('Keyword', function ($resource, DateUtils) {
        return $resource('api/keywords/:id', {}, {
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
	.factory('KeywordsByName', function($resource, DateUtils){
		return $resource('api/keywordsByName/:name', {}, {
			'get': { method: 'GET', isArray: true}
			});
	});
	