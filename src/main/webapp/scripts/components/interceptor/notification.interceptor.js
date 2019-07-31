 'use strict';

angular.module('profinetApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-profinetApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-profinetApp-params')});
                }
                return response;
            },
        };
    });