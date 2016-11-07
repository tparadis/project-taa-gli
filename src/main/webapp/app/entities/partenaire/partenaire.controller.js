(function() {
    'use strict';

    angular
        .module('projectTaaGliApp')
        .controller('PartenaireController', PartenaireController);

    PartenaireController.$inject = ['$scope', '$state','$log', 'Partenaire', 'PartenaireSearch'];

    function PartenaireController ($scope, $state,$log, Partenaire, PartenaireSearch) {
        var vm = this;

        vm.partenaires = [];
        vm.search = search;
        vm.loadAll = loadAll;
        vm.exporter = exporter;
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
        }
        function exporter(){
                var array = JSON.parse(vm.partenaires);

                var str = '';

                for (var i = 0; i < array.length; i++) {
                    var line = '';

                    for (var index in array[i]) {
                        line += array[i][index] + ',';
                    }

                    line.slice(0,line.Length-1);

                    str += line + '\r\n';
                }
                window.open( "data:text/csv;charset=utf-8," + escape(str))
            }

        }


})();
