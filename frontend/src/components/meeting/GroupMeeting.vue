<template>
  <main class="px-sm w-screen h-screen flex flex-col justify-center">
    <section class="w-full h-[85%] flex justify-center">
      <!-- 스포트라이트 레이아웃 -->
      <div v-if="!isGrid" class="w-full h-full flex basis-3/4">
        <div id="meeting-container" ref="groupVideo" class="w-full h-full flex justify-center">
          <div
            v-if="state.subscribers.length > 0"
            class="h-full basis-1/4 flex flex-col justify-start items-center overflow-y-scroll gap-[1vh]"
          >
            <user-Video
              v-for="sub in state.subscribers"
              :key="sub.stream.connection.connectionId"
              :stream-manager="sub"
              :main-stream="false"
              @click="updateMainVideoStreamManager(sub)"
            />
          </div>
          <div
            ref="myVideo"
            class="mr-[1vh] w-full h-full basis-3/4 flex justify-center items-start"
          >
            <user-Video
              :key="`main-${state.mainStreamManager?.stream.connection.connectionId}`"
              :stream-manager="state.mainStreamManager"
              :main-stream="true"
            />
          </div>
        </div>
      </div>
      <!-- 그리드 레이아웃 -->
      <div
        v-else
        ref="groupVideo"
        class="w-full h-full basis-3/4 grid grid-cols-3 gap-[1vh] overflow-y-scroll items-center"
      >
        <user-Video
          v-for="user in userList"
          :key="user.stream.connection.connectionId"
          :stream-manager="user"
          :main-stream="false"
        />
      </div>
      <!-- 참여자 목록, 채팅방-->
      <div
        v-if="isUserList || isChat"
        class="h-full basis-1/4 self-center flex flex-col justify-end"
      >
        <div
          v-if="isUserList"
          :class="{
            'w-full h-full border-sm border-neutral-300 rounded-r-lg flex flex-col':
              isUserList && !isChat,
            'w-full h-1/2 border-sm border-neutral-300 rounded-r-lg flex flex-col':
              isUserList && isChat
          }"
        >
          <div class="px-md w-full h-[13%] flex items-center">
            <div class="ml-[1vw] w-[30%] font-bold text-r-md">참여자</div>
            <div class="w-[70%] flex">
              <button
                class="ml-auto w-[45%] flex justify-center hover:bg-neutral-200 rounded-r-lg"
                @click="showInviteModal"
              >
                <div class="w-[100%] text-r-sm text-purple-400">초대하기</div>
                <IconInvite class="w-[4vw] size-[70%]" />
              </button>
              <button
                class="ml-[3vh] flex w-[2.3vw] aspect-square rounded-full hover:bg-neutral-200"
                @click="showUserList(), showInviteModal('close')"
              >
                <IconCancelPurple class="size-[80%]" />
              </button>
            </div>
          </div>
          <div
            class="bg-gray pt-[1vh] h-[80%] flex flex-col justify-start items-center overflow-y-scroll"
          >
            <UserList
              v-for="user in userList"
              :key="user.stream.connection.connectionId"
              :stream-manager="user"
              :search-user-name="searchUserName"
              :my-name="state.myUserName"
              :button-type="'user-list'"
              class="bg-white w-[90%] h-[10vh] rounded-r-lg text-r-sm flex justify-center items-center font-semibold"
            />
          </div>
          <div class="h-[13%] flex flex-col justify-center items-center">
            <div class="bg-gray w-[95%] h-[70%] rounded-r-xl flex items-center">
              <input
                type="text"
                name=""
                id="search-name"
                placeholder="참여자명 검색"
                class="my-0 ml-[2vb] pr-0 pl-[0.5vw] bg-gray w-[80%] h-[100%] border-0 rounded-full text-r-md rounded-r-xl"
                v-model="searchUserName"
              />
              <button
                class="ml-[1vw] w-[17%] h-[90%] rounded-full bg-purple-200 flex justify-center items-center hover:bg-purple-300"
              >
                <IconSearch class="size-[60%]" />
              </button>
            </div>
          </div>
        </div>
        <div v-if="isUserList && isChat" class="mb-[1vh]"></div>
        <div
          v-if="isChat"
          :class="{
            'w-full h-full border-sm border-neutral-300 rounded-r-lg flex flex-col':
              isChat && !isUserList,
            'w-full h-1/2 border-sm border-neutral-300 rounded-r-lg flex flex-col':
              isChat && isUserList
          }"
        >
          <div class="px-md w-full h-[13%] flex items-center">
            <div class="ml-[1vw] basis-3/12 font-bold text-r-md">채팅</div>
            <button
              class="ml-auto w-[2.3vw] aspect-square rounded-full flex hover:bg-neutral-200"
              @click="showChat"
            >
              <IconCancelPurple class="size-[80%]" />
            </button>
          </div>
          <div
            id="chat-container"
            class="pt-[1vh] bg-gray h-[80%] flex flex-col justify-start items-center overflow-y-scroll"
          >
            <ChatLog :chat-log="chatMessages" />
          </div>
          <div class="h-[13%] flex flex-col justify-center items-center">
            <div action="/meeting" class="bg-gray w-[95%] h-[70%] rounded-r-xl flex items-center">
              <input
                v-model="chatMessage"
                type="text"
                name=""
                id="chat-message"
                placeholder="메시지 보내기"
                class="my-0 ml-[2vb] pl-[0.5vw] bg-gray w-[80%] h-[100%] border-0 placeholder:items-center text-r-md rounded-r-xl"
                maxlength="100"
                @keypress.enter="sendMessage"
              />
              <button
                @click="sendMessage"
                type="submit"
                class="ml-[1vw] w-[17%] h-[90%] rounded-full bg-purple-200 flex justify-center items-center hover:bg-purple-300"
              >
                <IconSendMessage class="size-[60%]" />
              </button>
            </div>
          </div>
        </div>
      </div>
    </section>
    <!-- 기능 버튼 -->
    <section class="h-[15%] flex justify-center items-center">
      <div class="w-[75%] h-full flex justify-center items-center">
        <div class="w-[20%] h-full flex items-center">
          <button
            class="w-[20%] aspect-square rounded-full bg-white hover:bg-slate-200"
            @click="showMeetingDetailModal"
          >
            <IconInfo class="size-[100%]" />
          </button>
          <button
            class="w-[70%] aspect-[2.5] bg-neutral-500 hover:bg-neutral-800 text-white rounded-r-lg ml-sm"
            @click="setLayoutState"
          >
            <div class="w-[30%] h-full flex justify-center items-center">
              <IconLayout class="size-[80%]" />
            </div>
            <div class="w-[60%] button-text">화면 배치</div>
          </button>
          <button
            class="ml-[1lvh] w-[30%] aspect-square bg-neutral-500 hover:bg-neutral-800 rounded-full flex justify-center items-center"
            @click="toggleCamera"
          >
            <IconScreenChange id="screen-change-button" class="size-[70%]" />
            <ScreenChangeModal id="screen-change" />
          </button>
        </div>
        <div
          id="button-container-center"
          class="w-[70%] h-full flex flex-wrap justify-center items-center"
        >
          <div>
            <button
              id="button-mic"
              :class="{ 'bg-purple-200': isMic, 'bg-red-500': !isMic }"
              @click="setMicrophoneState"
            >
              <IconMicOn v-if="isMic" class="size-[50%]" />
              <IconMicOff v-else class="size-[50%]" />
            </button>
            <span v-if="isMic" @click="setMicrophoneState" class="button-text">ON</span>
            <span v-else @click="setMicrophoneState" class="button-text">OFF</span>
          </div>
          <div>
            <button
              id="button-camera"
              :class="{ 'bg-purple-200': isCamera, 'bg-red-500 hover:bg-red-900': !isCamera }"
              @click="setCameraState"
            >
              <IconVideoOn v-if="isCamera" class="size-[50%]" />
              <IconVideoOff v-else class="size-[50%]" />
            </button>
            <span v-if="isCamera" @click="setCameraState" class="button-text">ON</span>
            <span v-else @click="setCameraState" class="button-text">OFF</span>
          </div>
          <div>
            <button
              id="button-letter"
              :class="{ 'bg-purple-400': isLetterModal, 'bg-purple-200': !isLetterModal }"
              @click="showLetterModal"
            >
              <IconLetter class="size-[50%]" />
            </button>
            <span @click="showLetterModal" class="button-text">편지쓰기</span>
          </div>
          <div>
            <button
              id="button-gift"
              :class="{ 'bg-purple-400': isGiftModal, 'bg-purple-200': !isGiftModal }"
              @click="showGiftModal"
            >
              <IconGift class="size-[50%]" />
            </button>
            <span @click="showGiftModal" class="button-text">선물하기</span>
          </div>
          <div>
            <button
              id="button-picture"
              :class="{ 'bg-purple-400': isCaptureModal, 'bg-purple-200': !isCaptureModal }"
              @click="showCaptureModal"
            >
              <IconCamera class="size-[50%]" />
            </button>
            <span @click="showCaptureModal" class="button-text">사진찍기</span>
          </div>
        </div>
        <div id="button-container-right" class="w-[25%] h-full flex">
          <div class="w-1/4">
            <button
              id="button-subList"
              :class="{ 'bg-purple-400': isUserList, 'bg-purple-200': !isUserList }"
              @click="showUserList"
            >
              <IconUserList class="size-[50%]" />
            </button>
            <span @click="showUserList" class="button-text">참여자</span>
          </div>
          <div class="w-1/4 ml-sm">
            <button
              id="button-chat"
              :class="{ 'bg-purple-400': isChat, 'bg-purple-200': !isChat }"
              @click="showChat"
            >
              <IconChat class="size-[50%]" />
            </button>
            <span @click="showChat" class="button-text">채팅</span>
          </div>
          <div class="w-2/4">
            <div class="w-[80%] h-full flex items-center">
              <button
                id="button-quit"
                class="w-full aspect-[2] bg-red-500 hover:bg-red-400 text-white rounded-r-xl text-r-md"
                @click="showLeaveGroupModal"
              >
                나가기
              </button>
            </div>
          </div>
        </div>
      </div>
      <div v-if="isCount" class="fixed top-[10%] text-[20vw] text-purple-200 text-stroke">
        {{ setTime }}
      </div>
    </section>
    <transition-group name="up">
      <MeetingDetailModal
        v-if="isMeetingDetailModal"
        :session="state.session"
        @remove-detail-modal="showMeetingDetailModal"
      />
      <LetterModal v-if="isLetterModal" @remove-letter-modal="showLetterModal" />
      <GiftModal v-if="isGiftModal" />
      <LeaveGroupModal
        v-if="isLeaveGroupModal"
        @leave-main-meeting="leaveMainMeeting"
        @leave-group-meeting="leaveGroupMeeting"
      />
      <CaptureModal
        v-if="isCaptureModal"
        @capture-my-video="captureMyVideo"
        @capture-group-video="captureGroupVideo"
      />
    </transition-group>
    <transition-group name="down">
      <InviteModal v-if="isInviteModal" @remove-invite-modal="showInviteModal" />
      <MicModal v-if="isMicModal" :is-mic="isMic" />
      <CameraModal v-if="isCameraModal" :is-camera="isCamera" />
      <CaptureCheckModal v-if="isCaptureCheckModal" />
    </transition-group>
    <LoginCheckModal v-if="isLoginCheckModal" @remove-login-check-modal="showLoginCheckModal" />
  </main>
