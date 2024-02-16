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
          v-if="!isAudioRecorder"
          name="textFile"
          id="input-text"
          cols="30"
          rows="10"
          placeholder="메시지를 입력하세요"
          maxlength="100"
          class="h-[90%] bg-yellow-100 rounded-r-lg resize-none placeholder:text-[1.5vw] text-center text-r-md font-bold focus:outline-none"
          v-model="textFile"
        >
        </textarea>
        <!-- 음성 파일 -->
        <div
          v-if="isAudioRecorder"
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
              @click="startAudioRecording()"
              :disabled="isRecording"
            >
              <IconRecord class="size-[100%]" />
            </button>
            <button id="button" v-else @click="stopAudioRecording()" :disabled="!isRecording">
              <IconStop class="size-[100%]" />
            </button>
            <button
              id="button"
              v-if="recordedChunks.length !== 0 && !isRecording && !isPlaying"
              @click="playAudioRecording()"
            >
              <IconPlay class="size-[100%]" />
            </button>
            <button
              id="button"
              v-if="recordedChunks.length !== 0 && !isRecording && isPlaying"
              @click="pauseAudioPlaying()"
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
            남은 시간 : {{ setTime }}
          </div>
          <div
            v-if="recordedChunks.length !== 0 && !isRecording"
            class="mt-[1vh] flex justify-center items-center"
          >
            <button
              class="w-[8vw] aspect-[2.5] bg-purple-200 rounded-r-lg text-r-md flex justify-center items-center font-semibold hover:bg-purple-300 hover:cursor-pointer"
              @click="uploadAudioFile"
            >
              파일 업로드
            </button>
          </div>
        </div>
        <div class="flex h-[6vh]">
          <div class="basis-1/2 flex justify-start">
            <label
              class="mr-[1vw] hover:bg-red-100 w-[3vw] aspect-square rounded-full flex justify-center items-center"
              @click="showAudioRecorder"
            >
              <IconAudio class="size-[90%] hover:cursor-pointer" />
            </label>
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
              class="w-[1.5vw] hover:cursor-pointer hover:bg-red-100 rounded-full"
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
              class="ml-auto w-[1.5vw] hover:cursor-pointer hover:bg-red-100 rounded-full"
              @click="removeVideoFile"
            />
          </div>
        </div>
      </div>
      <div class="flex">
        <p v-if="isFileCheck" style="color: red" class="text-[1.4vw]">
          작성한 내용이 존재하지 않습니다.
        </p>
        <p v-if="isTypeCheck" style="color: red" class="text-[1.4vw]">
          mov, mp4 파일만 업로드 가능합니다.
        </p>
        <p v-if="isSizeCheck" style="color: red" class="text-[1.4vw]">
          최대 파일 용량은 40MB입니다.
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

defineEmits(['remove-letter-modal'])

const store = useLetterStore()

const isAudioRecorder = ref(false)
const isFileCheck = ref(false)
const isTypeCheck = ref(false)
const isSizeCheck = ref(false)
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
const setTime = ref(60)
const countTimeInterval = ref(null)
const allowedExtensions = ref(['mp4', 'mov'])

// 작성중인 롤링페이퍼 텍스트 모두 지우기
const removeContents = () => {
  document.getElementById('input-text').value = ''
  textFile.value = ''
}

// 오디오 녹음기 보여주기
const showAudioRecorder = () => {
  isAudioRecorder.value = !isAudioRecorder.value
}

// 녹음한 오디오 파일 업로드하기
const uploadAudioFile = () => {
  isAudioFile.value = true
  isAudioRecorder.value = false
  audioFileName.value = '나만의 녹음'
}

