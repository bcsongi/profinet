'use strict';

angular.module('profinetApp')
    .factory('Language', function ($resource, DateUtils) {
        return $resource('api/languages/:id', {}, {
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
