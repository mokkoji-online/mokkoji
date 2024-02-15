<template>
  <div class="w-80 items-center m-5 flex flex-col gap-4 mb-16 p-5">
    <img
      src="@/assets/logo/mokkoji_logo.png"
      class="w-24 p-2 m-2 mb-4 border-2 rounded-2xl border-primary bg-natural-beige shadow-md rounded-r-lg"
    />
    <div id="title-sub-bold">모꼬지 시작하기</div>
    <div id="box-shadow">
      <div class="flex flex-col justify-center">
        <a
          type="button"
          class="text-white bg-[#fde047] hover:bg-[#fed400cb] focus:ring-4 focus:outline-none focus:ring-[#FEE500]/40 font-bold rounded-lg text-sm px-5 py-2.5 text-center inline-flex items-center mb-4 w-[14lvw]"
          :href="kakaoApiRequestURI"
        >
          <svg
            version="1.0"
            xmlns="http://www.w3.org/2000/svg"
            width="18.000000pt"
            height="18.000000pt"
            viewBox="0 0 225.000000 225.000000"
            preserveAspectRatio="xMidYMid meet"
            class="m-0 mr-[0.5lvw]"
          >
            <g
              transform="translate(0.000000,183.000000) scale(0.100000,-0.100000)"
              fill="#000000"
              stroke="none"
            >
              <path
                d="M815 1799 c-33 -5 -100 -20 -149 -34 -438 -125 -702 -476 -635 -841
32 -175 164 -360 340 -475 l78 -51 -23 -64 c-27 -74 -72 -146 -143 -231 -81
-96 -72 -102 77 -50 123 43 263 108 370 172 l85 50 140 -3 c215 -5 380 27 552
108 210 98 379 278 443 470 30 89 37 224 16 319 -46 216 -223 421 -459 531
-99 46 -227 85 -331 99 -89 13 -274 12 -361 0z"
              />
            </g>
          </svg>
          카카오로 시작하기
        </a>
        <a
          type="button"
          class="text-white bg-[#4285F4] hover:bg-[#4285F4]/90 focus:ring-4 focus:outline-none focus:ring-[#4285F4]/50 font-medium rounded-lg text-sm pl-5 py-2.5 text-center inline-flex items-center m-0 w-[14lvw]"
          :href="googleApiRequestURI"
        >
          <svg
            class="w-4 h-4 me-2"
            aria-hidden="true"
            xmlns="http://www.w3.org/2000/svg"
            fill="currentColor"
            viewBox="0 0 18 19"
          >
            <path
              fill-rule="evenodd"
              d="M8.842 18.083a8.8 8.8 0 0 1-8.65-8.948 8.841 8.841 0 0 1 8.8-8.652h.153a8.464 8.464 0 0 1 5.7 2.257l-2.193 2.038A5.27 5.27 0 0 0 9.09 3.4a5.882 5.882 0 0 0-.2 11.76h.124a5.091 5.091 0 0 0 5.248-4.057L14.3 11H9V8h8.34c.066.543.095 1.09.088 1.636-.086 5.053-3.463 8.449-8.4 8.449l-.186-.002Z"
              clip-rule="evenodd"
            />
          </svg>
          구글로 시작하기
        </a>
        <a
          type="button"
          class="text-white bg-green-400 hover:bg-green-500 font-medium rounded-lg text-sm pl-5 py-2.5 text-center inline-flex items-center m-0 w-[14lvw] my-4"
          :href="naverApiRequestURI"
        >
          <svg
            version="1.0"
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 300.000000 265.000000"
            preserveAspectRatio="xMidYMid meet"
            class="w-4 h-4 me-2"
          >
            <g
              transform="translate(0.000000,265.000000) scale(0.100000,-0.100000)"
              fill="white"
              stroke="none"
            >
              <path
                d="M10 1843 c0 -445 -3 -1040 -7 -1321 l-6 -512 521 0 522 0 0 538 -1
537 -33 158 c-18 86 -30 157 -27 157 3 0 226 -313 495 -695 l488 -695 407 0
c223 0 457 -3 519 -7 l112 -6 -2 1319 -3 1319 -519 3 -518 2 6 -357 c12 -711
12 -711 70 -1036 11 -63 11 -76 0 -65 -7 7 -232 337 -499 733 -268 396 -488
721 -489 721 0 1 -234 5 -518 9 l-518 7 0 -809z"
              />
            </g>
          </svg>
          네이버로 시작하기
        </a>
      </div>
    </div>
  </div>
</template>
<script setup>
// .env에서 client id 및 uri import
const {
  VITE_NAVER_CLIENT_ID,
  VITE_GOOGLE_CLIENT_ID,
  VITE_KAKAO_CLIENT_ID,
  VITE_NAVER_REDIRECT_URI,
  VITE_GOOGLE_REDIRECT_URI,
  VITE_KAKAO_REDIRECT_URI
} = import.meta.env
// 플랫폼 별 request uri href 이동
const naverApiRequestURI =
  `https://nid.naver.com/oauth2.0/authorize?client_id=` +
  VITE_NAVER_CLIENT_ID +
  '&redirect_uri=' +
  VITE_NAVER_REDIRECT_URI +
  '&state=mokkoji&response_type=code'

const googleApiRequestURI =
  `https://accounts.google.com/o/oauth2/v2/auth?client_id=` +
  VITE_GOOGLE_CLIENT_ID +
  '&redirect_uri=' +
  VITE_GOOGLE_REDIRECT_URI +
  '&state=mokkoji&response_type=code&scope=email profile'

const kakaoApiRequestURI =
  `https://kauth.kakao.com/oauth/authorize?client_id=` +
  VITE_KAKAO_CLIENT_ID +
  '&redirect_uri=' +
  VITE_KAKAO_REDIRECT_URI +
  '&state=mokkoji&response_type=code'
</script>

<style scoped></style>
