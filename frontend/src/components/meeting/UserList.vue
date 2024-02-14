<template>
  <div
    v-if="streamManager && userName.includes(props.searchUserName)"
    action="/ses_AucL2KJFyW"
    name="group-user-list"
    accept-charset="utf-8"
    class="mb-[1vh] w-[95%] h-[15%] bg-white border-sm border-purple-300 rounded-r-md flex items-center"
  >
    <div class="w-full h-full flex items-center">
      <img :src="image" alt="사용자 프로필" class="ml-[0.5vw] h-[80%] aspect-square rounded-full" />
      <div class="ml-[1vw] text-[1vw] w-[40%] flex">
        {{ userName }}
        <div v-if="buttonType === 'user-list' && userName === myName" class="ml-[0.3vw]">(나)</div>
      </div>

      <div v-if="buttonType === 'group'" class="mr-[1vw] w-full h-full flex justify-end">
        <input
          type="checkbox"
          name="user"
          :id="`user-${userIndex}`"
          class="user-check"
          :value="userName"
          @click="handleUserCheck"
        />
        <label
          :for="`user-${userIndex}`"
          class="flex justify-center items-center"
          @click="checkUser"
        >
          <div class="flex justify-center items-center hover:bg-[#e7c6ff]">
            <IconCheck v-if="isChecked" class="h-[4vh]" />
          </div>
        </label>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onBeforeMount } from 'vue'
import IconCheck from '@/icons/meeting/IconCheck.vue'

const props = defineProps({
  userIndex: {
    type: Number
  },
  streamManager: {
    type: Object
  },
  myName: {
    type: String
  },
  searchUserName: {
    type: String
  },
  buttonType: {
    type: String
  }
})

const emit = defineEmits(['user-checked']['user-unchecked'])

const isChecked = ref(false)
const image = ref('')
const userName = ref('')
const checkBox = ref()

// 사용자 이름 가져오기
const getUserName = () => {
  const { clientData } = getConnectionData()
  userName.value = clientData
}

// 연결돼 있는 사용자 정보 가져오기
const getConnectionData = () => {
  const { connection } = props.streamManager.stream
  return JSON.parse(connection.data)
}

// 사용자 소그룹 초대 여부 확인
const checkUser = () => {
  isChecked.value = !isChecked.value
}

// 소그룹에 초대하고 싶은 사용자들 체크하기
const handleUserCheck = () => {
  if (checkBox.value.checked) {
    const checkedUser = {
      userIndex: props.userIndex,
      userName: userName.value
    }
    emit('user-checked', checkedUser)
  } else {
    const uncheckedUser = {
      userIndex: props.userIndex,
      userName: undefined
    }
    emit('user-unchecked', uncheckedUser)
  }
}

onBeforeMount(() => {
  getUserName()

  // 사용자 별로 체크박스 영역 나누기
  checkBox.value = document.getElementById(`user-${props.userIndex}`)

  // 로그인 유저는 프로필 정보를 가져오고 비로그인 유저는 디폴트 프로필 사진 부여
  if ($cookies.get('user') !== null) {
    image.value = $cookies.get('user').image
  } else {
    image.value = '@/assets/landing/profile_icon.jpg'
  }
})
</script>

<style>
input.user-check {
  display: none;
}

input.user-check + label div {
  display: inline-block;
  width: 2.5vw;
  aspect-ratio: 1/1;
  vertical-align: middle;
  border-radius: 1vb;
  cursor: pointer;
  border: 0.3vb solid #e7c6ff;
}

input.user-check:checked + label div {
  background: #e7c6ff;
}
</style>
