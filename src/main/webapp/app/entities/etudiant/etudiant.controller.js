(function() {
    'use strict';

    angular
        .module('projectTaaGliApp')
        .controller('EtudiantController', EtudiantController);

    EtudiantController.$inject = ['$scope','Principal', '$state', 'Etudiant', 'EtudiantSearch'];

    function EtudiantController ($scope,Principal, $state, Etudiant, EtudiantSearch) {
        var vm = this;

        vm.etudiants = [];
        vm.search = search;
        vm.loadAll = loadAll;

        vm.account = null;

        //??? on recupere le compte connecté pour vérifier ces autorisations ???
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

        loadAll();

        function loadAll() {
            Etudiant.query(function(result) {
                vm.etudiants = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            EtudiantSearch.query({query: vm.searchQuery}, function(result) {
                vm.etudiants = result;
            });
        }    }
})();
