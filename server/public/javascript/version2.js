/**
 *
 */

function fetchLoad(id, route) {
  fetch(route)
    .then((res) => res.text())
    .then((body) => {
      document.getElementById(id).innerHTML = body;
    });
}

function fetchPost(route, data, success) {
  fetch(route, {
    method: "POST",
    headers: { "Content-Type": "application/x-www-form-urlencoded" },
    body: Object.keys(data)
      .map(
        (key) => encodeURIComponent(key) + "=" + encodeURIComponent(data[key]),
      )
      .join("&"),
  })
    .then((res) => res.text())
    .then((body) => success(body));
}

const csrfToken = document.getElementById("csrfToken").value;
const loginRoute = document.getElementById("loginRoute").value;
const validateRoute = document.getElementById("validateRoute").value;
const createRoute = document.getElementById("createRoute").value;
const personalRoute = document.getElementById("personalRoute").value;
const generalRoute = document.getElementById("generalRoute").value;
const logoutRoute = document.getElementById("logoutRoute").value;

fetchLoad("contents", loginRoute);

function login() {
  const username = document.getElementById("loginName").value;
  const password = document.getElementById("loginPass").value;
  fetchPost( validateRoute, 
    { username, password, csrfToken }, 
    data => { document.getElementById("contents").innerHTML = data; }
  );
}

function create() {
  const username = document.getElementById("createName").value;
  const password = document.getElementById("createPass").value;
  fetchPost( createRoute,
    { username, password, csrfToken },
    data => { document.getElementById("contents").innerHTML = data; }
  );
}

function sendPersonal() {
  const message = document.getElementById("newPersonal").value;
  const recipient = document.getElementById("recipient").value;
  fetchPost(personalRoute,
    { message, recipient, csrfToken },
    data => { document.getElementById("contents").innerHTML = data; }
  );
}

function sendGeneral() {
  const message = document.getElementById("newGeneral").value;
  fetchPost(generalRoute,
    { message, csrfToken },
    data => { document.getElementById("contents").innerHTML = data; }
  );
}

function logout() {
  fetchLoad("contents", logoutRoute);
}
