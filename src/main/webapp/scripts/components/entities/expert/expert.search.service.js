'use strict';

angular.module('profinetApp')
    .factory('ExpertSearch', function ($resource) {
        return $resource('api/_search/experts/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
