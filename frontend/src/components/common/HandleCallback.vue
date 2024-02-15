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
import Swal from 'sweetalert2'

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
  // redirect url param으로 로그인
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
  // 백엔드로 로그인 코드 인증 + 사이트별 url
  let url = import.meta.env.VITE_API_URL + import.meta.env.VITE_SERVER
  if (isNaver.value) url = url + '/oauth2/naver?code=' + naverquerycode.value
  else if (isGoogle.value) url = url + '/oauth2/google?code=' + googlequerycode.value
  else if (isKakao.value) url = url + '/oauth2/kakao?code=' + kakaoquerycode.value

  axios({
    url: url
  })
    .then((res) => {
      // response의 token tokenservice + user data -> cookie 저장
      const token = res.headers['authorization']
      tokenService.setLocalAccessToken(token)
      tokenService.setUser(res.data)
      // userstore에 사용자 정보 저장
      store.name = res.data.name
      store.email = res.data.email
      store.image = res.data.image
      // res.data가 first 면 가입 이력이 없음
      if (res.data.first == true) {
        Swal.fire({
          title: '회원가입',
          text: '추가 정보 입력을 위해 회원가입 페이지로 이동합니다.',
          icon: 'info'
        }).then((result) => {
          if (result.isConfirmed) {
            router.push('/signup')
          }
        })
      } else {
        // 가입 이력이 있으므로 로그인 후 메인화면 이동
        store.isLogin = true
        // refresh token
        const refreshToken = res.headers['authorization-refresh']
        tokenService.setLocalRefreshToken(refreshToken)
        if (sessionStorage.getItem('place') === 'meeting') {
          // session storage에 행사 참여 이력이 있으면 행사 화면으로 다시 이동
          if (sessionStorage.getItem('groupSessionId') !== null) {
            router.push('/groupmeetings')
          } else {
            router.push('/meetings')
          }
        } else {
          // 홈 화면 새로고침 메서드
          store.forceReload = true
        }
      }
    })
    .catch((err) => {
      console.log(err)
    })
})
</script>

<style scoped></style>
