$(document).ready(function () {
  const auth = getToken();
  if (auth !== undefined && auth !== '') { //토큰이 존재 즉 로그인중
    console.log('auth:', auth);
    $('#logout-button').show();
    $('#login-button').hide();

  } else {
    $('#logout-button').hide();
    $('#login-button').show();
    return; //지워도 될듯하다 //이거 지우면 밑에 .ajax가 실행되는듯
  }

});

function getToken() {
  let auth = Cookies.get('AccessToken');

  if (auth === undefined) {
    return '';
  }

  return auth;
}
