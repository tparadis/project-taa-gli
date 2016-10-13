(function() {
    'use strict';

    angular
        .module('projectTaaGliApp')
        .controller('ContactController', ContactController);

    ContactController.$inject = ['$scope', '$state', 'Contact', 'ContactSearch'];

    function ContactController ($scope, $state, Contact, ContactSearch) {
        var vm = this;
        
        vm.contacts = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Contact.query(function(result) {
                vm.contacts = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ContactSearch.query({query: vm.searchQuery}, function(result) {
                vm.contacts = result;
            });
        }    }
})();
