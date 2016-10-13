(function() {
    'use strict';

    angular
        .module('projectTaaGliApp')
        .controller('EtudiantDialogController', EtudiantDialogController);

    EtudiantDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Etudiant', 'User', 'Partenaire', 'Stage', 'Enquete'];

    function EtudiantDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Etudiant, User, Partenaire, Stage, Enquete) {
        var vm = this;

        vm.etudiant = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();
        vm.partenaires = Partenaire.query();
        vm.stages = Stage.query();
        vm.enquetes = Enquete.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.etudiant.id !== null) {
                Etudiant.update(vm.etudiant, onSaveSuccess, onSaveError);
            } else {
                Etudiant.save(vm.etudiant, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('projectTaaGliApp:etudiantUpdate', result);
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
