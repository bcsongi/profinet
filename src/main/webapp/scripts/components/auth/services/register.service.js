'use strict';

angular.module('profinetApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


