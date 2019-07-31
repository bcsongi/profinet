'use strict';

angular.module('profinetApp')
    .factory('ExpertField', function ($resource, DateUtils) {
        return $resource('api/expertFields/:id', {}, {
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
	.factory('ExpertFieldsByUserLogin', function($resource, DateUtils) {
		return $resource('api/expertFieldsByUserLogin/:login', {}, {
            'get': {
                method: 'GET', isArray: true,
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.createdDate = DateUtils.convertDateTimeFromServer(data.createdDate);
                    return data;
                }
            }
        });
    });
	
angular.module('profinetApp')
	.factory('ExpertFieldIndex', function($resource, DateUtils) {
		return $resource('api/expertFields/:id', {}, {
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
	});
