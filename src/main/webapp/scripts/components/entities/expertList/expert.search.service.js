'use strict';

angular.module('profinetApp')
    .factory('ExpertFieldListSearch', function ($resource) {
        return $resource('api/_search/expertFieldsActive/:query', {}, {
            'query': { method: 'GET', isArray: true}
		})
	})
	.factory('ExpertFieldListFilterByLanguage', function ($resource) {
        return $resource('api/_search/expertFieldsFilterByLanguage/:query', {}, {
            'query': { method: 'GET', isArray: true}
		})
	})
	.factory('ExpertFieldListFilterByCategory', function ($resource) {
        return $resource('api/_search/expertFieldsFilterByCategory/:query', {}, {
            'query': { method: 'GET', isArray: true}
		})
	})
	.factory('ExpertFieldListFilter', function($resource){
		return $resource('api/_search/expertFieldsFilter/:query1&:query2', {}, {
            'query': { method: 'GET', isArray: true}
		})
	});
