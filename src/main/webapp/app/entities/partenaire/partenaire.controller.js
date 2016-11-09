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
        $scope.exporter = exporter;
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
        console.log("export");
        console.log(vm.partenaires);
                var array = vm.partenaires;

                var str = 'ID,Siret,Service ,Region,Code Activity,Rue ,Cplt Rue ,Code Dep ,Ville ,Tel ,Std ,Url ,Commentaire, Nom Signataire ,Effectif, Date Maj, Users, Stagiaires ,Stages \r\n';

                for (var i = 0; i < array.length ; i++) {
                    var line = '';

                    for (var index in array[i]) {
                        console.log(index);
                        if (index == "toJSON"){
                            console.log("coucou")
                           break;
                        }line += array[i][index] + ',';
                    }

                    line.slice(0,line.Length-1);

                    str += line + '\r\n';
                }
                window.open( "data:text/csv;charset=utf-8," + escape(str))
            }

        }


})();
