<template>
  <div
    v-if="streamManager && userName.includes(props.searchUserName)"
    action="/ses_AucL2KJFyW"
    name="group-user-list"
    accept-charset="utf-8"
    class="mb-[1vh] w-[95%] h-[15%] bg-white border-sm border-purple-300 rounded-r-md flex items-center"
  >
    <div class="w-full h-full flex items-center">
      <img
        src="@/assets/landing/profile_icon.jpg"
        alt=""
        class="ml-[0.5vw] h-[80%] aspect-square rounded-full"
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
            <IconCheck v-if="isChecked" class="h-[4vh]" />
          </div>
        </label>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
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
const checkBox = ref()

const clientData = () => {
  const { clientData } = getConnectionData()
  userName.value = clientData
}

// 사용자 데이터 가져오기
const getConnectionData = () => {
  const { connection } = props.streamManager.stream
  return JSON.parse(connection.data)
}

const checkUser = () => {
  isChecked.value = !isChecked.value
}

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

onMounted(() => {
  clientData()
  checkBox.value = document.getElementById(`user-${props.userIndex}`)
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
