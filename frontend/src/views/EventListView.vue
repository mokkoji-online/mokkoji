<template>
  <!--섹션 1/3, 유저 이름 -->
  <div class="text-[50px] flex px-2 h-[500px] items-center pl-32 bg-violet-50">
    <a v-if="isNotShown" class=""
      ><strong>{{ name }}</strong
      >님의 모꼬지</a
    >
    <a v-if="isShown" class="effect-pink"
      ><strong>{{ name }}</strong
      >님의 모꼬지</a
    >
    <div class="flex">
      <div v-if="isNotShownTwo" class=""><IconFlowers /></div>
      <div v-if="isShownTwo" class=""><IconFlowersColored /></div>
    </div>
    <div class="">
      <div class="ml-64">
        <a class="mr-10"
          ><span class="text-[#610091] text-lg highlight-pink" @mouseover="hoverPhotomosaic"
            >포토 모자이크란?</span
          ></a
        >
        <a class="mr-10"
          ><span class="text-[#610091] text-lg highlight-green" @mouseover="hoverRollingPaper"
            >롤링페이퍼란?</span
          ></a
        >
        <a class="mr-10"
          ><span class="text-[#610091] text-lg highlight-white" @mouseover="hoverBeCareful"
            >기억 주의사항</span
          ></a
        >
      </div>
      <div class="ml-48 w-[600px]">
        <div v-if="isHoveredPhotoMosaic" data-aos="fade-up" data-aos-duration="2000">
          <div class="flex text-lg">
            <div class="w-1/4 ml-10">
              <img src="@/assets/eventlist/photomosaic_ex.png" class="pt-10 w-24 h-40" />
            </div>
            <div class="w-3/4 pl-1">
              <p class="pt-20">여러 장의 사진을 색상에 맞게 배열해</p>
              <p class="">한 장의 큰 이미지를 표현하는 것입니다.</p>
            </div>
          </div>
        </div>

        <div v-if="isHoveredRollingPaper" data-aos="fade-up" data-aos-duration="2000">
          <div class="flex text-lg">
            <div class="w-1/4 ml-10">
              <img src="@/assets/eventlist/rollingpaper_ex.png" class="pt-10 w-24 h-40" />
            </div>
            <div class="w-3/4 pl-1">
              <p class="pt-20">참여자가 남긴 텍스트, 영상, 음성 메시지를</p>
              <p class="">모꼬지 전용 템플릿에서 한 눈에 확인해보세요.</p>
            </div>
          </div>
        </div>

        <div v-if="isHoveredBeCareful" data-aos="fade-up" data-aos-duration="2000">
          <div class="flex text-lg">
            <div class="w-1/4 ml-5">
              <img src="@/assets/eventlist/warning.png" class="pt-10 w-30 h-40" />
            </div>
            <div class="w-3/4 pl-1">
              <p class="pt-20">
                편집 가능 기간 <strong>(모임 주최일로부터 한 달)</strong>이 지나면
              </p>
              <p class="">
                롤링페이퍼, 포토 모자이크 생성 및 추억 생성이
                <strong class="text-red-400">불가</strong>합니다.
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <!-- 섹션 2/3, 기억: Memory -->
  <div class="bg-white">
    <div class="justify-center pt-10 ml-24 w-[40%]">
      <div class="flex">
        <strong class="text-[40px]">기억 </strong>
        <p class="text-3xl mt-3">: Memory</p>
      </div>
      <p class="pt-2 text-[20px]">편집 버튼으로 롤링페이퍼와 포토모자이크를 만드세요.</p>
      <p class="pt-2 text-[20px] mb-10">추억 쌓기를 통해 모꼬지를 추억으로 간직하세요.</p>
    </div>
    <div class="h-96 overflow-scroll mx-14 custom-cursor" ref="Memories">
      <div class="whitespace-nowrap flex">
        <MemoryList v-for="memory in memories" :key="memory.id" :memory="memory"> </MemoryList>
      </div>
    </div>
  </div>
  <!-- 섹션 3/3, 추억: Recollection -->
  <div class="h-[600px] bg-[#ffeff8] pt-10">
    <div class="justify-center ml-24">
      <div class="flex">
        <strong class="text-[40px]">추억 </strong>
        <p class="text-3xl mt-3">: Recollection</p>
      </div>
      <p class="pt-2 text-[20px]">
        대표 이미지를 클릭하면 완성된 롤링페이퍼와 포토 모자이크를 확인할 수 있어요.
      </p>
    </div>

    <div class="h-96 overflow-scroll mt-10 mx-14 custom-cursor" ref="Recollections">
      <div class="whitespace-nowrap flex">
        <!--추억 컴포넌트-->
        <RecollectionList
          v-for="recollection in recollections"
          :key="recollection.resultId"
          :recollection="recollection"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import {
  useEventList,
  useRecollectionsStore,
  useMemoriesStore,
  useUserNameStore
} from '@/stores/result.js'
import IconFlowers from '@/icons/result/IconFlowers.vue'
import IconFlowersColored from '@/icons/result/IconFlowersColored.vue'
import MemoryList from '@/components/myevent/MemoryList.vue'
import RecollectionList from '@/components/myevent/RecollectionList.vue'

