<template>
  <div class="h-[100vh] w-[100vw] bg-[#fef6fb] flex">
    <div class="ml-[10vb] h-[100vh] w-[55vw] mt-[8vh]">
      <!-- 추억 결과물 카드 -->
      <div class="mt-[10vh]" v-if="isPhotoCardResult">
        <div class="flex justify-center items-center">
          <RecollectionList :key="resultIDStore.getID" :recollection="photocard" />
        </div>
        <button
          class="w-[12vw] mx-auto mt-[1vh] p-2 opacity-70 border-2 rounded-lg flex items-center justify-center hover:opacity-100 effect-button"
          @click="downloadThumbnail"
        >
          대표 이미지 다운로드
        </button>
        <button
          class="w-[12vw] mx-auto mt-[1vh] p-2 opacity-70 border-2 rounded-lg flex items-center justify-center hover:opacity-100 effect-button"
          @click="shareThumbnail"
        >
          대표 이미지 공유하기
        </button>
      </div>
      <!--롤링페이퍼-->
      <div v-if="isRollingPaperResult">
        <div class="flex justify-center items-center">
          <p class="absolute z-20 text-[30px] pb-[500px]">{{ username }}님의 추억조각</p>
          <div class="absolute z-30 w-[800px] h-[1000px]"></div>

          <img
            :src="`src/assets/rollingtemplate/wedding.png`"
            :alt="`template_${design}`"
            width="450px"
            height="700px"
            class="z-10"
          />
          <img
            :src="`src/assets/rollingnote/rainbow.png`"
            :alt="`template_${color}`"
            class="absolute z-20"
            width="420px"
            height="650px"
          />
        </div>

        <PageNavigation
          :current-page="currentPage"
          :total-page="totalPage"
          @pageChange="onPageChange"
        ></PageNavigation>
      </div>
      <!--포토모자이크-->

      <div class="mt-[7vh]" v-if="isPhotomosaicResult">
        <div class="flex justify-center items-center">
          <div class="w-[500px] h-[500px] border-2 border-black"></div>
        </div>
        <div class="flex items-center justify-center mt-[3vh]">
          <button
            class="w-[12vw] p-2 opacity-70 border-2 rounded-lg flex items-center justify-center hover:opacity-100 effect-button"
            @click="downloadPhotomosaic"
          >
            포토 모자이크 다운로드
          </button>
          <div class="mx-4"></div>
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
          ><p class="effect-black flex">{{ photocard.name }}님의 추억 결과물</p>
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
        <!-- <div class="flex items-center justify-center">
          <IconPhoto />모꼬지 사진 {{ photocard.photos }}장
        </div> -->
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
  <div class="h-[30vh] w-[100vw] bg-pink-50">
    <p class="text-center text-2xl pt-[5vh]">모꼬지가 마음에 드셨나요?</p>
    <div class="flex justify-center items-center pt-[vh]">
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
</template>
<script setup>
import IconGift from '@/icons/result/IconGift.vue'
import IconHeart from '@/icons/result/IconHeart.vue'
import IconPeople from '@/icons/result/IconPeople.vue'
import IconPhoto from '@/icons/result/IconPhoto.vue'
import IconLetter from '@/icons/result/IconLetter.vue'
import msg from '@/temp/result/messages.json'
import RollingPaperItem from '@/components/myresult/RollingPaperItem.vue'
import PageNavigation from '@/components/common/PageNavigation.vue'

// Import Swiper styles
import 'swiper/css'
import 'swiper/css/pagination'
import '../pagination.css'

import RecollectionList from '@/components/myevent/RecollectionList.vue'
// import PhotoCard from '@/temp/result/photocard.json'

import { ref, onMounted } from 'vue'
import {
  useRecollection,
  useResultIDStore,
  useUserNameStore,
  useDownloadThumbnail,
  useDownloadPhotomosaic,
  useShareImage,
  useSharePhotomosaic
} from '@/stores/result.js'

// let photocard = PhotoCard

const photocard = ref({
  name: '',
  content: '',
  image: ''
})

const currentPage = ref(1)
const totalPage = ref(10)

const recollectionStore = useRecollection()
const resultIDStore = useResultIDStore()
const userNameStore = useUserNameStore()
const downloadThumbnailStore = useDownloadThumbnail()
const downloadPhotomosaicStore = useDownloadPhotomosaic()
const shareImageStore = useShareImage()
const sharePhotomosaicStore = useSharePhotomosaic()

const design = ref('')
const username = ref('')
const color = ref('')
const participantCount = ref(0)
const letterCount = ref(0)
// const currentPage = ref(1)
// const totalPage = ref(0)

const isRollingPaperResult = ref(false)
const isPhotomosaicResult = ref(false)
const isPhotoCardResult = ref(true)

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

const downloadThumbnail = () => {
  downloadThumbnailStore.DownloadThumbnail(
    resultIDStore.getID,
    (res) => {
      console.log(res)
    },
    (error) => {
      console.log(error)
    }
  )
}

const downloadPhotomosaic = () => {
  downloadPhotomosaicStore.DownloadPhotomosaic(
    resultIDStore.getID,
    (res) => {
      console.log(res)
    },
    (error) => {
      console.log(error)
    }
  )
}

const shareThumbnail = () => {
  shareImageStore.ShareImage(
    resultIDStore.getID,
    (res) => {
      console.log(res)
    },
    (error) => {
      console.log(error)
    }
  )
}

const sharePhotomosaic = () => {
  sharePhotomosaicStore.SharePhotomosaic(
    resultIDStore.getID,
    (res) => {
      console.log(res)
    },
    (error) => {
      console.log(error)
    }
  )
}

const onPageChange = (val) => {
  console.log(val + '번 페이지로 이동 준비 끝!!!')
  currentPage.value = val
  // param.value.pgno = val
  // getHotArticleList()
}
const getResultView = (id) => {
  console.log(`행사번호 ${id}의 result view 불러오기`)
  recollectionStore.RecollectionData(
    id,
    0,
    (res) => {
      design.value = res.data.backgroundTemplate
      color.value = res.data.postitTemplate
      participantCount.value = res.data.participantCount
      letterCount.value = res.data.messageCount
      photocard.value.image = res.data.thumbnail
      photocard.value.content = res.data.content
      photocard.value.name = res.data.name
      console.log(res)
    },
    (error) => {
      console.log(error)
    }
  )
}

onMounted(() => {
  getResultView(resultIDStore.getID)
  username.value = userNameStore.getName
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
