<template>
  <div class="h-[100vh] w-[100vw] bg-[#fef6fb] flex">
    <div class="ml-[20vb] h-[100vh] w-[55vw] mt-[20vh]">
      <!-- 추억 결과물 카드 -->
      <div class="mt-[10vh]" v-if="isPhotoCardResult">
        <div class="flex justify-center items-center">
          <RecollectionList :key="`${route.params.resultId}`" :recollection="photocard" />
        </div>

        <button
          class="w-[12vw] mx-auto mt-[1vh] p-2 opacity-70 border-2 rounded-lg flex items-center justify-center hover:opacity-100 effect-button"
          @click="shareThumbnail()"
        >
          대표 이미지 공유하기
        </button>
      </div>
      <!--롤링페이퍼-->
      <div v-if="isRollingPaperResult">
        <div class="flex justify-center items-center w-[500px] h-[600px]">
          <p class="absolute z-20 text-[30px] pb-[500px]">{{ username }}님의 추억조각</p>

          <img
            src="@/assets/rollingtemplate/wedding.png"
            alt="template_wedding"
            width="450px"
            height="700px"
            class="z-10"
          />
          <img
            src="@/assets/rollingnote/pink.png"
            alt="`template_pink`"
            class="absolute z-20"
            width="420px"
            height="650px"
          />
        </div>
        <div class="z-30 absolute">
          <div class="absolute w-[100px] h-[100px] bottom-[380px] left-[63px]">
            <p class="text-center text-[8px] mb-1">From {{ msg[0].writer }}</p>
            <p class="text-[9px]">{{ msg[0].text }}</p>
            <div class="flex">
              <a :href="`${msg[0].video}`"
                ><button v-if="msg[0].video != null" class="border-2 border-black p-1">
                  비디오
                </button></a
              >
              <a :href="`${msg[0].voice}`"
                ><button v-if="msg[0].voice != null" class="border-2 border-black p-1">
                  오디오
                </button></a
              >
            </div>
          </div>
          <div class="absolute w-[100px] h-[100px] bottom-[380px] left-[203px]">
            <p class="text-center text-[8px] mb-1">From {{ msg[1].writer }}</p>
            <p class="text-[9px]">{{ msg[1].text }}</p>
            <div class="flex">
              <a :href="`${msg[1].video}`"
                ><button v-if="msg[1].video != null" class="border-2 border-black p-1">
                  비디오
                </button></a
              >
              <a :href="`${msg[1].voice}`"
                ><button v-if="msg[1].voice != null" class="border-2 border-black p-1">
                  오디오
                </button></a
              >
            </div>
          </div>

          <div class="absolute w-[100px] h-[100px] bottom-[380px] left-[345px]">
            <p class="text-center text-[8px] mb-1">From {{ msg[2].writer }}</p>
            <p class="text-[9px]">{{ msg[2].text }}</p>
            <div class="flex">
              <a :href="`${msg[2].video}`"
                ><button v-if="msg[2].video != null" class="border-2 border-black p-1">
                  비디오
                </button></a
              >
              <a :href="`${msg[2].voice}`"
                ><button v-if="msg[2].voice != null" class="border-2 border-black p-1">
                  오디오
                </button></a
              >
            </div>
          </div>

          <div class="absolute w-[100px] h-[100px] bottom-[235px] left-[63px]">
            <p class="text-center text-[8px] mb-1">From {{ msg[3].writer }}</p>
            <p class="text-[9px]">{{ msg[3].text }}</p>
            <div class="flex">
              <a :href="`${msg[3].video}`"
                ><button v-if="msg[3].video != null" class="border-2 border-black p-1">
                  비디오
                </button></a
              >
              <a :href="`${msg[3].voice}`"
                ><button v-if="msg[3].voice != null" class="border-2 border-black p-1">
                  오디오
                </button></a
              >
            </div>
          </div>

          <div class="absolute w-[100px] h-[100px] bottom-[235px] left-[203px]">
            <p class="text-center text-[8px] mb-1">From {{ msg[4].writer }}</p>
            <p class="text-[9px]">
              {{ msg[4].text }}
            </p>
            <div class="flex">
              <a :href="`${msg[4].video}`"
                ><button v-if="msg[4].video != null" class="border-2 border-black p-1">
                  비디오
                </button></a
              >
              <a :href="`${msg[4].voice}`"
                ><button v-if="msg[4].voice != null" class="border-2 border-black p-1">
                  오디오
                </button></a
              >
            </div>
          </div>

          <div class="absolute w-[100px] h-[100px] bottom-[235px] left-[345px]">
            <p class="text-center text-[8px] mb-1">From {{ msg[5].writer }}</p>
            <p class="text-[9px]">{{ msg[5].text }}</p>
            <div class="flex">
              <a :href="`${msg[5].video}`"
                ><button v-if="msg[5].video != null" class="border-2 border-black p-1">
                  비디오
                </button></a
              >
              <a :href="`${msg[5].voice}`"
                ><button v-if="msg[5].voice != null" class="border-2 border-black p-1">
                  오디오
                </button></a
              >
            </div>
          </div>
          <div class="absolute w-[100px] h-[100px] bottom-[90px] left-[63px]">
            <p class="text-center text-[8px] mb-1">From {{ msg[6].writer }}</p>
            <p class="text-[9px]">{{ msg[6].text }}</p>
            <div class="flex">
              <a :href="`${msg[6].video}`"
                ><button v-if="msg[6].video != null" class="border-2 border-black p-1">
                  비디오
                </button></a
              >
              <a :href="`${msg[6].voice}`"
                ><button v-if="msg[6].voice != null" class="border-2 border-black p-1">
                  오디오
                </button></a
              >
            </div>
          </div>
          <div class="absolute w-[100px] h-[100px] bottom-[90px] left-[203px]">
            <p class="text-center text-[8px] mb-1">From {{ msg[7].writer }}</p>
            <p class="text-[9px]">{{ msg[7].text }}</p>
            <div class="flex">
              <a :href="`${msg[7].video}`"
                ><button v-if="msg[7].video != null" class="border-2 border-black p-1">
                  비디오
                </button></a
              >
              <a :href="`${msg[7].voice}`"
                ><button v-if="msg[7].voice != null" class="border-2 border-black p-1">
                  오디오
                </button></a
              >
            </div>
          </div>
          <div class="absolute w-[100px] h-[100px] bottom-[90px] left-[345px]">
            <p class="text-center text-[8px] mb-1">From {{ msg[8].writer }}</p>
            <p class="text-[9px]">{{ msg[8].text }}</p>
            <div class="flex">
              <a :href="`${msg[8].video}`"
                ><button v-if="msg[8].video != null" class="border-2 border-black p-1">
                  비디오
                </button></a
              >
              <a :href="`${msg[8].voice}`"
                ><button v-if="msg[8].voice != null" class="border-2 border-black p-1">
                  오디오
                </button></a
              >
            </div>
          </div>
        </div>
        <div class="w-[500px] h-[50px]">
          <PageNavigation
            :current-page="currentPage"
            :total-page="totalPage"
            @pageChange="onPageChange"
          ></PageNavigation>
        </div>
      </div>
      <!--포토모자이크-->

      <div class="mt-[7vh]" v-if="isPhotomosaicResult">
        <div class="flex justify-center items-center">
          <div class="w-[500px] h-500px">
            <img :src="`${photomosaic_url}`" width="500px" height="500px" />
          </div>
        </div>
        <div class="flex items-center justify-center mt-[3vh]">
          <!-- <button
            class="w-[12vw] p-2 opacity-70 border-2 rounded-lg flex items-center justify-center hover:opacity-100 effect-button"
            @click="downloadPhotomosaic"
          >
            포토 모자이크 다운로드
          </button> -->
          <!-- <div class="mx-4"></div> -->
          <button
            class="w-[12vw] p-2 opacity-70 border-2 rounded-lg flex items-center justify-center hover:opacity-100 effect-button"
            @click="sharePhotomosaic"
          >
            포토 모자이크 공유하기
          </button>
        </div>
      </div>
    </div>

    <!-- 선택버튼 -->
    <div class="h-[100vh] w-[40vw] justify-center items-center" ref="top">
      <div class="text-[20px] justify-center items-center flex pt-[20vh]">
        <strong
          ><p class="effect-black flex">{{ username }}님의 추억 결과물</p>
        </strong>
      </div>
      <div class="pt-[6vh]">
        <div class="text-[20px] flex justify-center items-center">
          <div
            class="w-[20vw] rounded-3xl border-solid border-[#feb3dc] p-10 shadow-lg shadow-[#ffcae9] hover:cursor-pointer flex flex-col justify-center items-center"
            id="btn"
            @click="showRollingPaper"
          >
            <p class="mb-[1vh]"><IconGift /></p>
            <button>롤링페이퍼</button>
          </div>
          <div class="mx-8"></div>
          <div
            class="w-[20vw] rounded-3xl border-solid border-[#c398ff] p-10 shadow-lg shadow-[#f27ba4] hover:cursor-pointer flex flex-col justify-center items-center"
            id="btn"
            @click="showPhotomosaic"
          >
            <p class="mb-[1vh]"><IconHeart /></p>
            <button>포토모자이크</button>
          </div>
        </div>
      </div>
      <div class="flex items-center justify-center pt-[8vh] mb-[5vb]">
        <div class="flex items-center justify-center pr-[5vb]">
          <IconPeople />모꼬지 참여자 {{ participantCount }}명
        </div>
        <div class="flex items-center justify-center pr-[5vb]">
          <IconLetter />모꼬지 편지 {{ letterCount }}장
        </div>
      </div>
      <div class="text-[20px] flex justify-center items-center">
        <div
          class="w-[10vw] rounded-3xl border-solid border-[#c0c0c0] p-3 shadow-lg shadow-[#c0c0c0] hover:cursor-pointer flex flex-col justify-center items-center"
          id="btn"
          @click="showPhotoCard"
        >
          <p class="mb-[1vh]"></p>
          <button>추억카드</button>
        </div>
      </div>
    </div>
    <div class="h-[100vh] w-[5vw]"></div>
  </div>
  <div class="h-[15vh] w-[100vw] bg-[#fef6fb]"></div>
  <div class="h-[20vh] w-[100vw] bg-pink-50">
    <p class="text-center text-2xl pt-[5vh]">모꼬지가 마음에 드셨나요?</p>
    <div class="flex justify-center items-center pb-[10vh]">
      <div class="rate">
        <input type="radio" id="star5" name="rate" value="5" />
        <label for="star5" title="text">5 stars</label>
        <input type="radio" id="star4" name="rate" value="4" />
        <label for="star4" title="text">4 stars</label>
        <input type="radio" id="star3" name="rate" value="3" />
        <label for="star3" title="text">3 stars</label>
        <input type="radio" id="star2" name="rate" value="2" />
        <label for="star2" title="text">2 stars</label>
        <input type="radio" id="star1" name="rate" value="1" />
        <label for="star1" title="text">1 star</label>
      </div>
    </div>
  </div>
  <!-- 재확인 모달 ex. 저장되었습니다 -->
  <transition name="modal-fade">
    <div
      v-if="isSaved"
      class="fixed bottom-[50%] left-[20%] custom-translate rounded-lg bg-slate-50 px-14 py-3 z-30"
    >
      <p class="flex text-[30px]">{{ alertText }}</p>
    </div>
  </transition>
  <!--- 클립보드 복사 모달 -->
  <transition name="modal-fade">
    <div
      v-if="isCopyBoard"
      class="fixed bottom-[50%] left-[10%] custom-translate rounded-lg bg-white px-14 py-3 z-30"
    >
      <div class="flex">
        <p class="">아래 링크를 공유해주세요.</p>
      </div>
      <p class="flex text-[10px]">{{ shareLink }}</p>
      <div class="flex">
        <div class="w-[300px]"></div>
        <button
          class="ml-16 text-[10px] rounded-lg border-2 border-solid border-pink-300 p-[1px] px-[2px] mt-1 mx-auto bg-pink-300 text-white"
          @click="copyShare(`${shareLink}`)"
        >
          링크 복사
        </button>
        <button
          class="mr-16 text-[10px] rounded-lg border-2 border-solid border-pink-300 p-[1px] px-[2px] mt-1 mx-auto bg-pink-300 text-white"
          @click="showCopyModal"
        >
          닫기
        </button>
        <div class="w-[300px]"></div>
      </div>
    </div>
  </transition>
