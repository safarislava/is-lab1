<script setup lang="ts">
import { ref, computed, watch } from 'vue';
import type { MagicCityResponse } from '@/types/city';
import { useCitiesStore } from '@/stores/citiesStore';
import BaseModal from '@/components/ui/BaseModal.vue';
import BaseButton from '@/components/ui/BaseButton.vue';
import BaseSelect from '@/components/ui/BaseSelect.vue';
import { AlertTriangle } from 'lucide-vue-next';

const props = defineProps<{
  isOpen: boolean;
  city: MagicCityResponse | null;
}>();

const emit = defineEmits<{
  (e: 'close'): void;
  (e: 'deleted'): void;
}>();

const citiesStore = useCitiesStore();
const replacementCityId = ref<number | null>(null);
const loading = ref(false);
const error = ref<string | null>(null);

const availableReplacements = computed(() => {
  if (!props.city) return [];
  return citiesStore.cities
    .filter((c) => c.id !== props.city!.id)
    .map((c) => ({
      label: `${c.name} (ID: #${c.id})`,
      value: c.id,
    }));
});

const canDelete = computed(() => {
  return availableReplacements.value.length > 0 && replacementCityId.value !== null;
});

watch(
  () => props.isOpen,
  (open) => {
    if (open) {
      error.value = null;
      if (availableReplacements.value.length > 0) {
        replacementCityId.value = availableReplacements.value[0].value;
      } else {
        replacementCityId.value = null;
      }
    }
  },
);

async function confirmDelete() {
  if (!props.city || !replacementCityId.value) return;

  loading.value = true;
  error.value = null;

  try {
    await citiesStore.deleteCity(props.city.id, replacementCityId.value);
    emit('deleted');
    emit('close');
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Не удалось удалить город';
  } finally {
    loading.value = false;
  }
}
</script>

<template>
  <BaseModal
    :is-open="isOpen"
    title="Удаление города и перепривязка"
    max-width="520px"
    @close="emit('close')"
  >
    <div v-if="city" class="delete-content">
      <div class="warning-banner">
        <AlertTriangle :size="24" class="warning-icon" />
        <div>
          <strong>Требование системы:</strong> Все существа, привязанные к удаляемому городу
          <strong>"{{ city.name }}"</strong>, должны быть автоматически перенесены в другой город по
          вашему выбору.
        </div>
      </div>

      <div v-if="availableReplacements.length > 0" class="replacement-selector">
        <BaseSelect
          v-model="replacementCityId"
          label="Выберите город-заместитель для существ"
          :options="availableReplacements"
          required
        />
      </div>

      <div v-else class="no-replacements-alert">
        ❌ <strong>Невозможно удалить город:</strong> В системе нет других городов для перепривязки
        существ. Создайте хотя бы еще один город перед удалением текущего.
      </div>

      <div v-if="error" class="error-msg">⚠️ {{ error }}</div>
    </div>

    <template #footer>
      <BaseButton variant="ghost" :disabled="loading" @click="emit('close')"> Отмена </BaseButton>
      <BaseButton variant="danger" :loading="loading" :disabled="!canDelete" @click="confirmDelete">
        Удалить и перепривязать
      </BaseButton>
    </template>
  </BaseModal>
</template>

<style scoped>
.delete-content {
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
}

.warning-banner {
  display: flex;
  align-items: flex-start;
  gap: 0.85rem;
  background: var(--warning-bg);
  border: 1px solid var(--warning);
  color: #fef08a;
  padding: 0.85rem 1rem;
  border-radius: var(--radius-md);
  font-size: 0.875rem;
  line-height: 1.4;
}

.warning-icon {
  color: var(--warning);
  flex-shrink: 0;
  margin-top: 0.1rem;
}

.no-replacements-alert {
  background: var(--danger-bg);
  border: 1px solid var(--danger);
  color: #fecdd3;
  padding: 0.85rem 1rem;
  border-radius: var(--radius-md);
  font-size: 0.85rem;
}

.error-msg {
  color: var(--danger);
  background: var(--danger-bg);
  padding: 0.5rem 0.75rem;
  border-radius: var(--radius-sm);
  font-size: 0.825rem;
}
</style>
