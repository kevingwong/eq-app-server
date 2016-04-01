(function() {
    var app, dependencies;

    dependencies = ['ngRoute', 'ngCookies', 'ui.bootstrap',
        'myApp.filters',
        'myApp.services',
        'myApp.controllers',
        'myApp.directives',
        'myApp.common',
        'myApp.routeConfig'];

    app = angular.module('myApp', dependencies);

    angular.module('myApp.routeConfig', ['ngRoute', 'ngCookies']).config([
        '$routeProvider', function($routeProvider) {
            return $routeProvider.when('/', {
                templateUrl: '/assets/partials/view.html'
            }).when('/login', {
                templateUrl: '/assets/partials/login.html'
            }).when('/users/create', {
                templateUrl: '/assets/partials/create.html'
            }).when('/users/edit/:firstName/:lastName', {
                templateUrl: '/assets/partials/update.html'
            }).otherwise({
                redirectTo: '/'
            });
        }
    ]).config([
        '$locationProvider', function($locationProvider) {
            return $locationProvider.html5Mode({
                enabled: true,
                requireBase: false
            });
        }
    ]);

    this.commonModule = angular.module('myApp.common', []);
    this.controllersModule = angular.module('myApp.controllers', ['ngCookies']);
    this.servicesModule = angular.module('myApp.services', []);
    this.modelsModule = angular.module('myApp.models', []);
    this.directivesModule = angular.module('myApp.directives', []);
    this.filtersModule = angular.module('myApp.filters', []);

}).call(this);