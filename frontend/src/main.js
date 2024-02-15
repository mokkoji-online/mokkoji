if (import.meta.env.MODE !== 'ssr') {
  import('./style.css')
  import('./App.vue')
}
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
//scroll animation
import AOS from 'aos'
import App from './App.vue'
import router from './router/index.js'
//vue-cookies
import VueCookies from 'vue-cookies'
import tokenService from '@/services/token.service'
//sweetalert
import VueSweetalert2 from 'vue-sweetalert2'
import 'sweetalert2/dist/sweetalert2.min.css';

// pinia 전역관리
const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)

const app = createApp(App)

app.use(pinia)
app.use(router)

// sweetalert custom option
const options = {
  confirmButtonColor : '#C8B6FF'
}
app.use(VueSweetalert2, options);

// vue-cookies 설정 + expired token 삭제처리
app.use(VueCookies, { expires: '1h' })
if ($cookies.isKey('token')) {
  if (tokenService.expiredToken($cookies.get('token'))) {
    $cookies.keys().forEach((cookie) => $cookies.remove(cookie))
  }
}

app.mount('#app')
