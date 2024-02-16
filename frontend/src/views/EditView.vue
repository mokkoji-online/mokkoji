<template>
  <div>
    <!-- 섹션 1/6 편집할 대상 선택 -->
    <div class="bg-violet-50 h-[600px] justify-center items-center" ref="top">
      <div class="text-[30px] justify-center items-center flex pb-10 pt-20">
        <strong><p class="effect-black flex">편집할 대상을 선택해주세요</p> </strong>
      </div>
      <div class="text-[50px] flex justify-center items-center pt-10">
        <div
          class="w-[450px] rounded-3xl border-solid border-[#D4ACDD] p-10 shadow-lg shadow-[#D4ACDD] hover:cursor-pointer flex flex-col justify-center items-center"
          id="btn"
          @click="scrollToRollingPaper"
        >
          <p class="mb-[1vh]"><IconLoveLetterColored /></p>
          <button>롤링페이퍼 편집</button>
        </div>
        <div class="mx-16"></div>
        <div
          class="w-[450px] rounded-3xl border-solid border-[#ba44de] p-10 shadow-lg shadow-[#b95fd4] hover:cursor-pointer flex flex-col justify-center items-center"
          id="btn"
          @click="scrollToPhotoMosaic"
        >
          <p class="mb-[1vh]"><IconGalleryTitle /></p>
          <button>포토모자이크 편집</button>
        </div>
      </div>
    </div>
    <!-- 섹션 2/6 모꼬지 정보, 내려가기 -->
    <div class="bg-[#ffffff] h-[500px] pt-5">
      <div class="justify-center flex hover:cursor-pointer" @click="scrollToRollingPaper">
        <IconScrollDownGray />
      </div>
      <p class="justify-center flex text-slate-500 hover:cursor-pointer">
        <strong>롤링페이퍼 편집</strong>
      </p>
      <p class="justify-center flex pt-20 text-[#c0aac7]"><IconVideoConference />Team MOKKOJI</p>
      <p class="justify-center flex pt-5 text-[#c0aac7]">
        <IconLocation />서울특별시 강남구 언주로 508
      </p>
      <p class="justify-center flex pt-5 text-[#c0aac7]"><IconInternet />https://mokkoji.online/</p>
      <p class="justify-center flex pt-5 text-[#c0aac7]"><IconWifi />i10a401.p.ssafy.io</p>
    </div>
    <!-- 섹션 3/6 롤링페이퍼 편집 -->
    <div class="bg-slate-100 h-[100vh] flex" ref="rollingpaper">
      <div class="w-[24vw] flex items-center">
        <div class="ml-[10vb]">
          <p class="flex items-center justify-center"><IconLoveLetterBlue /></p>
          <p class="flex items-center text-[25px] justify-center pt-2">원하는 템플릿을 선택한 후</p>
          <div class="flex items-center text-[25px] justify-center">'저장하기'를 눌러주세요</div>
        </div>
      </div>
      <div class="ml-[10vb] w-[25vw]">
        <div class="h-[60vh] flex pt-[30vh]">
          <div class="w-1/2">
            <p class="justify-center flex text-[25px]">배경지</p>
            <div class="justify-center flex text-[25px] mt-5 hover:cursor-pointer">
              <p class="effect-blue flex" v-if="isSelectedBasic">기본 <IconCheckBlue /></p>
              <p
                class="highlight-blue"
                v-if="isNotSelectedBasic"
                @click="selectBackground('BASIC')"
              >
                기본
              </p>
            </div>
            <div class="justify-center flex text-[25px] mt-5 hover:cursor-pointer">
              <p class="effect-blue flex" v-if="isSelectedWedding">결혼식<IconCheckBlue /></p>
              <p
                class="highlight-blue"
                v-if="isNotSelectedWedding"
                @click="selectBackground('WEDDING')"
              >
                결혼식
              </p>
            </div>
            <div class="justify-center flex text-[25px] mt-5 hover:cursor-pointer">
              <p class="effect-blue flex" v-if="isSelectedSchool">졸업식 <IconCheckBlue /></p>
              <p
                class="highlight-blue"
                v-if="isNotSelectedSchool"
                @click="selectBackground('SCHOOL')"
              >
                졸업식
              </p>
            </div>
            <div class="justify-center flex text-[25px] mt-5 hover:cursor-pointer">
              <p class="effect-blue flex" v-if="isSelectedBaby">돌잔치 <IconCheckBlue /></p>
              <p class="highlight-blue" v-if="isNotSelectedBaby" @click="selectBackground('BABY')">
                돌잔치
              </p>
            </div>
            <div class="justify-center flex text-[25px] mt-5 hover:cursor-pointer">
              <p class="effect-blue flex" v-if="isSelectedLunar">명절 <IconCheckBlue /></p>
              <p
                class="highlight-blue"
                v-if="isNotSelectedLunar"
                @click="selectBackground('LUNAR')"
              >
                명절
              </p>
            </div>
          </div>
          <div class="w-1/2">
            <p class="justify-center flex text-[25px]">속지</p>
            <div class="justify-center flex text-[25px] mt-5 hover:cursor-pointer">
              <p class="effect-skyblue flex" v-if="isSelectedPastel">파스텔 <IconCheckSkyBlue /></p>
              <p
                class="highlight-skyblue"
                v-if="isNotSelectedPastel"
                @click="selectInground('RAINBOW')"
              >
                파스텔
              </p>
            </div>
            <div class="justify-center flex text-[25px] mt-5 hover:cursor-pointer">
              <p class="effect-skyblue flex" v-if="isSelectedGreen">초록색 <IconCheckSkyBlue /></p>
              <p
                class="highlight-skyblue"
                v-if="isNotSelectedGreen"
                @click="selectInground('GREEN')"
              >
                초록색
              </p>
            </div>
            <div class="justify-center flex text-[25px] mt-5 hover:cursor-pointer">
              <p class="effect-skyblue flex" v-if="isSelectedBlue">파랑색 <IconCheckSkyBlue /></p>
              <p class="highlight-skyblue" v-if="isNotSelectedBlue" @click="selectInground('BLUE')">
                파랑색
              </p>
            </div>
            <div class="justify-center flex text-[25px] mt-5 hover:cursor-pointer">
              <p class="effect-skyblue flex" v-if="isSelectedPink">분홍색 <IconCheckSkyBlue /></p>
              <p class="highlight-skyblue" v-if="isNotSelectedPink" @click="selectInground('PINK')">
                분홍색
              </p>
            </div>
            <div class="justify-center flex text-[25px] mt-5 hover:cursor-pointer">
              <p class="effect-skyblue flex" v-if="isSelectedYellow">노랑색 <IconCheckSkyBlue /></p>
              <p
                class="highlight-skyblue"
                v-if="isNotSelectedYellow"
                @click="selectInground('YELLOW')"
              >
                노랑색
              </p>
            </div>
          </div>
        </div>
        <div class="h-[20vh]">
          <div
            class="mt-[20vh] opacity-70 border-2 rounded-lg w-40 mx-auto h-9 flex items-center justify-center hover:cursor-pointer hover:opacity-100 effect-button"
            @click="showSaved('템플릿')"
          >
            <p class="text-[20px] justify-center ml-2 flex">저장하기 <IconCheckMark /></p>
          </div>
        </div>
      </div>

      <div class="mr-[7vw] w-[50vw] flex justify-center items-center">
        <img
          :src="`src/assets/rollingtemplate/${design}.png`"
          :alt="`template_${design}`"
          class="z-10 h-[95vh]"
        />
        <img
          :src="`src/assets/rollingnote/${color}.png`"
          :alt="`template_${color}`"
          class="absolute z-20"
          width="450vw"
        />
      </div>
      <!-- 재확인 모달 ex. 저장되었습니다 -->
      <transition name="modal-fade">
        <div
          v-if="isSaved"
          class="fixed border-sm bottom-[50%] left-[50%] custom-translate rounded-lg bg-slate-50 px-14 py-3 z-30"
        >
          <p class="flex text-[30px]">{{ alertText }}</p>
        </div>
      </transition>
    </div>
    <!-- 섹션 4/6 도움말, 올라가기, 내려가기 -->
    <div class="h-[100vh] pt-5 bg-white" ref="help">
      <div class="flex justify-center items-center h-[120px]">
        <div class="mr-5 hover:cursor-pointer" @click="scrollToPhotoMosaic">
          <IconScrollDownBlue />
        </div>
        <div class="ml-5 hover:cursor-pointer" @click="scrollToTop"><IconUpArrowPurple /></div>
      </div>
      <div class="flex justify-center items-center h-[20px]">
        <div class="mr-20 text-slate-500 hover:cursor-pointer" @click="scrollToPhotoMosaic">
          <strong>포토모자이크 편집</strong>
        </div>
        <div class="mr-8 text-slate-500 hover:cursor-pointer" @click="scrollToTop">
          <strong>처음으로</strong>
        </div>
      </div>
      <div class="h-[100px] flex items-center justify-center mt-40">
        <a class="mr-12"
          ><span class="text-slate-500 text-2xl highlight-pink" @mouseover="hoverMainImage"
            >대표이미지란?</span
          ></a
        >
        <a class="mr-12"
          ><span class="text-slate-500 text-2xl highlight-green" @mouseover="hoverPhotomosaicTip"
            >포토모자이크 생성 TIP</span
          ></a
        >
        <!-- <a
          ><span class="text-slate-500 text-2xl highlight-white" @mouseover="hoverPhotoUpload"
            >사진 업로드 TIP</span
          ></a
        > -->
        <p />
      </div>
      <div class="h-[300px] bg-white flex items-center justify-center">
        <div v-if="isDefaultHelp">
          <div class="mb-10 flex items-center justify-center">
            <IconQuestionMarkGray />
          </div>
          <div>{{ direction }} 글씨 위에 마우스를 올려보세요!</div>
        </div>

        <div v-if="isHoveredMainImage" data-aos="fade-up" data-aos-duration="2000">
          <div class="flex">
            <div class="">
              <img src="@/assets/eventlist/recollection_ex.png" class="pt-10 mr-10 w-48" />
            </div>
            <div class="ml-10 text-xl">
              <p class="pt-32">추억 카드를 장식할 대표 이미지를 선택해 주세요.</p>
              <p class="">선택한 대표 이미지는 포토 모자이크 생성에도 활용됩니다.</p>
              <p class=""></p>
            </div>
          </div>
        </div>

        <div v-if="isHoveredPhotoMosaicTip" data-aos="fade-up" data-aos-duration="2000">
          <div class="flex">
            <div class="">
              <img src="@/assets/eventlist/photomosaic_ex.png" class="pt-10 mr-10 w-48" />
            </div>
            <div class="ml-10 text-xl">
              <p class="pt-32">멋진 포토 모자이크를 위해 다양한 색감의 사진들을 준비해 주세요.</p>
              <p class=""></p>
            </div>
          </div>
        </div>

        <!-- <div v-if="isHoveredPhotoUpload" data-aos="fade-up" data-aos-duration="2000">
          <div class="flex">
            <div class="">
              <img src="@/assets/eventlist/img_crop_ex.png" class="t-10 mr-10 w-48" />
            </div>
            <div class="ml-10 text-xl">
              <p class="pt-5">
                원활한 포토 모자이크 생성을 위해 허용 이미지 크기를 1:1 비율로 제한하고 있어요.
              </p>
              <p class="">'자르기 + 사진 추가'를 통해 사진을 1:1 비율로 자르고 추가해 보세요.</p>
              <p class="">
                '사진 여러 장 추가'를 통해 여러 장의 사진을 한 번에 추가할 수도 있습니다.
              </p>
            </div>
          </div>
        </div> -->
      </div>
    </div>
    <!-- 섹션 5/6 포토모자이크 편집 -->
    <div class="bg-primary2 h-[100vh]" ref="photomosaic">
      <div class="text-[25px] flex items-center justify-end pt-5 pr-5" v-if="isNotHoveredHelp">
        <p class="flex hover:cursor-pointer" @mouseover="onHelp">도움말<IconQuestionMark /></p>
      </div>
      <div class="text-[25px] flex items-center justify-end pt-5 pr-5" v-if="isHoveredHelp">
        <p
          class="effect flex hover:cursor-pointer"
          @mouseleave="outHelp"
          @click="scrollToHelp('포토 모자이크 생성 TIP')"
        >
          클릭!<IconQuestionMark />
        </p>
      </div>
      <div class="h-[100vh] flex">
        <div class="w-[27vw] flex items-center">
          <div class="ml-[10vb]">
            <p class="flex items-center justify-center"><IconGalleryColored /></p>
            <p class="flex items-center text-[25px] justify-center pt-5">대표 이미지를 지정하고</p>
            <div class="flex items-center text-[25px] justify-center">
              포토 모자이크를 생성해주세요
            </div>
          </div>
        </div>

        <div class="w-[80vw]">
          <Gallery :scrollToHelp="scrollToHelp" @reload="reloadParent" />
        </div>
      </div>
    </div>
    <!-- 섹션 6/6 올라가기 -->
    <div class="h-[200px] bg-white pt-[3vh]">
      <div class="flex justify-center items-center">
        <div class="w-[600px]"></div>
        <div class="hover:cursor-pointer">
          <div class="" @click="scrollToRollingPaper"><IconUpArrowPurple /></div>
          <div class="text-slate-500 justify-center flex" @click="scrollToTop">
            <strong>처음으로</strong>
          </div>
        </div>
        <div
          class="opacity-70 border-2 border-primary2 rounded-lg w-64 mx-auto h-9 hover:cursor-pointer hover:opacity-100 effect-button"
        >
          <a href="/eventlist"
            ><p class="text-[20px] border-sm rounded-r-lg justify-center items-center flex">
              행사 목록으로 돌아가기 <IconBackReverse />
            </p>
          </a>
        </div>
        <div class="w-[600px]"></div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import {
  useEditMemory,
  useSaveTemplate,
  useGalleryStore,
  useResultIDStore
} from '@/stores/result.js'
import IconLoveLetterColored from '@/icons/result/IconLoveLetterColored.vue'
import IconGalleryColored from '@/icons/result/IconGalleryColored.vue'
import IconGalleryTitle from '@/icons/result/IconGalleryTitle.vue'
import IconScrollDownBlue from '@/icons/result/IconScrollDownBlue.vue'
import IconUpArrowPurple from '@/icons/result/IconScrollUpPurple.vue'
import IconScrollDownGray from '@/icons/result/IconScrollDownGray.vue'
import IconInternet from '@/icons/result/IconInternet.vue'
import IconVideoConference from '@/icons/result/IconVideoConference.vue'
import IconLocation from '@/icons/result/IconLocation.vue'
import IconWifi from '@/icons/result/IconWifi.vue'
import IconLoveLetterBlue from '@/icons/result/IconLoveLetterBlue.vue'
import IconCheckMark from '@/icons/result/IconCheck.vue'
import IconQuestionMark from '@/icons/result/IconQuestionMark.vue'
import IconCheckSkyBlue from '@/icons/result/IconCheckSkyBlue.vue'
import IconCheckBlue from '@/icons/result/IconCheckBlue.vue'
import Gallery from '@/components/myedit/Gallery.vue'
import IconQuestionMarkGray from '@/icons/result/IconQuestionMarkGray.vue'
import IconBackReverse from '@/icons/result/IconBackReverse.vue'

