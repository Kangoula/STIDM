(function() {
    'use strict';

    angular
        .module('stidmApp')
        .controller('CartElementDetailController', CartElementDetailController);

    CartElementDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CartElement'];

    function CartElementDetailController($scope, $rootScope, $stateParams, previousState, entity, CartElement) {
        var vm = this;

        vm.cartElement = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('stidmApp:cartElementUpdate', function(event, result) {
            vm.cartElement = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