</template>
<script setup>
import IconGift from '@/icons/result/IconGift.vue'
import IconHeart from '@/icons/result/IconHeart.vue'
import IconPeople from '@/icons/result/IconPeople.vue'
import IconClose from '@/icons/result/IconClose.vue'
import IconLetter from '@/icons/result/IconLetter.vue'
import { useRoute } from 'vue-router'
import PageNavigation from '@/components/common/PageNavigation.vue'
import RecollectionList from '@/components/myevent/RecollectionList.vue'

import { ref, onMounted } from 'vue'
import {
  useRecollection,
  useUserNameStore,
  useDownloadThumbnail,
  useDownloadPhotomosaic,
  useShareImage,
  useSharePhotomosaic
} from '@/stores/result.js'

const route = useRoute()

//추억 카드 데이터
const photocard = ref({
  resultId: 0,
  name: '',
  content: '',
  image: ''
})
const msg = ref([])
const currentPage = ref(1)
const totalPage = ref(1)
const alertText = ref('')
const design = ref('')
const username = ref('')
const color = ref('')
const participantCount = ref(0)
const letterCount = ref(0)

const shareLink = ref('')
const photomosaic_url = ref('')

const recollectionStore = useRecollection()

const userNameStore = useUserNameStore()
const downloadThumbnailStore = useDownloadThumbnail()
const downloadPhotomosaicStore = useDownloadPhotomosaic()
const shareImageStore = useShareImage()
const sharePhotomosaicStore = useSharePhotomosaic()

