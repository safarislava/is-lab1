<script setup lang="ts">
import { onMounted, onUnmounted, watch, computed } from 'vue';
import { RouterLink, RouterView, useRouter, useRoute } from 'vue-router';
import { useAuthStore } from '@/stores/authStore';
import { sseClient } from '@/api/sseClient';
import ToastContainer from '@/components/ui/ToastContainer.vue';
import BaseBadge from '@/components/ui/BaseBadge.vue';
import { Sparkles, Building2, CircleDot, Zap, Upload, LogIn, LogOut, User } from 'lucide-vue-next';

const router = useRouter();
const route = useRoute();
const authStore = useAuthStore();

const isLoginPage = computed(() => route.name === 'login');

onMounted(async () => {
  if (!authStore.initialized) {
    await authStore.checkAuth();
  }
  if (authStore.isAuthenticated) {
    sseClient.connect();
  }
});

onUnmounted(() => {
  sseClient.disconnect();
});

watch(
  () => authStore.isAuthenticated,
  (isAuth) => {
    if (isAuth) {
      sseClient.connect();
    } else {
      sseClient.disconnect();
    }
  },
);

async function handleLogout() {
  await authStore.logout();
  await router.push('/login');
}
</script>

<template>
  <div class="app-layout">
    <header v-if="!isLoginPage" class="navbar">
      <div class="nav-container">
        <nav v-if="authStore.isAuthenticated" class="nav-menu">
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
          <RouterLink to="/import" class="nav-item">
            <Upload :size="16" />
            <span>Импорт</span>
          </RouterLink>
        </nav>

        <div class="nav-auth">
          <template v-if="authStore.isAuthenticated">
            <div class="user-info">
              <User :size="14" class="user-icon" />
              <span class="user-name">{{ authStore.username }}</span>
              <div class="role-badges">
                <BaseBadge
                  v-for="role in authStore.roles"
                  :key="role"
                  :variant="role === 'ADMIN' ? 'warning' : 'info'"
                  size="sm"
                >
                  {{ role }}
                </BaseBadge>
              </div>
            </div>
            <button type="button" class="nav-item btn-logout" @click="handleLogout">
              <LogOut :size="14" />
              <span>Выход</span>
            </button>
          </template>
          <template v-else>
            <RouterLink to="/login" class="nav-item">
              <LogIn :size="14" />
              <span>Вход</span>
            </RouterLink>
          </template>
        </div>
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

    <ToastContainer />
  </div>
</template>

<style scoped>
.app-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: var(--bg-base);
  width: 100%;
  overflow-x: hidden;
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
  width: 100%;
  min-width: 0;
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

.nav-auth {
  display: flex;
  align-items: center;
  gap: 0.6rem;
  margin-left: auto;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 0.45rem;
  padding: 0.25rem 0.55rem;
  background: var(--bg-input);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-sm);
  font-size: 12px;
  color: var(--text-main);
}

.user-icon {
  color: var(--text-muted);
}

.user-name {
  font-weight: 500;
}

.role-badges {
  display: flex;
  gap: 0.25rem;
}

.btn-logout {
  background: transparent;
  border: 1px solid var(--border-color);
  cursor: pointer;
}

.btn-logout:hover {
  background: var(--danger-bg);
  border-color: var(--danger);
  color: var(--danger);
}
</style>
