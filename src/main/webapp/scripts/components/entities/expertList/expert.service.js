'use strict';

angular.module('profinetApp')
    .factory('ExpertFieldList', function ($resource, DateUtils) {
        return $resource('api/expertFieldsList', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.lastActive = DateUtils.convertDateTimeFromServer(data.lastActive);
                    return data;
                }
            }
        })
	})
	.factory('ExpertFieldByRating', function($resource){
		return $resource('api/expertFieldsByRating', {}, {
            'query': { method: 'GET', isArray: true}
		})
	});