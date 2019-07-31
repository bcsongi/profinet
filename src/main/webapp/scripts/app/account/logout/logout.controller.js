'use strict';

angular.module('profinetApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
