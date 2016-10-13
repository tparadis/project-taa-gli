(function() {
    'use strict';

    angular
        .module('projectTaaGliApp')
        .controller('ContactDetailController', ContactDetailController);

    ContactDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Contact', 'Partenaire'];

    function ContactDetailController($scope, $rootScope, $stateParams, previousState, entity, Contact, Partenaire) {
        var vm = this;

        vm.contact = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('projectTaaGliApp:contactUpdate', function(event, result) {
            vm.contact = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
