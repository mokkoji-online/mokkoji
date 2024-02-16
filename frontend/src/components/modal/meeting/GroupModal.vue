<template>
  <div class="fixed w-full h-full flex justify-center items-center">
    <div
      class="p-[5vh] fixed w-[35vw] h-[80vh] top-[3%] bg-white border-sm border-gray rounded-r-lg"
    >
      <div class="pb-[4vh] h-[5%] flex items-center">
        <div class="basis-4/12 font-bold text-r-md">소그룹 생성하기</div>
        <button
          class="ml-auto w-[4vh] aspect-square flex justify-center rounded-r-xl hover:bg-gray"
          @click="$emit('remove-group-modal')"
        >
          <IconCancelBlack class="size-[50%]" />
        </button>
      </div>
      <div class="mb-[1vh] p-[2vh] h-[90%] bg-purple-100 rounded-r-lg">
        <div class="h-[100%] flex justify-center items-center space-x-[1vw]">
          <div class="w-[70%] h-full bg-white rounded-r-lg flex flex-col items-center">
            <div
              class="pl-[1vw] mb-[1vh] w-full h-[10%] bg-purple-200 font-bold text-r-sm rounded-r-md flex items-center"
            >
              참여 가능한 참여자
            </div>
            <input
              type="text"
              placeholder="참여자명 검색"
              class="m-0 p-0 mb-[1vh] w-[95%] h-[10%] border-sm bg-gray rounded-full text-r-sm pl-[2vh] hover:bg-neutral-200 focus:bg-neutral-200"
              v-model="searchUserName"
            />
            <div class="w-full h-[80%] flex flex-col justify-start items-center overflow-y-scroll">
              <user-list
                v-for="(sub, idx) in subscribers"
                :key="sub"
                :user-index="idx"
                :stream-manager="sub"
                :search-user-name="searchUserName"
                :button-type="buttonType"
                @user-checked="handleUserChecked"
                @user-unchecked="handleUserChecked"
              />
            </div>
          </div>
        </div>
      </div>
      <div class="h-[5%] flex justify-end">
        <button
          id="create-button"
          class="w-[6vw] h-[6vh] bg-purple-200 font-bold text-r-sm text-center rounded-r-lg hover:bg-purple-300"
          @click="createGroupMeeting"
        >
          생성하기
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import UserList from '@/components/meeting/UserList.vue'
import IconCancelBlack from '@/icons/meeting/IconCancelBlack.vue'

const props = defineProps({
  subscribers: {
    type: Object
  },
  buttonType: {
    type: String
  }
})

const emit = defineEmits(['remove-group-modal']['create-group-meeting'])

const searchUserName = ref('')
const checkedUserList = ref([])

const handleUserChecked = (checkedUser) => {
  if (checkedUser.userName !== undefined) {
    checkedUserList.value.push(checkedUser)
  } else {
    checkedUserList.value.forEach((user, index) => {
      if (user.userIndex === checkedUser.userIndex) {
        checkedUserList.value.splice(index, 1)
      }
    })
  }
}

const createGroupMeeting = () => {
  emit('create-group-meeting', checkedUserList)
}
</script>

<style>
input::placeholder {
  align-content: center;
  font-size: 1vw;
}

#submit-button:hover {
  background-color: #c084fc;
}

.bg-purple-200 {
  background-color: #e7c6ff;
}
</style>
