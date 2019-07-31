'use strict';

var app = angular.module('profinetApp').controller('ProfileDialogController',
    ['$scope', '$stateParams', '$timeout', 'Expert', 'Field', 'Keyword', 'KeywordsByName', 'ExpertField', 'Language', 'User', 'Principal', 'ExpertByLogin', 'ExpertFieldsByUserLogin', 'FieldsByExpertId', 'GetExpertInstance',
        function($scope, $stateParams, $timeout, Expert, Field, Keyword, KeywordsByName, ExpertField, Language, User, Principal, ExpertByLogin, ExpertFieldsByUserLogin, FieldsByExpertId, GetExpertInstance) {
	
		$scope.nrOfGeoQuery = 0;
		$scope.selectedKeywords = [];

		$scope.load = function () {	
			// get actual login expert info
			ExpertByLogin.get({login: $scope.login}, function (result) {
				if (result != null) { 	
					$scope.exp = result;

					if (typeof $scope.exp.id == 'undefined') {				
						$scope.fields = Field.query();
						$scope.alreadyExpert = false;
						
						GetExpertInstance.get({}, function (result) {
							$scope.exp = result;
						});
					} else {
						if ($scope.exp.latitude != "" && $scope.exp.longitude != "") {
							$scope.placeMarker(new google.maps.LatLng($scope.exp.latitude, $scope.exp.longitude));
						}
						FieldsByExpertId.query({id: $scope.exp.id}, function (result) {
							$scope.fields = result;
						});
						$scope.alreadyExpert = true;
						
						$scope.selectedKeywords= $scope.exp.expertkeywords.slice(0);
						$scope.selectedKeywords.reverse();
					}
					
					//get actual user info
					User.get({login: $scope.account.login}, function(result) {
						if (result != null) { 	
							$scope.user = result;
							$scope.exp.expertuser = $scope.user;
						}
					});	
					$scope.myexpertfields = [];
					// load the own expertfields
					for (var i = 0; i < $scope.expertfields.length; i++) {
						if ($scope.expertfields[i].expert.id == $scope.exp.id) {
							$scope.myexpertfields.push($scope.expertfields[i]);
						}
					}
					$scope.myexpertfields.push(new ExpertField());	
				} 
			});
		};
		
		Principal.identity().then(function(account) {   
			$scope.account = account;
			$scope.login = $scope.account.login;
			ExpertFieldsByUserLogin.get({login: $scope.account.login}, function (result) {
				$scope.expertfields = result;
				
				$scope.load();
			});
		});
		
        $scope.languages = Language.query();
        $scope.users = User.query();
		$scope.keywords = Keyword.query();
		$scope.mynewfield = [];
		$scope.deletedFields = [];
		
		$scope.addNewExpertFieldBox = function () {
			var newItemNo = $scope.myexpertfields.length + 1;
			$scope.myexpertfields.push(new ExpertField());
		};
	
		$scope.addExpertField = function () {
			var lastItemOfExpertField = $scope.myexpertfields.length - 1;
			$scope.myexpertfields[lastItemOfExpertField].expert = $scope.exp;
			$scope.myexpertfields[lastItemOfExpertField].field = $scope.mynewfield;
			$scope.myexpertfields[lastItemOfExpertField].description = "";
			$scope.myexpertfields[lastItemOfExpertField].description = $scope.mynewdescription;
			
			// remove data from memory
			$scope.mynewfield = "";
			$scope.mynewdescription = "";
			
			var index = null;
			for (var i = 0; i < $scope.fields.length; i++) {
				if ($scope.fields[i].name == $scope.myexpertfields[lastItemOfExpertField].field.name) {
					index = i;
					break;
				}
			}
			if (index != null) {
				$scope.fields.splice(index, 1);
			}
			
			$scope.addNewExpertFieldBox();
		}
		
		$scope.removeExpertField = function (index) {
			var lastItemOfExpertField = $scope.myexpertfields.length - 1;
			$scope.obj = $scope.myexpertfields[index];
			if ($scope.obj.id != undefined) {
				$scope.deletedFields.push($scope.obj);
			}
			
			var obj = $scope.myexpertfields.splice(index, 1);
		};
		
        var onSaveFinished = function (result) {
				ExpertByLogin.get({login: $scope.login}, function (result) {
				if (result != null) { 	
					$scope.exp = result;
				}
			
				for (var i = 0; i < $scope.myexpertfields.length; ++i) {
					$scope.myexpertfields[i].expert = $scope.exp;
					if (typeof $scope.myexpertfields[i].id == 'undefined') {
						ExpertField.save($scope.myexpertfields[i]);
					} else {
						ExpertField.update($scope.myexpertfields[i]);
					}
				}
				
				for (var i = 0; i < $scope.deletedFields.length; ++i) {
						$scope.deletedFields[i].expert = $scope.exp;
						ExpertField.delete({id: $scope.deletedFields[i].id}, function () {
					});
				}
				
				setTimeout(function(){
					$scope.myexpertfields.push(new ExpertField());
				}, 1);
				location.reload();
			});
        };

        $scope.save = function () {
			// push last object
			var lastItemOfExp = $scope.myexpertfields.length - 1;
			$scope.myexpertfields.splice(lastItemOfExp);
			
			$scope.saveKeywords();
			alert($scope.exp.id);
			if (typeof $scope.exp.id == 'undefined' || $scope.exp.id == 'undefined' || $scope.exp.id == null) {
				Expert.save($scope.exp, onSaveFinished);
			} else {
				Expert.update($scope.exp, onSaveFinished);
			}
		};
		
		$scope.delete = function (id) {
            ExpertField.get({id: id}, function(result) {
                $scope.expertField = result;
                $('#deleteExpertFieldConfirmation').modal('show');
            });
        };

        $scope.arrayObjectIndexOf = function arrayObjectIndexOf(myArray, searchTerm) {
			if (myArray != null) { // angular loads the object in later, and the .length will give an error on an undefined object sometimes
				for(var i = 0, len = myArray.length; i < len; i++) {
					if (myArray[i].name === searchTerm)
						return i;
				}
			}
			return -1;
		}
		
		$scope.toggleSelection = function toggleSelection(language) {	
			var idx = $scope.arrayObjectIndexOf($scope.exp.expertlanguages, language.name);
			if (idx > -1) {			
				$scope.exp.expertlanguages.splice(idx, 1);
			} else {
				$scope.exp.expertlanguages.push(language);
			}
		};
		
		$scope.saveKeywords = function () {
			$scope.exp.expertkeywords = $scope.selectedKeywords.slice(0);
			$scope.exp.expertkeywords.reverse();
			
		};
		
		$scope.suggestions = [];

        $scope.selectedIndex = -1;

        $scope.removeTag = function (index) {
            $scope.selectedKeywords.splice(index,1);
        }

		$scope.getKeywordsByName = function (keywordname) {
			var keyword;

			for (var i = 0; i < $scope.keywords.length; i++) {
				if ($scope.keywords[i].name == keywordname) {
					keyword = $scope.keywords[i];
					break;
				}
			}
			
			return keyword;
		};
		
        $scope.search = function () {
			/*var changed = true;
			while (changed) {
				var olderText = $scope.searchText;
				$scope.searchText = olderText.replace(" ", "");
				if (olderText == $scope.searchText) {
					changed = false;
				}
			}*/
	
			if (/*$scope.searchText.indexOf(" ") === -1 && */$scope.searchText !== "") {
				KeywordsByName.get({name: $scope.searchText}, function (result) {
					if (result != null) { 	
						if($scope.arrayObjectIndexOf(result, $scope.searchText) === -1){
							result.unshift({id: null, name:$scope.searchText});
						}
						
						$scope.suggestions=result;
						$scope.selectedIndex=-1;
					} 		
				});
			}
		}
		
        $scope.addToSelectedTags = function(index) {
			if($scope.arrayObjectIndexOf($scope.exp.selectedkeywords, $scope.suggestions[index].name) === -1){

				if ($scope.suggestions[index].id != null) {
					$scope.suggestions[index] = $scope.getKeywordsByName($scope.suggestions[index].name);
				}
				
				$scope.selectedKeywords.push($scope.suggestions[index]);
				$scope.searchText='';
				$scope.suggestions=[];
			}
        }

        $scope.checkKeyDown = function (event) {
			if (event.keyCode === 40) {		// down arrow keyCode
				event.preventDefault();
				if ($scope.selectedIndex + 1 !== $scope.suggestions.length) {
					$scope.selectedIndex++;
				}
			}
			else if (event.keyCode === 38) {	// up arrow keyCode
				event.preventDefault();
				if ($scope.selectedIndex - 1 !== -1) {
					$scope.selectedIndex--;
				}
			}
			else if (event.keyCode === 13) {	// enter keyCode
				$scope.addToSelectedTags($scope.selectedIndex);
			}
        }

        $scope.$watch('selectedIndex', function(val) {
            if (val !== -1) {
                $scope.searchText = $scope.suggestions[$scope.selectedIndex].name;
            }
        });
		
		var onSaveFinishedKeyword = function (result) {
            $scope.$emit('profinetApp:keywordUpdate', result);
        };
		
		//	GOOGLE MAPS
		$timeout(function () {
            var latlng = new google.maps.LatLng(46.75361923240725, 23.568192422389984);

            var myOptions = {
                zoom: 10,
                center: latlng,
                mapTypeId: google.maps.MapTypeId.ROADMAP
            };

            $scope.map = new google.maps.Map(document.getElementById("googleMap"), myOptions);
			
            document.getElementById("googleMap").setAttribute("on-click", "placeMarker()");
            $scope.overlay = new google.maps.OverlayView();
            $scope.overlay.draw = function() {};
            $scope.overlay.setMap($scope.map);
            $scope.element = document.getElementById('googleMap');

            $scope.hammertime = Hammer($scope.element).on("hold", function(event) {
                $scope.addOnClick(event);
            });

            if (navigator.geolocation) {
                 navigator.geolocation.getCurrentPosition(function (position) {
                     $scope.map.setCenter(new google.maps.LatLng(position.coords.latitude, position.coords.longitude));
                 });
            }

            google.maps.event.addListener($scope.map, "click", function(event) {				
				$scope.nrOfGeoQuery += 1;
				
				$scope.reverseGeocodingService(event.latLng);
				$scope.setModelAndPlaceMarker(event.latLng);
                $scope.setModelAndPlaceMarker(event.latLng);
				
            });
			
			$scope.setModelAndPlaceMarker = function (location) {
				var latitude = document.getElementById("field_latitude");
                var longitude = document.getElementById("field_longitude");
				latitude.value = location.lat();
				$scope.exp.latitude = location.lat();
                longitude.value = location.lng();
				$scope.exp.longitude = location.lng();
				
                $scope.placeMarker(location);
			};
			
			var marker = null;
			var infowindow = null;
			$scope.placeMarker = function (location) {
				if (marker == null || marker == 'undefined') {
					marker = new google.maps.Marker({
						position: location,
						map: $scope.map,
					});
				} else {
					marker.setMap(null);
					infowindow.close();
					
					marker = new google.maps.Marker({
						position: location,
						map: $scope.map,
					});
				}
				
				infowindow = new google.maps.InfoWindow({
					content: $scope.exp.country
				});
				
				if ($scope.exp.regio != null && $scope.exp.regio != "undefined") {
					infowindow.setContent($scope.exp.regio + ", " + infowindow.getContent());
				}
				if ($scope.exp.locality != null && $scope.exp.locality != "undefined") {
					infowindow.setContent($scope.exp.locality + ", " + infowindow.getContent());
				}
				if ($scope.exp.street_number != null && $scope.exp.street_number != "undefined") {
					infowindow.setContent($scope.exp.street_number + ", " + infowindow.getContent());
				} 
				if ($scope.exp.route != null && $scope.exp.route != "undefined") {
					if ($scope.exp.street_number == null && $scope.exp.street_number == "undefined") {
						infowindow.setContent($scope.exp.route + ", " + infowindow.getContent());
					} else {
						infowindow.setContent($scope.exp.route + " " + infowindow.getContent());
					}
				} 
				
				var searchBox = document.getElementById("placesearch-input");
				searchBox.value = infowindow.getContent();
				infowindow.open($scope.map, marker);
			};
		
			// GOOGLE place search box	
			var input = document.getElementById("placesearch-input");
			$scope.searchBox = new google.maps.places.SearchBox((input));
			
			$scope.map.addListener('bounds_changed', function () {
				$scope.searchBox.setBounds($scope.map.getBounds());
			});

			$scope.searchBox.addListener('places_changed', function () {
				var places = $scope.searchBox.getPlaces();
				
				if (places.length == 0) {
					return;
				} 
				
				$scope.geocodingService(places[0].formatted_address);
				$scope.setModelAndPlaceMarker(places[0].geometry.location);
				$scope.map.setCenter(places[0].geometry.location);
			});

			$scope.geocodingService = function (formatted_address, location) {
				var geocoder = new google.maps.Geocoder();
				geocoder.geocode( { 'address': formatted_address}, function (results, status) {
					if (status == google.maps.GeocoderStatus.OK) {
						$scope.exp.street_number = null;
						$scope.exp.route = null;
						$scope.exp.locality = null;
						$scope.exp.regio = null;
						$scope.exp.country = "";
						
						for (var i = 0; i < results[0].address_components.length; i++) {
							if (results[0].address_components[i].types[0] == "street_number") {
								$scope.exp.street_number = results[0].address_components[i].long_name;
							} else if (results[0].address_components[i].types[0] == "route") {
								$scope.exp.route = results[0].address_components[i].long_name;
							} else if (results[0].address_components[i].types[0] == "locality") {
								$scope.exp.locality = results[0].address_components[i].long_name;
							}  else if (results[0].address_components[i].types[0] == "administrative_area_level_1") {
								$scope.exp.regio = results[0].address_components[i].long_name;
							} else if (results[0].address_components[i].types[0] == "country") {
								$scope.exp.country = results[0].address_components[i].long_name;
							}
						}
					} else {
						alert(status);
					}
				});
				
				$scope.nrOfGeoQuery -= 1;
			};
			
			$scope.reverseGeocodingService = function (location) {
				setTimeout(function () {
					var geocoder = new google.maps.Geocoder();
					geocoder.geocode({ 'location': location }, function (results, status) {
						if (status !== google.maps.GeocoderStatus.OK) {
							alert(status);
							return;
						}
						$scope.geocodingService(results[0].formatted_address);
					});
					
				} , $scope.nrOfGeoQuery * 1000);
			};
		}, 0);
	}])
