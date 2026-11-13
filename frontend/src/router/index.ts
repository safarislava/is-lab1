import { createRouter, createWebHashHistory } from 'vue-router';
import CreaturesView from '@/views/CreaturesView.vue';
import CitiesView from '@/views/CitiesView.vue';
import RingsView from '@/views/RingsView.vue';
import SpecialOperationsView from '@/views/SpecialOperationsView.vue';
import ImportView from '@/views/ImportView.vue';
import LoginView from '@/views/LoginView.vue';
import { useAuthStore } from '@/stores/authStore';

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    {
      path: '/',
      redirect: '/creatures',
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
    },
    {
      path: '/creatures',
      name: 'creatures',
      component: CreaturesView,
    },
    {
      path: '/cities',
      name: 'cities',
      component: CitiesView,
    },
    {
      path: '/rings',
      name: 'rings',
      component: RingsView,
    },
    {
      path: '/special',
      name: 'special',
      component: SpecialOperationsView,
    },
    {
      path: '/import',
      name: 'import',
      component: ImportView,
    },
  ],
});

router.beforeEach(async (to) => {
  const authStore = useAuthStore();
  if (!authStore.initialized) {
    await authStore.checkAuth();
  }

  if (to.name !== 'login' && !authStore.isAuthenticated) {
    return { name: 'login', query: { redirect: to.fullPath } };
  }

  if (to.name === 'login' && authStore.isAuthenticated) {
    return { path: '/creatures' };
  }
});

export default router;
