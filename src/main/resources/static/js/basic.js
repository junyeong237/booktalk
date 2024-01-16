const host = 'http://' + window.location.host;

$(document).ready(function () {
  const auth = getToken();
  if (auth !== undefined && auth !== '') {
    console.log('auth:', auth);
    $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
      jqXHR.setRequestHeader('AccessToken', auth);
      // ajax 요청헤더에 set
    });
  } else {
    return; //지워도 될듯하다 //이거 지우면 밑에 .ajax가 실행되는듯
  }

  $.ajax({
    type: 'GET',
    url: `/api/v1/admin/users`,
    contentType: 'application/json',
  })
  .done(function (res) {
    $('#body-container').empty();
    renderUsers(res)

  })
  .fail(function (jqXHR, textStatus) {

  });

});

function renderUsers(userList) {
  var bodyContainer = document.getElementById('body-container');
  // Create a new div element with the 'container' and 'mt-5' classes
  var mainContainer = document.createElement('div');
  mainContainer.className = 'container mt-5';

  // Create a new h2 element with the 'text-center' and 'mb-4' classes
  var heading = document.createElement('h2');
  heading.textContent = 'User List';
  heading.className = 'text-center mb-4';

  // Create a new div element with the 'card-body' class
  var cardBody = document.createElement('div');
  cardBody.className = 'card-body';

  // Create a new ul element with the 'list-group' class
  var userListContainer = document.createElement('ul');
  userListContainer.className = 'list-group';

  // Append the ul element to the card body
  cardBody.appendChild(userListContainer);

  // Append the heading and card body to the main container
  mainContainer.appendChild(heading);
  mainContainer.appendChild(cardBody);

  // Append the main container to the document body
  document.body.appendChild(mainContainer);

  // Loop through the userList and create a list item for each user
  userList.forEach(function (user) {
    var listItem = document.createElement('li');
    listItem.className = 'list-group-item d-flex justify-content-between align-items-center';

    var userName = document.createElement('span');
    userName.textContent = user.name;
    userName.className = 'me-auto';

    var myButton = document.createElement('button');
    myButton.textContent = '채팅하기';
    myButton.className = 'btn btn-primary';

    myButton.addEventListener("click", function () {
      // Redirect to the desired HTML file

      $.ajax({
        type: "POST",
        url: `/api/v1/chats/room`,
        contentType: "application/json",
        data: JSON.stringify(data),
      })
      .done(function (res) {
        $('#body-container').empty();
        chat(res, res.sender);
        //window.location.href = host + '/api/v1/chats/room';
      })
      .fail(function (jqXHR, textStatus) {
        alert("chatingReoom Fail");
        window.location.href = host + '/api/v1/';
      });

    });

    // Append the elements to the list item
    listItem.appendChild(userName);
    listItem.appendChild(myButton);

    // Append the list item to the user list container
    userListContainer.appendChild(listItem);

  });
  bodyContainer.appendChild(userListContainer);
}

function chat(result, sender) {
  console.log(result);
  var bodyContainer = document.getElementById('body-container');
  var container = document.createElement('div');
  container.className = 'container';

// 새로운 div 요소 생성
  var divRow = document.createElement('div');
  divRow.className = 'row';

  var divCol = document.createElement('div');
  divCol.className = 'col-md-6 offset-md-3';

  var chatBox = document.createElement('div');
  chatBox.id = 'chatBox';
  chatBox.style.height = '300px';
  chatBox.style.overflowY = 'scroll';

  var messageInput = document.createElement('input');
  messageInput.type = 'text';
  messageInput.id = 'messageInput';
  messageInput.className = 'form-control mt-2';
  messageInput.placeholder = 'Type your message...';

  var sendButton = document.createElement('button');
  sendButton.textContent = 'Send';
  sendButton.className = 'btn btn-primary mt-2';
  //sendButton.onclick = sendMessage(result); // 이벤트 핸들러 연결

  sendButton.onclick = function () {
    sendMessage(result.id);
  }; // 익명 함수를 이벤트 핸들러로 등록

// 생성한 요소들을 계층 구조에 추가
  divCol.appendChild(chatBox);
  divCol.appendChild(messageInput);
  divCol.appendChild(sendButton);

  divRow.appendChild(divCol);
  container.appendChild(divRow);
  bodyContainer.appendChild(container);
  connect(result.id, sender.name);

}

function connect(roomId, username) {

//
// // 혹은 모든 input 요소의 클릭 이벤트 막기
//         $("input").on('click', function (e) {
//             e.preventDefault();
//         });

  var socket = new SockJS('/chat');
  console.log(roomId);
  console.log(username);
  stompClient = Stomp.over(socket);
  var headers = {
    name: username
  };

  stompClient.connect(headers, function (frame) {
    setConnected(true);
    console.log('Connected: ' + frame);
    loadChat(roomId);
    stompClient.subscribe('/topic/' + roomId,
        function (greeting) {
          showGreeting(JSON.parse(greeting.body));
        });
    stompClient.send("/app/" + roomId + "/enter", {},
        JSON.stringify({'message': $("#messageInput").val()}));
  });
}

function sendMessage(roomId) {
  stompClient.send("/app/" + roomId + "/chat", {},
      JSON.stringify({'message': $("#messageInput").val()}));
  $('#messageInput').val('');
}

function showGreeting(message) {

  if (message.name == null) {
    $("#chatBox").append("<tr><td>" + message.message + "</td></tr>");
  } else {
    $("#chatBox").append(
        "<tr><td>" + message.name + ": " + message.message
        + "</td></tr>");
  }

}

function disconnect(username) {
  if (stompClient !== null) {
    exit();
    stompClient.disconnect();
  }

  console.log("Disconnected");
}

function loadChat(roomId) {
  $.ajax({
    url: `/api/v1/chats/room/` + roomId,
    type: 'GET',
    success: function (chatList) {
      console.log(chatList)
      if (chatList != null) {

        for (ct in chatList) {
          $("#chatBox").append(
              "<tr><td>" + chatList[ct].name + ": " + chatList[ct].content
              + "</td></tr>");
        }

      }
    },
    error: function (error) {
      // 서버 요청 중 오류가 발생했을 때의 처리
      console.error('Error:', error);
      window.location.href = "/";
    }
  });

}

function getToken() {
  let auth = Cookies.get('AccessToken');

  if (auth === undefined) {
    return '';
  }

  return auth;
}
