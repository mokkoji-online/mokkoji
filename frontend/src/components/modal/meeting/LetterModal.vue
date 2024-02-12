<template>
  <div
    id="letter-container"
    class="fixed py-[4vh] px-[3vw] w-[37vw] left-[33vw] aspect-square bg-yellow-100 flex flex-col justify-center rounded-r-lg"
  >
    <div class="flex pb-[4vb] w-[31vw] items-center">
      <div class="text-[1.5vw] basis-1/2 font-bold">롤링페이퍼 작성</div>
      <button
        class="ml-auto w-[3vw] hover:bg-red-100 aspect-square rounded-full"
        @click="$emit('remove-letter-modal')"
      >
        <IconCancelBlack class="size-[50%]" />
      </button>
    </div>
    <div class="flex flex-col justify-center">
      <div class="p-[2vw] flex flex-col border-sm rounded-r-lg">
        <textarea
          name=""
          id="input-text"
          value=""
          cols="30"
          rows="10"
          placeholder="메시지를 입력하세요"
          class="h-[90%] bg-yellow-100 rounded-r-lg resize-none placeholder:text-[1.5vw] text-center text-r-md font-bold focus:outline-none"
          v-model="textFile"
        >
        </textarea>
        <div class="flex h-[6vh]">
          <div class="basis-1/2 flex justify-start">
            <label
              className="input-audio-button"
              for="input-audio"
              class="mr-[1vw] hover:bg-red-100 w-[3vw] aspect-square rounded-full flex justify-center items-center"
              @click="$emit('show-audio-recorder')"
            >
              <IconAudio class="size-[90%] hover:cursor-pointer" />
            </label>
            <!-- <input type="file" id="input-audio" class="hidden" @change="uploadAudioFile" /> -->
            <label
              for="input-video"
              class="hover:bg-red-100 w-[3vw] aspect-square rounded-full flex justify-center items-center"
            >
              <IconVideo class="size-[85%] hover:cursor-pointer" />
            </label>
            <input type="file" id="input-video" class="hidden" @change="uploadVideoFile" />
          </div>
          <div class="basis-1/2 flex justify-end">
            <button
              type="button"
              class="hover:bg-red-100 w-[3vw] aspect-square rounded-full flex justify-center items-center"
              @click="removeContents"
            >
              <IconRemove class="size-[90%]" />
            </button>
          </div>
        </div>
      </div>
      <div>
        <!-- <audio-recorder src=""></audio-recorder> -->
      </div>
      <div class="pt-[1vw] pb-[1vw] flex flex-wrap">
        <div
          v-if="isAudioFile"
          class="text-black mr-[1vw] w-[15vw] h-[5vh] border-sm rounded-r-lg flex items-center"
        >
          <div class="ml-[0.5vw] flex">
            <img
              src="@/assets/meeting/clip.png"
              alt="파일 클립 이미지"
              class="mr-[0.5vw] w-[1.5vw] size-[70%]"
            /><strong class="text-r-sm w-[10.5vw] whitespace-nowrap overflow-hidden"
              >음성파일: {{ audioFileName }}</strong
            >
            <img
              src="@/assets/meeting/delete.png"
              alt="음성 파일 삭제 버튼"
              class="w-[1.5vw] hover:cursor-pointer"
              @click="removeAudioFile"
            />
          </div>
        </div>
        <div
          v-if="isVideoFile"
          class="text-black w-[15vw] h-[5vh] border-sm rounded-r-lg flex items-center"
        >
          <div class="ml-[0.5vw] text-r-sm whitespace-nowrap overflow-hidden flex">
            <img
              src="@/assets/meeting/clip.png"
              alt="파일 클립 이미지"
              class="mr-[0.5vw] w-[1.5vw] size-[70%]"
            /><strong class="text-r-sm w-[10.5vw] whitespace-nowrap overflow-hidden"
              >영상파일: {{ videoFileName }}</strong
            >
            <img
              src="@/assets/meeting/delete.png"
              alt="영상 파일 삭제 버튼"
              class="ml-auto w-[1.5vw] hover:cursor-pointer"
              @click="removeVideoFile"
            />
          </div>
        </div>
      </div>
      <div class="flex">
        <p v-if="isFileCheck" style="color: red" class="text-[1.4vw]">
          작성한 내용이 존재하지 않습니다.
        </p>
        <button
          type="submit"
          form="letterForm"
          class="ml-auto w-[7vw] aspect-[2] text-r-md bg-red-200 hover:bg-red-300 border-sm rounded-r-lg font-semibold"
          @click="sendLetter"
        >
          작성하기
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useLetterStore } from '@/stores/meeting'
import IconAudio from '@/icons/meeting/IconAudio.vue'
import IconVideo from '@/icons/meeting/IconVideo.vue'
import IconRemove from '@/icons/meeting/IconRemove.vue'
import IconCancelBlack from '@/icons/meeting/IconCancelBlack.vue'

defineEmits(['remove-letter-modal']['show-audio-recorder'])

const store = useLetterStore()

const isFileCheck = ref(false)
const isVideoFile = ref(false)
const isAudioFile = ref(false)
const videoFile = ref(null)
const audioFile = ref(null)
const textFile = ref('')
const videoFileName = ref('')
const audioFileName = ref('')

const removeContents = () => {
  document.getElementById('input-text').value = ''
  textFile.value = ''
}

const uploadAudioFile = (event) => {
  isAudioFile.value = true
  audioFile.value = event.target.files[0]
  audioFileName.value = event.target.files[0].name
}

const uploadVideoFile = (event) => {
  isVideoFile.value = true
  videoFile.value = event.target.files[0]
  videoFileName.value = event.target.files[0].name
}

const removeAudioFile = (event) => {
  isAudioFile.value = false
  audioFile.value = null
}

const removeVideoFile = (event) => {
  isVideoFile.value = false
  videoFile.value = null
}

const sendLetter = () => {
  if (videoFile.value === null && audioFile.value === null && textFile.value === '') {
    isFileCheck.value = true
    return
  } else {
    store.sendLetter(videoFile.value, audioFile.value, textFile.value)
  }

  isFileCheck.value = false
  audioFile.value = null
  videoFile.value = null
  textFile.value = ''
}
</script>

<style></style>
