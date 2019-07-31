'use strict';

angular.module('profinetApp').controller('AppointmentRatingController', 
	['$scope', '$modalInstance', 'entity', '$stateParams', 'Appointment', 'ExpertField', 'UserFeedback', 'UserFeedbackForCalcFormula',
		function ($scope, $modalInstance, entity, $stateParams, Appointment, ExpertField, UserFeedback, UserFeedbackForCalcFormula) {
			
			$scope.appointmentID = $stateParams.id;			
			$scope.allUserfeedback = UserFeedback.query();
			$scope.rating = 0;
			$scope.ratings = [{
				current: 10,
				max: 10
			}];
			
			// get appointment object by parameter appointmentID
			Appointment.get({id : $scope.appointmentID}, function(result) {
                $scope.appointment = result;
			
				$scope.userFeedback = new UserFeedback();
				$scope.userFeedback.rating = 10;
			
				$scope.userFeedback.appointmentuserfeedback = $scope.appointment;
				// get expertField object by appointment expertFieldID
				ExpertField.get({id : $scope.appointment.expertField.id}, function(result) {
					$scope.expertfield = result;
				});	
			});

			//	set userFeedback			
			$scope.getSelectedRating = function (rating) {
				$scope.userFeedback.rating = rating;		
			}
			
			$scope.saveUserFeedback = function() {
				// save userFeedback & setExpertField
				UserFeedbackForCalcFormula.setExpertFieldByUserFeedback({rating : $scope.userFeedback.rating, comment: $scope.userFeedback.comment, appointmentid: $scope.appointment.id}, function(result) {
					$scope.expertfield.rating = result;
				});
				
				$scope.close();
			}
			
			$scope.close = function() {
				document.getElementById('ratingbtn').style.visibility = "hidden";
				$modalInstance.dismiss('cancel');
			}
		}
	]
)
angular.module('profinetApp').directive('starRating', function () {
    return {
        restrict: 'A',
        template: '<ul class="rating">' +
            '<li ng-repeat="star in stars" ng-class="star" ng-click="toggle($index)">' +
            '\u2605' +
            '</li>' +
            '</ul>',
        scope: {
            ratingValue: '=',
            max: '=',
            onRatingSelected: '&'
        },
        link: function (scope, elem, attrs) {

            var updateStars = function () {
                scope.stars = [];
                for (var i = 0; i < scope.max; i++) {
                    scope.stars.push({
                        filled: i < scope.ratingValue
                    });
                }
            };

            scope.toggle = function (index) {
                scope.ratingValue = index + 1;
                scope.onRatingSelected({
                    rating: index + 1
                });
            };

            scope.$watch('ratingValue', function (oldVal, newVal) {
                if (newVal) {
                    updateStars();
                }
            });
        }
    }
});