document.addEventListener('DOMContentLoaded', () => {
   M.Modal.init( document.querySelectorAll('.modal'), {
      opacity: 0.6,
      inDuration: 200,
      outDuration: 200,
      onOpenStart: onModalOpens
   });
   const authModalSignInButton = document.getElementById("auth-modal-sign-in-button");
   if(authModalSignInButton) authModalSignInButton.addEventListener('click', signInButtonClick);

   const spaContainer = document.getElementById("spa-container");
   if(spaContainer) {
      const token = window.localStorage.getItem('token202');
      if( token ) {
         spaContainer.innerText = token;
      }
      else {
         spaContainer.innerText = 'Немає токену';
      }
   }
   const spaLogoutButton = document.getElementById("spa-btn-logout");
   if( spaLogoutButton ) spaLogoutButton.addEventListener('click', spaLogoutClick);
   const spaGetInfoButton = document.getElementById("spa-btn-get-info");
   if( spaGetInfoButton ) spaGetInfoButton.addEventListener('click', spaGetInfoClick);
});
function spaLogoutClick() {
   window.localStorage.removeItem('token202');
   window.location.reload();
}
function spaGetInfoClick() {
   const spaContainer = document.getElementById("spa-container") ;
   if( ! spaContainer ) throw "#spa-container not found" ;

   fetch(`${getAppContext()}/uk/tpl/template1.html`,{
      method: 'GET',
      headers: {
         'Authorization': `Bearer ${window.localStorage.getItem('token202')}`
      }
   }).then(r=>r.text()).then(t => {
      spaContainer.innerHTML += t ;
   });

   fetch(`${getAppContext()}/uk/tpl/NP.png`,{
      method: 'GET',
      headers: {
         'Authorization': `Bearer ${window.localStorage.getItem('token202')}`
      }
   }).then( r => r.blob() )  // BLOB - Binary Large Object
       .then( blob => {
          // Розміщуємо об'єкт та одержуємо на нього посилання
          const blobUrl = URL.createObjectURL( blob ) ;
          console.log( blobUrl ) ;
          spaContainer.innerHTML += `<img src="${blobUrl}" />`;
       });
}
function onModalOpens() {
   const [authLogin, authPassword, authMessage] = getModalElements();
   authLogin.value = '';
   authPassword.value = '';
   authMessage.innerText = '';
}
function getModalElements() {
   const authLogin = document.getElementById('auth-login');
   if( ! authLogin ) throw "#auth-login not found" ;
   const authPassword = document.getElementById('auth-password');
   if( ! authPassword ) throw "#auth-password not found" ;
   const authMessage = document.getElementById('auth-message-container');
   if( ! authMessage ) throw "#auth-message-container not found" ;
   return [authLogin, authPassword, authMessage];
}
function signInButtonClick() {
   const [authLogin, authPassword, authMessage] = getModalElements();
   const login = authLogin.value;
   if( login.length === 0 ) {
      authMessage.innerText = 'Заповніть поле "логін"';
      return;
   }
   const password = authPassword.value;
   if( password.length === 0 ) {
      authMessage.innerText = 'Заповніть поле "пароль"';
      return;
   }
   authMessage.innerText = '' ;
   const url = `${getAppContext()}/auth?login=${login}&password=${password}` ;
   fetch(url).then(r=>{
      if( r.status === 202 ) {  // одержуємо токен
         r.json().then( encodedToken => {
            // перевіряємо цілісність токена шляхом його декодування та наявність jti
            try {
               const token = JSON.parse( atob( encodedToken ) ) ;
               if( typeof token.jti === 'undefined' ) {
                  authMessage.innerText = 'Цілісність токена порушена';
               }
               else {
                  window.localStorage.setItem('token202', encodedToken);
                  window.location.reload();
               }
            }
            catch (e) {
               authMessage.innerText = 'Отримано помилкові дані';
            }
            console.log(encodedToken);
         });
      }
      else {  // помилка одержання токена
         authMessage.innerText = 'В автентифікації відмовлено';
      }
   });
}
function getAppContext() {
   var isContextPreset = false ;
   return isContextPreset ? "" : '/' + window.location.pathname.split('/')[1] ;
}