(function() {
    'use strict';

    angular
        .module('projectTaaGliApp')
        .controller('EnseignantDetailController', EnseignantDetailController);

    EnseignantDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Enseignant'];

    function EnseignantDetailController($scope, $rootScope, $stateParams, previousState, entity, Enseignant) {
        var vm = this;

        vm.enseignant = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('projectTaaGliApp:enseignantUpdate', function(event, result) {
            vm.enseignant = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
