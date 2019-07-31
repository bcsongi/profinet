'use strict';

angular.module('profinetApp')
    .factory('User', function ($resource) {
        return $resource('api/users/:login', {}, {
                'query': { method: 'GET', isArray: true },
                'get': {
                    method: 'GET',
                    transformResponse: function (data) {
                        data = angular.fromJson(data);
                        return data;
                    }
                }
            });
        })
	.factory('CheckUserPassword', function ($resource) {
        return $resource('api/checkUserPassword/:login&:password', {}, {
                'get': { method: 'GET', isArray: true }
            });
        });
