(function() {
    'use strict';

    angular
        .module('projectTaaGliApp')
        .factory('PartenaireSearch', PartenaireSearch);

    PartenaireSearch.$inject = ['$resource'];

    function PartenaireSearch($resource) {
        var resourceUrl =  'api/_search/partenaires/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
