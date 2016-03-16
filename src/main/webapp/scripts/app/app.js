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
                controller: 'HomeController',
                controllerAs: 'main'
            })
            .otherwise({
                redirectTo: "/"
            });
    });
