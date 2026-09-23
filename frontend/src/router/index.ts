import { createRouter, createWebHistory } from 'vue-router';
import CreaturesView from '@/views/CreaturesView.vue';
import CitiesView from '@/views/CitiesView.vue';
import RingsView from '@/views/RingsView.vue';
import SpecialOperationsView from '@/views/SpecialOperationsView.vue';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      redirect: '/creatures',
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
  ],
});

export default router;
