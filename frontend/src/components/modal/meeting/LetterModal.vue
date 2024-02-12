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
        <!-- 텍스트 파일 -->
        <textarea
          v-if="!isAudioRecorder && !isVideoRecorder"
          name="textFile"
          id="input-text"
          cols="30"
          rows="10"
          placeholder="메시지를 입력하세요"
          class="h-[90%] bg-yellow-100 rounded-r-lg resize-none placeholder:text-[1.5vw] text-center text-r-md font-bold focus:outline-none"
          v-model="textFile"
        >
        </textarea>
        <!-- 음성 파일 -->
        <div
          v-if="isAudioRecorder && !isVideoRecorder"
          class="h-[90%] aspect-[1.37] bg-yellow-100 rounded-r-xl flex flex-col justify-center items-center"
        >
          <div class="ml-[15vw] w-full flex items-center mb-[1vh]">
            <div class="w-[50%] text-r-md font-semibold">최대 녹음 시간 : 60초</div>
            <button
              class="w-[3vw] aspect-square rounded-full hover:bg-gray hover:cursor-pointer flex justify-center items-center"
              @click="showAudioRecorder"
            >
              <IconCancelBlack class="size-[40%]" />
            </button>
          </div>
          <div class="w-[25vw] bg-purple-200 rounded-r-xl flex items-center">
            <button
              id="button"
              v-if="!isRecording"
              @click="startRecording()"
              :disabled="isRecording"
            >
              <IconRecord class="size-[100%]" />
            </button>
            <button id="button" v-else @click="stopRecording()" :disabled="!isRecording">
              <IconStop class="size-[100%]" />
            </button>
            <button
              id="button"
              v-if="recordedChunks.length !== 0 && !isRecording && !isPlaying"
              @click="playRecording()"
            >
              <IconPlay class="size-[100%]" />
            </button>
            <button
              id="button"
              v-if="recordedChunks.length !== 0 && !isRecording && isPlaying"
              @click="pausePlaying()"
            >
              <IconPause class="size-[90%]" />
            </button>
            <canvas
              ref="soundBarCanvas"
              width="150"
              height="50"
              class="bg-transparent aspect-[3]"
            ></canvas>
            <audio hidden ref="audio" crossorigin="anonymous" @ended="onAudioEnded"></audio>
          </div>
          <div v-if="isRecording" class="mt-[1vh] w-[8vw] aspect-[2.5] text-r-md font-semibold">
            남은 시간 : {{ leftTime }}
          </div>
          <div
            v-if="recordedChunks.length !== 0 && !isRecording"
            class="mt-[1vh] flex justify-center items-center"
          >
            <button
              class="w-[8vw] aspect-[2.5] bg-purple-200 rounded-r-lg text-r-md flex justify-center items-center font-semibold hover:bg-purple-300 hover:cursor-pointer"
              @click="uploadAudioFile('record')"
            >
              파일 업로드
            </button>
          </div>
        </div>
        <!-- 영상 파일 -->
        <div v-if="!isAudioRecorder && isVideoRecorder"></div>
        <div class="flex h-[6vh]">
          <div class="basis-1/2 flex justify-start">
            <label
              class="mr-[1vw] hover:bg-red-100 w-[3vw] aspect-square rounded-full flex justify-center items-center"
              @click="showAudioModal"
            >
              <IconAudio class="size-[90%] hover:cursor-pointer" />
            </label>
            <input type="file" id="input-audio" class="hidden" @change="uploadAudioFile" />
            <label
              class="hover:bg-red-100 w-[3vw] aspect-square rounded-full flex justify-center items-center"
              @click="showVideoModal"
            >
              <IconVideo class="size-[85%] hover:cursor-pointer" />
            </label>
            <!-- <input type="file" id="input-video" class="hidden" @change="uploadVideoFile" /> -->
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
    <!-- 음성, 영상 파일 업로드 모달 -->
    <div
      v-if="isAudioModal"
      class="absolute w-[8vw] top-[54%] left-[7%] text-r-md flex flex-col items-center bg-red-100 rounded-r-lg border-sm"
    >
      <div class="w-full border-b-[0.3vb]">
        <button class="w-full p-[0.5vw] hover:bg-red-200 rounded-r-lg" @click="showAudioRecorder">
          음성 녹음
        </button>
      </div>
      <div class="w-full">
        <button
          for="input-audio"
          class="w-full p-[0.5vw] hover:bg-red-200 rounded-r-lg"
          @click="clickUpload('input-audio')"
        >
          파일 업로드
        </button>
      </div>
    </div>
    <div
      v-if="isVideoModal"
      class="absolute w-[8vw] top-[54%] left-[17%] text-r-md flex flex-col items-center bg-red-100 rounded-r-lg border-sm"
    >
      <div class="w-full border-b-[0.3vb]">
        <button
          class="w-full p-[0.5vw] hover:bg-red-200 rounded-r-lg border-sm border-black"
          @click="showVideoRecorder"
        >
          영상 촬영
        </button>
      </div>
      <div class="w-full">
        <button
          for="input-video"
          class="w-full p-[0.5vw] hover:bg-red-200 rounded-r-lg"
          @click="clickUpload('input-video')"
        >
          파일 업로드
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onBeforeUnmount } from 'vue'
import { useLetterStore } from '@/stores/meeting'
import IconAudio from '@/icons/meeting/IconAudio.vue'
import IconVideo from '@/icons/meeting/IconVideo.vue'
import IconRemove from '@/icons/meeting/IconRemove.vue'
import IconCancelBlack from '@/icons/meeting/IconCancelBlack.vue'
import IconRecord from '@/icons/meeting/IconRecord.vue'
import IconStop from '@/icons/meeting/IconStop.vue'
import IconPlay from '@/icons/meeting/IconPlay.vue'
import IconPause from '@/icons/meeting/IconPause.vue'

