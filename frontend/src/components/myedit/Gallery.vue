<template>
  <div class="flex mt-10 ml-10">
    <div class="w-[20vw] pt-[15vh]">
      <div class="flex justify-center items-center text-[25px]">
        <p class="flex ml-3">선택한 사진 <IconCheckBlue /></p>
      </div>
      <div class="flex justify-center items-center">
        <div
          class="flex justify-center items-center w-[14vw] h-[14vw] mt-5 border-[10px] border-[#9f46c8]"
        >
          <img
            :src="selectedImage"
            alt="Selected Image"
            width="80%"
            height="80%"
            class="mx-10 my-10"
          />
        </div>
      </div>
      <div class="">
        <div
          class="mt-[3vh] opacity-70 border-[3px] rounded-lg h-[2.5vw] w-[15vw] mx-auto border-[#9f46c8] justify-center items-center flex hover:cursor-pointer hover:opacity-100"
          @click="showSaved('대표이미지')"
        >
          <p class="text-[20px] flex text-center">대표이미지 설정하기 <IconMainPhoto /></p>
        </div>

        <div
          class="mt-[3vh] opacity-70 border-[3px] rounded-lg h-[2.5vw] w-[15vw] mx-auto border-[#9f46c8] justify-center items-center flex hover:cursor-pointer hover:opacity-100"
          @click="showSaved('포토모자이크')"
        >
          <p class="text-[20px] flex text-center">포토모자이크 생성하기 <IconMosaic /></p>
        </div>
      </div>
    </div>
    <div class="w-[2vw]"></div>
    <div class="">
      <div class="flex justify-end items-end pt-[1vh]">
        <button
          class="opacity-70 text-sm mr-5 w-[10vw] h-[4.5vh] border-[#5da2bd] rounded-lg hover:opacity-100 border-2 border-solid mb-5 effect-button-two"
          @click="showPhotoUploadModal"
        >
          <IconPhotoAdd /> 사진 여러 장 추가
        </button>
        <button
          class="opacity-70 text-sm w-[10vw] h-[4.5vh] border-[#5da2bd] rounded-lg hover:opacity-100 border-2 border-solid mb-5 effect-button-two"
          @click="showCutPhotoUploadModal"
        >
          <IconCrop /> 자르기 + 사진 추가
        </button>
      </div>
      <div class="ml-4 h-[70vh] w-[40vw] border-2 rounded-lg border-[#5da2bd]">
        <div ref="scrollContainer" class="custom-scroll-container">
          <div ref="scrollComponent" class="custom-scroll-content">
            <ul class="photo-grid">
              <li v-for="(photo, index) in photos" :key="index" @click="selectedPhoto(photo)">
                <img :src="`${photo}`" />
              </li>
            </ul>
          </div>
          <div v-if="isPreviewOpen" class="photo-preview-modal" @click="closePhotoPreview">
            <img :src="previewPhoto" class="preview-image" />
          </div>
        </div>
      </div>
    </div>
  </div>
  <!-- 재확인 모달 ex. 저장되었습니다 -->
  <transition name="modal-fade">
    <div
      v-if="isSaved"
      class="fixed bottom-[50%] left-[50%] custom-translate rounded-lg bg-slate-50 px-14 py-3 z-30"
    >
      <p class="flex text-[30px]">{{ alertText }}</p>
    </div>
  </transition>
  <!-- 사진 업로드 모달 - 자르기 -->
  <div
    v-if="isCutPhotoUploadModal"
    class="fixed bg-white bottom-[100px] left-[500px] w-[800px] h-[500px] border-2 rounded-lg border-[#5da2bd] z-20 bg-[#f5fcff]"
  >
    <div class="flex">
      <p class="text-lg my-5 ml-40">
        원하는 이미지를 정사각형 형태로 자른 뒤, 이미지 업로드를 진행해 주세요
      </p>
      <button class="ml-24 mb-4" @click="closeCutPhotoUploadModal"><IconClose /></button>
    </div>
    <div class="flex content-center items-center">
      <ImgUpload @closeModal="closeCutPhotoUploadModal" />

      <!--성공, 실패 여부에 따라 메시지 출력 필요.-->
    </div>
  </div>
  <!-- 사진 업로드 모달 - bulk -->
  <div
    v-if="isPhotoUploadModal"
    class="fixed bottom-[30px] left-[200px] w-[1000px] h-[600px] border-2 rounded-lg border-[#5da2bd] z-20 bg-[#f5fcff]"
  >
    <button class="absolute right-[10px] top-[10px]" @click="closePhotoUploadModal">
      <IconClose />
    </button>
    <div class="flex content-center items-center">
      <ImgUploadBulk @closeModal="closePhotoUploadModal" />
    </div>
  </div>
  <!-- 사진 업로드 설명 모달-->
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import getImages from '@/api/get_images'
import {
  useMainImageStore,
  useGalleryStore,
  useSaveThumbnail,
  useResultIDStore,
  useCreatePhotomosaic
} from '@/stores/result.js'
import IconPhotoAdd from '@/icons/result/IconPhotoAdd.vue'
import IconClose from '@/icons/result/IconClose.vue'
import ImgUpload from '@/components/myedit/ImgUpload.vue'
import ImgUploadBulk from '@/components/myedit/ImgUploadBulk.vue'
import IconCrop from '@/icons/result/IconCrop.vue'
import IconMosaic from '@/icons/result/IconMosaic.vue'
import IconMainPhoto from '@/icons/result/IconMainPhoto.vue'
import IconCheckBlue from '@/icons/result/IconCheckMark.vue'