const isSaved = ref(false)
const isCopyBoard = ref(false)
const isRollingPaperResult = ref(false)
const isPhotomosaicResult = ref(false)
const isPhotoCardResult = ref(true)

const showCopyModal = () => {
  isCopyBoard.value = false
}
const showRollingPaper = () => {
  isRollingPaperResult.value = true
  isPhotomosaicResult.value = false
  isPhotoCardResult.value = false
}

const showPhotomosaic = () => {
  isRollingPaperResult.value = false
  isPhotomosaicResult.value = true
  isPhotoCardResult.value = false
}

const showPhotoCard = () => {
  isRollingPaperResult.value = false
  isPhotomosaicResult.value = false
  isPhotoCardResult.value = true
}

const showSucceeded = async (e) => {
  isSaved.value = true
  setTimeout(() => {
    isSaved.value = false
  }, 2000)
  switch (e) {
    case '대표이미지다운':
      if (downloadThumbnail() === 1) {
        alertText.value = '바탕화면 mokkoji 폴더에 대표이미지가 저장되었어요'
      } else {
        alertText.value = '대표이미지 저장에 실패했어요'
      }
      break
    case '복사성공':
      isCopyBoard.value = false
      alertText.value = '클립보드에 복사되었습니다.'
  }
}

// const videoSource = ref('')
// const audioSource = ref('')

