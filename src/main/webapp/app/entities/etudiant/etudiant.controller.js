(function() {
    'use strict';

    angular
        .module('projectTaaGliApp')
        .controller('EtudiantController', EtudiantController);

    EtudiantController.$inject = ['$scope', '$state', 'Etudiant', 'EtudiantSearch'];

    function EtudiantController ($scope, $state, Etudiant, EtudiantSearch) {
        var vm = this;
        
        vm.etudiants = [];
        vm.search = search;
        vm.loadAll = loadAll;

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
