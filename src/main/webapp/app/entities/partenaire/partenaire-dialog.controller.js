(function() {
    'use strict';

    angular
        .module('projectTaaGliApp')
        .controller('PartenaireDialogController', PartenaireDialogController);

    PartenaireDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Partenaire', 'User', 'Contact', 'Etudiant', 'Stage'];

    function PartenaireDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Partenaire, User, Contact, Etudiant, Stage) {
        var vm = this;

        vm.partenaire = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();
        vm.contacts = Contact.query();
        vm.etudiants = Etudiant.query();
        vm.stages = Stage.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.partenaire.id !== null) {
                Partenaire.update(vm.partenaire, onSaveSuccess, onSaveError);
            } else {
                Partenaire.save(vm.partenaire, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('projectTaaGliApp:partenaireUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateMaj = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
