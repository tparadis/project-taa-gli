(function() {
    'use strict';

    angular
        .module('projectTaaGliApp')
        .controller('PartenaireController', PartenaireController);

    PartenaireController.$inject = ['$scope', '$state', 'Partenaire', 'PartenaireSearch'];

    function PartenaireController ($scope, $state, Partenaire, PartenaireSearch) {
        var vm = this;
        
        vm.partenaires = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Partenaire.query(function(result) {
                vm.partenaires = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            PartenaireSearch.query({query: vm.searchQuery}, function(result) {
                vm.partenaires = result;
            });
        }    }
})();
