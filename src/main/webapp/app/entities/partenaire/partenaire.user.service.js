(function() {
    'use strict';

    angular
        .module('projectTaaGliApp')
        .factory('PartenaireUser', PartenaireUser);

    PartenaireUser.$inject = ['$resource'];

    function PartenaireUser($resource) {
        var resourceUrl =  'api/partenaires/user/:id';

        return $resource(resourceUrl,{ userId: '@id' });
    }
})();
