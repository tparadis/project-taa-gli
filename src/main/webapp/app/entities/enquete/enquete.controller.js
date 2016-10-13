(function() {
    'use strict';

    angular
        .module('projectTaaGliApp')
        .controller('EnqueteController', EnqueteController);

    EnqueteController.$inject = ['$scope', '$state', 'Enquete', 'EnqueteSearch'];

    function EnqueteController ($scope, $state, Enquete, EnqueteSearch) {
        var vm = this;
        
        vm.enquetes = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Enquete.query(function(result) {
                vm.enquetes = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            EnqueteSearch.query({query: vm.searchQuery}, function(result) {
                vm.enquetes = result;
            });
        }    }
})();
