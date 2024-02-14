<template>
  <div class="flex space-x-2 justify-center items-center h-screen" id="main-gradient">
    <div class="flex rounded-full bg-white w-[50vh] h-[50vh] items-center justify-center">
      <div class="h-8 w-8 bg-black rounded-full animate-bounce [animation-delay:-0.3s]"></div>
      <div class="h-8 w-8 bg-black rounded-full animate-bounce [animation-delay:-0.15s]"></div>
      <div class="h-8 w-8 bg-black rounded-full animate-bounce"></div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, onBeforeMount, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import axios from 'axios'
import tokenService from '@/services/token.service'
const router = useRouter()
const route = useRoute()
const store = useUserStore()
const naverquerycode = ref('')
const googlequerycode = ref('')
const kakaoquerycode = ref('')
const isGoogle = ref(false)
const isNaver = ref(false)
const isKakao = ref(false)

onBeforeMount(() => {
  if (window.location.href.includes('google')) {
    isGoogle.value = true
    googlequerycode.value = route.query.code
  } else if (window.location.href.includes('naver')) {
    isNaver.value = true
    naverquerycode.value = route.query.code
  } else if (window.location.href.includes('kakao')) {
    isKakao.value = true
    kakaoquerycode.value = route.query.code
  }
})
onMounted(() => {
  // 백엔드로 로그인 코드 인증
  let url = import.meta.env.VITE_API_URL + import.meta.env.VITE_SERVER
  if (isNaver.value) url = url + '/oauth2/naver?code=' + naverquerycode.value
  else if (isGoogle.value) url = url + '/oauth2/google?code=' + googlequerycode.value
  else if (isKakao.value) url = url + '/oauth2/kakao?code=' + kakaoquerycode.value
  
  axios({
    url: url
  })
    .then((res) => {
      const token = res.headers['authorization']
      tokenService.setLocalAccessToken(token)
      tokenService.setUser(res.data)
      store.name = res.data.name
      store.email = res.data.email
      store.image = res.data.image
      if (res.data.first == true) {
        router.push('/signup')
      } else {
        store.isLogin = true
        const refreshToken = res.headers['authorization-refresh']
        tokenService.setLocalRefreshToken(refreshToken)
        store.forceReload = true
        router.push('/')
      }
    })
    .catch((err) => {
      console.log(err)
    })
})
</script>
<style></style>