//클립보드 링크 복사
const copyShare = (shareLink) => {
  navigator.clipboard.writeText(shareLink)
  showSucceeded('복사성공')
}

//대표이미지 다운로드
const downloadThumbnail = () => {
  downloadThumbnailStore.DownloadThumbnail(
    photocard.value.resultId,
    (res) => {
      console.log(res)
    },
    (error) => {
      console.log(error)
    }
  )
  return 1
}

//대표이미지 공유하기
const shareThumbnail = () => {
  shareImageStore.ShareImage(
    photocard.value.resultId,
    (res) => {
      console.log(res)
      shareLink.value = res.data
      isCopyBoard.value = true
      console.log(shareLink.value)
    },
    (error) => {
      console.log(error)
    }
  )
}

//포토모자이크 다운로드 Axios
const downloadPhotomosaic = () => {
  downloadPhotomosaicStore.DownloadPhotomosaic(
    photocard.value.resultId,
    (res) => {
      console.log(res)
    },
    (error) => {
      console.log(error)
    }
  )
}

//포토모자이크 공유 Axios
const sharePhotomosaic = () => {
  sharePhotomosaicStore.SharePhotomosaic(
    photocard.value.resultId,
    (res) => {
      console.log(res)
      shareLink.value = res.data
      isCopyBoard.value = true
      console.log(shareLink.value)
    },
    (error) => {
      console.log(error)
    }
  )
}

