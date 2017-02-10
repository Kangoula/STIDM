(function() {
    'use strict';
    angular
        .module('stidmApp')
        .factory('Game', Game);

    Game.$inject = ['$resource', 'DateUtils'];

    function Game ($resource, DateUtils) {
        var resourceUrl =  'api/games/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.release = DateUtils.convertLocalDateFromServer(data.release);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.release = DateUtils.convertLocalDateToServer(copy.release);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.release = DateUtils.convertLocalDateToServer(copy.release);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
