var app = angular.module('App');

app.controller('PageCtrl', ['UserService', '$scope', '$state', 'user', '$rootScope', function (UserService, $scope, $state, user, $rootScope) {

	this.user = user;

	// set a fallback avatar image
	if (user && !user.imageUrl) {
		if (user.sex === 'M') {
			user.localImageUrl = 'images/user-boy.png';
		} else if (user.sex == 'F') {
			user.localImageUrl = 'images/user-girl.png';
		} else {
			user.localImageUrl = 'images/user-neutral.png';
		}
	}

	this.logout = () => {
		UserService.logout().then(() => {
			$state.go('page.home', null, { reload: true });
		});
	};

	// the error event is emitted when some error are found and need to be displayed
	$rootScope.$on('error', (event, error) => {
		console.log(error);
		if (error.errors && error.errors.length > 0) {
			// detailed error message
			error.message = error.errors[0].field + " " + error.errors[0].defaultMessage;
		}
		$scope.safeApply(() => {
			this.message = {
				type: "error",
				class: "alert-danger",
				value: error.message
			};
		})

	});

	// this is a friendly message to be shown
	$rootScope.$on('success', (event, message) => {
		console.log(message);
		this.message = {
			type: "message",
			class: "alert-success",
			value: message
		};
	});

	this.hideMessage = () => {
		this.message = null;
	}
}]);