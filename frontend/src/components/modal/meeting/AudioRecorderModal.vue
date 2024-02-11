<template>
  <div class="pt-[50vh]">
    <button @click="startRecording" :disabled="isRecording">Start Recording</button>
    <canvas ref="soundBarCanvas" width="300" height="100" class="bg-black"></canvas>
    <button @click="stopRecording" :disabled="!isRecording">Stop Recording</button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      mediaRecorder: null,
      recordedChunks: [],
      isRecording: false,
      audioContext: null,
      analyzer: null,
      canvas: null,
      canvasContext: null,
      animationId: null
    }
  },
  methods: {
    async startRecording() {
      this.isRecording = true
      const stream = await navigator.mediaDevices.getUserMedia({ audio: true })
      this.mediaRecorder = new MediaRecorder(stream)
      this.mediaRecorder.ondataavailable = (event) => {
        this.recordedChunks.push(event.data)
      }
      this.mediaRecorder.start()

      // Setup audio context and analyzer
      this.audioContext = new (window.AudioContext || window.webkitAudioContext)()
      this.analyzer = this.audioContext.createAnalyser()
      const source = this.audioContext.createMediaStreamSource(stream)
      source.connect(this.analyzer)

      // Setup canvas
      this.canvas = this.$refs.soundBarCanvas
      this.canvasContext = this.canvas.getContext('2d')

      // Start drawing sound bar
      this.drawSoundBar()
    },

    stopRecording() {
      this.isRecording = false
      this.mediaRecorder.stop()

      const blob = new Blob(this.recordedChunks, { type: 'audio/wav' })
      const audioUrl = URL.createObjectURL(blob)
      const audio = new Audio(audioUrl)
      audio.play()

      // Reset recording
      this.recordedChunks = []
      this.mediaRecorder = null

      // Stop drawing sound bar
      cancelAnimationFrame(this.animationId)
    },
    drawSoundBar() {
      // Clear canvas
      this.canvasContext.clearRect(0, 0, this.canvas.width, this.canvas.height)

      // Get frequency data
      const bufferLength = this.analyzer.frequencyBinCount
      const dataArray = new Uint8Array(bufferLength)
      this.analyzer.getByteFrequencyData(dataArray)

      // Draw sound bar
      const barWidth = (this.canvas.width / bufferLength) * 2.5
      let x = 0
      for (let i = 0; i < bufferLength; i++) {
        const barHeight = dataArray[i] / 2
        const hue = (i / bufferLength) * 360
        this.canvasContext.fillStyle = `hsl(${hue}, 100%, 50%)`
        this.canvasContext.fillRect(x, this.canvas.height - barHeight, barWidth, barHeight)
        x += barWidth + 1
      }

      // Schedule next frame
      this.animationId = requestAnimationFrame(this.drawSoundBar)
    }
  }
}
</script>

<style scoped>
button {
  margin: 5px;
  padding: 10px;
  cursor: pointer;
}
</style>
