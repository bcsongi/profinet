'use strict';

angular.module('profinetApp')
    .factory('KeywordSearch', function ($resource) {
        return $resource('api/_search/keywords/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
