<template>
  <div class="absolute left-[39vw] w-[25vw] bg-purple-200 rounded-r-xl flex items-center">
    <button v-if="!isRecording" @click="startRecording()" :disabled="isRecording">
      <IconRecord class="size-[100%]" />
    </button>
    <button v-else @click="stopRecording()" :disabled="!isRecording">
      <IconStop class="size-[100%]" />
    </button>
    <button
      v-if="recordedChunks.length !== 0 && !isRecording && !isPlaying"
      @click="playRecording()"
    >
      <IconPlay class="size-[100%]" />
    </button>
    <button v-if="recordedChunks.length !== 0 && !isRecording && isPlaying" @click="pausePlaying()">
      <IconPause class="size-[90%]" />
    </button>
    <canvas ref="soundBarCanvas" width="150" height="50" class="bg-transparent aspect-[3]"></canvas>
    <audio hidden ref="audio" crossorigin="anonymous" @ended="onAudioEnded"></audio>
  </div>
</template>

<script setup>
import { ref, onBeforeUnmount } from 'vue'
import IconRecord from '@/icons/meeting/IconRecord.vue'
import IconStop from '@/icons/meeting/IconStop.vue'
import IconPlay from '@/icons/meeting/IconPlay.vue'
import IconPause from '@/icons/meeting/IconPause.vue'

const audio = ref(null)
const audioFile = ref('')
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

const drawSoundBar = () => {
  // Clear canvas
  canvasContext.value.clearRect(0, 0, canvas.value.width, canvas.value.height)

  // Get frequency data
  const bufferLength = analyzer.value.frequencyBinCount
  const dataArray = new Uint8Array(bufferLength)
  analyzer.value.getByteFrequencyData(dataArray)

  // Draw sound bar
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
  audioFile.value = new File([blob], '음성 녹음 파일', { type: 'audio/webm' })

  // Stop drawing sound bar
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

button {
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

button:hover {
  background: #a78bfa;
}
</style>
