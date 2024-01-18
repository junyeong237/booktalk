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

  let protocol = window.location.protocol === 'https:' ? 'https://' : 'http://';
  let eventSource = new EventSource(
      protocol + window.location.host + '/api/notification/subscribe');
  // let eventSource = new EventSource(
  //     'http://' + window.location.host + '/api/notification/subscribe');
  console.log('확인');
  eventSource.addEventListener("createChatRoom", function (event) {
    console.log(event);
    let message = event.data;
    alert(message);
    alertBadge();
  })

  eventSource.addEventListener("error", function (event) {
    console.log('eventSource종료')
    eventSource.close()
  })

});

function chatRooms() {

  window.location.href = 'http://' + window.location.host
      + '/api/v1/chats/room/list';
}

function alertBadge() {

  var headerChatlist = document.getElementById('header-chat-list');
  const newSpan = document.createElement('span');
  newSpan.className = 'position-absolute top-0 start-100 translate-middle p-2 bg-danger border border-light rounded-circle';
  console.log(newSpan);
  const innerSpan = document.createElement('span');
  innerSpan.className = 'visually-hidden';
  innerSpan.textContent = 'New alerts';
  headerChatlist.appendChild(newSpan);
  console.log(headerChatlist);
}

function getToken() {
  let auth = Cookies.get('AccessToken');

  if (auth === undefined) {
    return '';
  }

  return auth;
}
