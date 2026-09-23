<script setup lang="ts">
import { useCreaturesStore } from '@/stores/creaturesStore';
import BaseButton from '@/components/ui/BaseButton.vue';
import BaseSelect from '@/components/ui/BaseSelect.vue';
import { Search, RotateCcw } from 'lucide-vue-next';
import type { BookCreatureType } from '@/types/creature';

const store = useCreaturesStore();

const typeOptions: Array<{ label: string; value: BookCreatureType | '' }> = [
  { label: 'Все типы', value: '' },
  { label: 'Хоббит', value: 'HOBBIT' },
  { label: 'Эльф', value: 'ELF' },
  { label: 'Человек', value: 'HUMAN' },
  { label: 'Голлум', value: 'GOLLUM' },
  { label: 'Орк', value: 'ORC' },
];

const pageSizeOptions = [
  { label: '5', value: 5 },
  { label: '10', value: 10 },
  { label: '20', value: 20 },
  { label: '50', value: 50 },
];

function onSearch() {
  store.applyFilters();
}

function onReset() {
  store.resetFilters();
}
</script>

<template>
  <div class="filters-panel card">
    <div class="filters-grid">
      <div class="filter-item">
        <label class="filter-label">Имя существа</label>
        <input
          v-model="store.nameFilter"
          type="text"
          placeholder="Поиск по имени..."
          class="form-input"
          @keydown.enter="onSearch"
        />
      </div>

      <div class="filter-item">
        <label class="filter-label">Тип существа</label>
        <BaseSelect
          v-model="store.creatureTypeFilter"
          :options="typeOptions"
          @update:model-value="onSearch"
        />
      </div>

      <div class="filter-item">
        <label class="filter-label">Город</label>
        <input
          v-model="store.cityNameFilter"
          type="text"
          placeholder="Поиск по городу..."
          class="form-input"
          @keydown.enter="onSearch"
        />
      </div>

      <div class="filter-item">
        <label class="filter-label">Кольцо</label>
        <input
          v-model="store.ringNameFilter"
          type="text"
          placeholder="Поиск по кольцу..."
          class="form-input"
          @keydown.enter="onSearch"
        />
      </div>
    </div>

    <div class="filters-actions">
      <div class="page-size-selector">
        <label class="filter-label">Показывать по:</label>
        <div class="size-select-wrap">
          <BaseSelect
            :model-value="store.size"
            :options="pageSizeOptions"
            size="sm"
            @update:model-value="(val) => store.setSize(Number(val))"
          />
        </div>
      </div>

      <div class="btn-group">
        <BaseButton variant="ghost" size="sm" @click="onReset">
          <RotateCcw :size="14" />
          Сбросить
        </BaseButton>
        <BaseButton variant="primary" size="sm" :loading="store.loading" @click="onSearch">
          <Search :size="14" />
          Найти
        </BaseButton>
      </div>
    </div>
  </div>
</template>

<style scoped>
.filters-panel {
  margin-bottom: 1.25rem;
  padding: 1rem 1.25rem;
  background: var(--bg-surface);
}

.filters-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1rem;
  margin-bottom: 1rem;
}

.filter-item {
  display: flex;
  flex-direction: column;
  gap: 0.35rem;
}

.filter-label {
  font-size: 0.775rem;
  font-weight: 600;
  color: var(--text-muted);
  text-transform: uppercase;
  letter-spacing: 0.03em;
}

.filters-actions {
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-top: 1px solid var(--border-color);
  padding-top: 0.75rem;
  flex-wrap: wrap;
  gap: 0.75rem;
}

.page-size-selector {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.size-select-wrap {
  width: 70px;
}

.size-select-wrap :deep(.form-group),
.filter-item :deep(.form-group) {
  margin-bottom: 0;
}

.btn-group {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}
</style>
