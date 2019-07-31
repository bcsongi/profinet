'use strict';

angular.module('profinetApp')
    .factory('ExpertContactRequestSearch', function ($resource) {
        return $resource('api/_search/expertContactRequests/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