// 선택한 비디오 파일 업로드하기
const uploadVideoFile = (event) => {
  const file = event.target.files[0]
  const fileName = event.target.files[0].name
  const extension = fileName.split('.').pop().toLowerCase()

  if (allowedExtensions.value.includes(extension) && checkFileSize()) {
    isVideoFile.value = true
    isTypeCheck.value = false
    isFileCheck.value = false
    isSizeCheck.value = false
    videoFile.value = file
    videoFileName.value = '나만의 영상'
  } else if (!allowedExtensions.value.includes(extension)) {
    isTypeCheck.value = true
    isFileCheck.value = false
    isSizeCheck.value = false
  } else if (!checkFileSize()) {
    isSizeCheck.value = true
    isTypeCheck.value = false
    isFileCheck.value = false
  }

  event.target.value = ''
}

// 선택한 비디오 파일 용량 체크(최대 40MB)
const checkFileSize = () => {
  const fileInput = document.getElementById('input-video')

  if (fileInput.files.length > 0) {
    const fileSize = fileInput.files[0].size // 파일 크기(바이트)
    const maxSize = 40 * 1024 * 1024 // 40MB를 바이트 단위로 변환

    if (fileSize > maxSize) {
      isSizeCheck.value = true
      isFileCheck.value = false
      isTypeCheck.value = false
      fileInput.value = ''
      return false
    }

    return true
  }
}

// 업로드한 오디오 파일 지우기
const removeAudioFile = () => {
  isAudioFile.value = false
  audioFile.value = null
  audioFileName.value = ''
}

// 업로드한 비디오 파일 지우기
const removeVideoFile = () => {
  isVideoFile.value = false
  videoFile.value = null
  videoFileName.value = ''
}

// 작성한 롤링페이퍼 보내기
const sendLetter = () => {
  // 텍스트, 음성, 영상 중 하나라도 작성 필수
  if (videoFile.value === null && audioFile.value === null && textFile.value === '') {
    isFileCheck.value = true
    isTypeCheck.value = false
    isSizeCheck.value = false
    return
  } else {
    store.sendLetter(videoFile.value, audioFile.value, textFile.value)
  }

  // 값 초기화
  isFileCheck.value = false
  isTypeCheck.value = false
  isSizeCheck.value = false
  isVideoFile.value = false
  isAudioFile.value = false
  audioFile.value = null
  videoFile.value = null
  videoFileName.value = ''
  audioFileName.value = ''
  textFile.value = ''
}

// 오디오 녹음 시작
const startAudioRecording = async () => {
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

  countTime()

  // 60초가 지나면 녹음 종료
  if (isRecording.value) {
    setTimeout(() => {
      clearInterval(countTime)
      setTime.value = 60
      stopAudioRecording()
    }, 60000)
  }
}

// 현재 남은 녹음 가능 시간 보여주기
const countTime = () => {
  // 숫자 카운트를 시작하고 이를 countTimeInterval에 저장
  countTimeInterval.value = setInterval(() => {
    setTime.value--
    // 시간이 0이하가 되면 자동으로 녹음 중지
    if (setTime.value <= 0) {
      stopAudioRecording()
    }
  }, 1000)
}

// 사운드바 제작
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

// 오디오 녹음 중지
const stopAudioRecording = async () => {
  setTime.value = 60
  isRecording.value = false
  clearInterval(countTimeInterval.value)
  await mediaRecorder.value.stop()

  const blob = new Blob(recordedChunks.value, { type: 'audio/webm' })
  const audioUrl = URL.createObjectURL(blob)
  audio.value.src = audioUrl
  audioFile.value = new File([blob], 'audio.webm', { type: 'audio/webm' })

  cancelAnimationFrame(animationId.value)
}

// 녹음된 오디오 재생
const playAudioRecording = async () => {
  isPlaying.value = true
  await audio.value.play()
}

// 오디오 재생 중지
const pauseAudioPlaying = () => {
  isPlaying.value = false
  audio.value.pause()
}

// 오디오 재생 완료
const onAudioEnded = () => {
  isPlaying.value = false
}

onBeforeUnmount(() => {
  // Unmount 전에 녹음이 진행되고 있으면 녹음 종료
  if (isRecording.value) {
    stopAudioRecording()
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
