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

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
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
      component: () => import('@/components/common/SignUp.vue')
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
      path: '/resultpage',
      name: 'resultpage',
      component: ResultPage,
      meta: {
        requireAuth: true
      }
    }
  ],
  scrollBehavior() {
    return { top: 0 }
  },
})

router.beforeEach((to, from, next) => {
  if (to.meta.requireAuth) {
    if (!($cookies.isKey('token') || $cookies.isKey('refresh-token'))) {
      Swal.fire({
        title : '로그인 해주세요',
        text : '일부 서비스는 로그인이 필요할 수 있습니다.',
        icon: 'warning',
      })
      next('/')
      return
    }
  }

  next()

  // if (from.path === '/meetings') {
  //   if (confirm('회의 페이지를 벗어나면 변경사항이 저장되지 않을 수 있습니다.')) {
  //     next();
  //   }
  //   else {
  //     next(false);
  //   }
  // }
})

export default router
