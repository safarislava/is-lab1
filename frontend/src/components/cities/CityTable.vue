<script setup lang="ts">
import type { MagicCityResponse } from '@/types/city';
import BaseBadge from '@/components/ui/BaseBadge.vue';

defineProps<{
  cities: MagicCityResponse[];
  loading?: boolean;
}>();

const emit = defineEmits<{
  (e: 'edit', city: MagicCityResponse): void;
}>();

function formatDate(isoString?: string | null): string {
  if (!isoString) return '—';
  try {
    const d = new Date(isoString);
    return new Intl.DateTimeFormat('ru-RU', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    }).format(d);
  } catch {
    return isoString;
  }
}
</script>

<template>
  <div class="table-container">
    <table class="data-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>Название города</th>
          <th>Площадь (км²)</th>
          <th>Население</th>
          <th>Плотность (чел/км²)</th>
          <th>Губернатор</th>
          <th>Статус столицы</th>
          <th>Дата основания</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="loading && cities.length === 0">
          <td colspan="8" class="empty-cell">
            <div class="spinner-center">
              <span class="spinner" />
              <span>Загрузка городов...</span>
            </div>
          </td>
        </tr>

        <tr v-else-if="cities.length === 0">
          <td colspan="8" class="empty-cell">
            <p>Города не найдены. Создайте первый волшебный город!</p>
          </td>
        </tr>

        <tr
          v-for="city in cities"
          :key="city.id"
          class="clickable-row"
          title="Нажмите для редактирования"
          @click="emit('edit', city)"
        >
          <td>
            <span class="id-badge">#{{ city.id }}</span>
          </td>
          <td>
            <strong>{{ city.name }}</strong>
          </td>
          <td>{{ city.area.toLocaleString() }}</td>
          <td>{{ city.population.toLocaleString() }}</td>
          <td>
            {{
              city.populationDensity !== null && city.populationDensity !== undefined
                ? city.populationDensity
                : '—'
            }}
          </td>
          <td>
            <BaseBadge v-if="city.governor" variant="neutral" size="sm">
              {{ city.governor }}
            </BaseBadge>
            <span v-else class="text-dim">—</span>
          </td>
          <td>
            <BaseBadge v-if="city.capital" variant="warning" size="sm"> 👑 Столица </BaseBadge>
            <span v-else class="text-dim">Нет</span>
          </td>
          <td class="text-muted">
            {{ formatDate(city.establishmentDate) }}
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<style scoped>
.clickable-row {
  cursor: pointer;
  transition: background-color 0.15s ease;
}

.clickable-row:hover {
  background: var(--bg-card-hover) !important;
}

.text-muted {
  color: var(--text-muted);
}

.text-dim {
  color: var(--text-dim);
}

.id-badge {
  font-family: monospace;
  color: var(--text-dim);
  font-weight: 600;
}

.empty-cell {
  text-align: center;
  padding: 3rem 1rem !important;
  color: var(--text-muted);
}

.spinner-center {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.75rem;
}
</style>
