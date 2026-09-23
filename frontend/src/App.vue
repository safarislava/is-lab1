<script setup lang="ts">
import { onMounted, onUnmounted } from 'vue';
import { RouterLink, RouterView } from 'vue-router';
import { useNotificationStore } from '@/stores/notificationStore';
import { useCreaturesStore } from '@/stores/creaturesStore';
import { useCitiesStore } from '@/stores/citiesStore';
import { useRingsStore } from '@/stores/ringsStore';
import { sseClient } from '@/api/sseClient';
import ToastContainer from '@/components/ui/ToastContainer.vue';
import { Sparkles, Building2, CircleDot, Zap } from 'lucide-vue-next';

const notificationStore = useNotificationStore();
const creaturesStore = useCreaturesStore();
const citiesStore = useCitiesStore();
const ringsStore = useRingsStore();

let unsubscribeEvent: (() => void) | null = null;
let unsubscribeStatus: (() => void) | null = null;

onMounted(() => {
  unsubscribeStatus = sseClient.onStatusChange((status) => {
    notificationStore.setStatus(status);
  });

  unsubscribeEvent = sseClient.onEvent((event) => {
    creaturesStore.handleSseEvent(event);
    citiesStore.handleSseEvent(event);
    ringsStore.handleSseEvent(event);
  });

  sseClient.connect();
});

onUnmounted(() => {
  if (unsubscribeEvent) unsubscribeEvent();
  if (unsubscribeStatus) unsubscribeStatus();
  sseClient.disconnect();
});
</script>

<template>
  <div class="app-layout">
    <header class="navbar">
      <div class="nav-container">
        <!-- Logo -->
        <RouterLink to="/" class="nav-brand">
          <div class="brand-icon">🧙‍♂️</div>
          <div class="brand-text">
            <span class="brand-title">BookCreatures</span>
          </div>
        </RouterLink>

        <!-- Navigation Menu -->
        <nav class="nav-menu">
          <RouterLink to="/creatures" class="nav-item">
            <Sparkles :size="16" />
            <span>Существа</span>
          </RouterLink>
          <RouterLink to="/cities" class="nav-item">
            <Building2 :size="16" />
            <span>Города</span>
          </RouterLink>
          <RouterLink to="/rings" class="nav-item">
            <CircleDot :size="16" />
            <span>Кольца</span>
          </RouterLink>
          <RouterLink to="/special" class="nav-item">
            <Zap :size="16" />
            <span>Спец. операции</span>
          </RouterLink>
        </nav>
      </div>
    </header>

    <main class="main-content">
      <div class="container">
        <RouterView v-slot="{ Component }">
          <Transition name="page-fade" mode="out-in">
            <component :is="Component" />
          </Transition>
        </RouterView>
      </div>
    </main>

    <!-- Global Toasts -->
    <ToastContainer />
  </div>
</template>

<style scoped>
.app-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: var(--bg-base);
}

.navbar {
  position: sticky;
  top: 0;
  z-index: 500;
  background: var(--bg-header);
  border-bottom: 1px solid var(--border-color);
}

.nav-container {
  max-width: 1440px;
  margin: 0 auto;
  padding: 0.5rem 1.5rem;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1.5rem;
}

.nav-brand {
  display: flex;
  align-items: center;
  gap: 0.6rem;
  text-decoration: none;
  color: inherit;
}

.brand-icon {
  font-size: 1.35rem;
  line-height: 1;
}

.brand-text {
  display: flex;
  flex-direction: column;
}

.brand-title {
  font-weight: 700;
  font-size: 1.05rem;
  letter-spacing: -0.01em;
  color: var(--text-main);
}

.nav-menu {
  display: flex;
  align-items: center;
  gap: 0.25rem;
  background: var(--bg-input);
  padding: 0.2rem;
  border-radius: var(--radius-sm);
  border: 1px solid var(--border-color);
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 0.4rem;
  padding: 0.35rem 0.75rem;
  border-radius: var(--radius-xs);
  color: var(--text-muted);
  font-size: 12.5px;
  font-weight: 500;
  transition: all 0.12s ease;
  text-decoration: none;
}

.nav-item:hover {
  color: var(--text-main);
  background: var(--bg-card-hover);
}

.nav-item.router-link-active {
  background: var(--primary);
  color: #ffffff;
  font-weight: 500;
}

.main-content {
  flex: 1;
  padding-bottom: 2.5rem;
}

.page-fade-enter-active,
.page-fade-leave-active {
  transition:
    opacity 0.12s ease,
    transform 0.12s ease;
}

.page-fade-enter-from {
  opacity: 0;
  transform: translateY(4px);
}

.page-fade-leave-to {
  opacity: 0;
  transform: translateY(-4px);
}

@media (max-width: 768px) {
  .nav-container {
    flex-direction: column;
    align-items: stretch;
  }
  .nav-menu {
    overflow-x: auto;
  }
}
</style>
