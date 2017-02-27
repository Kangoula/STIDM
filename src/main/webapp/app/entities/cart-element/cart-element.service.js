(function() {
    'use strict';
    angular
        .module('stidmApp')
        .factory('CartElement', CartElement);

    CartElement.$inject = ['$resource'];

    function CartElement ($resource) {
        var resourceUrl =  'api/cart-elements/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