const editMemoryStore = useEditMemory()
const saveTemplateStore = useSaveTemplate()
const galleryStore = useGalleryStore()
const resultIDStore = useResultIDStore()

const isSaved = ref(false)
const isHoveredHelp = ref(false)
const isNotHoveredHelp = ref(true)
const isDefaultHelp = ref(true)
const isHoveredMainImage = ref(false)
const isHoveredPhotoMosaicTip = ref(false)
const isHoveredPhotoUpload = ref(false)

const rollingpaper = ref(null)
const photomosaic = ref(null)
const help = ref(null)
const top = ref(null)
const resultID = ref('')
const alertText = ref('')
const direction = ref('대표이미지란?')

//템플릿
const design = ref('basic')
const color = ref('rainbow')

const isSelectedBasic = ref(true)
const isSelectedBaby = ref(false)
const isSelectedWedding = ref(false)
const isSelectedLunar = ref(false)
const isSelectedSchool = ref(false)

const isNotSelectedBasic = ref(false)
const isNotSelectedBaby = ref(true)
const isNotSelectedWedding = ref(true)
const isNotSelectedLunar = ref(true)
const isNotSelectedSchool = ref(true)

const isSelectedPastel = ref(true)
const isSelectedYellow = ref(false)
const isSelectedBlue = ref(false)
const isSelectedGreen = ref(false)
const isSelectedPink = ref(false)

