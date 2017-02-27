(function() {
    'use strict';

    angular
        .module('stidmApp')
        .controller('CartElementController', CartElementController);

    CartElementController.$inject = ['$scope', '$state', 'CartElement'];

    function CartElementController ($scope, $state, CartElement) {
        var vm = this;

        vm.cartElements = [];

        loadAll();

        function loadAll() {
            CartElement.query(function(result) {
                vm.cartElements = result;
                vm.searchQuery = null;
            });
        }
    }
})();
