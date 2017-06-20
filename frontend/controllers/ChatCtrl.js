var app = angular.module('App');

app.controller('ChatCtrl', ['$scope', '$uibModal', '$stateParams', '$localStorage', 'ChatService', function ($scope, $uibModal, $stateParams, $localStorage, chatService) {
    this.messages = chatService.getMessages();

    var connectHeaders = {};
    var chatEndpoint = "http://localhost:8888/chat";
    var jwtParam = "?jwt=Bearer " + $localStorage.token;
    var topicId = 1; // TODO get topic id from the app state

    this.template = "templates/popovers/popoverTemplate.html";

    this.msg_text = "";
    

    this.sendMessage = function() {
        if (this.msg_text != "") {
            var newMsg = {
                username: "Alessio",
                timestamp: new Date(),
                text: this.msg_text,
                side: "right"
            };

            chatService.sendMessage(newMsg);
            this.msg_text = "";
        }
    }


    this.openNewWarningModal = function (size, parentSelector) {
        //var parentElem = parentSelector ? angular.element($document[0].querySelector('.modal-demo ' + parentSelector)) : undefined;
        
        var modalInstance = $uibModal.open({
            /*ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',*/
            templateUrl: 'templates/modals/newWarningModal.html',
            controller: 'NewWarningModalCtrl',
            controllerAs: 'ctrl',
            size: 'lg'
            /*resolve: {
                items: function () {
                return $ctrl.items;
                }
            }*/
        });

        modalInstance.result.then(function (selectedItem) {
            $ctrl.selected = selectedItem;
            }, function () {
            $log.info('Modal dismissed at: ' + new Date());
        });
    };

    this.openAddPictureModal = function () {
        var modalInstance = $uibModal.open({
            templateUrl: 'templates/modals/addPictureModal.html',
            controller: 'AddPictureModalCtrl',
            controllerAs: 'ctrl',
            size: 'lg'
        });

        modalInstance.result.then(function (selectedItem) {
            $ctrl.selected = selectedItem;
            },function () {
            $log.info('Modal dismissed at: ' + new Date());
        });
    };

}]);