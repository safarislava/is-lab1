<script setup lang="ts">
import type { BookCreatureResponse } from '@/types/creature';
import type { SortDirection } from '@/types/common';
import BaseBadge, { type BadgeVariant } from '@/components/ui/BaseBadge.vue';
import { ArrowUp, ArrowDown, ArrowUpDown } from 'lucide-vue-next';

defineProps<{
  creatures: BookCreatureResponse[];
  loading?: boolean;
  sortBy?: string | null;
  sortDirection?: SortDirection;
}>();

const emit = defineEmits<{
  (e: 'sort', column: string): void;
  (e: 'view', creature: BookCreatureResponse): void;
}>();

function formatDate(isoString: string): string {
  if (!isoString) return '-';
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

function getBadgeVariant(type: string): BadgeVariant {
  const map: Record<string, BadgeVariant> = {
    HOBBIT: 'hobbit',
    ELF: 'elf',
    HUMAN: 'human',
    GOLLUM: 'gollum',
    ORC: 'orc',
  };
  return map[type] || 'neutral';
}

function getTypeLabel(type: string): string {
  const map: Record<string, string> = {
    HOBBIT: 'Хоббит',
    ELF: 'Эльф',
    HUMAN: 'Человек',
    GOLLUM: 'Голлум',
    ORC: 'Орк',
  };
  return map[type] || type;
}
</script>

<template>
  <div class="table-container">
    <table class="data-table">
      <thead>
        <tr>
          <th>ID</th>
          <th class="sortable" @click="emit('sort', 'name')">
            <div class="th-content">
              <span>Имя</span>
              <ArrowUp
                v-if="sortBy === 'name' && sortDirection === 'ASC'"
                :size="14"
                class="sort-active"
              />
              <ArrowDown
                v-else-if="sortBy === 'name' && sortDirection === 'DESC'"
                :size="14"
                class="sort-active"
              />
              <ArrowUpDown v-else :size="13" class="sort-idle" />
            </div>
          </th>
          <th class="sortable" @click="emit('sort', 'coordinate_x')">
            <div class="th-content">
              <span>Координаты</span>
              <ArrowUp
                v-if="
                  (sortBy === 'coordinate_x' || sortBy === 'coordinate_y') &&
                  sortDirection === 'ASC'
                "
                :size="14"
                class="sort-active"
              />
              <ArrowDown
                v-else-if="
                  (sortBy === 'coordinate_x' || sortBy === 'coordinate_y') &&
                  sortDirection === 'DESC'
                "
                :size="14"
                class="sort-active"
              />
              <ArrowUpDown v-else :size="13" class="sort-idle" />
            </div>
          </th>
          <th class="sortable" @click="emit('sort', 'creation_date')">
            <div class="th-content">
              <span>Дата создания</span>
              <ArrowUp
                v-if="sortBy === 'creation_date' && sortDirection === 'ASC'"
                :size="14"
                class="sort-active"
              />
              <ArrowDown
                v-else-if="sortBy === 'creation_date' && sortDirection === 'DESC'"
                :size="14"
                class="sort-active"
              />
              <ArrowUpDown v-else :size="13" class="sort-idle" />
            </div>
          </th>
          <th class="sortable" @click="emit('sort', 'age')">
            <div class="th-content">
              <span>Возраст</span>
              <ArrowUp
                v-if="sortBy === 'age' && sortDirection === 'ASC'"
                :size="14"
                class="sort-active"
              />
              <ArrowDown
                v-else-if="sortBy === 'age' && sortDirection === 'DESC'"
                :size="14"
                class="sort-active"
              />
              <ArrowUpDown v-else :size="13" class="sort-idle" />
            </div>
          </th>
          <th class="sortable" @click="emit('sort', 'creature_type')">
            <div class="th-content">
              <span>Тип</span>
              <ArrowUp
                v-if="sortBy === 'creature_type' && sortDirection === 'ASC'"
                :size="14"
                class="sort-active"
              />
              <ArrowDown
                v-else-if="sortBy === 'creature_type' && sortDirection === 'DESC'"
                :size="14"
                class="sort-active"
              />
              <ArrowUpDown v-else :size="13" class="sort-idle" />
            </div>
          </th>
          <th class="sortable" @click="emit('sort', 'city_name')">
            <div class="th-content">
              <span>Город</span>
              <ArrowUp
                v-if="sortBy === 'city_name' && sortDirection === 'ASC'"
                :size="14"
                class="sort-active"
              />
              <ArrowDown
                v-else-if="sortBy === 'city_name' && sortDirection === 'DESC'"
                :size="14"
                class="sort-active"
              />
              <ArrowUpDown v-else :size="13" class="sort-idle" />
            </div>
          </th>
          <th class="sortable" @click="emit('sort', 'attack_level')">
            <div class="th-content">
              <span>Атака</span>
              <ArrowUp
                v-if="sortBy === 'attack_level' && sortDirection === 'ASC'"
                :size="14"
                class="sort-active"
              />
              <ArrowDown
                v-else-if="sortBy === 'attack_level' && sortDirection === 'DESC'"
                :size="14"
                class="sort-active"
              />
              <ArrowUpDown v-else :size="13" class="sort-idle" />
            </div>
          </th>
          <th class="sortable" @click="emit('sort', 'defense_level')">
            <div class="th-content">
              <span>Защита</span>
              <ArrowUp
                v-if="sortBy === 'defense_level' && sortDirection === 'ASC'"
                :size="14"
                class="sort-active"
              />
              <ArrowDown
                v-else-if="sortBy === 'defense_level' && sortDirection === 'DESC'"
                :size="14"
                class="sort-active"
              />
              <ArrowUpDown v-else :size="13" class="sort-idle" />
            </div>
          </th>
          <th class="sortable" @click="emit('sort', 'ring_name')">
            <div class="th-content">
              <span>Кольцо</span>
              <ArrowUp
                v-if="sortBy === 'ring_name' && sortDirection === 'ASC'"
                :size="14"
                class="sort-active"
              />
              <ArrowDown
                v-else-if="sortBy === 'ring_name' && sortDirection === 'DESC'"
                :size="14"
                class="sort-active"
              />
              <ArrowUpDown v-else :size="13" class="sort-idle" />
            </div>
          </th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="loading && creatures.length === 0">
          <td colspan="10" class="empty-cell">
            <div class="spinner-center">
              <span class="spinner" />
              <span>Загрузка данных...</span>
            </div>
          </td>
        </tr>

        <tr v-else-if="creatures.length === 0">
          <td colspan="10" class="empty-cell">
            <p>Существа не найдены. Попробуйте изменить параметры фильтрации.</p>
          </td>
        </tr>

        <tr
          v-for="c in creatures"
          :key="c.id"
          class="clickable-row"
          title="Нажмите для просмотра и редактирования"
          @click="emit('view', c)"
        >
          <td>
            <span class="id-badge">#{{ c.id }}</span>
          </td>
          <td>
            <strong>{{ c.name }}</strong>
          </td>
          <td>
            <span class="coord-badge" :title="`X: ${c.coordinates.x}, Y: ${c.coordinates.y}`">
              X: {{ c.coordinates.x }}, Y: {{ c.coordinates.y }}
            </span>
          </td>
          <td class="text-muted">
            {{ formatDate(c.creationDate) }}
          </td>
          <td>{{ c.age }}</td>
          <td>
            <BaseBadge :variant="getBadgeVariant(c.creatureType)">
              {{ getTypeLabel(c.creatureType) }}
            </BaseBadge>
          </td>
          <td>
            <span v-if="c.creatureLocation" class="city-tag">
              🏙️ {{ c.creatureLocation.name }}
            </span>
            <span v-else class="text-dim">—</span>
          </td>
          <td>
            <span class="stat-attack">⚔️ {{ c.attackLevel }}</span>
          </td>
          <td>
            <span class="stat-defense">🛡️ {{ c.defenseLevel }}</span>
          </td>
          <td>
            <span v-if="c.ring" class="ring-tag" :title="`Сила: ${c.ring.power}`">
              💍 {{ c.ring.name }} ({{ c.ring.power }})
            </span>
            <span v-else class="text-dim">—</span>
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

.th-content {
  display: flex;
  align-items: center;
  gap: 0.4rem;
}

.sort-active {
  color: var(--primary);
}

.sort-idle {
  color: var(--text-dim);
  opacity: 0.5;
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

.coord-badge {
  background: var(--bg-card);
  padding: 0.2rem 0.45rem;
  border-radius: var(--radius-sm);
  font-family: monospace;
  font-size: 0.8rem;
  border: 1px solid var(--border-subtle);
}

.city-tag {
  color: var(--info);
  font-weight: 500;
}

.ring-tag {
  color: var(--warning);
  font-weight: 500;
}

.stat-attack {
  color: var(--danger);
  font-weight: 600;
}

.stat-defense {
  color: var(--success);
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
