<template>
 <!--기억-->
  <div class=" mx-8 my-4  bg-white">
    <div class="flex h-1/3 bg-[#e7cffe] rounded-lg w-[288px]">
      <div class="w-2/5">
        <img
          :src="`src/assets/memory/memory_random_${imgNo}.png`"
          alt="memory-random"
          class="w-24 h-24 ml-3 mt-3"

        />
      </div>
      <div class="w-3/5 ml-6 mt-10">
        <p class="text-[13px] mb-1 flex" v-if="isRollingDone"><strong>롤링페이퍼:</strong> 편집 완료<IconCheckMark /></p>
        <p class="text-[13px] mb-1 flex" v-else><strong>롤링페이퍼:</strong> 편집 대기중<IconSandTimer /></p>
        <p class="text-[13px] mb-1 flex" v-if="isPhotoDone">
          <strong>포토 모자이크:</strong> 생성 완료<IconCheckMark />
        </p>
        <p class="text-[13px] mb-1 flex" v-else><strong>포토 모자이크:</strong> 생성 대기중<IconSandTimer /></p>
      
      </div>
    </div>
    <div class="h-2/3 flex justify-center">
      <div class="h-[100%] w-[100%] pl-1 pt-5 rounded-lg bg-[#ffffff] border-2 border-solid border-[#e7cffe] text-center">
        <p class="flex  justify-center items-center">
          모꼬지 날짜 <Icon-calen /> : {{ memory.date }} ({{ lastDayToEdit.dayOfWeek }})
        </p>
        <!-- <p class="flex  justify-center items-center"> 모꼬지 진행 시간 <IconClock />: {{ memory.eventRunTime }}</p> -->
        <p class="flex justify-center items-center"> 참여 인원 수 <IconPeople />: {{ memory.participantCount }}</p>
        <p>편집 가능 기간 : <a class="effect-yellow"> {{ lastDayToEdit.expireDay }}</a></p>
        <p class="text-center mt-8">
          편집 가능 기간이 <strong> {{ lastDayToEdit.editablePeriod }}일 </strong> 남았어요
        </p>
        <div class="flex justify-center items-center pb-3">
        <button class= "bg-[#f5eefc] mr-5 rounded-2xl h-8 w-20 mt-2  hover:bg-[#e7cffe]" @click='showWarningModal'>
          추억쌓기
          </button>
         <router-link to="/editpage"><button class="text-md  bg-[#fff8c6]  mr-3 rounded-2xl h-8 w-20 mt-2 hover:bg-[#fff396]" @click="bindResultID">
          편집
        </button></router-link>
        </div>
      </div>
    </div>
  </div>
  <!--추억 생성 경고 모달 1 시작-->
  <transition
    name="modal-fade"
    class="fixed top-0 left-0 h-full w-full border-solid custom-modal-bg z-20 flex items-center justify-center"
  >
    <div v-if='isOpenWarningModal' class="">
      <div class="bg-violet-50 fixed top-[100px] w-[800px] h-[500px] rounded-lg custom-background">
        <div
          class="absolute h-[22rem] w-72 top-[80px] left-[420px]  bg-[#e7cffe] rounded-2xl"
        >
          <div class="flex h-1/3">
           <div class="w-2/5">
            <img
          :src="`src/assets/memory/memory_random_${imgNo}.png`"
          alt="memory-random"
          class="w-24 h-24 ml-3 mt-3"

            />
             </div>
            <div class="w-3/5 ml-6 mt-10">
        <p class="text-[13px] mb-1 flex" v-if="isRollingDone"><strong>롤링페이퍼:</strong> 편집 완료<IconCheckMark /></p>
        <p class="text-[13px] mb-1 flex" v-else><strong>롤링페이퍼:</strong> 편집 대기중<IconSandTimer /></p>
        <p class="text-[13px] mb-1 flex" v-if="isPhotoDone">
          <strong>포토 모자이크:</strong> 생성 완료<IconCheckMark />
        </p>
        <p class="text-[13px] mb-1 flex" v-else><strong>포토 모자이크:</strong> 생성 대기중<IconSandTimer /></p>
      
      </div>
          </div>
          <div class="h-2/3 flex justify-center">
      <div class="h-[100%] w-[100%] pl-1 pt-5 rounded-lg bg-[#ffffff] border-2 border-solid border-[#e7cffe] text-center">
        <p class="flex  justify-center items-center">
          모꼬지 날짜 <Icon-calen /> : {{ memory.date }} ({{ lastDayToEdit.dayOfWeek }})
        </p>
        <!-- <p class="flex justify-center items-center"> 모꼬지 진행 시간 <IconClock />: {{ memory.eventRunTime }}</p> -->
        <p class="flex justify-center items-center"> 참여 인원 수 <IconPeople />: {{ memory.participantCount }}</p>
        <p>편집 가능 기간 : <a class="effect-yellow"> {{ lastDayToEdit.expireDay }}</a></p>
        <p class="text-center mt-8">
          편집 가능 기간이 <strong> {{ lastDayToEdit.editablePeriod }}일 </strong> 남았어요
        </p>
        <div v-if="isOpenInputModal" class="flex justify-center items-center">
          <button class= "bg-[#f5eefc] mr-5 rounded-2xl h-8 w-20 mt-2  hover:bg-[#e7cffe]">
          추억쌓기
          </button>
          <button class="text-md  bg-[#fff8c6]  mr-3 rounded-2xl h-8 w-20 mt-2 hover:bg-[#fff396]">
          편집
          </button>
        </div>
      </div>
    </div>
        </div>
        <div>
          <p class="absolute flex text-3xl top-[150px] right-[545px]"><IconWarning /> 주의</p>
          <p class="absolute text-2xl top-[200px] right-[530px]">추억이 되면</p>
          <p class="absolute text-2xl top-[240px] right-[470px]">결과물 편집이 어려워요.</p>
          <p class="absolute text-2xl top-[280px] right-[480px]">충분히 확인하셨나요?</p>
        </div>
        <div>
          <button
            @click="showInputModal"
            class="text-white rounded-2xl mr-5 bg-green-300 hover:bg-green-400 h-8 w-14 absolute top-[330px] left-[150px]"
          >
            네
          </button>
          <button
            @click="showWarningModal"
            class="text-white h-8 w-14 rounded-2xl mr-5 bg-red-300 hover:bg-red-400 absolute top-[330px] left-[230px]"
          >
            아니요
          </button>
        </div>
      </div>
    </div>
  </transition>