defineEmits(['remove-letter-modal', 'show-audio-recorder', 'show-video-recorder']);

const store = useLetterStore()

const isAudioModal = ref(false)
const isVideoModal = ref(false)
const isAudioRecorder = ref(false)
const isVideoRecorder = ref(false)
const isFileCheck = ref(false)
const isVideoFile = ref(false)
const isAudioFile = ref(false)
const videoFile = ref(null)
const audioFile = ref(null)
const textFile = ref('')
const videoFileName = ref('')
const audioFileName = ref('')

// 음성 녹음 관련

const audio = ref(null)
const mediaRecorder = ref(null)
const soundBarCanvas = ref(null)
const audioContext = ref(null)
const analyzer = ref(null)
const canvas = ref(null)
const canvasContext = ref(null)
const animationId = ref(null)
const recordedChunks = ref([])
const isRecording = ref(false)
const isPlaying = ref(false)
const leftTime = ref(60)

const removeContents = () => {
  document.getElementById('input-text').value = ''
  textFile.value = ''
}

const showAudioModal = () => {
  isAudioModal.value = !isAudioModal.value
  isVideoModal.value = false
}

const showVideoModal = () => {
  isVideoModal.value = !isVideoModal.value
  isAudioModal.value = false
}

const showAudioRecorder = () => {
  isAudioModal.value = false
  isAudioRecorder.value = !isAudioRecorder.value
}

const showVideoRecorder = () => {
  isVideoModal.value = false
  isVideoRecorder.value = !isVideoRecorder.value
}

const clickUpload = (event) => {
  const myInput = document.getElementById(event)
  myInput.click()

  if (event === 'input-audio') {
    showAudioModal()
  } else if (event === 'input-video') {
    showVideoModal()
  }
}

const uploadAudioFile = (event) => {
  isAudioFile.value = true

  if (event === 'record') {
    audioFileName.value = 'audio recording'
  } else {
    audioFile.value = event.target.files[0]
    audioFileName.value = event.target.files[0].name
  }
}

const uploadVideoFile = (event) => {
  isVideoFile.value = true

  if (event === 'record') {
    videoFileName.value = 'video recording'
  } else {
    videoFile.value = event.target.files[0]
    videoFileName.value = event.target.files[0].name
  }
}

const removeAudioFile = () => {
  isAudioFile.value = false
  audioFile.value = null
}

const removeVideoFile = () => {
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

// 음성 녹음 관련

const startRecording = async () => {
  recordedChunks.value = []

  isRecording.value = true
  const stream = await navigator.mediaDevices.getUserMedia({ audio: true })
  mediaRecorder.value = new MediaRecorder(stream, {
    mimeType: 'audio/webm;codecs=opus'
  })
  mediaRecorder.value.addEventListener('dataavailable', (event) => {
    if (event.data.size > 0) {
      recordedChunks.value.push(event.data)
    }
  })

  mediaRecorder.value.start(100)

  audioContext.value = new (window.AudioContext || window.webkitAudioContext)()
  analyzer.value = audioContext.value.createAnalyser()
  const source = audioContext.value.createMediaStreamSource(stream)
  source.connect(analyzer.value)

  canvas.value = soundBarCanvas.value
  canvasContext.value = canvas.value.getContext('2d')

  drawSoundBar()
}

const countTime = () => {
  setTimeout(() => {
    leftTime.value--
  }, 1000)
}

const drawSoundBar = () => {
  canvasContext.value.clearRect(0, 0, canvas.value.width, canvas.value.height)

  const bufferLength = analyzer.value.frequencyBinCount
  const dataArray = new Uint8Array(bufferLength)
  analyzer.value.getByteFrequencyData(dataArray)

  const barWidth = (canvas.value.width / bufferLength) * 3
  let x = 0
  for (let i = 0; i < bufferLength; i++) {
    const barHeight = dataArray[i] / 4
    canvasContext.value.fillStyle = '#a78bfa'
    canvasContext.value.fillRect(x, canvas.value.height - barHeight, barWidth, barHeight)
    x += barWidth + 1
  }

  animationId.value = requestAnimationFrame(drawSoundBar)
}

const stopRecording = async () => {
  isRecording.value = false
  await mediaRecorder.value.stop()

  const blob = new Blob(recordedChunks.value, { type: 'audio/webm' })
  const audioUrl = URL.createObjectURL(blob)
  audio.value.src = audioUrl
  audioFile.value = new File([blob], 'audio recording', { type: 'audio/webm' })

  cancelAnimationFrame(animationId.value)
}

const playRecording = async () => {
  isPlaying.value = true
  await audio.value.play()
}

const pausePlaying = () => {
  isPlaying.value = false
  audio.value.pause()
}

const onAudioEnded = () => {
  isPlaying.value = false
}

// 영상 촬영 관련

onBeforeUnmount(() => {
  if (isRecording.value) {
    stopRecording()
  }
})
</script>

<style scoped>
canvas {
  padding-right: 1vw;
  width: 50%;
}

#button {
  width: 4.5vw;
  margin: 0.8vw;
  padding: 1vw;
  aspect-ratio: 1 / 1;
  background-color: #d8b4fe;
  border-radius: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
}

#button:hover {
  background: #a78bfa;
}
</style>
