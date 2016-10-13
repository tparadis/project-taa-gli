(function() {
    'use strict';

    angular
        .module('projectTaaGliApp')
        .factory('StageSearch', StageSearch);

    StageSearch.$inject = ['$resource'];

    function StageSearch($resource) {
        var resourceUrl =  'api/_search/stages/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
