<script setup lang="ts">
import { ref, computed, watch } from 'vue';
import type {
  BookCreatureResponse,
  BookCreatureCreateRequest,
  BookCreatureUpdateRequest,
  BookCreatureType,
} from '@/types/creature';
import { useCreaturesStore } from '@/stores/creaturesStore';
import { useCitiesStore } from '@/stores/citiesStore';
import { useRingsStore } from '@/stores/ringsStore';
import { ApiError } from '@/api/httpClient';
import BaseModal from '@/components/ui/BaseModal.vue';
import BaseButton from '@/components/ui/BaseButton.vue';
import BaseInput from '@/components/ui/BaseInput.vue';
import BaseSelect from '@/components/ui/BaseSelect.vue';

const props = defineProps<{
  isOpen: boolean;
  creature?: BookCreatureResponse | null;
}>();

const emit = defineEmits<{
  (e: 'close'): void;
  (e: 'saved', creature: BookCreatureResponse): void;
}>();

const creaturesStore = useCreaturesStore();
const citiesStore = useCitiesStore();
const ringsStore = useRingsStore();

const isEdit = computed(() => !!props.creature);
const modalTitle = computed(() =>
  isEdit.value ? `Редактирование существа #${props.creature?.id}` : 'Создание нового существа',
);

// Form state
const name = ref('');
const coordX = ref<number | null>(0);
const coordY = ref<number | null>(0);
const age = ref<number | null>(100);
const creatureType = ref<BookCreatureType>('HOBBIT');
const creatureLocationId = ref<number | null>(null);
const attackLevel = ref<number | null>(10.0);
const defenseLevel = ref<number | null>(10.0);
const ringId = ref<number | null>(null);

const errors = ref<Record<string, string>>({});
const serverError = ref<string | null>(null);
const loading = ref(false);

const creatureTypeOptions = [
  { label: 'Хоббит', value: 'HOBBIT' },
  { label: 'Эльф', value: 'ELF' },
  { label: 'Человек', value: 'HUMAN' },
  { label: 'Голлум', value: 'GOLLUM' },
  { label: 'Орк', value: 'ORC' },
];

const cityOptions = computed(() => [
  { label: '— Без города —', value: null },
  ...citiesStore.cities.map((c) => ({
    label: `${c.name} — Площадь: ${c.area}, Население: ${c.population}`,
    value: c.id,
  })),
]);

const ringOptions = computed(() => [
  { label: '— Без кольца —', value: null },
  ...ringsStore.rings.map((r) => ({
    label: `${r.name} — Сила: ${r.power}`,
    value: r.id,
  })),
]);

watch(
  () => props.isOpen,
  (open) => {
    if (open) {
      citiesStore.fetchCities().catch(() => {});
      ringsStore.fetchRings().catch(() => {});

      errors.value = {};
      serverError.value = null;

      if (props.creature) {
        name.value = props.creature.name;
        coordX.value = props.creature.coordinates.x;
        coordY.value = props.creature.coordinates.y;
        age.value = props.creature.age;
        creatureType.value = props.creature.creatureType;
        creatureLocationId.value = props.creature.creatureLocation
          ? props.creature.creatureLocation.id
          : null;
        attackLevel.value = props.creature.attackLevel;
        defenseLevel.value = props.creature.defenseLevel;
        ringId.value = props.creature.ring ? props.creature.ring.id : null;
      } else {
        name.value = '';
        coordX.value = 0;
        coordY.value = 0;
        age.value = 50;
        creatureType.value = 'HOBBIT';
        creatureLocationId.value = null;
        attackLevel.value = 10;
        defenseLevel.value = 10;
        ringId.value = null;
      }
    }
  },
);

function validate(): boolean {
  const errs: Record<string, string> = {};

  if (!name.value || !name.value.trim()) {
    errs.name = 'Имя не может быть пустым';
  }

  if (coordX.value === null || isNaN(coordX.value)) {
    errs.coordX = 'Координата X обязательна';
  } else if (coordX.value > 500) {
    errs.coordX = 'Максимальное значение X: 500';
  }

  if (coordY.value === null || isNaN(coordY.value)) {
    errs.coordY = 'Координата Y обязательна';
  } else if (coordY.value <= -780) {
    errs.coordY = 'Значение Y должно быть строго больше -780';
  }

  if (age.value === null || isNaN(age.value) || age.value <= 0) {
    errs.age = 'Возраст должен быть больше 0';
  }

  if (!creatureType.value) {
    errs.creatureType = 'Выберите тип существа';
  }

  if (attackLevel.value === null || isNaN(attackLevel.value) || attackLevel.value <= 0) {
    errs.attackLevel = 'Уровень атаки должен быть больше 0';
  }

  if (defenseLevel.value === null || isNaN(defenseLevel.value) || defenseLevel.value <= 0) {
    errs.defenseLevel = 'Уровень защиты должен быть больше 0';
  }

  errors.value = errs;
  return Object.keys(errs).length === 0;
}

