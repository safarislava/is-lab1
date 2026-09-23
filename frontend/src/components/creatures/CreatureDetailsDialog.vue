<script setup lang="ts">
import type { BookCreatureResponse } from '@/types/creature';
import BaseModal from '@/components/ui/BaseModal.vue';
import BaseButton from '@/components/ui/BaseButton.vue';
import BaseBadge, { type BadgeVariant } from '@/components/ui/BaseBadge.vue';
import { Edit2, Trash2 } from 'lucide-vue-next';

defineProps<{
  isOpen: boolean;
  creature: BookCreatureResponse | null;
}>();

const emit = defineEmits<{
  (e: 'close'): void;
  (e: 'edit', creature: BookCreatureResponse): void;
  (e: 'delete', creature: BookCreatureResponse): void;
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

function getBadgeVariant(type?: string): BadgeVariant {
  const map: Record<string, BadgeVariant> = {
    HOBBIT: 'hobbit',
    ELF: 'elf',
    HUMAN: 'human',
    GOLLUM: 'gollum',
    ORC: 'orc',
  };
  return (type && map[type]) || 'neutral';
}

function getTypeLabel(type?: string): string {
  if (!type) return '';
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
  <BaseModal
    :is-open="isOpen"
    :title="creature ? `Информация о существе: ${creature.name}` : 'Информация о существе'"
    max-width="600px"
    @close="emit('close')"
  >
    <div v-if="creature" class="details-content">
      <!-- Main Info -->
      <div class="detail-section">
        <div class="header-card">
          <div class="title-with-badge">
            <h2 class="creature-name">
              {{ creature.name }}
            </h2>
            <span class="creature-id">#{{ creature.id }}</span>
          </div>
          <BaseBadge :variant="getBadgeVariant(creature.creatureType)">
            {{ getTypeLabel(creature.creatureType) }}
          </BaseBadge>
        </div>

        <div class="grid-stats">
          <div class="stat-box">
            <span class="stat-label">Возраст</span>
            <span class="stat-value">{{ creature.age }} лет</span>
          </div>
          <div class="stat-box">
            <span class="stat-label">Атака</span>
            <span class="stat-value stat-attack">⚔️ {{ creature.attackLevel }}</span>
          </div>
          <div class="stat-box">
            <span class="stat-label">Защита</span>
            <span class="stat-value stat-defense">🛡️ {{ creature.defenseLevel }}</span>
          </div>
          <div class="stat-box">
            <span class="stat-label">Дата создания</span>
            <span class="stat-value text-sm">{{ formatDate(creature.creationDate) }}</span>
          </div>
        </div>
      </div>

      <!-- Coordinates -->
      <div class="detail-section">
        <h4 class="section-title">📍 Координаты</h4>
        <div class="coords-box">
          <div class="coord-val">
            <span class="coord-axis">X:</span>
            <span class="coord-num">{{ creature.coordinates.x }}</span>
            <span class="coord-limit">максимум 500</span>
          </div>
          <div class="coord-val">
            <span class="coord-axis">Y:</span>
            <span class="coord-num">{{ creature.coordinates.y }}</span>
            <span class="coord-limit">больше -780</span>
          </div>
        </div>
      </div>

      <!-- Magic City -->
      <div class="detail-section">
        <h4 class="section-title">🏙️ Волшебный город</h4>
        <div v-if="creature.creatureLocation" class="card inner-card">
          <div class="city-header">
            <strong>{{ creature.creatureLocation.name }}</strong>
            <span class="city-id">ID: {{ creature.creatureLocation.id }}</span>
            <BaseBadge v-if="creature.creatureLocation.capital" variant="warning" size="sm">
              Столица
            </BaseBadge>
          </div>
          <div class="city-details-grid">
            <div>
              <span class="muted-label">Площадь:</span>
              {{ creature.creatureLocation.area }} км²
            </div>
            <div>
              <span class="muted-label">Население:</span>
              {{ creature.creatureLocation.population }}
            </div>
            <div>
              <span class="muted-label">Плотность:</span>
              {{ creature.creatureLocation.populationDensity ?? '—' }} чел/км²
            </div>
            <div>
              <span class="muted-label">Губернатор:</span>
              {{ getTypeLabel(creature.creatureLocation.governor ?? undefined) || '—' }}
            </div>
            <div class="full-width">
              <span class="muted-label">Основан:</span>
              {{ formatDate(creature.creatureLocation.establishmentDate) }}
            </div>
          </div>
        </div>
        <div v-else class="empty-relation">
          Существо не привязано ни к одному волшебному городу.
        </div>
      </div>

      <!-- Ring -->
      <div class="detail-section">
        <h4 class="section-title">💍 Кольцо</h4>
        <div v-if="creature.ring" class="card inner-card ring-card">
          <div class="ring-header">
            <strong class="ring-name">{{ creature.ring.name }}</strong>
            <span class="ring-power">Сила: {{ creature.ring.power }}</span>
          </div>
        </div>
        <div v-else class="empty-relation">Существо не владеет кольцом.</div>
      </div>
    </div>

    <template #footer>
      <div class="dialog-actions">
        <div class="left-actions">
          <BaseButton
            v-if="creature"
            variant="danger"
            size="sm"
            @click="
              emit('delete', creature);
              emit('close');
            "
          >
            <Trash2 :size="14" />
            Удалить
          </BaseButton>
          <BaseButton
            v-if="creature"
            variant="secondary"
            size="sm"
            @click="
              emit('edit', creature);
              emit('close');
            "
          >
            <Edit2 :size="14" />
            Редактировать
          </BaseButton>
        </div>
        <BaseButton variant="ghost" @click="emit('close')"> Закрыть </BaseButton>
      </div>
    </template>
  </BaseModal>
</template>

<style scoped>
.details-content {
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
}

.header-card {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 1rem;
}

.title-with-badge {
  display: flex;
  align-items: center;
  gap: 0.6rem;
}

.creature-name {
  font-size: 1.35rem;
  font-weight: 700;
  margin: 0;
}

.creature-id {
  color: var(--text-dim);
  font-family: monospace;
  font-size: 0.9rem;
}

.grid-stats {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 0.75rem;
}

.stat-box {
  background: var(--bg-card);
  padding: 0.65rem 0.75rem;
  border-radius: var(--radius-sm);
  border: 1px solid var(--border-subtle);
  display: flex;
  flex-direction: column;
}

.stat-label {
  font-size: 0.7rem;
  color: var(--text-muted);
  text-transform: uppercase;
  font-weight: 600;
}

.stat-value {
  font-size: 0.95rem;
  font-weight: 600;
  margin-top: 0.2rem;
}

.stat-attack {
  color: #fb7185;
}

.stat-defense {
  color: #34d399;
}

.text-sm {
  font-size: 0.775rem !important;
  font-weight: 400 !important;
}

.section-title {
  font-size: 0.85rem;
  font-weight: 600;
  color: var(--text-muted);
  text-transform: uppercase;
  letter-spacing: 0.03em;
  margin-bottom: 0.5rem;
}

.coords-box {
  display: flex;
  gap: 1.5rem;
  background: var(--bg-card);
  padding: 0.75rem 1rem;
  border-radius: var(--radius-sm);
  border: 1px solid var(--border-subtle);
}

.coord-val {
  display: flex;
  align-items: center;
  gap: 0.4rem;
}

.coord-axis {
  font-weight: 700;
  color: var(--primary);
}

.coord-num {
  font-family: monospace;
  font-weight: 600;
}

.coord-limit {
  font-size: 0.75rem;
  color: var(--text-dim);
}

.inner-card {
  padding: 0.85rem 1rem;
  background: var(--bg-card);
}

.city-header,
.ring-header {
  display: flex;
  align-items: center;
  gap: 0.6rem;
  margin-bottom: 0.6rem;
}

.city-id {
  font-size: 0.75rem;
  color: var(--text-dim);
}

.city-details-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 0.4rem 1rem;
  font-size: 0.825rem;
}

.full-width {
  grid-column: span 2;
}

.muted-label {
  color: var(--text-muted);
}

.ring-card {
  border-left: 3px solid #f59e0b;
}

.ring-name {
  color: #f59e0b;
}

.ring-power {
  font-size: 0.85rem;
  color: var(--text-muted);
  font-weight: 500;
}

.empty-relation {
  font-size: 0.825rem;
  color: var(--text-dim);
  font-style: italic;
  padding: 0.5rem 0;
}

.dialog-actions {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
}

.left-actions {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}
</style>
