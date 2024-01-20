$(document).ready(function () {
  const auth = getToken();

  if (auth !== undefined && auth !== '') { //토큰이 존재 즉 로그인중
    console.log('auth:', auth);
    $('#logout-button').show();
    $('#login-button').hide();
    const role = getUserRole();
    if(role !== null ) {
      if(role === 'ADMIN') {
        $('#admin-page').show();
      }else {
        $('#admin-page').hide();
      }
    }

    if(role === 'BLOCK') {
      clearCookie();
      alert('신고누적으로 차단되엇습니다.');
      window.location.href = "/api/v1";
    }
  } else {
    $('#logout-button').hide();
    $('#login-button').show();
    $('#admin-page').hide();
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

  $('#logout-button').click(function () {
    $.ajax({
      type: 'POST', // or 'GET', depending on your server implementation
      url: '/api/v1/users/logout',
      success: function () {
        alert('로그아웃 되었습니다.');
      },
      error: function (error) {
        console.error('Logout error:', error);
        // Handle error, if needed
      }
    });
  });

});

function chatRooms() {
  const auth = getToken();
  if (auth === undefined || auth === '') {
    alert('로그인 후 이용 가능합니다.');
  } else {
    window.location.href = 'http://' + window.location.host
        + '/api/v1/chats/room/list';
  }
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

function getUserRole() {
  let role;
  $.ajax({
    url: '/api/v1/users/role',
    method: 'GET',
    dataType: 'json',
    async: false, // 동기적으로 설정
    success: function (data) {
      role = data;
    },
    error: function (error) {
      alert('알 수 없는 오류 발생');
    }
  });
  return role;
}

function clearCookie() {
  document.cookie = "AccessToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/";
  document.cookie = "RefreshToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/";
}

