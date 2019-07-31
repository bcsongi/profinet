'use strict';

angular.module('profinetApp')
    .factory('AppointmentSearch', function ($resource) {
        return $resource('api/_search/appointments/:query', {}, {
            'query': { method: 'GET', isArray: true }
        });
    })
	.factory('AppointmentSearchForUser', function($resource){
		return $resource('api/_search/appointmentsListForUser/:query', {}, {
			'query': { method: 'GET', isArray: true }
		});
	})
	.factory('AppointmentSearchForExpert', function($resource){
		return $resource('api/_search/appointmentsListForExpert/:query', {}, {
			'query': {method: 'GET', isArray: true}
		});
	});
