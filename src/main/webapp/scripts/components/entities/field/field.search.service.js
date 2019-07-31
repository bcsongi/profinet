'use strict';

angular.module('profinetApp')
    .factory('FieldSearch', function ($resource) {
        return $resource('api/_search/fields/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