<!--추억 생성 Input 모달 시작-->
  <transition

    class="fixed top-0 left-0 h-full w-full border-solid z-20 flex custom-modal-bg items-center justify-center"
  >
    <div v-if="isOpenInputModal" class="">
      <div class="bg-violet-50 fixed top-[100px] w-[600px] h-[500px] rounded-lg custom-background">
        <p class="absolute top-[20px] text-3xl left-[130px] font-bold effect-yellow">행사 정보를 추가로 입력하세요</p>
        <button class="absolute top-[10px] text-3xl right-1" @click="showInputModal"><IconClosePurple/></button>
        <p class="absolute top-[60px] text-xl left-[150px] text-gray-500">
          입력하신 내용은 추억 카드에 기록됩니다.
        </p>
        <p class="absolute top-[120px] left-[30px]"><IconSingleFlower/></p>
        <p class="absolute top-[115px] text-2xl left-[50px] effect-purple">행사명</p>
        <input v-model="eventDataAdd.name" type="text" @input="inputHandlerName" :disabled="isDisabledName" maxlength = "10" placeholder="ex. 결혼식" class="text-center bg-white placeholder:text-2xl absolute top-[100px] text-2xl left-[150px] rounded-lg w-[350px] border-violet-300  border-2 border-solid placeholder:text-center focus:border-black" ></input>
        <p class="absolute top-[195px] left-[30px]"><IconSingleFlower/></p>
        <p class="absolute top-[190px] text-2xl left-[50px] effect-purple">행사설명</p>
        <textarea v-model="eventDataAdd.memo" @input="inputHandlerContent" :disabled="isDisabledContent" placeholder="ex. 검은 머리 파뿌리 될때까지... &#13;ex. Will you marry me? 가장 많이 웃은 날" maxlength = "35" class="resize-none bg-white absolute top-[190px] text-2xl left-[160px] rounded-lg w-[350px] h-[200px] focus:border-black border-violet-300  border-2 border-solid placeholder: text-center"></textarea>
        <button @click="closeAndRecollection" class="absolute top-[415px] text-lg left-[160px] border-violet-300 border-2 border-solid rounded-lg px-4 py-2 w-[350px]  hover:bg-[#e7cffe] ">입력 완료 및 추억 쌓기</button>
      </div>
    </div>
  </transition>
</template>

