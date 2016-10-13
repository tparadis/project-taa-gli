(function() {
    'use strict';

    angular
        .module('projectTaaGliApp')
        .controller('PartenaireDetailController', PartenaireDetailController);

    PartenaireDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Partenaire', 'User', 'Contact', 'Etudiant', 'Stage'];

    function PartenaireDetailController($scope, $rootScope, $stateParams, previousState, entity, Partenaire, User, Contact, Etudiant, Stage) {
        var vm = this;

        vm.partenaire = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('projectTaaGliApp:partenaireUpdate', function(event, result) {
            vm.partenaire = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
