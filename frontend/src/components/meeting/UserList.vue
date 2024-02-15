<template>
  <div
    v-if="shouldRender"
    action="/ses_AucL2KJFyW"
    name="group-user-list"
    accept-charset="utf-8"
    class="mb-[1vh] w-[95%] h-[15%] bg-white border-sm border-purple-300 rounded-r-md flex items-center"
  >
    <div class="w-full h-full flex items-center">
      <img
        src="@/assets/landing/profile.png"
        alt="사용자 프로필"
        class="ml-[0.5vw] h-[90%] aspect-square rounded-full"
      />
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
            <IconCheck v-if="isChecked" class="size-[100%]" />
          </div>
        </label>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
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
const userName = ref('')
const hostName = ref(sessionStorage.getItem('host'))
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

// 조건부 렌더링을 활용해 소그룹 리스트에서는 호스트가 보이지 않고
// 참여자 목록에서는 호스트가 보이도록 값 지정
const shouldRender = computed(() => {
  if (props.buttonType === 'user-list') {
    return props.streamManager && userName.value.includes(props.searchUserName)
  } else {
    return (
      props.streamManager &&
      userName.value.includes(props.searchUserName) &&
      userName.value !== hostName.value
    )
  }
})

onMounted(() => {
  getUserName()
  // 사용자 별로 체크박스 영역 나누기
  checkBox.value = document.getElementById(`user-${props.userIndex}`)
})
</script>

<style scoped>
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
