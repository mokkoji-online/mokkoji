<template>
  <div class="w-80 items-center m-5 flex flex-col gap-2 mb-16">
    <img
      src="@/assets/logo/mokkoji_logo.png"
      class="w-24 p-2 m-2 mb-4 border-2 rounded-2xl border-primary bg-natural-beige shadow-md"
    />
    <div id="title-sub-bold" class="ml-auto mr-auto mb-5 tracking-wider pb-10">모꼬지 시작하기</div>
    <div id="box-shadow">
      <div class="flex flex-col justify-center">
        <a
          type="button"
          class="text-white bg-[#FEE500] hover:bg-[#FEE500]/50 focus:ring-4 focus:outline-none focus:ring-[#FEE500]/40 font-bold rounded-lg text-sm px-5 py-2.5 text-center inline-flex items-center me-2 mb-4 w-[20lvw]"
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
              transform="translate(0.000000,225.000000) scale(0.100000,-0.100000)"
              fill="#000000"
              stroke="none"
            >
              <path
                d="M943 1845 c-207 -38 -433 -163 -535 -297 -72 -95 -128 -251 -128
-358 0 -194 142 -418 332 -523 32 -18 58 -36 58 -42 0 -5 -18 -75 -40 -155
-54 -202 -57 -202 160 -55 l172 116 177 3 c209 4 267 18 436 101 209 104 334
251 381 450 34 148 -13 335 -119 467 -81 102 -253 214 -402 262 -142 45 -343
58 -492 31z m40 -477 c14 -13 37 -57 52 -98 14 -41 38 -106 52 -144 14 -38 26
-85 27 -105 1 -33 -1 -36 -28 -36 -24 0 -31 6 -41 35 l-12 35 -84 3 -84 2 -17
-41 c-15 -35 -21 -40 -45 -37 -19 2 -29 9 -31 23 -2 16 63 199 123 348 7 16
35 34 57 36 4 1 18 -9 31 -21z m248 12 c18 -10 19 -23 19 -170 l0 -160 69 0
c61 0 70 -2 76 -20 13 -40 -10 -50 -109 -50 -58 0 -96 4 -104 12 -8 8 -12 66
-12 195 0 163 2 183 18 192 21 13 20 13 43 1z m267 -2 c7 -7 12 -42 12 -87 l0
-76 83 83 c82 82 102 93 125 70 19 -19 15 -26 -54 -98 l-66 -68 66 -84 c74
-93 83 -130 34 -136 -24 -3 -35 7 -90 82 -58 80 -63 85 -79 70 -13 -12 -19
-35 -21 -83 -3 -66 -3 -66 -33 -66 -39 0 -43 16 -44 209 -1 135 2 162 16 178
19 21 34 23 51 6z m-681 -14 c12 -31 -11 -49 -66 -52 l-51 -3 0 -152 c0 -84
-4 -158 -9 -165 -5 -8 -21 -12 -37 -10 l-29 3 -3 162 -2 162 -52 3 c-54 3 -77
21 -65 52 6 14 27 16 157 16 130 0 151 -2 157 -16z"
              />
              <path
                d="M920 1233 c-12 -43 -25 -84 -27 -90 -4 -10 11 -13 57 -13 55 0 61 2
55 18 -3 9 -16 46 -27 82 -11 36 -24 69 -28 73 -4 4 -17 -27 -30 -70z"
              />
            </g>
          </svg>
          카카오 계정으로 시작하기
        </a>
        <a
          type="button"
          class="text-white bg-[#4285F4] hover:bg-[#4285F4]/90 focus:ring-4 focus:outline-none focus:ring-[#4285F4]/50 font-medium rounded-lg text-sm pl-5 py-2.5 text-center inline-flex items-center m-0 w-[25lvw]"
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
          구글 계정으로 시작하기
        </a>
        <a
          type="button"
          class="text-white bg-green-400 hover:bg-green-500 me-2 mb-2 mt-4 rounded-lg font-medium text-sm p-2 w-[25lvw]"
          :href="naverApiRequestURI"
        >
          <img
            src="@/assets/logo/logoN_naver.svg"
            class="w-4 h-43 inline-block mb-2 ml-2 mt-1 mr-2"
          />
          네이버 계정으로 시작하기
        </a>
      </div>
    </div>
  </div>
</template>
<script setup>
const {
  VITE_NAVER_CLIENT_ID,
  VITE_GOOGLE_CLIENT_ID,
  VITE_KAKAO_CLIENT_ID,
  VITE_NAVER_REDIRECT_URI,
  VITE_GOOGLE_REDIRECT_URI,
  VITE_KAKAO_REDIRECT_URI
} = import.meta.env
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
<style></style>
