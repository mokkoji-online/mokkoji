<template>
  <header
    :class="{ 'transparent-header': isTransparent }"
    class="fixed h-[10vh] z-10 my-auto w-full bg-transparent"
  >
    <nav class="w-full flex items-center px-[2vw]">
      <a href="/" class="w-[8vw] flex items-center rtl:l:space-x-reverse cursor-grab">
        <img src="@/assets/logo/mokkoji_logo.png" class="w-[8vw]" alt="모꼬지 로고" />
      </a>

      <div class="ml-auto self-center relative">
        <ul class="font-medium flex md:flex-row">
          <li v-show="!(store.isLogin || isLogin)">
            <button id="button-header" @click="showLoginModal" class="text-[2.5vh] cursor-grab">
              로그인
            </button>
            <ModalView v-if="isLoginModal" :show-modal="isLoginModal" @close-modal="showLoginModal">
              <LoginModal />
            </ModalView>
          </li>
          <li v-show="store.isLogin || isLogin">
            <div class="flex flex-row relative justify-center gap-[2.5vh] text-[2.5vh]">
              <div class="flex justify-center items-center rounded-full mr-[2lvw]">
                <img
                  class="overflow-hidden rounded-full w-[7vh] h-[7vh] m-0 mr-[1vw]"
                  :src="image"
                />
                <p class="text-black text-[2.5vh]">{{ name }}님</p>
              </div>
              <div class="relative inline-block m-0 items-center pt-[0.5vh]">
                <button id="button-header" @click="showDropdown">내 서비스</button>
                <div
                  id="dropdown"
                  class="absolute -right-7 mt-2 bg-white divide-y divide-slate-200 rounded-lg w-32"
                  v-show="isShowDropdown"
                >
                  <ul class="w-50">
                    <li id="li-dropdown" class="text-[2.5vh]">
                      <router-link to="mypage">마이페이지</router-link>
                    </li>
                    <li id="li-dropdown" class="text-[2.5vh]">
                      <router-link to="eventlist">내 결과물</router-link>
                    </li>
                  </ul>
                </div>
              </div>
              <button id="button-header" @click="logout">로그아웃</button>
            </div>
          </li>
        </ul>
      </div>
    </nav>
  </header>
</template>

<script setup>
import { ref, onBeforeMount, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import ModalView from '@/views/ModalView.vue'
import LoginModal from '@/components/modal/home/LoginModal.vue'
import tokenService from '@/services/token.service'
import Swal from 'sweetalert2'

const isShowDropdown = ref(false)

const showDropdown = () => (isShowDropdown.value = !isShowDropdown.value)

const router = useRouter()
const store = useUserStore()
const isLoginModal = ref(false)
const isTransparent = ref(false)
const image = ref('')
const name = ref('')
const isLogin = ref(false)
const limitHeight = 200

// header 홈뷰에서 새로고침
const reloadPage = () => {
  router.push('/').then(() => {
    window.location.reload()
  })
}

const showLoginModal = () => {
  isLoginModal.value = !isLoginModal.value
}
// 스크롤 일정 높이 이상 내려가면 transparent
const handleScroll = () => {
  if (scrollY > limitHeight) isTransparent.value = true
  if (scrollY < limitHeight) isTransparent.value = false
}
// 컨펌 후 로그아웃
const logout = () => {
  Swal.fire({
    title: '로그아웃 하시겠습니까?',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: '네',
    cancelButtonText: '돌아가기'
  }).then((result) => {
    if (result.isConfirmed) {
      Swal.fire({
        title: '로그아웃 되었습니다!',
        icon: 'info'
      }).then(() => {
        // tokenservice로 cookie 저장된 정보 삭제 + 로그아웃 처리
        tokenService.removeUser()
        isLogin.value = false
        store.isLogin = false
        router.push('/')
      })
    }
  })
}

onBeforeMount(() => {
  window.addEventListener('scroll', handleScroll)
  // 브라우저를 재연결시 이미 쿠키에 저장된 토큰 만료 여부 처리
  if ($cookies.isKey('user')) {
    if (tokenService.expiredToken($cookies.get('token'))) {
      $cookies.keys().forEach((cookie) => $cookies.remove(cookie))
    } else {
      isLogin.value = true
      image.value = $cookies.get('user').image
      name.value = $cookies.get('user').name
    }
  }
})

watch(
  // store에서 새로고침 요청이 들어오면 (로그인 및 회원가입 성공시)
  () => store.forceReload,
  (newValue, oldValue) => {
    if (newValue === true) {
      // 알림 후 새로고침
      Swal.fire({
        icon: 'success',
        title: `환영합니다, ${store.name} 님!`
      }).then((result) => {
        if (result.isConfirmed) {
          store.forceReload = false
          setTimeout(reloadPage, 100)
        }
      })
    }
  }
)
</script>

<style scoped>
.transparent-header {
  @apply opacity-0 transition-opacity duration-500 z-10 pointer-events-none;
}

li {
  @apply m-[1vw] p-[1vw];
}
</style>
