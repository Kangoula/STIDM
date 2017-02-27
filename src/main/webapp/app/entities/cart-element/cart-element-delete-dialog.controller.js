(function() {
    'use strict';

    angular
        .module('stidmApp')
        .controller('CartElementDeleteController',CartElementDeleteController);

    CartElementDeleteController.$inject = ['$uibModalInstance', 'entity', 'CartElement'];

    function CartElementDeleteController($uibModalInstance, entity, CartElement) {
        var vm = this;

        vm.cartElement = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CartElement.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
