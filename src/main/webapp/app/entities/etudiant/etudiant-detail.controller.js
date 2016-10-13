(function() {
    'use strict';

    angular
        .module('projectTaaGliApp')
        .controller('EtudiantDetailController', EtudiantDetailController);

    EtudiantDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Etudiant', 'User', 'Partenaire', 'Stage', 'Enquete'];

    function EtudiantDetailController($scope, $rootScope, $stateParams, previousState, entity, Etudiant, User, Partenaire, Stage, Enquete) {
        var vm = this;

        vm.etudiant = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('projectTaaGliApp:etudiantUpdate', function(event, result) {
            vm.etudiant = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
