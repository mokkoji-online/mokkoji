<template>
  <main>
    <section class="pt-[20vh] z-2 h-full pb-[15vh]" id="main-gradient2">
      <div id="flex" class="h-full ml-[3vw] flex justify-center items-center">
        <div
          class="h-full text-center ml-[2vw] w-1/2 flex flex-col justify-center items-center whitespace-wrap"
        >
          <div class="m-2 rounded-2xl p-6">
            <h1 id="title" class="underline decoration-red-300">모꼬지</h1>
            <div class="text-[1.5vw]">
              <p>
                화상 행사 플랫폼 ‘모꼬지’를 통해
                <strong class="text-purple-500">결혼식, 졸업식, 돌잔치</strong> 등
              </p>
              <p class="mb-[1vw]">
                다양한 행사를
                <strong class="text-purple-500">집에서 편하게</strong> 참여할 수 있어요.
              </p>
              <br />
              <p>순간을 더욱 특별하게 추억하기 위한</p>
              <p>
                <strong class="text-purple-500">롤링페이퍼, 포토 모자이크 기능</strong> 을
                제공합니다.
              </p>
            </div>
          </div>
          <div class="flex flex-col mt-[2vw] justify-center items-center">
            <button
              class="my-[1vh] rounded-r-lg hover:bg-purple-500 text-[1.2vw] text-white w-[15vw] aspect-[5] bg-purple-400"
              @click="createMeeting"
            >
              모꼬지 시작
            </button>
            <div>
              <div
                class="flex items-center border-[0.3vb] bg-white border-slate-500 rounded-r-lg w-[15vw] aspect-[5]"
              >
                <input
                  type="text"
                  placeholder="행사 링크(ID)로 참여"
                  v-model="conferenceIdInput"
                  @keyup.enter="submitConferenceId"
                  class="m-0 p-0 pl-[2.5vw] border-none w-[90%] h-full rounded-r-lg text-[1.2vw]"
                />
                <button
                  @click="submitConferenceId"
                  class="rounded-full mr-[0.3vw] size-[2vw] aspect-square bg-primary hover:bg-primary3"
                >
                  <img src="@/assets/landing/send.png" class="size-[1.5vw]" />
                </button>
              </div>
              <p v-if="isInputError" style="color: red" class="text-[2vh]">
                올바른 행사 ID가 아닙니다
              </p>
            </div>
          </div>
        </div>
        <div class="w-1/2 h-full">
          <div class="w-[70%] aspect-square border-[0.6vb] border-white cursor-grab rounded-r-xl">
            <swiper
              :spaceBetween="30"
              :autoplay="{
                delay: 5000,
                pauseOnMouseEnter: true,
                waitForTransition: 500
              }"
              :pagination="{
                clickable: true
              }"
              :loop="true"
              :modules="modules"
              class="rounded-r-xl"
            >
              <swiper-slide v-for="photo in photos" :key="photo" class="w-[70%]">
                <img
                  :src="`src/assets/landing/${photo}`"
                  class="w-[100%] rounded-r-xl aspect-square"
                />
              </swiper-slide>
            </swiper>
          </div>
        </div>
      </div>
    </section>

    <!-- first -->
    <section id="section-main">
      <div class="w-full overflow-hidden">
        <svg
          id="svg-curve-1"
          xmlns="http://www.w3.org/2000/svg"
          viewBox="0 0 1200 120"
          preserveAspectRatio="none"
        >
          <path
            d="M321.39,56.44c58-10.79,114.16-30.13,172-41.86,82.39-16.72,168.19-17.73,250.45-.39C823.78,31,906.67,72,985.66,92.83c70.05,18.48,146.53,26.09,214.34,3V0H0V27.35A600.21,600.21,0,0,0,321.39,56.44Z"
          ></path>
        </svg>
      </div>
      <div
        class="mt-[2vh] mb-[4vh] grid grid-cols-2 justify-center items-center"
        data-aos="fade-up"
      >
        <div class="ml-[8vw] px-[vw] text-center">
          <h1 id="title-sub-bold">롤링페이퍼를 통해 친구들의</h1>
          <h1 id="title-sub-bold">한 마디를 간직하세요.</h1>

          <br />
          <p id="p-main">모꼬지만의 템플릿을 이용하여 롤링페이퍼를 디자인하고 추억하세요.</p>
          <p id="p-main">참여자는 행사중 언제나 텍스트, 음성, 영상 메시지를 기록할 수 있습니다.</p>
        </div>
        <div class="flex justify-center items-center">
          <img src="@/assets/landing/main1.png" class="size-[60%]" />
        </div>
      </div>
      <div id="space"></div>
    </section>

    <!-- second -->
    <section id="section-main" class="bg-natural-yellow">
      <div class="w-full overflow-hidden">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          viewBox="0 0 1200 120"
          preserveAspectRatio="none"
          id="svg-curve-2"
        >
          <path
            d="M321.39,56.44c58-10.79,114.16-30.13,172-41.86,82.39-16.72,168.19-17.73,250.45-.39C823.78,31,906.67,72,985.66,92.83c70.05,18.48,146.53,26.09,214.34,3V0H0V27.35A600.21,600.21,0,0,0,321.39,56.44Z"
            class="shape-fill"
          ></path>
        </svg>
      </div>
      <div class="mt-[2vh] mb-[2vh] flex grid-cols-2 items-center" data-aos="fade-up">
        <div class="basis-2/5 flex justify-end">
          <img class="size-[60%]" src="@/assets/landing/main2.png" />
        </div>
        <div class="basis-3/5 text-center mr-[4vw]">
          <h2 id="title-sub-bold">포토 모자이크로 사진들을</h2>
          <h2 id="title-sub-bold">한 눈에 구경하세요.</h2>

          <br />
          <p id="p-main">
            포토 모자이크 기능은 서로 다른 사진들을 조합하여 하나의 이미지를 표현합니다.
          </p>
          <p id="p-main">행사 사진을 업로드하고 포토 모자이크 기술을 경험해 보세요.</p>
        </div>
      </div>
      <div id="space"></div>
    </section>

    <!-- third -->
    <section id="section-main" class="bg-natural-beige">
      <div class="w-full overflow-hidden">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          viewBox="0 0 1200 120"
          preserveAspectRatio="none"
          id="svg-curve-3"
        >
          <path
            d="M321.39,56.44c58-10.79,114.16-30.13,172-41.86,82.39-16.72,168.19-17.73,250.45-.39C823.78,31,906.67,72,985.66,92.83c70.05,18.48,146.53,26.09,214.34,3V0H0V27.35A600.21,600.21,0,0,0,321.39,56.44Z"
            class="shape-fill"
          ></path>
        </svg>
      </div>
      <div class="mt-[2vh] grid grid-cols-2 items-center" data-aos="fade-up">
        <div class="ml-[5vw] text-center">
          <h1 id="title-sub-bold">소그룹을 형성하여 친구들과</h1>
          <h1 id="title-sub-bold">자유롭게 소통하세요.</h1>

          <br />
          <p id="p-main">소그룹 기능을 이용하여 소통하고 싶은 다른 참여자들과</p>
          <p id="p-main">사적인 공간에서 자유롭게 이야기 할 수 있습니다.</p>
        </div>
        <div class="flex justify-center items-center mr-[5vw]">
          <img src="@/assets/landing/main3.png" class="size-[60%]" />
        </div>
      </div>
      <div id="space"></div>
    </section>

    <!-- fourth -->
    <section id="section-main" class="bg-primary2">
      <div class="w-full overflow-hidden">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          viewBox="0 0 1200 120"
          preserveAspectRatio="none"
          id="svg-curve-4"
        >
          <path
            d="M321.39,56.44c58-10.79,114.16-30.13,172-41.86,82.39-16.72,168.19-17.73,250.45-.39C823.78,31,906.67,72,985.66,92.83c70.05,18.48,146.53,26.09,214.34,3V0H0V27.35A600.21,600.21,0,0,0,321.39,56.44Z"
            class="shape-fill"
          ></path>
        </svg>
      </div>
      <div class="mt-[1vh] grid grid-cols-2 items-center" data-aos="fade-up">
        <div class="flex justify-center">
          <img src="@/assets/landing/main4.png" class="size-[60%]" />
        </div>
        <div class="mr-[10vw] text-center">
          <h1 id="title-sub-bold">QR코드를 통해 축하금을</h1>
          <h1 id="title-sub-bold">간편하게 보내세요.</h1>

          <br />
          <p id="p-main">화상 행사 내에서 주최자의 계좌와 연동된 QR코드가 제공됩니다.</p>
          <p id="p-main">메세지로 다 담지 못한 축하하는 마음을 전달해보세요.</p>
        </div>
      </div>
      <div id="space"></div>
    </section>
    <section class="flex items-center justify-center py-[5vh] bg-primary2">
      <img src="@/assets/logo/mokkoji_logo.png" class="w-[20vh] px-[3vh]" />

      <h1 class="text-[2vw] font-bold">모꼬지를 사용하고 싶으신가요?</h1>
      <button
        @click="toTop"
        class="animate-bounce duration-700 text-r-md my-[1vh] py-[1vh] rounded-r-lg hover:bg-blue-500 text-white duration-300 w-fit px-[2vh] mx-[2vh] text-[1vw] bg-blue-400"
      >
        처음으로 돌아가기
      </button>
    </section>
  </main>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Swiper, SwiperSlide } from 'swiper/vue'
