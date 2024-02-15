<template>
  <div id="main-gradient2" class="py-40">
    <div class="mx-auto w-[40lvw] bg-white rounded-lg shadow-md p-20">
      <div class="space-y-2 text-center">
        <h1 class="text-3xl font-bold">모꼬지 시작하기</h1>
        <p class="text-slate-400">회원정보를 입력해요</p>
      </div>
      <div class="space-y-4">
        <div class="space-y-2">
          <label class="text-sm font-medium text-slate-500" for="email"> 이메일 </label>
          <input
            class="flex h-10 w-full bg-background px-3 py-2 text-sm border-2 border-gray-300 rounded-md"
            v-model="email"
            placeholder="example@email.com"
          />
        </div>

        <div class="space-y-2">
          <label class="text-sm font-medium text-slate-500" for="name"> 이름 </label>
          <input
            class="flex h-10 w-full bg-background px-3 py-2 text-sm border-2 border-gray-300 rounded-md"
            v-model="name"
          />
        </div>

        <div class="flex items-center space-x-4">
          <div class="flex-auto w-max m-5">
            <label>
              <img id="image-profile" :src="image" class="w-40" />
            </label>
            <input
              class="mx-auto h-10 w-full rounded-md border-2 border-slate-200 bg-background px-1 py-2 text-sm file:border-0 file:bg-transparent file:text-sm"
              id="profile-picture"
              type="file"
              @change="getFileName($event.target.files)"
            />
          </div>
        </div>
        <div>
          <div class="p-2">
            <label class="text-sm font-light"> 계좌번호 </label>

            <div class="flex flex-row items-baseline">
              <select
                v-model="bank"
                id="input"
                aria-placeholder="은행명"
                class="h-10 border-2 border-slate-300"
              >
                <option v-for="bank in banks" :key="bank" :value="bank">
                  {{ bank }}
                </option>
              </select>
              <input
                id="input"
                class="w-auto border-2 border-slate-300"
                placeholder="계좌번호를 입력하세요"
                v-model="accountNumber"
              />
            </div>
          </div>
        </div>
      </div>
      <button class="float-right" @click="signUp">회원가입</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onBeforeMount } from 'vue'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'
import axios from '@/services/api'
import Swal from 'sweetalert2'

const router = useRouter()
const store = useUserStore()

const name = ref('')
const image = ref('')
const email = ref('')
const fileName = ref('')

// file input 최근 파일 byte 64로 preview
const getFileName = async (files) => {
  const maxFileSize = 1024 * 1024 * 2
  if (files[0].size > maxFileSize) {
    alert('파일 크기가 2MB를 초과했습니다')
    return
  }

  fileName.value = files[0].name
  await base64(files[0])
}

const base64 = (file) => {
  return new Promise((resolve) => {
    const reader = new FileReader()
    reader.onload = (e) => {
      resolve(e.target.result)
      const previewImage = document.getElementById('image-profile')
      previewImage.src = e.target.result
      console.log(previewImage)
      image.value = previewImage.src0
    }
    reader.readAsDataURL(file)
  })
}
// 은행 선택 및 계좌번호 입력
const banks = ['KB', '농협', '기업', '카카오뱅크', '하나', '신한', 'SC제일']
const bank = ref('')
const accountNumber = ref('')

// 회원가입 axios
const signUp = async () => {
  if (email.value.length === 0) {
    email.value = 'example@email.com'
  }
  await axios({
    url: import.meta.env.VITE_SERVER + '/users',
    method: 'POST',
    data: {
      email: email.value,
      name: name.value,
      image: image.value,
      bank: bank.value,
      accountNumber: accountNumber.value
    }
  })
    .then(() => {
      // 홈 화면 이동 후 새로고침 처리
      store.isLogin = true
      store.forceReload = true
      Swal.fire({
        title: '회원가입 성공!',
        text: '모꼬지 서비스를 사용해보세요',
        icon: 'success'
      }).then((result) => {
        if (result.isConfirmed) {
          if (sessionStorage.getItem('place') === 'meeting') {
            // session storage에 행사 참여 이력이 있으면 행사 화면으로 다시 이동
            if (sessionStorage.getItem('groupSessionId') !== null) {
              router.push('/groupmeetings')
            } else {
              router.push('/meetings')
            }
          } else {
            // 홈 화면 새로고침 메서드
            router.push('/')
          }
        }
      })
    })
    .catch((err) => {
      console.log(err)
    })
}

onBeforeMount(() => {
  name.value = store.name
  image.value = store.image
  email.value = store.email
})
</script>

<style scoped></style>
