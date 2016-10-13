'use strict';

describe('Controller Tests', function() {

    describe('Etudiant Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockEtudiant, MockUser, MockPartenaire, MockStage, MockEnquete;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockEtudiant = jasmine.createSpy('MockEtudiant');
            MockUser = jasmine.createSpy('MockUser');
            MockPartenaire = jasmine.createSpy('MockPartenaire');
            MockStage = jasmine.createSpy('MockStage');
            MockEnquete = jasmine.createSpy('MockEnquete');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Etudiant': MockEtudiant,
                'User': MockUser,
                'Partenaire': MockPartenaire,
                'Stage': MockStage,
                'Enquete': MockEnquete
            };
            createController = function() {
                $injector.get('$controller')("EtudiantDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'projectTaaGliApp:etudiantUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
