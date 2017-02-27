(function() {
    'use strict';

    angular
        .module('stidmApp')
        .controller('CartElementDialogController', CartElementDialogController);

    CartElementDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CartElement'];

    function CartElementDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CartElement) {
        var vm = this;

        vm.cartElement = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.cartElement.id !== null) {
                CartElement.update(vm.cartElement, onSaveSuccess, onSaveError);
            } else {
                CartElement.save(vm.cartElement, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('stidmApp:cartElementUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