const isNotSelectedPastel = ref(false)
const isNotSelectedBlue = ref(true)
const isNotSelectedYellow = ref(true)
const isNotSelectedGreen = ref(true)
const isNotSelectedPink = ref(true)

//아무것도 지정 안해도 값을 넘겨줌
const rollingPaperTemplate = ref({
  backgroundName: 'basic',
  postitName: 'rainbow'
})

const onHelp = () => {
  isHoveredHelp.value = true
  isNotHoveredHelp.value = false
}

const outHelp = () => {
  isHoveredHelp.value = false
  isNotHoveredHelp.value = true
}

const scrollToTop = () => {
  top.value.scrollIntoView({ behavior: 'smooth' })
}

const scrollToRollingPaper = () => {
  rollingpaper.value.scrollIntoView({ behavior: 'smooth' })
}

const scrollToPhotoMosaic = () => {
  photomosaic.value.scrollIntoView({ behavior: 'smooth' })
}

const scrollToHelp = (text) => {
  console.log(text)
  direction.value = text
  help.value.scrollIntoView({ behavior: 'smooth' })
}

const hoverPhotomosaicTip = () => {
  console.log("I'm here")
  isHoveredPhotoMosaicTip.value = true
  isHoveredMainImage.value = false
  isHoveredPhotoUpload.value = false
  isDefaultHelp.value = false
}