const eventListStore = useEventList()
const recollectionsStore = useRecollectionsStore()
const memoriesStore = useMemoriesStore()
const userNameStore = useUserNameStore()
const name = ref('')

const isHoveredPhotoMosaic = ref(false)
const isHoveredRollingPaper = ref(true)
const isHoveredBeCareful = ref(false)
const isShown = ref(false)
const isNotShown = ref(true)
const isShownTwo = ref(false)
const isNotShownTwo = ref(true)

const isMouseDown = ref(false)
const startX = ref(0)
const scrollLeft = ref(0)

let memories = ref([])
let recollections = ref([])

const Recollections = ref(null)
const Memories = ref(null)
const hoverPhotomosaic = () => {
  isHoveredPhotoMosaic.value = true
  isHoveredRollingPaper.value = false
  isHoveredBeCareful.value = false
}

const hoverRollingPaper = () => {
  isHoveredPhotoMosaic.value = false
  isHoveredRollingPaper.value = true
  isHoveredBeCareful.value = false
}

const hoverBeCareful = () => {
  isHoveredPhotoMosaic.value = false
  isHoveredRollingPaper.value = false
  isHoveredBeCareful.value = true
}

const setShow = () => {
  setTimeout(() => {
    isShown.value = true
    isNotShown.value = false
  }, 500)
}

const setShowTwo = () => {
  setTimeout(() => {
    isShownTwo.value = true
    isNotShownTwo.value = false
  }, 1000)
}

//EventList 불러오기 Axios
const getEventList = () => {
  console.log('user이름님의 event list 불러오기')
  //API 호출
  eventListStore.eventList(
    (res) => {
      console.log(res)
      memories.value = res.data.memoryList
      memoriesStore.setMemories = memories.value
      console.log(memoriesStore.getMemories)
      recollectionsStore.setRecollections = recollections.value
      recollections.value = res.data.recollectionList
      console.log(recollectionsStore.getRecollections)
    },
    (error) => {
      console.log(error)
    }
  )
}

//기억, 추억 리스트 마우스로 스크롤
const scrollMethods = (e) => {
  e.value.addEventListener('mousedown', (event) => {
    isMouseDown.value = true
    startX.value = event.pageX - e.value.offsetLeft
    scrollLeft.value = e.value.scrollLeft
  })

  e.value.addEventListener('mouseleave', () => {
    isMouseDown.value = false
  })

  e.value.addEventListener('mouseup', () => {
    isMouseDown.value = false
  })

  e.value.addEventListener('mousemove', (event) => {
    if (!isMouseDown.value) return

    event.preventDefault()
    const x = event.pageX - e.value.offsetLeft
    const beforeScrollLeft = (x - startX.value) * 1
    e.value.scrollLeft = scrollLeft.value - beforeScrollLeft
  })
}

onMounted(() => {
  userNameStore.setName($cookies.get('user').name)

  name.value = userNameStore.getName
  console.log(name.value)
  setShow()
  setShowTwo()
  getEventList()
  window.onload = () => {
    scrollMethods(Memories)
    scrollMethods(Recollections)
  }
})
</script>

<style scoped>
.modal-fade-enter-active,
.modal-fade-leave-active {
  transition: opacity 0.3s ease-in-out;
}

.modal-fade-enter,
.modal-fade-leave-to {
  opacity: 0;
}

span {
  box-shadow: inset 0 0 0 0 #000608;
  color: #610091;
  margin: 0 -0.25rem;
  padding: 0 0.25rem;
  transition:
    color 0.3s ease-in-out,
    box-shadow 0.3s ease-in-out;
}
.effect-pink {
  box-shadow: inset 0 -10px 0 #d486fe;
  color: black;
}

.highlight-green:hover {
  box-shadow: inset 0 -3px 0 hsl(123, 92%, 76%);
  color: black;
}

.highlight-pink:hover {
  box-shadow: inset 0 -3px 0 #ffa4dc;
  color: black;
}
.highlight-white:hover {
  box-shadow: inset 0 -3px 0 #bab9b9;
  color: black;
}

.custom-cursor {
  cursor:
    url('src/assets/eventlist/scrollIcon.svg') 2 2,
    auto;
}
</style>
