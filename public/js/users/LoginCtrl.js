(function() {
    var LoginCtrl;

    LoginCtrl = (function() {
        function LoginCtrl($log, $location, $cookies, UserService) {
            this.$log = $log;
            this.$location = $location;
            this.$cookies = $cookies;
            this.UserService = UserService;
            this.$log.debug("constructing LoginController");
            this.username = "";
            this.password = "";
        }

        LoginCtrl.prototype.login = function() {
            this.$log.debug("login(); username:" + this.username + " password:" + this.password);
            return this.UserService.login(this.username, this.password).then((function(_this) {
                return function(data) {
                    _this.$log.debug("Promise returned " + data.session + " User");
                    _this.session = data;
                    _this.$cookies['session'] = data.session;
                    return _this.$location.path("/");
                };
            })(this), (function(_this) {
                return function(error) {
                    return _this.$log.error("Unable to login User: " + error);
                };
            })(this));
        };

        return LoginCtrl;

    })();

    controllersModule.controller('LoginCtrl', ['$log', '$location', '$cookies', 'UserService', LoginCtrl]);
}).call(this);