'use strict';

angular
    .module('tronApp', [
        'ngCookies',
        'ngResource',
        'ngRoute',
        'ngSanitize'
    ])
    .config(function ($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'scripts/app/views/home.html',
                controller: 'HomeController'
            })
            .when('/lobby', {
                templateUrl: 'scripts/app/views/lobby.html',
                controller: 'LobbyController'
            })
            .otherwise({
                redirectTo: "/"
            });
    });
