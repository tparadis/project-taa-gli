(function() {
    'use strict';

    angular
        .module('projectTaaGliApp')
        .controller('EnqueteController', EnqueteController);

    EnqueteController.$inject = ['$scope', '$state', 'Enquete', 'EnqueteSearch','EnqueteSend','$log' ];

    function EnqueteController ($scope, $state, Enquete, EnqueteSearch, EnqueteSend,$log) {
        var vm = this;

        vm.enquetes = [];
        vm.search = search;
        vm.loadAll = loadAll;
        $scope.sendMail = sendMail;

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
        }
           function sendMail(id) {
               EnqueteSend.query({id: id});
                console.log("sent");
                  }
     }
})();
