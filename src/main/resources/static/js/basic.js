$(document).ready(function () {
  const auth = getToken();

  if (auth !== undefined && auth !== '') { //토큰이 존재 즉 로그인중
    $('#logout-button').show();
    $('#login-button').hide();
    const role = getUserRole();
    if (role !== null) {
      if (role === 'ADMIN') {
        $('#admin-page').show();
      } else {
        $('#admin-page').hide();
      }
    }

    if (role === 'BLOCK') {
      clearCookie();
      alert('신고누적으로 차단되엇습니다.');
      window.location.href = "/booktalk";
    }
  } else {
    $('#logout-button').hide();
    $('#login-button').show();
    $('#admin-page').hide();
    return; //지워도 될듯하다 //이거 지우면 밑에 .ajax가 실행되는듯
  }

  let protocol = window.location.protocol === 'https:' ? 'https://' : 'http://';
  let eventSource = new EventSource(
      protocol + window.location.host + '/api/v2/notifications/subscribe');
  // let eventSource = new EventSource(
  //     'http://' + window.location.host + '/api/notification/subscribe');
  eventSource.addEventListener("createChatRoom", function (event) {
    let message = event.data;
    alert(message);
    alertBadge();
  })
  eventSource.addEventListener("createReview", function (event) {
    let message = event.data;
    alert(message);
    alertReviewBadge();
  })

  eventSource.addEventListener("error", function (event) {
    eventSource.close()
  })

  $('#logout-button').click(function () {
    $.ajax({
      type: 'POST', // or 'GET', depending on your server implementation
      url: '/api/v2/users/logout',
      success: function () {
        alert('로그아웃 되었습니다.');
      },
      error: function (error) {
      }
    });
  });

});

function myPage() {
  const auth = getToken();
  if (auth === undefined || auth === '') {
    alert('로그인 후 이용 가능합니다.');
  } else {
    window.location.href = 'http://' + window.location.host
        + '/booktalk/users/profile';
  }
}

function products() {
  window.location.href = 'http://' + window.location.host
      + '/booktalk/products/list';
}

function chatRooms() {
  const auth = getToken();
  if (auth === undefined || auth === '') {
    alert('로그인 후 이용 가능합니다.');
  } else {
    window.location.href = 'http://' + window.location.host
        + '/booktalk/chats/rooms/list';
  }
}

function reviews() {
  const auth = getToken();
  if (auth === undefined || auth === '') {
    alert('로그인 후 이용 가능합니다.');
  } else {
    window.location.href = 'http://' + window.location.host
        + '/booktalk/reviews/list';
  }
}

function alertBadge() {

  var headerChatlist = document.getElementById('header-chat-list');
  const newSpan = document.createElement('span');
  newSpan.className = 'position-absolute top-0 start-100 translate-middle p-2 bg-danger border border-light rounded-circle';
  const innerSpan = document.createElement('span');
  innerSpan.className = 'visually-hidden';
  innerSpan.textContent = 'New alerts';
  headerChatlist.appendChild(newSpan);
}

function alertReviewBadge() {

  var headerReviewlist = document.getElementById('header-review-list');
  const newSpan = document.createElement('span');
  newSpan.className = 'position-absolute top-0 start-100 translate-middle p-2 bg-danger border border-light rounded-circle';
  const innerSpan = document.createElement('span');
  innerSpan.className = 'visually-hidden';
  innerSpan.textContent = 'New alerts';
  headerReviewlist.appendChild(newSpan);
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
    url: '/api/v2/users/role',
    method: 'GET',
    dataType: 'json',
    async: false, // 동기적으로 설정
    success: function (data) {
      role = data;
    },
    error: function (error) {
      const jsonObject = JSON.parse(error.responseText);
      const messages = jsonObject.messages;
      alert(messages);
    }
  });
  return role;
}

function clearCookie() {
  document.cookie = "AccessToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/";
  document.cookie = "RefreshToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/";
}

