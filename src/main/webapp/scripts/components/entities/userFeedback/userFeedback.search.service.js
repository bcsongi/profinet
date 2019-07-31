'use strict';

angular.module('profinetApp')
    .factory('UserFeedbackSearch', function ($resource) {
        return $resource('api/_search/userFeedbacks/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
