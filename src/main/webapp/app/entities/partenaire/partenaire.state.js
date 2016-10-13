(function() {
    'use strict';

    angular
        .module('projectTaaGliApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('partenaire', {
            parent: 'entity',
            url: '/partenaire',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Partenaires'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/partenaire/partenaires.html',
                    controller: 'PartenaireController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('partenaire-detail', {
            parent: 'entity',
            url: '/partenaire/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Partenaire'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/partenaire/partenaire-detail.html',
                    controller: 'PartenaireDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Partenaire', function($stateParams, Partenaire) {
                    return Partenaire.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'partenaire',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('partenaire-detail.edit', {
            parent: 'partenaire-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/partenaire/partenaire-dialog.html',
                    controller: 'PartenaireDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Partenaire', function(Partenaire) {
                            return Partenaire.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('partenaire.new', {
            parent: 'partenaire',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/partenaire/partenaire-dialog.html',
                    controller: 'PartenaireDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                siret: null,
                                service: null,
                                region: null,
                                codeActivity: null,
                                rue: null,
                                cpltRue: null,
                                codeDep: null,
                                ville: null,
                                telStd: null,
                                url: null,
                                commentaire: null,
                                nomSignataire: null,
                                effectif: null,
                                dateMaj: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('partenaire', null, { reload: 'partenaire' });
                }, function() {
                    $state.go('partenaire');
                });
            }]
        })
        .state('partenaire.edit', {
            parent: 'partenaire',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/partenaire/partenaire-dialog.html',
                    controller: 'PartenaireDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Partenaire', function(Partenaire) {
                            return Partenaire.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('partenaire', null, { reload: 'partenaire' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('partenaire.delete', {
            parent: 'partenaire',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/partenaire/partenaire-delete-dialog.html',
                    controller: 'PartenaireDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Partenaire', function(Partenaire) {
                            return Partenaire.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('partenaire', null, { reload: 'partenaire' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
