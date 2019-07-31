'use strict';

angular.module('profinetApp')
    .factory('ExpertFieldSearch', function ($resource) {
        return $resource('api/_search/expertFields/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
