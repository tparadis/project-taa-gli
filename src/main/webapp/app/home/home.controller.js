(function() {
    'use strict';

    angular
        .module('projectTaaGliApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state','Etudiant', 'PartenaireUser'];

    function HomeController ($scope, Principal, LoginService, $state,Etudiant, PartenaireUser) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.user = null;
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
        
        function verifUser() {
        	if(vm.account != null){
        		vm.partenaire = PartenaireUser.get({id : vm.account.login});
        	}
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
