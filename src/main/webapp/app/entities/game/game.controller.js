(function() {
    'use strict';

    angular
        .module('stidmApp')
        .controller('GameController', GameController);

    GameController.$inject = ['$scope', '$state', 'DataUtils', 'Game', 'ParseLinks', 'AlertService', 'paginationConstants', 'Principal'];

    function GameController ($scope, $state, DataUtils, Game, ParseLinks, AlertService, paginationConstants, Principal) {
        var vm = this;

        vm.games = [];
        vm.loadPage = loadPage;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = true;
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.account = null;
        vm.isAuthenticated = null;
        vm.onChange = onChange;
        vm.name = null;
        vm.games_bak = null;

        function onChange(){
            if(vm.name !== "" && vm.name !== null) {

                vm.games = [];
                vm.games_bak.forEach(function (game) {
                    var g = angular.copy(game.name);
                    g = g.toLowerCase();
                    if (g.match(vm.name)) {
                        vm.games.push(game);
                    }
                });
            }
            else {
                vm.games = angular.copy(vm.games_bak);
            }
        }

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }

        loadAll();

        function loadAll () {
            Game.query({
                page: vm.page,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.games.push(data[i]);
                }
                vm.games_bak = angular.copy(vm.games);
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function reset () {
            vm.page = 0;
            vm.games = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
        }
    }
})();
