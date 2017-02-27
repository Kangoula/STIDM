(function() {
    'use strict';

    angular
        .module('stidmApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('cart-element', {
            parent: 'app',
            url: '/cart-element',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'stidmApp.cartElement.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cart-element/cart-elements.html',
                    controller: 'CartElementController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cartElement');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('cart-element-detail', {
            parent: 'cart-element',
            url: '/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'stidmApp.cartElement.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cart-element/cart-element-detail.html',
                    controller: 'CartElementDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cartElement');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CartElement', function($stateParams, CartElement) {
                    return CartElement.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'cart-element',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('cart-element-detail.edit', {
            parent: 'cart-element-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cart-element/cart-element-dialog.html',
                    controller: 'CartElementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CartElement', function(CartElement) {
                            return CartElement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cart-element.new', {
            parent: 'cart-element',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cart-element/cart-element-dialog.html',
                    controller: 'CartElementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                idGame: null,
                                refGame: null,
                                nameGame: null,
                                price: null,
                                idUser: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('cart-element', null, { reload: 'cart-element' });
                }, function() {
                    $state.go('cart-element');
                });
            }]
        })
        .state('cart-element.edit', {
            parent: 'cart-element',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cart-element/cart-element-dialog.html',
                    controller: 'CartElementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CartElement', function(CartElement) {
                            return CartElement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cart-element', null, { reload: 'cart-element' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cart-element.delete', {
            parent: 'cart-element',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cart-element/cart-element-delete-dialog.html',
                    controller: 'CartElementDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CartElement', function(CartElement) {
                            return CartElement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cart-element', null, { reload: 'cart-element' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