import { Autoplay, Pagination } from 'swiper/modules'
import { useSessionStore } from '@/stores/meeting'
import AOS from 'aos'
import Swal from 'sweetalert2'
import 'vue3-toastify/dist/index.css'
import 'swiper/css'
import 'swiper/css/pagination'
import 'aos/dist/aos.css'

const emit = defineEmits(['load-home'])

const store = useSessionStore()
const router = useRouter()

const conferenceIdInput = ref('')
const isInputError = ref(false)

const modules = [Autoplay, Pagination]
const photos = ['carousel1.png', 'carousel2.png', 'carousel3.png', 'carousel4.png']

const submitConferenceId = async () => {
  const result = await store.findSession(conferenceIdInput.value)
  console.log($cookies.get('user'))

  if (result === 'success') {
    isInputError.value = false

    if ($cookies.get('user') !== undefined && $cookies.get('user') !== null) {
      router.push('/meetings')
    } else {
      Swal.fire({
        title: '잠깐! 로그인을 하지 않으셨습니다.',
        text: `로그인을 하지 않으면 롤링페이퍼 등 일부 모꼬지 서비스를 이용할 수 없어요.`,
        showCancelButton: true,
        confirmButtonText: '알겠어요',
        cancelButtonText: '돌아가기'
      }).then((result) => {
        if (result.isConfirmed) {
          Swal.fire({
            title: '사용자 이름 설정하기',
            text: '참가자들에게 이름을 알려주세요!',
            input: 'text',
            inputLabel: '참가자명 정하기',
            inputPlaceholder: '참가자명은 6자 이내여야 합니다.',
            showCancelButton: true,
            confirmButtonText: '참가하기',
            cancelButtonText: '돌아가기'
          }).then((inputResult) => {
            if (inputResult.isConfirmed) {
              const userInput = inputResult.value.trim()
              if (userInput.length === 0) {
                Swal.fire({
                  title: '이름은 빈칸일 수 없어요.',
                  icon: 'warning',
                  allowOutsideClick: false,
                  showCloseButton: true
                })
              } else if (userInput.length > 6) {
                Swal.fire({
                  title: '6자 이내의 이름을 입력해 주세요.',
                  icon: 'warning',
                  allowOutsideClick: false
                })
              } else {
                sessionStorage.setItem('userName', userInput)
                router.push('/meetings')
              }
            }
          })
        } else {
          Swal.close()
        }
      })
    }
  } else {
    isInputError.value = true
    conferenceIdInput.value = ''
  }
}

const createMeeting = () => {
  if ($cookies.get('user') !== null) {
    store.createSession()
  } else {
    Swal.fire({
      title: '로그인이 필요합니다.',
      text: '서비스를 사용하기 위해 로그인을 해주세요',
      icon: 'warning'
    })
  }
}

const toTop = () => {
  window.scrollTo({
    top: 0,
    behavior: 'smooth'
  })
}

document.addEventListener('DOMContentLoaded', () => {
  setTimeout(() => {
    AOS.refresh()
  }, 500)
})

onMounted(() => {
  AOS.init()
  window.scrollTo(0, 0)
  emit('load-home')
})
</script>

<style scoped>
.swiper-pagination-bullet {
  @apply size-[0.6lvw] bg-white;
}

.swiper-pagination-bullet-active {
  @apply bg-primary3 size-[0.75lvw];
}
</style>
