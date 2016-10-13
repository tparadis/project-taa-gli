'use strict';

describe('Controller Tests', function() {

    describe('Partenaire Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPartenaire, MockUser, MockContact, MockEtudiant, MockStage;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPartenaire = jasmine.createSpy('MockPartenaire');
            MockUser = jasmine.createSpy('MockUser');
            MockContact = jasmine.createSpy('MockContact');
            MockEtudiant = jasmine.createSpy('MockEtudiant');
            MockStage = jasmine.createSpy('MockStage');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Partenaire': MockPartenaire,
                'User': MockUser,
                'Contact': MockContact,
                'Etudiant': MockEtudiant,
                'Stage': MockStage
            };
            createController = function() {
                $injector.get('$controller')("PartenaireDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'projectTaaGliApp:partenaireUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
