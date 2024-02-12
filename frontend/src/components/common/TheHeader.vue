<template>
  <header
    :class="{ 'transparent-header': isTransparent }"
    class="fixed h-[10vh] z-10 my-auto w-full bg-transparent"
  >
    <nav class="w-full flex items-center px-[2vw]">
      <router-link to="/" class="w-[8vw] flex items-center rtl:l:space-x-reverse">
        <img
          src="/src/assets/logo/mokkoji_logo.png"
          class="w-[8vw] transition ease-in-out hover:animate-pulse"
          alt="모꼬지 로고"
        />
        <!-- <span id="title-bold">모꼬지</span> -->
      </router-link>

      <div class="ml-auto self-center">
        <ul class="font-medium flex md:flex-row ml-10">
          <button id="button-header"><a href="/" class="text-[3vh]">홈으로</a></button>
          <li v-if="!(store.isLogin || isLogin)">
            <button id="button-header" @click="showLoginModal" class="text-[3vh]">로그인</button>
            <ModalView v-if="isLoginModal" :show-modal="isLoginModal" @close-modal="showLoginModal">
              <LoginModal />
            </ModalView>
          </li>
          <li v-else>
            <div class="flex flex-row relative justify-center items-center gap-5">
              <button
                id="button-header"
                data-dropdown-toggle="dropdown"
                data-dropdown-trigger="click"
              >
                내 서비스
              </button>
              <div class="flex justify-center items-center gap-2 rounded-full mx-2">
                <img class="overflow-hidden rounded-full w-12 m-0;" :src="image" />
                <p class="text-black text-[3vh]">{{ name }}님</p>
              </div>
              <div
                id="dropdown"
                class="z-10 hidden bg-white divide-y divide-slate-200 rounded-lg w-32"
              >
                <ul aria-labelledby="dropdownHoverButton" class="w-50">
                  <li id="li-dropdown" class="text-[2vh]">
                    <router-link to="mypage">마이페이지</router-link>
                  </li>
                  <li id="li-dropdown" class="text-[2vh]">
                    <router-link to="eventlist">내 결과물</router-link>
                  </li>
                  <li id="li-dropdown" class="text-[2vh]">
                    <a @click="logout">로그아웃</a>
                  </li>
                </ul>
              </div>
            </div>
          </li>
        </ul>
      </div>
    </nav>
  </header>
</template>

<script setup>
import { ref, onBeforeMount } from 'vue'
import { initFlowbite } from 'flowbite'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import ModalView from '@/views/ModalView.vue'
import LoginModal from '@/components/modal/home/LoginModal.vue'
import tokenService from '@/services/token.service'

const router = useRouter()
const store = useUserStore()
const isLoginModal = ref(false)
const isTransparent = ref(false)
const image = ref('')
const name = ref('')
const isLogin = ref(false)

const limitHeight = 200

initFlowbite()

const showLoginModal = () => {
  isLoginModal.value = !isLoginModal.value
}

const handleScroll = () => {
  if (scrollY > limitHeight) isTransparent.value = true
  if (scrollY < limitHeight) isTransparent.value = false
}

const logout = () => {
  tokenService.removeUser()
  isLogin.value = false
  store.isLogin = false
  router.push('/')
}

onBeforeMount(() => {
  window.addEventListener('scroll', handleScroll)
  if ($cookies.get('user') !== null) {
    isLogin.value = true
    image.value = $cookies.get('user').image
    name.value = $cookies.get('user').name
  }
})

</script>

<style>
.transparent-header {
  @apply opacity-0 transition-opacity duration-500 z-10;
  pointer-events: none;
}

li {
  @apply m-2 p-5;
}
</style>
