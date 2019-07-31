'use strict';

angular.module('profinetApp')
    .factory('Expert', function ($resource, DateUtils) {
        return $resource('api/experts/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.lastActive = DateUtils.convertDateTimeFromServer(data.lastActive);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    })
	.factory('ExpertByLogin', function ($resource, DateUtils) {
        return $resource('api/expertByLogin/:login', {}, {
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.lastActive = DateUtils.convertDateTimeFromServer(data.lastActive);
                    return data;
                }
            }
        });
    })
	.factory('ExpertUpdateLastDate', function ($resource, DateUtils) {
        return $resource('api/expertUpdateLastDate', {}, {
            'update': { method: 'PUT' }
        });
    })
	.factory('GetExpertInstance', function ($resource, DateUtils) {
        return $resource('api/getExpertInstance', {}, {
            'get': { method: 'GET' }
        });
    });
