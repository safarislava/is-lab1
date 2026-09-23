<script setup lang="ts">
import { ref, computed, watch } from 'vue';
import type { RingResponse, RingCreateRequest, RingUpdateRequest } from '@/types/ring';
import { useRingsStore } from '@/stores/ringsStore';
import { ApiError } from '@/api/httpClient';
import BaseModal from '@/components/ui/BaseModal.vue';
import BaseButton from '@/components/ui/BaseButton.vue';
import BaseInput from '@/components/ui/BaseInput.vue';

const props = defineProps<{
  isOpen: boolean;
  ring?: RingResponse | null;
}>();

const emit = defineEmits<{
  (e: 'close'): void;
  (e: 'saved', ring: RingResponse): void;
  (e: 'delete', ring: RingResponse): void;
}>();

const ringsStore = useRingsStore();

const isEdit = computed(() => !!props.ring);
const modalTitle = computed(() =>
  isEdit.value ? `Редактирование кольца #${props.ring?.id}` : 'Создание магического кольца',
);

const name = ref('');
const power = ref<number | null>(100);

const errors = ref<Record<string, string>>({});
const serverError = ref<string | null>(null);
const loading = ref(false);

watch(
  () => props.isOpen,
  (open) => {
    if (open) {
      errors.value = {};
      serverError.value = null;

      if (props.ring) {
        name.value = props.ring.name;
        power.value = props.ring.power;
      } else {
        name.value = '';
        power.value = 50;
      }
    }
  },
);

function validate(): boolean {
  const errs: Record<string, string> = {};

  if (!name.value || !name.value.trim()) {
    errs.name = 'Название кольца обязательно';
  }

  if (power.value === null || isNaN(power.value) || power.value <= 0) {
    errs.power = 'Сила кольца должна быть больше 0';
  }

  errors.value = errs;
  return Object.keys(errs).length === 0;
}

async function onSubmit() {
  serverError.value = null;
  if (!validate()) return;

  loading.value = true;
  const payload: RingCreateRequest | RingUpdateRequest = {
    name: name.value.trim(),
    power: Math.round(power.value!),
  };

  try {
    let res: RingResponse;
    if (isEdit.value && props.ring) {
      res = await ringsStore.updateRing(props.ring.id, payload);
    } else {
      res = await ringsStore.createRing(payload);
    }
    emit('saved', res);
    emit('close');
  } catch (err) {
    if (err instanceof ApiError) {
      if (err.details && err.details.length > 0) {
        serverError.value = err.details.join('; ');
      } else {
        serverError.value = err.message;
      }
    } else {
      serverError.value = err instanceof Error ? err.message : 'Ошибка при сохранении кольца';
    }
  } finally {
    loading.value = false;
  }
}
</script>

<template>
  <BaseModal :is-open="isOpen" :title="modalTitle" max-width="480px" @close="emit('close')">
    <form id="ringForm" novalidate @submit.prevent="onSubmit">
      <div v-if="serverError" class="server-error-banner">⚠️ {{ serverError }}</div>

      <BaseInput
        v-model="name"
        label="Название кольца"
        placeholder="Например: Единое Кольцо, Нарья, Ненья"
        required
        :error="errors.name"
      />

      <BaseInput
        v-model="power"
        label="Магическая сила"
        type="number"
        :min="1"
        :step="1"
        required
        :error="errors.power"
        help-text="Целое положительное число (> 0)"
      />
    </form>

    <template #footer>
      <div class="dialog-footer-content">
        <BaseButton
          v-if="isEdit && ring"
          variant="danger"
          size="sm"
          :disabled="loading"
          @click="
            emit('delete', ring);
            emit('close');
          "
        >
          Удалить кольцо
        </BaseButton>
        <div class="right-buttons">
          <BaseButton variant="ghost" :disabled="loading" @click="emit('close')">
            Отмена
          </BaseButton>
          <BaseButton variant="primary" :loading="loading" form="ringForm" type="submit">
            {{ isEdit ? 'Сохранить изменения' : 'Создать кольцо' }}
          </BaseButton>
        </div>
      </div>
    </template>
  </BaseModal>
</template>

<style scoped>
.server-error-banner {
  background: var(--danger-bg);
  border: 1px solid var(--danger);
  color: var(--danger);
  padding: 0.75rem 1rem;
  border-radius: var(--radius-sm);
  font-size: 0.85rem;
  margin-bottom: 1.25rem;
}

.dialog-footer-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
}

.right-buttons {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin-left: auto;
}
</style>