<script setup>
import { computed, ref, onMounted } from 'vue'
import IconCalen from '@/icons/result/IconCalen.vue'
import IconClock from '@/icons/result/IconClock.vue'
import IconPeople from '@/icons/result/IconPeople.vue'
import IconSandTimer from '@/icons/result/IconSandTimer.vue'
import IconCheckMark from '@/icons/result/IconCheckMark.vue'
import IconWarning from '@/icons/result/IconWarning.vue'
import IconSingleFlower from '@/icons/result/IconSingleFlower.vue'
import IconClosePurple from '@/icons/result/IconClosePurple.vue'
import {useToRecollection, useResultIDStore } from '@/stores/result.js'

const props = defineProps(['memory'])
const toRecollectionStore = useToRecollection()
const resultIDStore = useResultIDStore()
const eventDataAdd = ref({
  name: '',
  memo: ''
})

const { VITE_MEMORY_IMG_COUNT } = import.meta.env
const isRollingDone = ref(false)
const isPhotoDone = ref(false)
const isOpenWarningModal = ref(false)
const isOpenInputModal = ref(false)


const isDisabledName= ref(false)
const isDisabledContent=ref(false)


const toRecollection = (id) => {
  console.log(`${id}를 기억에서 추억으로....`)
  console.log(eventDataAdd.value)
  toRecollectionStore.toRecollection(id, eventDataAdd.value, (res) => {
    console.log(res)
  }, (error) => {
    console.log(error)
  })
}


//입력 글자수 제한
const inputHandlerName = (e) => {
    const target = e.target.value;
    const max = e.currentTarget.getAttribute('maxlength');
    if (target.length > max) {
      this.isDisabledName = true
    }
    else {
      this.isDisabledName = false
    }
    

}


//입력 글자수 제한
const inputHandlerContent = (e) => {
    const target = e.target.value;
    const max = e.currentTarget.getAttribute('maxlength');
    if (target.length > max) {
      this.isDisabledContent = true
    }
    else {
      this.isDisabledContent = false
    }
    

}

//기억 카드 무작위 행사 이미지 배치
const imgNo = computed(() => {
  let no = props.memory.resultId % VITE_MEMORY_IMG_COUNT
  return no === 0 ? VITE_MEMORY_IMG_COUNT : no
})

const lastDayToEdit = computed(() => {
  let eventDate = new Date(props.memory.date) //편집 마감 기한 계산하기
  let expireMonth = eventDate.getMonth() + 2 //0 - 11월
  let expireDay = eventDate.getDate()
  let expireYear = eventDate.getYear() + 1900
  if (expireMonth === 13) {
    //13월 -> 다음년도 1월
    expireMonth = 1
    expireYear = expireYear + 1
  }
  let currentDate = new Date()
  let expireDate = new Date(expireYear, expireMonth - 1, expireDay)

  let editablePeriod = Math.abs(expireDate.getTime() - currentDate.getTime())
  editablePeriod = Math.ceil(editablePeriod / (1000 * 60 * 60 * 24))
  return {
    editablePeriod: editablePeriod,
    expireDay: '~' + expireMonth + '월 ' + expireDay +'일',
    dayOfWeek: dayToKorean(eventDate.getDay())
  }
})

const dayToKorean = (value) => {
  let day = ''
  switch (value) {
    case 0:
      day = '일'
      break
    case 1:
      day = '월'
      break
    case 2:
      day = '화'
      break
    case 3:
      day = '수'
      break
    case 4:
      day = '목'
      break
    case 5:
      day = '금'
      break
    case 6:
      day = '토'
      break
  }
  return day
}


const showWarningModal = () => {
  isOpenWarningModal.value = !isOpenWarningModal.value
}

const showInputModal = () => {
  isOpenWarningModal.value = false
  isOpenInputModal.value = !isOpenInputModal.value

}
const closeAndRecollection = () => {
  showInputModal()
  toRecollection(props.memory.resultId)
  location.reload()
}

const bindResultID = () => {
  resultIDStore.assignID(props.memory.resultId)
  console.log(`바인딩된 resultID - ${resultIDStore.getID}`)
}

onMounted(() => {
  isRollingDone.value = props.memory.paperEdited
  isPhotoDone.value = props.memory.mosaicCreated

})
</script>

<style scoped>
.custom-modal {
  transform: translate(-50%, -50%);
}

.custom-modal-bg {
  background-color: rgba(0, 0, 0, 0.5);
}

.custom-background {
  overflow: auto;
}


.effect-yellow {
  box-shadow: inset 0 -5px 0 #f9ef99;
  color: black;
}

.effect-purple {
  box-shadow: inset 0 -10px 0 #e1cdfb;
  color: black;
}
</style>
