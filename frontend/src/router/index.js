import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'
import MyPageView from '@/views/MyPageView.vue'
import EventListPage from '@/views/EventListView.vue'
import MainMeetingView from '@/views/MainMeetingView.vue'
import GroupMeetingView from '@/views/GroupMeetingView.vue'
import EditPage from '@/views/EditView.vue'
import ResultPage from '@/views/ResultView.vue'
import MyPage from '@/components/mypage/MyPage.vue'
import MyDetail from '@/components/mypage/MyDetail.vue'
import Error404 from '@/components/common/Error404.vue'
import HandleCallback from '@/components/common/HandleCallback.vue'
import WaitingRoom from '@/components/meeting/WaitingRoom.vue'
import ReloadingRoom from '@/components/meeting/ReloadingRoom.vue'
import CloseRoom from '@/components/meeting/CloseRoom.vue'
import Swal from 'sweetalert2'
import SignUp from '@/components/common/SignUp.vue'

import tokenService from '@/services/token.service'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  // meta -> requireauth : 로그인 필요한 기능 식별
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/oauth2/:pathMatch(.*)*',
      component: HandleCallback
    },
    {
      path: '/signup',
      name: 'signup',
      component: SignUp
    },
    {
      path: '/mypage',
      name: 'mypage',
      component: MyPageView,
      meta: {
        requireAuth: true
      },
      children: [
        {
          path: '',
          name: 'mypage',
          component: MyPage
        },
        {
          path: 'detail',
          name: 'mydetail',
          component: MyDetail
        }
      ]
    },
    {
      path: '/meetings',
      name: 'mainmeeting',
      component: MainMeetingView,
      props: true
    },
    {
      path: '/groupmeetings',
      name: 'groupmeeting',
      component: GroupMeetingView,
      props: true
    },

    {
      path: '/eventlist',
      name: 'eventlist',
      component: EventListPage,
      meta: {
        requireAuth: true
      }
    },
    {
      path: '/editpage',
      name: 'editpage',
      component: EditPage,
      meta: {
        requireAuth: true
      }
    },
    // 존재하지 않는 경로 404 reroute
    {
      path: '/:pathMatch(.*)*',
      redirect: '404'
    },
    {
      path: '/404',
      name: 'Error404',
      component: Error404
    },
    {
      path: '/meetings'
    },

    {
      path: '/meetings/:sessionId',
      name: 'waitingroom',
      component: WaitingRoom,
      props: true
    },
    {
      path: '/reloadingroom',
      name: 'reloadingroom',
      component: ReloadingRoom
    },
    {
      path: '/closeroom',
      name: 'closeroom',
      component: CloseRoom
    },
    {
      path: '/resultpage/:resultId',
      name: 'resultpage',
      component: ResultPage,
      meta: {
        requireAuth: true
      }
    }
  ],
  // 모든 라우터 이동 후 스크롤 맨 위로
  scrollBehavior() {
    return { top: 0 }
  }
})

router.beforeEach((to, from, next) => {
  // 로그인 필요한 화면 이동 시 로그인 상태 확인
  let isAuth = false
  if (to.meta.requireAuth) {
    // 저장된 토큰이 없다면 로그인 x
    if (!$cookies.isKey('token')) {
      isAuth = true
    } else {
      // 쿠키에 올바르지 않은 토큰이 저장된 경우 try-catch 처리
      try {
        // 토큰이 parsing 되지만 expired 된 경우
        if (tokenService.expiredToken($cookies.get('token'))) {
          isAuth = true
        }
      } catch (err) {
        // 올바르지 않은 토큰이 저장된 경우
        console.log(err)
        isAuth = true
      }
    }
  }
  if (isAuth) {
    // 저장된 쿠키 삭제
    $cookies.keys().forEach((cookie) => $cookies.remove(cookie))
    // 로그인 alert 후 홈 화면 이동
    Swal.fire({
      title: '로그인 해주세요',
      text: '일부 서비스는 로그인이 필요할 수 있습니다.',
      icon: 'warning'
    })
    next('/')
    return
  }
  // 아닌 경우 정상 처리
  next()
})

export default router
