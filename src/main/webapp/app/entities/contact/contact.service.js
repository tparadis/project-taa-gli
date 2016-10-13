(function() {
    'use strict';
    angular
        .module('projectTaaGliApp')
        .factory('Contact', Contact);

    Contact.$inject = ['$resource', 'DateUtils'];

    function Contact ($resource, DateUtils) {
        var resourceUrl =  'api/contacts/:id';

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
