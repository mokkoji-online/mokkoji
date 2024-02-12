import { ref } from 'vue'
import { defineStore } from 'pinia'
import { useRouter } from 'vue-router'
import axiosJwt from '@/services/api'
import axios from 'axios'

const { VITE_API_URL } = import.meta.env
const { VITE_SERVER } = import.meta.env

export const useSessionStore = defineStore('session', () => {
  const router = useRouter()

  const createSession = async () => {
    const response = await axiosJwt.post(VITE_SERVER + '/meetings/sessions', {
      headers: { 'Content-Type': 'application/json' }
    })

    sessionStorage.setItem('sessionId', response.data)
    sessionStorage.setItem('host', $cookies.get('user').name)
    sessionStorage.setItem('isHost', true)

    router.push('/meetings')
  }

  const createGroupSession = async (sessionId) => {
    const response = await axiosJwt.post(
      VITE_SERVER + '/meetings/sessions',
      { customSessionId: sessionId },
      {
        headers: { 'Content-Type': 'application/json' }
      }
    )

    sessionStorage.setItem('groupSessionId', response.data)
  }

  const findSession = async (sessionId) => {
    try {
      const res = await axios.get(VITE_API_URL + VITE_SERVER + `/meetings/sessions/${sessionId}`)
      if (res.data !== undefined) {
        sessionStorage.setItem('sessionId', res.data.session.sessionId)
        sessionStorage.setItem('host', res.data.hostName)
        sessionStorage.setItem('isHost', false)
        return 'success'
      } else {
        return 'fail'
      }
    } catch (error) {
      console.error(error)
    }
  }

  const deleteSession = async (sessionId) => {
    try {
      const res = await axiosJwt.delete(VITE_SERVER + `/meetings/sessions/${sessionId}`)
    } catch (error) {
      console.error(error)
    }
  }

  return {
    createSession,
    createGroupSession,
    findSession,
    deleteSession
  }
})

export const useLetterStore = defineStore('letter', () => {
  const sendLetter = async (videoFile, audioFile, textFile) => {
    // FormData 객체 생성
    const formData = new FormData()

    // FormData에 음성 파일 추가
    if (videoFile) formData.append('video', videoFile)
    // FormData에 영상 파일 추가
    if (audioFile) formData.append('audio', audioFile)

    // JSON 데이터 추가
    const text = {
      writer: $cookies.get('user').name,
      text: textFile
    }

    const json = JSON.stringify(text)
    const blob = new Blob([json], { type: 'application/json' })

    formData.append('writerAndText', blob)

    try {
      // axios를 사용하여 POST 요청 보내기
      const response = await axiosJwt.post(
        VITE_SERVER + `/events/rollingpapers/${sessionStorage.getItem('sessionId')}`,
        formData,
        {
          // 필수: FormData를 사용할 때는 이 헤더를 설정해야 함
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        }
      )

      // 성공 시 서버의 응답을 처리
      console.log('서버 응답:', response.data)
    } catch (error) {
      // 오류 처리
      console.error('오류 발생:', error)
    }
  }

  return {
    sendLetter
  }
})
