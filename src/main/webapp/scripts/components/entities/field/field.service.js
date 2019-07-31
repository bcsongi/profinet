'use strict';

angular.module('profinetApp')
    .factory('Field', function ($resource, DateUtils) {
        return $resource('api/fields/:id', {}, {
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
	.factory('FieldsByExpertId', function ($resource, DateUtils) {
        return $resource('api/fieldsByExpertId/:id', {}, {
            'query': { method: 'GET', isArray: true}
		})
	});