const galleryStore = useGalleryStore()
const saveThumbnailStore = useSaveThumbnail()
const resultIDStore = useResultIDStore()
const createPhotomosaicStore = useCreatePhotomosaic()

const props = defineProps({
  scrollToHelp: Function
})
const emit = defineEmits(['reload'])

const store = useMainImageStore()
const selectedImage = ref('src/assets/edit/no_image.png')

const alertText = ref('')
const isSaved = ref(false)
const isCutPhotoUploadModal = ref(false)
const isPhotoUploadModal = ref(false)
const isSelectedImage = ref(false)

const photos = ref([])
const gallery = ref([])
const endPoint = ref(0)
const totalPhotos = ref(0)
const scrollContainer = ref(null)
const scrollComponent = ref(null)
const isPreviewOpen = ref(false)
const previewPhoto = ref('')

const showCutPhotoUploadModal = () => {
  isPhotoUploadModal.value = false
  isCutPhotoUploadModal.value = !isCutPhotoUploadModal.value
}

const closeCutPhotoUploadModal = () => {
  isPhotoUploadModal.value = false
  isCutPhotoUploadModal.value = !isCutPhotoUploadModal.value
  loadGallery()
}

const showPhotoUploadModal = () => {
  isCutPhotoUploadModal.value = false
  isPhotoUploadModal.value = !isPhotoUploadModal.value
}

const closePhotoUploadModal = () => {
  isCutPhotoUploadModal.value = false
  isPhotoUploadModal.value = !isPhotoUploadModal.value
  loadGallery()
}
//무한스크롤
const loadPhotos = (number) => {
  const newImages = getImages(0, number, gallery.value)
  endPoint.value = number
  photos.value = newImages
}

const loadMorePhotos = () => {
  const remainingPhotosCount = totalPhotos.value - endPoint.value
  const newImages = getImages(
    endPoint.value,
    endPoint.value + Math.min(10, remainingPhotosCount),
    gallery.value
  )
  endPoint.value = endPoint.value + Math.min(10, remainingPhotosCount)
  photos.value.push(...newImages)
}

