'use strict';

angular.module('profinetApp')
    .factory('LanguageSearch', function ($resource) {
        return $resource('api/_search/languages/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
