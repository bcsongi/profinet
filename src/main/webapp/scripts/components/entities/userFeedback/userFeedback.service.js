'use strict';

angular.module('profinetApp')
    .factory('UserFeedback', function ($resource, DateUtils) {
        return $resource('api/userFeedbacks/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.date = DateUtils.convertDateTimeFromServer(data.date);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    })
	.factory('UserFeedbackForExpertField', function ($resource, DateUtils) {
        return $resource('api/_search/userFeedbacksByExpertField/:expertFieldId', {}, {
            'get': { 
				method: 'GET', isArray: true 
				}
        });
    })
	.factory('UserFeedbackForCalcFormula', function ($resource, DateUtils) {
        return $resource('api/userFeedbacksRating/:rating&:comment&:appointmentid', {}, {
            'setExpertFieldByUserFeedback': {
					method: 'GET', isArray: true
				}
        });
    })
	.factory('UserFeedbackByAppointment', function ($resource, DateUtils) {
        return $resource('api/userFeedbackByAppointment/:id', {}, {
            'get': { method: 'GET' }
        });
    });
