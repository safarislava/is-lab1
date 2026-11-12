import { createApp } from 'vue';
import { createPinia } from 'pinia';
import { VueQueryPlugin } from '@tanstack/vue-query';
import { queryClient } from '@/api';
import App from './App.vue';
import router from './router';
import './assets/styles/main.css';

const app = createApp(App);
const pinia = createPinia();

app.use(pinia);
app.use(VueQueryPlugin, { queryClient });
app.use(router);

app.mount('#app');
