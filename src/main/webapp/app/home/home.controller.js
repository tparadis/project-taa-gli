(function() {
    'use strict';

    angular
        .module('projectTaaGliApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state','Etudiant'];

    function HomeController ($scope, Principal, LoginService, $state,Etudiant) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;

        vm.etudiants = [];
         vm.loadAll = loadAll;

        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }

        loadAll();
                //load les étudiants
                function loadAll() {
                    Etudiant.query(function(result) {
                        vm.etudiants = result;
                    });
                }
    }
})();
