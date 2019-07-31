'use strict';

angular.module('profinetApp')
    .factory('Appointment', function ($resource, DateUtils) {
        return $resource('api/appointments/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.startingDate = DateUtils.convertDateTimeFromServer(data.startingDate);
                    data.endingDate = DateUtils.convertDateTimeFromServer(data.endingDate);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    })
	.factory('AppointmentsForUser', function ($resource, DateUtils) {
        return $resource('api/appointmentsListForUser', {}, {
            'query': { method: 'GET', isArray: true},
        });
    })
	.factory('AppointmentsForExpert', function($resource, DateUtils) {
		return $resource('api/appointmentsListForExpert',{},{
			'query': {method: 'GET', isArray: true},
		});
	});