const hoverPhotoUpload = () => {
  isHoveredPhotoMosaicTip.value = false
  isHoveredMainImage.value = false
  isHoveredPhotoUpload.value = true
  isDefaultHelp.value = false
}

const reloadParent = () => {
  setTimeout(() => {
    location.reload()
    console.log('reload 완료')
    scrollToPhotoMosaic()
  }, 5000)
}

const hoverMainImage = () => {
  isHoveredPhotoMosaicTip.value = false
  isHoveredMainImage.value = true
  isHoveredPhotoUpload.value = false
  isDefaultHelp.value = false
}

//저장 모달
const showSaved = (e) => {
  isSaved.value = true
  setTimeout(() => {
    isSaved.value = false
  }, 2000)
  switch (e) {
    case '템플릿':
      if (saveTemplate(resultID.value) === 1) {
        alertText.value = '선택한 템플릿을 저장했어요'
      } else {
        alertText.value = '저장에 실패했어요'
      }
      break
  }
}

//템플릿 저장 Axios
const saveTemplate = (id) => {
  console.log(`행사번호 ${id}의 템플릿 저장하기`)
  let result = 1
  saveTemplateStore.saveTemplate(
    id,
    rollingPaperTemplate.value,
    (res) => {
      console.log(res)
      console.log('저장되었습니다.')
      result = 1
    },
    (error) => {
      console.log(error)
      result = 0
    }
  )
  return result
}

