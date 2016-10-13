(function() {
    'use strict';

    angular
        .module('projectTaaGliApp')
        .controller('StageDialogController', StageDialogController);

    StageDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Stage', 'Enseignant', 'Contact', 'Etudiant', 'Partenaire'];

    function StageDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Stage, Enseignant, Contact, Etudiant, Partenaire) {
        var vm = this;

        vm.stage = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.referents = Enseignant.query({filter: 'stage-is-null'});
        $q.all([vm.stage.$promise, vm.referents.$promise]).then(function() {
            if (!vm.stage.referent || !vm.stage.referent.id) {
                return $q.reject();
            }
            return Enseignant.get({id : vm.stage.referent.id}).$promise;
        }).then(function(referent) {
            vm.referents.push(referent);
        });
        vm.contacts = Contact.query({filter: 'stage-is-null'});
        $q.all([vm.stage.$promise, vm.contacts.$promise]).then(function() {
            if (!vm.stage.contact || !vm.stage.contact.id) {
                return $q.reject();
            }
            return Contact.get({id : vm.stage.contact.id}).$promise;
        }).then(function(contact) {
            vm.contacts.push(contact);
        });
        vm.etudiants = Etudiant.query({filter: 'stage-is-null'});
        $q.all([vm.stage.$promise, vm.etudiants.$promise]).then(function() {
            if (!vm.stage.etudiant || !vm.stage.etudiant.id) {
                return $q.reject();
            }
            return Etudiant.get({id : vm.stage.etudiant.id}).$promise;
        }).then(function(etudiant) {
            vm.etudiants.push(etudiant);
        });
        vm.partenaires = Partenaire.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.stage.id !== null) {
                Stage.update(vm.stage, onSaveSuccess, onSaveError);
            } else {
                Stage.save(vm.stage, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('projectTaaGliApp:stageUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateDebut = false;
        vm.datePickerOpenStatus.finConv = false;
        vm.datePickerOpenStatus.finStage = false;
        vm.datePickerOpenStatus.soutenance = false;
        vm.datePickerOpenStatus.rapport = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
