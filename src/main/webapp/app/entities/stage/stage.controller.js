(function() {
    'use strict';

    angular
        .module('projectTaaGliApp')
        .controller('StageController', StageController);

    StageController.$inject = ['$scope', '$state', 'Stage', 'StageSearch'];

    function StageController ($scope, $state, Stage, StageSearch) {
        var vm = this;
        
        vm.stages = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Stage.query(function(result) {
                vm.stages = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            StageSearch.query({query: vm.searchQuery}, function(result) {
                vm.stages = result;
            });
        }    }
})();