//편집 페이지 Axios
const getEditView = (id) => {
  console.log(`행사번호 ${id}의 edit view 불러오기`)
  editMemoryStore.editMemory(
    id,
    (res) => {
      console.log(res)

      selectBackground(res.data.rollingpaperTemplate.backgroundTemplate.backgroundName)
      selectInground(res.data.rollingpaperTemplate.postitTemplate.postitName)
      galleryStore.setUploadedPhotos(res.data.photoPath)
      // console.log(res.data.photoPath)
      // console.log(galleryStore.uploadedPhotos)
      // console.log('edit view 데이터 불러옴')
    },
    (error) => {
      console.log(error)
    }
  )
}

//배경지
const selectBackground = (value) => {
  isSelectedBasic.value = false
  isSelectedBaby.value = false
  isSelectedWedding.value = false
  isSelectedLunar.value = false
  isSelectedSchool.value = false

  isNotSelectedBasic.value = true
  isNotSelectedBaby.value = true
  isNotSelectedWedding.value = true
  isNotSelectedLunar.value = true
  isNotSelectedSchool.value = true

  switch (value) {
    case 'BASIC':
      design.value = 'basic'
      rollingPaperTemplate.value.backgroundName = 'basic'
      isSelectedBasic.value = true
      isNotSelectedBasic.value = false
      break
    case 'WEDDING':
      design.value = 'wedding'
      rollingPaperTemplate.value.backgroundName = 'wedding'
      isSelectedWedding.value = true
      isNotSelectedWedding.value = false
      break
    case 'SCHOOL':
      design.value = 'school'
      rollingPaperTemplate.value.backgroundName = 'school'
      isSelectedSchool.value = true
      isNotSelectedSchool.value = false
      break
    case 'LUNAR':
      design.value = 'lunar'
      rollingPaperTemplate.value.backgroundName = 'lunar'
      isSelectedLunar.value = true
      isNotSelectedLunar.value = false
      break
    case 'BABY':
      design.value = 'baby'
      rollingPaperTemplate.value.backgroundName = 'baby'
      isSelectedBaby.value = true
      isNotSelectedBaby.value = false
      break
    default:
      design.value = 'basic'
      rollingPaperTemplate.value.backgroundName = 'basic'
      isSelectedBasic.value = true
      isNotSelectedBasic.value = false
  }
}
//속지
const selectInground = (value) => {
  isSelectedPastel.value = false
  isSelectedYellow.value = false
  isSelectedBlue.value = false
  isSelectedGreen.value = false
  isSelectedPink.value = false

  isNotSelectedPastel.value = true
  isNotSelectedYellow.value = true
  isNotSelectedBlue.value = true
  isNotSelectedGreen.value = true
  isNotSelectedPink.value = true
  switch (value) {
    case 'RAINBOW':
      color.value = 'rainbow'
      rollingPaperTemplate.value.postitName = 'rainbow'
      isNotSelectedPastel.value = false
      isSelectedPastel.value = true
      break
    case 'GREEN':
      color.value = 'green'
      rollingPaperTemplate.value.postitName = 'green'
      isNotSelectedGreen.value = false
      isSelectedGreen.value = true
      break
    case 'BLUE':
      color.value = 'blue'
      rollingPaperTemplate.value.postitName = 'blue'
      isNotSelectedBlue.value = false
      isSelectedBlue.value = true
      break
    case 'PINK':
      color.value = 'pink'
      rollingPaperTemplate.value.postitName = 'pink'
      isNotSelectedPink.value = false
      isSelectedPink.value = true
      break
    case 'YELLOW':
      color.value = 'yellow'
      rollingPaperTemplate.value.postitName = 'yellow'
      isNotSelectedYellow.value = false
      isSelectedYellow.value = true
      break
    default:
      color.value = 'rainbow'
      rollingPaperTemplate.value.postitName = 'rainbow'
      isNotSelectedPastel.value = false
      isSelectedPastel.value = true
  }
}