async function onSubmit() {
  serverError.value = null;
  if (!validate()) return;

  loading.value = true;
  const payload: BookCreatureCreateRequest | BookCreatureUpdateRequest = {
    name: name.value.trim(),
    coordinates: {
      x: Math.round(coordX.value!),
      y: Number(coordY.value!),
    },
    age: Math.round(age.value!),
    creatureType: creatureType.value,
    creatureLocationId: creatureLocationId.value,
    attackLevel: Number(attackLevel.value!),
    defenseLevel: Number(defenseLevel.value!),
    ringId: ringId.value,
  };

  try {
    let result: BookCreatureResponse;
    if (isEdit.value && props.creature) {
      result = await creaturesStore.updateCreature(props.creature.id, payload);
    } else {
      result = await creaturesStore.createCreature(payload);
    }
    emit('saved', result);
    emit('close');
  } catch (err) {
    if (err instanceof ApiError) {
      if (err.details && err.details.length > 0) {
        serverError.value = err.details.join('; ');
      } else {
        serverError.value = err.message;
      }
    } else {
      serverError.value = err instanceof Error ? err.message : 'Произошла ошибка при сохранении';
    }
  } finally {
    loading.value = false;
  }
}
</script>

<template>
  <BaseModal :is-open="isOpen" :title="modalTitle" max-width="640px" @close="emit('close')">
    <form id="creatureForm" novalidate @submit.prevent="onSubmit">
      <div v-if="serverError" class="server-error-banner">⚠️ {{ serverError }}</div>

      <div class="form-row">
        <BaseInput
          v-model="name"
          label="Имя существа"
          placeholder="Например: Фродо Бэггинс"
          required
          :error="errors.name"
        />

        <BaseSelect
          v-model="creatureType"
          label="Тип существа"
          :options="creatureTypeOptions"
          required
          :error="errors.creatureType"
        />
      </div>

      <div class="form-row coords-row">
        <BaseInput
          v-model="coordX"
          label="Координата X"
          type="number"
          :max="500"
          :step="1"
          required
          :error="errors.coordX"
          help-text="Целое число, максимум 500"
        />

        <BaseInput
          v-model="coordY"
          label="Координата Y"
          type="number"
          step="any"
          required
          :error="errors.coordY"
          help-text="Число больше -780"
        />
      </div>

      <div class="form-row">
        <BaseInput
          v-model="age"
          label="Возраст"
          type="number"
          :min="1"
          :step="1"
          required
          :error="errors.age"
        />

        <BaseInput
          v-model="attackLevel"
          label="Уровень атаки"
          type="number"
          step="any"
          required
          :error="errors.attackLevel"
        />

        <BaseInput
          v-model="defenseLevel"
          label="Уровень защиты"
          type="number"
          step="any"
          required
          :error="errors.defenseLevel"
        />
      </div>

      <div class="form-row">
        <BaseSelect
          v-model="creatureLocationId"
          label="Волшебный город"
          :options="cityOptions"
          placeholder="Выберите город..."
        />

        <BaseSelect
          v-model="ringId"
          label="Кольцо"
          :options="ringOptions"
          placeholder="Выберите кольцо..."
        />
      </div>
    </form>

    <template #footer>
      <BaseButton variant="ghost" :disabled="loading" @click="emit('close')"> Отмена </BaseButton>
      <BaseButton variant="primary" :loading="loading" form="creatureForm" type="submit">
        {{ isEdit ? 'Сохранить изменения' : 'Создать существо' }}
      </BaseButton>
    </template>
  </BaseModal>
</template>

<style scoped>
.form-row {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 1rem;
}

.coords-row {
  background: var(--bg-card);
  padding: 0.85rem;
  border-radius: var(--radius-md);
  border: 1px solid var(--border-subtle);
  margin-bottom: 1rem;
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
</style>