const handleScroll = () => {
  const component = scrollComponent.value
  const container = scrollContainer.value
  if (container.scrollTop + container.clientHeight >= component.offsetHeight) {
    loadMorePhotos()
  }
}
//사진 선택
const selectedPhoto = (photo) => {
  store.selectedImage(photo) //data 보내기
  selectedImage.value = store.getSelectedImage
  isSelectedImage.value = true
}

//대표 이미지 생성 Axios
const saveThumbnail = (id) => {
  console.log(`행사번호 ${id}의 대표이미지 저장하기`)
  let result = 1
  saveThumbnailStore.saveThumbnail(
    id,
    selectedImage.value,
    (res) => {
      console.log(res)
      console.log('대표이미지가 저장되었습니다.')
      result = 1
    },
    (error) => {
      console.log(error)
      result = 0
    }
  )
  return result
}

//포토 모자이크 생성 Axios
const createPhotomosaic = (id) => {
  console.log(`행사번호 ${id}의 포토모자이크 생성하기`)
  let result = 1
  createPhotomosaicStore.CreatePhotomosaic(
    id,
    (res) => {
      console.log(res)
      result = 1
    },
    (error) => {
      console.log(error)
      result = 0
    }
  )
  return result
}

//성공 실패 모달 알림창
const showSaved = (e) => {
  isSaved.value = true
  setTimeout(() => {
    isSaved.value = false
  }, 2000)
  switch (e) {
    case '포토모자이크':
      if (createPhotomosaic(resultIDStore.getID) === 1) {
        alertText.value = '포토모자이크를 생성했어요'
      } else {
        alertText.value = '포토모자이크 생성에 실패했어요'
      }
      break
    case '대표이미지':
      if (!isSelectedImage.value) {
        alertText.value = '대표이미지 설정에 실패했어요'
      } else {
        if (saveThumbnail(resultIDStore.getID) === 1) {
          alertText.value = '대표이미지를 설정했어요'
        } else {
          alertText.value = '대표이미지 설정에 실패했어요'
        }
      }
      break
  }
}

const loadGallery = () => {
  emit('reload')
}

onMounted(() => {
  setTimeout(() => {
    totalPhotos.value = galleryStore.uploadedPhotos.length //총 사진의 수
    console.log(totalPhotos.value)
    gallery.value = galleryStore.uploadedPhotos //사진 전체

    if (totalPhotos.value < 30) {
      //20장 이하이면 그 갯수만
      loadPhotos(totalPhotos.value)
    } else {
      loadPhotos(30)
    }
  }, 200)
  scrollContainer.value.addEventListener('scroll', handleScroll)
})
</script>

<style scoped>
.highlight-yellow {
  box-shadow: inset 0 -6px 0 #833c7e;
  color: black;
}

.custom-scroll-container {
  overflow-y: scroll;
  height: 68vh;
  scrollbar-width: thin; /* For Firefox */
  scrollbar-color: rgb(255, 255, 255) rgb(190, 212, 244); /* For Firefox */
}

.photo-grid {
  list-style: none;
  padding: 0;
  margin: 0;
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 0px;
}

.custom-scroll-container::-webkit-scrollbar {
  width: 8px;
}

.custom-scroll-container::-webkit-scrollbar-thumb {
  background-color: rgb(121, 146, 220);
}

.custom-scroll-container::-webkit-scrollbar-track {
  background-color: rgb(60, 169, 208);
}

.photo-grid li {
  flex: 0 0 calc(20% - 16px); /* 20% width with a 16px gap */
  margin-bottom: 16px;
}

.photo-grid img {
  width: 100%;
  height: auto;
  display: block;
  border-radius: 8px; /* Add border radius for rounded corners */
}

.photo-preview-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.8);
  display: flex;
  justify-content: center;
  align-items: center;
}

.preview-image {
  max-width: 90%;
  max-height: 90%;
  border-radius: 8px;
}

.effect-button:hover {
  box-shadow: inset 0 -200px 0 #e7ebff;
  color: black;
}
</style>