const onPageChange = (val) => {
  console.log(val + '번 페이지로 이동 준비 끝!!!')
  currentPage.value = val
  setTimeout(() => {
    getResultView(photocard.value.resultId)
  }, 500)
}
//결과물 페이지 Axios
const getResultView = (id) => {
  console.log(`행사번호 ${id}의 result view 불러오기`)
  recollectionStore.RecollectionData(
    id,
    currentPage.value,
    (res) => {
      design.value = res.data.backgroundTemplate.toLowerCase()
      color.value = res.data.postitTemplate.toLowerCase()
      participantCount.value = res.data.participantCount
      letterCount.value = res.data.messageCount
      photocard.value.image = res.data.thumbnail
      photocard.value.content = res.data.content
      photocard.value.name = res.data.name
      photomosaic_url.value = res.data.photomosaic
      totalPage.value = res.data.totalPage
      msg.value = res.data.messageList.content
      if (msg.value.length < 9) {
        for (let i = msg.value.length; i < 9; i++) {
          msg.value.push(i)
        }
      }
      console.log(msg.value)
      console.log(res)
      console.log(design.value)
      console.log(color.value.toLowerCase().trim())
      console.log(photocard.value.image)
      console.log(photomosaic_url.value)
    },
    (error) => {
      console.log(error)
    }
  )
}

onMounted(() => {
  console.log(route.params.resultId)
  // console.log(msg.value[5].voice)
  // console.log(msg.value[5].video)
  // console.log(msg.value[4].voice)
  // console.log(msg.value[4].video)
  // console.log(msg.value[3].voice)
  // console.log(msg.value[3].video)
  photocard.value.resultId = route.params.resultId
  getResultView(photocard.value.resultId)
  setTimeout(() => {
    username.value = $cookies.get('user').name
    isSaved.value = false

    console.log(currentPage.value)
  }, 1000)
})
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
}
.rate {
  float: left;
  height: 46px;
  padding: 0 10px;
}
.rate:not(:checked) > input {
  position: absolute;
  top: -9999px;
}
.rate:not(:checked) > label {
  float: right;
  width: 1em;
  overflow: hidden;
  white-space: nowrap;
  cursor: pointer;
  font-size: 30px;
  color: #ccc;
}
.rate:not(:checked) > label:before {
  content: '★ ';
}
.rate > input:checked ~ label {
  color: #ffc700;
}
.rate:not(:checked) > label:hover,
.rate:not(:checked) > label:hover ~ label {
  color: #deb217;
}
.rate > input:checked + label:hover,
.rate > input:checked + label:hover ~ label,
.rate > input:checked ~ label:hover,
.rate > input:checked ~ label:hover ~ label,
.rate > label:hover ~ input:checked ~ label {
  color: #c59b08;
}

.swiper-container {
  width: 100%;
  height: 100%;
}
.swiper-slide {
  height: 200px;
}
</style>