</template>

<script setup>
import { ref, reactive, onMounted, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import { OpenVidu } from 'openvidu-browser'
import { useSessionStore } from '@/stores/meeting'
import html2canvas from 'html2canvas'
import axiosJwt from '@/services/api'
import UserList from '@/components/meeting/UserList.vue'
import UserVideo from '@/components/meeting/UserVideo.vue'
import ChatLog from '@/components/meeting/ChatLog.vue'
import IconInfo from '@/icons/meeting/IconInfo.vue'
import IconSearch from '@/icons/meeting/IconSearch.vue'
import IconInvite from '@/icons/meeting/IconInvite.vue'
import IconCancelPurple from '@/icons/meeting/IconCancelPurple.vue'
import IconLayout from '@/icons/meeting/IconLayout.vue'
import IconMicOn from '@/icons/meeting/IconMicOn.vue'
import IconMicOff from '@/icons/meeting/IconMicOff.vue'
import IconVideoOn from '@/icons/meeting/IconVideoOn.vue'
import IconVideoOff from '@/icons/meeting/IconVideoOff.vue'
import IconGift from '@/icons/meeting/IconGift.vue'
import IconLetter from '@/icons/meeting/IconLetter.vue'
import IconCamera from '@/icons/meeting/IconCamera.vue'
import IconUserList from '@/icons/meeting/IconUserList.vue'
import IconChat from '@/icons/meeting/IconChat.vue'
import IconSendMessage from '@/icons/meeting/IconSendMessage.vue'
import InviteModal from '@/components/modal/meeting/InviteModal.vue'
import MeetingDetailModal from '@/components/modal/meeting/MeetingDetailModal.vue'
import MicModal from '@/components/modal/meeting/MicModal.vue'
import CameraModal from '@/components/modal/meeting/CameraModal.vue'
import GiftModal from '@/components/modal/meeting/GiftModal.vue'
import LetterModal from '@/components/modal/meeting/LetterModal.vue'
import CaptureModal from '@/components/modal/meeting/CaptureModal.vue'
import CaptureCheckModal from '@/components/modal/meeting/CaptureCheckModal.vue'
import LeaveGroupModal from '@/components/modal/meeting/LeaveGroupModal.vue'
import IconScreenChange from '@/icons/meeting/IconScreenChange.vue'
import ScreenChangeModal from '@/components/modal/meeting/ScreenChangeModal.vue'
import LoginCheckModal from '@/components/modal/meeting/LoginCheckModal.vue'

const emit = defineEmits(['leave-meeting'])

const router = useRouter()
const store = useSessionStore()

const videoWidth = window.screen.width * 0.65
const videoHeight = window.screen.height * 0.85

const isGrid = ref(false)
const isMic = ref(true)
const isMicModal = ref(false)
const isCamera = ref(true)
const isCameraModal = ref(false)
const isInviteModal = ref(false)
const isMeetingDetailModal = ref(false)
const isLetterModal = ref(false)
const isGiftModal = ref(false)
const isCount = ref(false)
const isCaptureModal = ref(false)
const isCaptureCheckModal = ref(false)
const isUserList = ref(false)
const isChat = ref(false)
const isLeaveGroupModal = ref(false)
const isFrontCamera = ref(true)
const isLoginCheckModal = ref(false)

const searchUserName = ref('')
const chatMessage = ref('')
const chatMessages = ref([])
const userList = ref([])
const myVideo = ref(null)
const groupVideo = ref(null)
const setTime = ref(3)

// 행사 초대 링크 모달
const showInviteModal = (event) => {
  if (event === 'close') {
    isInviteModal.value = false
  } else {
    isInviteModal.value = !isInviteModal.value
  }
}

// 행사 세부 정보
const showMeetingDetailModal = () => {
  isMeetingDetailModal.value = !isMeetingDetailModal.value
}

// 레이아웃 스포트라이트 / 그리드
const setLayoutState = () => {
  isGrid.value = !isGrid.value
}

// 마이크 on / off
const setMicrophoneState = () => {
  isMic.value = !isMic.value

  if (isMic.value) {
    state.publisher.publishAudio(true)
  } else {
    state.publisher.publishAudio(false)
  }

  isMicModal.value = true

  setTimeout(() => {
    isMicModal.value = false
  }, 1000)
}

// 카메라 on / off
const setCameraState = () => {
  isCamera.value = !isCamera.value

  if (isCamera.value) {
    state.publisher.publishVideo(true)
  } else {
    state.publisher.publishVideo(false)
  }

  isCameraModal.value = true

  setTimeout(() => {
    isCameraModal.value = false
  }, 1000)
}

// 롤링페이퍼, 사진 촬영 버튼 클릭시 비로그인 상태면 로그인을 할 수 있는 모달 생성
const showLoginCheckModal = () => {
  isLoginCheckModal.value = !isLoginCheckModal.value
}

// 롤링페이퍼 작성 모달 생성
const showLetterModal = () => {
  if ($cookies.get('user') !== null) {
    isLetterModal.value = !isLetterModal.value
  } else {
    isLoginCheckModal.value = true
  }
}

// 선물하기 모달 생성
const showGiftModal = () => {
  isGiftModal.value = !isGiftModal.value
}

// 개인 사진, 단체 사진 촬영 모달 생성
const showCaptureModal = () => {
  if ($cookies.get('user') !== null) {
    isCaptureModal.value = !isCaptureModal.value
  } else {
    isLoginCheckModal.value = true
  }
}

// 사진 촬영 확인 모달 생성
const showCaptureCheckModal = () => {
  isCaptureCheckModal.value = true

  setTimeout(() => {
    isCaptureCheckModal.value = false
  }, 600)
}

// 소그룹 참여자 목록 보여주기
const showUserList = () => {
  isUserList.value = !isUserList.value
}

// 채팅 메시지 보여주기
const showChat = () => {
  isChat.value = !isChat.value
}

// 소그룹 나가기, 행사 나가기를 선택하는 모달 생성
const showLeaveGroupModal = () => {
  isLeaveGroupModal.value = !isLeaveGroupModal.value
}

// 새로운 채팅 메시지가 오면 메시지창을 최하단으로 내리기
const scrollToBottom = () => {
  const scroll = document.getElementById('chat-container')

  scroll.scrollTop = scroll.scrollHeight
}

// 카메라 전, 후면 전환
const toggleCamera = async () => {
  const devices = await state.OV.getDevices()
  const videoDevices = await devices.filter((device) => device.kind === 'videoinput')

  isFrontCamera.value = !isFrontCamera.value

  if (videoDevices && videoDevices.length > 1) {
    const mediaStream = await state.OV.getUserMedia({
      audioSource: false,
      videoSource: isFrontCamera.value ? videoDevices[0].deviceId : videoDevices[1].deviceId,
      resolution: `${videoWidth}x${videoHeight}`,
      frameRate: 30
    })

    const myTrack = mediaStream.getVideoTracks()[0]

    state.publisher.replaceTrack(myTrack).then(() => {
      console.log('New track has been published')
    })
  } else {
    console.log('전환할 화면이 존재하지 않습니다.')
  }
}

// 개인 사진 촬영
const captureMyVideo = () => {
  isCount.value = true

  const countTime = setInterval(() => {
    setTime.value--
  }, 1000)

  setTimeout(() => {
    const target = myVideo.value

    if (!target) {
      isCount.value = false
      setTime.value = 3
      clearInterval(countTime)
      return alert('사진 촬영 실패')
    }

    html2canvas(target).then((canvas) => {
      canvas.toBlob((blob) => {
        const file = new File([blob], 'myVideo.png', { type: 'image/png' })
        store.sendPicture(file)
      })
    })

    isCount.value = false
    setTime.value = 3
    showCaptureCheckModal()
    clearInterval(countTime)
  }, 3000)
}

// 단체 사진 촬영
const captureGroupVideo = async () => {
  isGrid.value = true
  isCount.value = true

  const countTime = setInterval(() => {
    setTime.value--
  }, 1000)

  setTimeout(() => {
    const target = groupVideo.value

    if (!target) {
      isGrid.value = false
      isCount.value = false
      setTime.value = 3
      clearInterval(countTime)
      return alert('사진 촬영 실패')
    }

    html2canvas(target).then((canvas) => {
      canvas.toBlob((blob) => {
        const file = new File([blob], 'groupVideo.png', { type: 'image/png' })
        store.sendPicture(file)
      })
    })

    isGrid.value = false
    isCount.value = false
    setTime.value = 3
    showCaptureCheckModal()
    clearInterval(countTime)
  }, 3000)
}

// 행사 참여시 뒤로가기 버튼 클릭 막기
const noBack = () => {
  history.pushState(null, null, location.href)
  window.onpopstate = () => {
    history.go(1)
  }
}

// ---------------- OpenVidu 관련 ----------------

axiosJwt.defaults.headers.post['Content-Type'] = 'application/json'

const { VITE_SERVER } = import.meta.env

const state = reactive({
  OV: undefined,
  session: undefined,
  mainStreamManager: undefined,
  publisher: undefined,
  subscribers: [],
  myUserName:
    $cookies.get('user') !== null ? $cookies.get('user').name : sessionStorage.getItem('userName')
})

// 세션 참가하기
const joinSession = () => {
  // 2) Openvidu 객체 생성
  state.OV = new OpenVidu()

  // 3) 세션 시작
  state.session = state.OV.initSession()

  // 4) 유효한 사용자 토큰으로 세션에 연결하기
  getToken()
    .then((token) => {
      state.session.connect(token, { clientData: state.myUserName }).then(() => {
        let publisher = state.OV.initPublisher(undefined, {
          audioSource: undefined,
          videoSource: undefined,
          publishAudio: true,
          publishVideo: true,
          allowTranscoding: true,
          resolution: `${videoWidth}x${videoHeight}`,
          frameRate: 30,
          insertMode: 'APPEND',
          mirror: false
        })

        state.mainStreamManager = publisher
        state.publisher = publisher
        state.session.publish(publisher)
        userList.value.unshift(publisher)
      })
    })
    .catch((error) => {
      console.log('세션에 연결하는 과정에서 에러가 발생했습니다.', error.code, error.message)
    })

  // 비동기 예외
  state.session.on('exception', ({ exception }) => {
    console.warn(exception)
  })

  // 새로운 참가자 입장
  state.session.on('streamCreated', ({ stream }) => {
    const subscriber = state.session.subscribe(stream, undefined)
    state.subscribers.push(subscriber)
    userList.value.push(subscriber)
  })

  // 시그널링 서버로부터 수신된 채팅 메시지 처리
  state.session.on('signal:chat', (event) => {
    const sender = JSON.parse(event.from.data)
    const now = new Date()
    const time = ref()

    if (now.getHours() > 12) {
      time.value = (now.getHours() % 12) + ':' + now.getMinutes() + ' PM'
    } else {
      time.value = now.getHours() + ':' + now.getMinutes() + ' AM'
    }

    const chatData = {
      sender: sender.clientData,
      content: event.data,
      time: time
    }

    chatMessages.value.push(chatData)

    window.setTimeout(scrollToBottom, 50)
  })

  // 참가자 퇴장
  state.session.on('streamDestroyed', ({ stream }) => {
    const index = state.subscribers.indexOf(stream.streamManager, 0)
    if (index >= 0) {
      state.subscribers.splice(index, 1)
    }

    for (let idx = 0; idx < userList.value.length; idx++) {
      if (stream.streamId === userList.value[idx].stream.streamId) {
        userList.value.splice(idx, 1)
      }
    }
  })
}

// 토큰 생성
const createToken = async (sessionId) => {
  const response = await axiosJwt.post(
    VITE_SERVER + '/meetings/sessions/' + sessionId + '/connections',
    {
      role: 'MODERATOR'
    },
    {
      headers: { 'Content-Type': 'application/json' }
    }
  )
  return response.data.connectionToken // 토큰
}

const getToken = async () => {
  return await createToken(sessionStorage.getItem('groupSessionId'))
}

// 세션 연결 끊기
const disconnectSession = () => {
  if (state.session) {
    state.session.disconnect()

    state.session = undefined
    state.mainStreamManager = undefined
    state.publisher = undefined
    state.subscribers = []
    state.OV = undefined
  }
}

// 행사 나가기
const leaveMainMeeting = async () => {
  disconnectSession()

  sessionStorage.clear()

  emit('leave-meeting')
  router.push('/')
}

// 소그룹 나가기
const leaveGroupMeeting = async () => {
  disconnectSession()

  sessionStorage.removeItem('groupSessionId')

  router.push(`/meetings`)
}

// 소그룹 참여자들에게 메시지 보내기
const sendMessage = () => {
  if (chatMessage.value.trim() !== '') {
    state.session.signal({
      data: chatMessage.value,
      to: [],
      type: 'chat'
    })
    chatMessage.value = ''
  }
}

// 사용자 화면 클릭시 메인 화면 전환
const updateMainVideoStreamManager = async ({ stream }) => {
  if (state.mainStreamManager === stream.streamManager) return

  const index = state.subscribers.indexOf(stream.streamManager)
  if (index >= 0) {
    // state.mainStreamManager를 변경
    state.mainStreamManager = stream.streamManager
    // state.subscribers 배열에서 stream.streamManager를 제거
    state.subscribers.splice(index, 1)
    // state.publisher를 subscribers 배열의 맨 앞에 추가
    state.subscribers.splice(index, 0, state.publisher)
    state.publisher = stream.streamManager
  }
}

onMounted(() => {
  noBack()
  joinSession()
  window.addEventListener('beforeunload', disconnectSession)
})

onBeforeUnmount(() => {
  window.removeEventListener('beforeunload', disconnectSession)
})
</script>

<style scoped>
#button-container-center {
  margin: auto 0;
}

