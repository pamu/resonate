$(document).ready(function () {
  $('.login-link').click(function () {
    FB.login(function(response) {
      FB.getLoginStatus(function (resp) {
        console.log(resp);
      }, true);
    }, {scope: 'email'});
  });
});