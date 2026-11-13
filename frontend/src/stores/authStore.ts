import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { authApi } from '@/api/authApi';
import type { LoginRequest, RegisterRequest, UserResponse } from '@/types/auth';
import { ApiError } from '@/api/httpClient';

export const useAuthStore = defineStore('auth', () => {
  const user = ref<UserResponse | null>(null);
  const loading = ref(false);
  const error = ref<string | null>(null);
  const initialized = ref(false);

  const isAuthenticated = computed(() => !!user.value);
  const username = computed(() => user.value?.username ?? '');
  const roles = computed(() => user.value?.roles ?? []);
  const isAdmin = computed(() => user.value?.roles.includes('ADMIN') ?? false);
  const isUser = computed(() => user.value?.roles.includes('USER') ?? false);

  async function checkAuth(): Promise<boolean> {
    try {
      user.value = await authApi.getCurrentUser();
      return true;
    } catch {
      user.value = null;
      return false;
    } finally {
      initialized.value = true;
    }
  }

  async function login(credentials: LoginRequest): Promise<void> {
    loading.value = true;
    error.value = null;
    try {
      await authApi.login(credentials);
      user.value = await authApi.getCurrentUser();
    } catch (err) {
      if (err instanceof ApiError) {
        error.value = err.message;
      } else {
        error.value = 'Ошибка входа в систему';
      }
      throw err;
    } finally {
      loading.value = false;
    }
  }

  async function register(credentials: RegisterRequest): Promise<void> {
    loading.value = true;
    error.value = null;
    try {
      await authApi.register(credentials);
      await authApi.login(credentials);
      user.value = await authApi.getCurrentUser();
    } catch (err) {
      if (err instanceof ApiError) {
        error.value = err.message;
      } else {
        error.value = 'Ошибка регистрации';
      }
      throw err;
    } finally {
      loading.value = false;
    }
  }

  async function logout(): Promise<void> {
    loading.value = true;
    try {
      await authApi.logout();
    } catch {
      void 0;
    } finally {
      user.value = null;
      loading.value = false;
    }
  }

  return {
    user,
    loading,
    error,
    initialized,
    isAuthenticated,
    username,
    roles,
    isAdmin,
    isUser,
    checkAuth,
    login,
    register,
    logout,
  };
});
