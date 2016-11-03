(function() {
    'use strict';

    angular
        .module('projectTaaGliApp')
        .factory('EnqueteSend', EnqueteSend);

    EnqueteSend.$inject = ['$resource'];

    function EnqueteSend($resource) {
        var resourceUrl =  'api/enquetes/send/:id';

        return $resource(resourceUrl,{enqId : '@id'});
    }
})();
