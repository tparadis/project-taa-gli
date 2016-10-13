(function() {
    'use strict';
    angular
        .module('projectTaaGliApp')
        .factory('Enseignant', Enseignant);

    Enseignant.$inject = ['$resource', 'DateUtils'];

    function Enseignant ($resource, DateUtils) {
        var resourceUrl =  'api/enseignants/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateMaj = DateUtils.convertDateTimeFromServer(data.dateMaj);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
