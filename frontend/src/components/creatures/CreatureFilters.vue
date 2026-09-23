<script setup lang="ts">
import { useCreaturesStore } from '@/stores/creaturesStore';
import BaseButton from '@/components/ui/BaseButton.vue';
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
        <div class="input-with-icon">
          <input
            v-model="store.nameFilter"
            type="text"
            placeholder="Поиск по имени..."
            class="form-input"
            @keydown.enter="onSearch"
          />
        </div>
      </div>

      <div class="filter-item">
        <label class="filter-label">Тип существа</label>
        <select v-model="store.creatureTypeFilter" class="form-select" @change="onSearch">
          <option v-for="opt in typeOptions" :key="opt.value" :value="opt.value">
            {{ opt.label }}
          </option>
        </select>
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
        <select
          :value="store.size"
          class="form-select size-select"
          @change="(e) => store.setSize(Number((e.target as HTMLSelectElement).value))"
        >
          <option :value="5">5</option>
          <option :value="10">10</option>
          <option :value="20">20</option>
          <option :value="50">50</option>
        </select>
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

.size-select {
  width: auto;
  padding: 0.35rem 0.65rem;
}

.btn-group {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}
</style>
