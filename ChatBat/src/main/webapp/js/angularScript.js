//Init angular
var mainApp = angular.module("onlineApp", []);

mainApp.controller('MainCtrl', function ($scope, $http) {
    $scope.online = 0;
    function poll() {
        //Bijna identiek aan $.ajax
        //Haal aantal vrienden die online zijn op
        $http({
            url: "ChatController?dispatcher=friendsOnline",
            method: "GET",
            dataType: "json",
            data: {"action": "friendsOnline"}
        }).success(function (data, status, headers, config) {
            console.log("friends online fetched");
            $scope.online = data.amount;
        });
    }
    poll();
    //Start pollen
    setInterval(function () {
        poll();
    }, 7500);
});