//처음으로
onMounted(() => {
  window.scrollTo(0, 0)
  resultID.value = resultIDStore.getID
  getEditView(resultIDStore.getID)
})
</script>

<style scoped>
.effect-black {
  box-shadow: inset 0 -2px 0 #070700;
  color: black;
}

.effect {
  box-shadow: inset 0 -6px 0 #57b5be;
  color: black;
}

.effect-button:hover {
  box-shadow: inset 0 -200px 0 #b0e3e2;
  color: black;
}

.effect-button-two:hover {
  box-shadow: inset 0 -50px 0 #c8eaed;
  color: black;
}

.effect-blue {
  box-shadow: inset 0 -2px 0 hsl(257, 91%, 25%);
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

.highlight-blue:hover {
  box-shadow: inset 0 -2px 0 hsl(257, 91%, 25%);
  color: black;
}

.highlight-skyblue:hover {
  box-shadow: inset 0 -2px 0 black;
  color: black;
}

.effect-skyblue {
  box-shadow: inset 0 -2px 0 black;
  color: black;
}

#btn:active {
  margin-top: 5px;
  box-shadow: none;
}

.modal-fade-enter-active,
.modal-fade-leave-active {
  transition: opacity 0.5s;
}
.modal-fade-enter,
.modal-fade-leave-to {
  opacity: 0;
}

.custom-translate {
  transform: translate(-50%, -50%);
}
</style>
