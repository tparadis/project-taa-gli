(function() {
    'use strict';
    angular
        .module('projectTaaGliApp')
        .factory('Enquete', Enquete);

    Enquete.$inject = ['$resource', 'DateUtils'];

    function Enquete ($resource, DateUtils) {
        var resourceUrl =  'api/enquetes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateDebut = DateUtils.convertDateTimeFromServer(data.dateDebut);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
