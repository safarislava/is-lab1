<script setup lang="ts">
import { ref } from 'vue';
import type { RingResponse } from '@/types/ring';
import { useRingsStore } from '@/stores/ringsStore';
import BaseModal from '@/components/ui/BaseModal.vue';
import BaseButton from '@/components/ui/BaseButton.vue';
import { AlertTriangle } from 'lucide-vue-next';

const props = defineProps<{
  isOpen: boolean;
  ring: RingResponse | null;
}>();

const emit = defineEmits<{
  (e: 'close'): void;
  (e: 'deleted'): void;
}>();

const ringsStore = useRingsStore();
const loading = ref(false);
const error = ref<string | null>(null);

async function confirmDelete() {
  if (!props.ring) return;
  loading.value = true;
  error.value = null;

  try {
    await ringsStore.deleteRing(props.ring.id);
    emit('deleted');
    emit('close');
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Не удалось удалить кольцо';
  } finally {
    loading.value = false;
  }
}
</script>

<template>
  <BaseModal
    :is-open="isOpen"
    title="Удаление магического кольца"
    max-width="480px"
    @close="emit('close')"
  >
    <div v-if="ring" class="delete-confirmation">
      <div class="warning-icon">
        <AlertTriangle :size="32" />
      </div>

      <div class="warning-text">
        <p>
          Вы действительно хотите удалить кольцо <strong>"{{ ring.name }}"</strong> (ID: #{{
            ring.id
          }}, Сила: {{ ring.power }})?
        </p>
        <p class="warning-sub">
          Если это кольцо надето на существо, оно будет автоматически снято перед удалением.
        </p>
      </div>

      <div v-if="error" class="error-msg">⚠️ {{ error }}</div>
    </div>

    <template #footer>
      <BaseButton variant="ghost" :disabled="loading" @click="emit('close')"> Отмена </BaseButton>
      <BaseButton variant="danger" :loading="loading" @click="confirmDelete">
        Удалить кольцо
      </BaseButton>
    </template>
  </BaseModal>
</template>

<style scoped>
.delete-confirmation {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  padding: 0.5rem 0;
  gap: 1rem;
}

.warning-icon {
  color: var(--danger);
  background: var(--danger-bg);
  padding: 0.85rem;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.warning-text p {
  margin-bottom: 0.35rem;
  font-size: 0.95rem;
}

.warning-sub {
  color: var(--text-muted);
  font-size: 0.825rem !important;
}

.error-msg {
  color: var(--danger);
  background: var(--danger-bg);
  padding: 0.5rem 0.75rem;
  border-radius: var(--radius-sm);
  font-size: 0.825rem;
  width: 100%;
}
</style>
