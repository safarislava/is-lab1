<script setup lang="ts">
import { ref, computed, watch } from 'vue';
import type {
  MagicCityResponse,
  MagicCityCreateRequest,
  MagicCityUpdateRequest,
  BookCreatureType,
} from '@/types/city';
import { useCitiesStore } from '@/stores/citiesStore';
import { ApiError } from '@/api/httpClient';
import BaseModal from '@/components/ui/BaseModal.vue';
import BaseButton from '@/components/ui/BaseButton.vue';
import BaseInput from '@/components/ui/BaseInput.vue';
import BaseSelect from '@/components/ui/BaseSelect.vue';

const props = defineProps<{
  isOpen: boolean;
  city?: MagicCityResponse | null;
}>();

const emit = defineEmits<{
  (e: 'close'): void;
  (e: 'saved', city: MagicCityResponse): void;
  (e: 'delete', city: MagicCityResponse): void;
}>();

const citiesStore = useCitiesStore();

const isEdit = computed(() => !!props.city);
const modalTitle = computed(() =>
  isEdit.value ? `Редактирование города #${props.city?.id}` : 'Создание волшебного города',
);

const name = ref('');
const area = ref<number | null>(100);
const population = ref<number | null>(5000);
const establishmentDate = ref('');
const governor = ref<BookCreatureType | null>(null);
const capital = ref<boolean | null>(false);
const populationDensity = ref<number | null>(null);

const errors = ref<Record<string, string>>({});
const serverError = ref<string | null>(null);
const loading = ref(false);

const governorOptions = [
  { label: '— Без губернатора —', value: null },
  { label: 'Хоббит', value: 'HOBBIT' },
  { label: 'Эльф', value: 'ELF' },
  { label: 'Человек', value: 'HUMAN' },
  { label: 'Голлум', value: 'GOLLUM' },
  { label: 'Орк', value: 'ORC' },
];

const capitalOptions = [
  { label: 'Обычный город', value: false },
  { label: '👑 Столица государства', value: true },
];

watch(
  () => props.isOpen,
  (open) => {
    if (open) {
      errors.value = {};
      serverError.value = null;

      if (props.city) {
        name.value = props.city.name;
        area.value = props.city.area;
        population.value = props.city.population;
        establishmentDate.value = props.city.establishmentDate
          ? props.city.establishmentDate.slice(0, 16)
          : '';
        governor.value = props.city.governor ?? null;
        capital.value = props.city.capital ?? false;
        populationDensity.value = props.city.populationDensity ?? null;
      } else {
        name.value = '';
        area.value = 100;
        population.value = 1000;
        establishmentDate.value = '';
        governor.value = null;
        capital.value = false;
        populationDensity.value = null;
      }
    }
  },
);

function validate(): boolean {
  const errs: Record<string, string> = {};

  if (!name.value || !name.value.trim()) {
    errs.name = 'Название города обязательно';
  }

  if (area.value === null || isNaN(area.value) || area.value <= 0) {
    errs.area = 'Площадь должна быть больше 0';
  }

  if (population.value === null || isNaN(population.value) || population.value <= 0) {
    errs.population = 'Население должно быть больше 0';
  }

  if (
    populationDensity.value !== null &&
    populationDensity.value !== undefined &&
    String(populationDensity.value) !== ''
  ) {
    if (isNaN(populationDensity.value) || populationDensity.value <= 0) {
      errs.populationDensity = 'Плотность населения должна быть больше 0';
    }
  }

  errors.value = errs;
  return Object.keys(errs).length === 0;
}

async function onSubmit() {
  serverError.value = null;
  if (!validate()) return;

  loading.value = true;
  const payload: MagicCityCreateRequest | MagicCityUpdateRequest = {
    name: name.value.trim(),
    area: Math.round(area.value!),
    population: Math.round(population.value!),
    establishmentDate: establishmentDate.value ? `${establishmentDate.value}:00` : null,
    governor: governor.value,
    capital: capital.value,
    populationDensity: populationDensity.value ? Number(populationDensity.value) : null,
  };

  try {
    let res: MagicCityResponse;
    if (isEdit.value && props.city) {
      res = await citiesStore.updateCity(props.city.id, payload);
    } else {
      res = await citiesStore.createCity(payload);
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
      serverError.value = err instanceof Error ? err.message : 'Ошибка при сохранении города';
    }
  } finally {
    loading.value = false;
  }
}
</script>

<template>
  <BaseModal :is-open="isOpen" :title="modalTitle" max-width="560px" @close="emit('close')">
    <form id="cityForm" novalidate @submit.prevent="onSubmit">
      <div v-if="serverError" class="server-error-banner">⚠️ {{ serverError }}</div>

      <BaseInput
        v-model="name"
        label="Название города"
        placeholder="Например: Гондор, Ривенделл, Мордор"
        required
        :error="errors.name"
      />

      <div class="form-row">
        <BaseInput
          v-model="area"
          label="Площадь (км²)"
          type="number"
          :min="1"
          :step="1"
          required
          :error="errors.area"
        />

        <BaseInput
          v-model="population"
          label="Население (чел.)"
          type="number"
          :min="1"
          :step="1"
          required
          :error="errors.population"
        />
      </div>

      <div class="form-row">
        <BaseInput
          v-model="populationDensity"
          label="Плотность населения (чел/км²)"
          type="number"
          step="any"
          placeholder="Опционально"
          :error="errors.populationDensity"
        />

        <BaseInput v-model="establishmentDate" label="Дата основания" type="datetime-local" />
      </div>

      <div class="form-row">
        <BaseSelect v-model="governor" label="Губернатор города" :options="governorOptions" />

        <BaseSelect v-model="capital" label="Статус столицы" :options="capitalOptions" />
      </div>
    </form>

    <template #footer>
      <div class="dialog-footer-content">
        <BaseButton
          v-if="isEdit && city"
          variant="danger"
          size="sm"
          :disabled="loading"
          @click="
            emit('delete', city);
            emit('close');
          "
        >
          Удалить город
        </BaseButton>
        <div class="right-buttons">
          <BaseButton variant="ghost" :disabled="loading" @click="emit('close')">
            Отмена
          </BaseButton>
          <BaseButton variant="primary" :loading="loading" form="cityForm" type="submit">
            {{ isEdit ? 'Сохранить изменения' : 'Создать город' }}
          </BaseButton>
        </div>
      </div>
    </template>
  </BaseModal>
</template>

<style scoped>
.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
}

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
