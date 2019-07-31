'use strict';

angular.module('profinetApp')
    .controller('OutsideExpertFieldController', 
	function ($scope, $timeout, Expert, ExpertFieldList, ExpertFieldListFilter, ExpertFieldListSearch, ExpertFieldListFilterByLanguage, 
		ExpertFieldListFilterByCategory, Field, Language, ParseLinks, Auth, Principal, ExpertContactRequestBetween, 
		ExpertField, ExpertContactRequest, User, UserFeedback, UserFeedbackForExpertField, ExpertFieldByRating) {
		
		$scope.approved= null;
		$scope.isAuthenticated = Principal.isAuthenticated;
        $scope.expertfields = [];
		$scope.approved={};
		$scope.existsRequest = {};
        $scope.page = 1;
		$scope.ratings = [];
		$scope.loadmore = [];
		var ratingsPage = [];
		$scope.markers = [];
		
		$timeout(function () {
			$scope.loadAll = function () {
				ExpertFieldByRating.query({}, function (result) {
					$scope.expertfields = result;
					
					for (var i = 0; i < $scope.expertfields.length; i++) {
						$scope.placeMarker(new google.maps.LatLng($scope.expertfields[i].expert.latitude, $scope.expertfields[i].expert.longitude), $scope.expertfields[i]);
					}
				});
				
				Field.query({}, function(result, headers) {
					$scope.fields =  result;
				});
				Language.query({}, function(result, headers) {
					$scope.languages = result;
				});
			};

			$scope.loadAll();
			
			// GOOGLE MAP	
		    var latlng = new google.maps.LatLng(46.75361923240725, 23.568192422389984);

            var myOptions = {
                zoom: 10,
                center: latlng,
                mapTypeId: google.maps.MapTypeId.ROADMAP
            };

            $scope.map = new google.maps.Map(document.getElementById("map"), myOptions);
			
			var marker = null;
			var infowindow = null;
			
			$scope.placeMarker = function (location, expertfield) {
				var indexes = $scope.existsLogin(expertfield.expert.expertuser.login);
				var fieldnames = "";
				if (indexes.length != 0) {
					fieldnames = $scope.markers[indexes[indexes.length - 1]].fieldnames + ", ";;
				}
				fieldnames = fieldnames + "<a onclick=\"location.href='#EF" + expertfield.id + "'\">" + expertfield.field.name + "</a>";
				
				for (var i = 0; i < indexes.length; i++) {	
					$scope.clearMarkersByIndex(indexes[i]);
				}
				$scope.createMarkerAndInfowindow(location, expertfield, fieldnames);
			};
			
			$scope.createMarkerAndInfowindow = function(location, expertfield, fieldnames) {
				marker = new google.maps.Marker({
					position: location,
					map: $scope.map,
					id: expertfield.id,
					login: expertfield.expert.expertuser.login,
					fieldnames: fieldnames,
				});
				
				infowindow = new google.maps.InfoWindow({
					content: "<table style='width:100%'> " + 
							"<tr> <td>Username:</td> <td>" + expertfield.expert.expertuser.login + "</td> </tr>" +
							"<tr> <td>Fields:</td> <td>" + fieldnames + "</td>" + 
							"</tr> </table>"
				});
			
				infowindow.open($scope.map, marker);
				$scope.markers[$scope.markers.length] = marker;
			};
					
			$scope.existsLogin = function (login) {
				var indexes = [];
				for (var i = 0; i < $scope.markers.length; i++) {
					if ($scope.markers[i].login == login) {
						indexes.push(i);
					}
				}
				return indexes;
			};
			
			$scope.clearMarkers = function() {
				for(var i=0; i < $scope.markers.length; i++){
					$scope.markers[i].setMap(null);
				}
				$scope.markers = [];
			};
		
			$scope.clearMarkersByIndex = function(index) {
				$scope.markers[index].setMap(null);
			};
			
        }, 0);
		
		//	~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		$scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        
		$scope.filterByField = function () {
            ExpertFieldListFilterByCategory.query({query: $scope.fieldfilter.name}, function(result) {
                $scope.expertfields = result;
				$scope.approved=[];
				$scope.existsRequest = [];
				
				$scope.clearMarkers();
				for (var i = 0; i < $scope.expertfields.length; i++) {
					$scope.placeMarker(new google.maps.LatLng($scope.expertfields[i].expert.latitude, $scope.expertfields[i].expert.longitude), $scope.expertfields[i]);
				}
            }, function (response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };
		
		$scope.filterByLanguage = function () {
			ExpertFieldListFilterByLanguage.query({query: $scope.languagefilter.name}, function (result) {
                $scope.expertfields = result;
				$scope.approved = [];
				$scope.existsRequest = [];
				
				$scope.clearMarkers();
				for (var i = 0; i < $scope.expertfields.length; i++) {
					$scope.placeMarker(new google.maps.LatLng($scope.expertfields[i].expert.latitude, $scope.expertfields[i].expert.longitude), $scope.expertfields[i]);
				}
            }, function (response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };
		
        $scope.search = function () {
            ExpertFieldListSearch.query({query: $scope.searchQuery}, function (result) {
                $scope.expertfields = result;
				$scope.approved = [];
				$scope.existsRequest = [];
				
				$scope.clearMarkers();
				for (var i = 0; i < $scope.expertfields.length; i++) {
					$scope.placeMarker(new google.maps.LatLng($scope.expertfields[i].expert.latitude, $scope.expertfields[i].expert.longitude), $scope.expertfields[i]);
				}
				$scope.scroll();
			}, function (response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };
		
		$scope.filter = function () {
			if (!($scope.fieldfilter === null || $scope.languagefilter === null)) {
				ExpertFieldListFilter.query({query1: $scope.fieldfilter.name, query2: $scope.languagefilter.name}, function (result) {
					$scope.expertfields = result;
					$scope.approved = [];
					$scope.existsRequest = [];
					
					$scope.clearMarkers();
					for (var i = 0; i < $scope.expertfields.length; i++) {
						$scope.placeMarker(new google.maps.LatLng($scope.expertfields[i].expert.latitude, $scope.expertfields[i].expert.longitude), $scope.expertfields[i]);
					}
				}, function (response) {
					if(response.status === 404) {
						$scope.loadAll();
					}
				});
			};
			$scope.scroll();
		};
		
		$scope.sortByRating = function () {
			ExpertFieldByRating.query({}, function (result) {
				$scope.expertfields = result;
				$scope.approved = [];
				$scope.existsRequest = [];
			});
		};
		
		$scope.isApproved = function (offer){	
			Principal.identity(true).then(function (account) {
				User.get({login: account.login}, function (result) {
					if (result != null) { 	
						$scope.user = result;
						$scope.approved[offer.id] = false;
						$scope.existsRequest[offer.id] = false;
						ExpertContactRequestBetween.query({expertid: offer.expert.id, userid: $scope.user.login}, function (result) {
							if (result.length !== 0){
								$scope.approved[offer.id] = result[0].approved;
								$scope.existsRequest[offer.id] = true;					
							} else {
								$scope.existsRequest[offer.id] = false;
							}
						});
					}
				});	
			});
		};
		
		$scope.sendRequest = function (offer) {
			var expertContactRequest = new ExpertContactRequest();
			expertContactRequest.userRequest = $scope.user;
			expertContactRequest.expertrequest = offer.expert;
			ExpertContactRequest.save(expertContactRequest);
			$scope.approved[offer.id] = null;
			$scope.existsRequest[offer.id] = true;
		};
		
		$scope.loadRatings = function (offerId, index) {
			document.getElementsByClassName("review")[index].style.visibility = "hidden";
			$scope.ratings[offerId] = [];
			ratingsPage[offerId] = 0;
			
			ratingsPage[offerId]++;
			UserFeedbackForExpertField.get({page: ratingsPage[offerId], per_page: 10, expertFieldId : offerId}, function (result) {
				if (result != null) {
					$scope.ratings[offerId] = $scope.ratings[offerId].concat(result);
					if (result.length <= 10) {
						$scope.loadmore[offerId] = false;
					} else {
						$scope.loadmore[offerId] = true;
					};
				};
			});
		};
		
		$scope.hideRatings = function (offerId, index) {
			document.getElementsByClassName("review")[index].style.visibility = "initial";	
			$scope.ratings[offerId] = null;
		};
})

angular.module('profinetApp').directive('starRating2', function () {
    return {
        restrict: 'A',
        template: '<ul class="rating">' +
            '<li ng-repeat="star in stars" ng-class="star">' +
            '\u2605' +
            '</li>' +
            '</ul>',
        scope: {
            ratingValue: '=',
            max: '='
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

            scope.$watch('ratingValue', function (oldVal, newVal) {
                if (newVal) {
                    updateStars();
                }
            });
        }
    }
});

