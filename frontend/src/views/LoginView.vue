<script setup lang="ts">
import { ref, computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useAuthStore } from '@/stores/authStore';
import { useNotificationStore } from '@/stores/notificationStore';
import BaseInput from '@/components/ui/BaseInput.vue';
import BaseButton from '@/components/ui/BaseButton.vue';
import { LogIn, UserPlus, AlertCircle } from 'lucide-vue-next';

const router = useRouter();
const route = useRoute();
const authStore = useAuthStore();
const notificationStore = useNotificationStore();

const isRegisterMode = ref(false);
const username = ref('');
const password = ref('');
const fieldErrors = ref<{ username?: string; password?: string }>({});
const generalError = ref<string | null>(null);

const submitText = computed(() => (isRegisterMode.value ? 'Зарегистрироваться' : 'Войти'));

function validate(): boolean {
  fieldErrors.value = {};
  generalError.value = null;

  if (!username.value.trim()) {
    fieldErrors.value.username = 'Введите имя пользователя';
  } else if (isRegisterMode.value && (username.value.length < 3 || username.value.length > 50)) {
    fieldErrors.value.username = 'Длина имени должна быть от 3 до 50 символов';
  }

  if (!password.value) {
    fieldErrors.value.password = 'Введите пароль';
  } else if (isRegisterMode.value && password.value.length < 6) {
    fieldErrors.value.password = 'Пароль должен быть не менее 6 символов';
  }

  return Object.keys(fieldErrors.value).length === 0;
}

async function onSubmit() {
  if (!validate()) return;

  try {
    if (isRegisterMode.value) {
      await authStore.register({
        username: username.value.trim(),
        password: password.value,
      });
      notificationStore.addToast({
        type: 'success',
        title: 'Регистрация',
        message: 'Учетная запись успешно создана',
      });
    } else {
      await authStore.login({
        username: username.value.trim(),
        password: password.value,
      });
      notificationStore.addToast({
        type: 'success',
        title: 'Авторизация',
        message: 'Успешный вход в систему',
      });
    }

    const redirectPath =
      typeof route.query.redirect === 'string' ? route.query.redirect : '/creatures';
    await router.push(redirectPath);
  } catch (err: unknown) {
    generalError.value = authStore.error || 'Произошла ошибка при выполнении запроса';
    void err;
  }
}

function switchMode(register: boolean) {
  isRegisterMode.value = register;
  fieldErrors.value = {};
  generalError.value = null;
}
</script>

<template>
  <div class="login-wrapper">
    <div class="login-card">
      <div class="card-header">
        <div class="mode-toggle">
          <button
            type="button"
            class="toggle-btn"
            :class="{ active: !isRegisterMode }"
            @click="switchMode(false)"
          >
            <LogIn :size="15" />
            <span>Вход</span>
          </button>
          <button
            type="button"
            class="toggle-btn"
            :class="{ active: isRegisterMode }"
            @click="switchMode(true)"
          >
            <UserPlus :size="15" />
            <span>Регистрация</span>
          </button>
        </div>
      </div>

      <div v-if="generalError" class="error-banner">
        <AlertCircle :size="16" />
        <span>{{ generalError }}</span>
      </div>

      <form class="login-form" @submit.prevent="onSubmit">
        <BaseInput
          v-model="username"
          label="Имя пользователя"
          placeholder="Имя пользователя"
          :error="fieldErrors.username"
          required
        />

        <BaseInput
          v-model="password"
          type="password"
          label="Пароль"
          placeholder="Пароль"
          :error="fieldErrors.password"
          required
        />

        <div class="form-actions">
          <BaseButton
            type="submit"
            variant="primary"
            size="md"
            :loading="authStore.loading"
            class="submit-btn"
          >
            <component :is="isRegisterMode ? UserPlus : LogIn" :size="16" />
            <span>{{ submitText }}</span>
          </BaseButton>
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped>
.login-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 2.5rem);
  padding: 1.5rem;
}

.login-card {
  width: 100%;
  max-width: 400px;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  padding: 1.75rem;
  box-shadow: var(--shadow-lg);
}

.card-header {
  margin-bottom: 1.5rem;
}

.mode-toggle {
  display: flex;
  background: var(--bg-input);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-sm);
  padding: 0.2rem;
  gap: 0.25rem;
}

.toggle-btn {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.45rem;
  padding: 0.45rem 0.75rem;
  border: none;
  background: transparent;
  color: var(--text-muted);
  font-size: 13px;
  font-weight: 500;
  border-radius: var(--radius-xs);
  cursor: pointer;
  transition: all 0.12s ease;
}

.toggle-btn:hover {
  color: var(--text-main);
  background: var(--bg-card-hover);
}

.toggle-btn.active {
  background: var(--primary);
  color: #ffffff;
}

.error-banner {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.65rem 0.85rem;
  margin-bottom: 1rem;
  border-radius: var(--radius-sm);
  background: var(--danger-bg);
  border: 1px solid var(--danger);
  color: var(--danger);
  font-size: 12.5px;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 1.15rem;
}

.form-actions {
  margin-top: 0.5rem;
}

.submit-btn {
  width: 100%;
  justify-content: center;
  gap: 0.5rem;
}
</style>
