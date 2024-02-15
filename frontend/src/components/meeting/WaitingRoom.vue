<template>
  <div
    class="flex items-center justify-center w-full h-screen text-gray-900 px-[2vw]"
    id="main-gradient3"
  >
    <div
      class="flex flex-col justify-center items-center w-full h-3/4 mx-[14vw] bg-white rounded-r-xl shadow-md border-sm border-yellow-100"
    >
      <div class="relative w-full h-[100%] flex items-center">
        <div class="w-1/2 h-full flex justify-center items-center">
          <img src="@/assets/logo/mokkoji_logo.png" alt="" class="h-[80%]" />
        </div>
        <div
          class="w-1/2 h-full pr-[7vh] flex flex-col justify-center items-center right-[8%] text-r-md font-bold"
        >
          <h1 class="text-[2.6lvw]">모꼬지에 오신걸 환영합니다!</h1>
          <br />
          <div class="text-[1.8lvw]">행사에서 사용할 이름을 입력하세요</div>
          <input
            type="text"
            name="userName"
            id="userName"
            placeholder="최대 6글자"
            maxlength="6"
            v-model="userName"
            class="m-0 p-0 my-[2vh] w-[40%] h-[10%] border-sm border-purple-200 text-r-md text-center focus:bg-purple-100"
          />
          <p v-if="isInputEmpty" style="color: red" class="text-r-md mb-[2vh]">
            이름을 입력해 주세요.
          </p>
          <p v-if="isInputError" style="color: red" class="text-r-md mb-[2vh]">
            존재하지 않는 행사 주소입니다.
          </p>
          <button
            class="w-[30%] aspect-[3] bg-purple-200 rounded-r-lg hover:bg-purple-300"
            @click="joinMeeting"
          >
            행사 참가
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeMount } from 'vue'
import { useRouter } from 'vue-router'
import { useSessionStore } from '@/stores/meeting'

const props = defineProps({
  sessionId: {
    type: String
  }
})

const emit = defineEmits(['waiting-room']['leave-meeting'])

const router = useRouter()
const store = useSessionStore()

const isInputEmpty = ref(false)
const isInputError = ref(false)
const userName = ref('')

const joinMeeting = async () => {
  if (userName.value === '') {
    isInputEmpty.value = true
  } else {
    isInputEmpty.value = false

    const result = await store.findSession(props.sessionId)

    if (result === 'success') {
      isInputError.value = false
      sessionStorage.setItem('userName', userName.value)
      router.push('/meetings')
    } else {
      isInputError.value = true
    }
  }
}

onMounted(() => {
  emit('waiting-room')
})

onBeforeMount(() => {
  emit('leave-meeting')
})
</script>

<style scoped></style>