#button-container-center > div,
#button-container-right > div {
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

#button-container-center > div {
  width: 12%;
}

#button-container-center > div > button {
  margin-bottom: 10%;
  width: 65%;
  aspect-ratio: 1/1;
  border-radius: 50%;
}

#button-container-right > div > button {
  margin-bottom: 10%;
  width: 90%;
  aspect-ratio: 1/1;
  border-radius: 100%;
}

#button-container-center > div > button:hover,
#button-container-right > div > button:hover {
  background-color: #c084fc;
}

#grid-container {
  width: 100%;
  height: 100%;
  display: grid;
  grid-template-columns: repeat(auto-fit, 33%);
  align-items: center;
  justify-content: center;
  gap: 1rem;
  overflow-x: scroll;
}

#screen-change-button:hover ~ #screen-change {
  display: block;
}

#screen-change {
  display: none;
}

span {
  text-align: center;
}

span:hover {
  cursor: pointer;
}

input {
  margin: 0;
  padding: 0;
  padding-left: 2vw;
  justify-content: center;
  align-items: center;
  font-size: 1vw;
}

input::placeholder {
  align-content: center;
  font-size: 1vw;
}

.up-enter-active {
  transition: all 0.3s;
}

.up-leave-active {
  transition: all 0.3s;
}

.up-enter-from,
.up-leave-to {
  transform: translateY(10px);
  opacity: 0;
}

.down-enter-active {
  transition: all 0.3s;
}

.down-leave-active {
  transition: all 0.3s;
}

.down-enter-from,
.down-leave-to {
  transform: translateY(-10px);
  opacity: 0;
}
</style>
