<script setup lang="ts">
import { ref, computed } from 'vue';
import { useSpecialOperationsStore } from '@/stores/specialOperationsStore';
import { useCreaturesStore } from '@/stores/creaturesStore';
import BaseButton from '@/components/ui/BaseButton.vue';
import BaseInput from '@/components/ui/BaseInput.vue';
import BaseSelect from '@/components/ui/BaseSelect.vue';
import BaseBadge, { type BadgeVariant } from '@/components/ui/BaseBadge.vue';
import CreaturePagination from '@/components/creatures/CreaturePagination.vue';
import { Trash2, Calculator, Search, ShieldAlert, Sparkles } from 'lucide-vue-next';

const specialStore = useSpecialOperationsStore();
const creaturesStore = useCreaturesStore();

const searchPageSizeOptions = [
  { label: '5', value: 5 },
  { label: '10', value: 10 },
  { label: '25', value: 25 },
];

// Form inputs
const targetDefenseLevel = ref<number | null>(10.0);
const maxAttackLevel = ref<number | null>(50.0);

// Server-side pagination for search results
const searchPage = ref(0);
const searchPageSize = ref(5);

const totalSearchResults = computed(() => specialStore.attackLessThanResult?.total ?? 0);
const searchResultsList = computed(() => specialStore.attackLessThanResult?.content ?? []);

async function fetchSearchResults() {
  if (maxAttackLevel.value === null || maxAttackLevel.value <= 0) return;
  try {
    await specialStore.searchAttackLessThan(
      Number(maxAttackLevel.value),
      searchPage.value,
      searchPageSize.value,
    );
  } catch (err) {
    error3.value = err instanceof Error ? err.message : 'Ошибка при поиске существ';
  }
}

function onSearchPageChange(newPage: number) {
  searchPage.value = newPage;
  fetchSearchResults();
}

function onSearchPageSizeChange() {
  searchPage.value = 0;
  fetchSearchResults();
}

const error1 = ref<string | null>(null);
const error2 = ref<string | null>(null);
const error3 = ref<string | null>(null);
const error4 = ref<string | null>(null);
const error5 = ref<string | null>(null);

async function handleDeleteByDefense() {
  error1.value = null;
  if (targetDefenseLevel.value === null || targetDefenseLevel.value <= 0) {
    error1.value = 'Укажите положительный уровень защиты (> 0)';
    return;
  }
  try {
    await specialStore.deleteByDefense(Number(targetDefenseLevel.value));
    creaturesStore.fetchCreatures().catch(() => {});
  } catch (err) {
    error1.value = err instanceof Error ? err.message : 'Ошибка при выполнении операции';
  }
}

async function handleCalculateAvgDefense() {
  error2.value = null;
  try {
    await specialStore.calculateAvgDefense();
  } catch (err) {
    error2.value = err instanceof Error ? err.message : 'Ошибка при расчете среднего';
  }
}

async function handleSearchAttackLessThan() {
  error3.value = null;
  if (maxAttackLevel.value === null || maxAttackLevel.value <= 0) {
    error3.value = 'Укажите максимальный уровень атаки (> 0)';
    return;
  }
  searchPage.value = 0;
  await fetchSearchResults();
}

async function handleTakeRingsFromHobbits() {
  if (!confirm('Вы уверены, что хотите отобрать все кольца у хоббитов?')) return;
  error4.value = null;
  try {
    await specialStore.takeRingsFromHobbits();
    creaturesStore.fetchCreatures().catch(() => {});
  } catch (err) {
    error4.value = err instanceof Error ? err.message : 'Ошибка при выполнении операции';
  }
}

async function handleMoveHobbitsToMordor() {
  if (!confirm('Вы уверены, что хотите переместить всех хоббитов с кольцами в Мордор?')) return;
  error5.value = null;
  try {
    await specialStore.moveHobbitsToMordor();
    creaturesStore.fetchCreatures().catch(() => {});
  } catch (err) {
    error5.value = err instanceof Error ? err.message : 'Ошибка при перемещении в Мордор';
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
  <div class="special-grid">
    <!-- Op 1: Delete by Defense -->
    <div class="card op-card">
      <div class="op-header">
        <div class="op-icon bg-danger-icon">
          <Trash2 :size="20" />
        </div>
        <div>
          <h3 class="op-title">Удалить по уровню защиты</h3>
          <p class="op-desc">Удаляет всех существ с точно указанным значением поля defenseLevel</p>
        </div>
      </div>

      <div class="op-body">
        <BaseInput
          v-model="targetDefenseLevel"
          label="Значение defenseLevel"
          type="number"
          step="any"
          placeholder="Например: 10.0"
          :error="error1 || undefined"
        />

        <div class="op-action-row">
          <BaseButton
            variant="danger"
            size="sm"
            :loading="specialStore.deleteByDefenseLoading"
            @click="handleDeleteByDefense"
          >
            Удалить существ
          </BaseButton>

          <div v-if="specialStore.deleteByDefenseResult !== null" class="result-badge">
            Удалено: <strong>{{ specialStore.deleteByDefenseResult }}</strong>
          </div>
        </div>
      </div>
    </div>

    <!-- Op 2: Avg Defense -->
    <div class="card op-card">
      <div class="op-header">
        <div class="op-icon bg-info-icon">
          <Calculator :size="20" />
        </div>
        <div>
          <h3 class="op-title">Средний уровень защиты</h3>
          <p class="op-desc">
            Вычисляет среднее арифметическое defenseLevel для всех существ в базе
          </p>
        </div>
      </div>

      <div class="op-body">
        <div class="calc-display">
          <span class="calc-label">Текущее среднее значение:</span>
          <span v-if="specialStore.avgDefenseResult !== null" class="calc-value">
            🛡️ {{ specialStore.avgDefenseResult.toFixed(2) }}
          </span>
          <span v-else class="calc-empty">Нажмите кнопку для расчета</span>
        </div>

        <div v-if="error2" class="error-text">⚠️ {{ error2 }}</div>

        <div class="op-action-row">
          <BaseButton
            variant="primary"
            size="sm"
            :loading="specialStore.avgDefenseLoading"
            @click="handleCalculateAvgDefense"
          >
            Рассчитать среднее
          </BaseButton>
        </div>
      </div>
    </div>

    <!-- Op 4: Take Rings from Hobbits -->
    <div class="card op-card">
      <div class="op-header">
        <div class="op-icon bg-warning-icon">
          <ShieldAlert :size="20" />
        </div>
        <div>
          <h3 class="op-title">Забрать все кольца у хоббитов</h3>
          <p class="op-desc">
            Снимает кольца со всех существ типа HOBBIT в системе (ring_id = NULL)
          </p>
        </div>
      </div>

      <div class="op-body">
        <p class="op-hint">Операция выполняется на уровне хранимой процедуры PostgreSQL.</p>

        <div v-if="error4" class="error-text">⚠️ {{ error4 }}</div>

        <div class="op-action-row">
          <BaseButton
            variant="secondary"
            size="sm"
            :loading="specialStore.takeRingsLoading"
            @click="handleTakeRingsFromHobbits"
          >
            💍 Отобрать кольца
          </BaseButton>

          <div v-if="specialStore.takeRingsResult !== null" class="result-badge">
            Обновлено хоббитов:
            <strong>{{ specialStore.takeRingsResult }}</strong>
          </div>
        </div>
      </div>
    </div>

    <!-- Op 5: Move Hobbits to Mordor -->
    <div class="card op-card">
      <div class="op-header">
        <div class="op-icon bg-danger-icon">
          <Sparkles :size="20" />
        </div>
        <div>
          <h3 class="op-title">Переместить хоббитов с кольцами в Мордор</h3>
          <p class="op-desc">
            Находит всех хоббитов с надетыми кольцами и меняет их город на Мордор
          </p>
        </div>
      </div>

      <div class="op-body">
        <p class="op-hint">
          Если город Мордор еще не создан, хранимая процедура создаст его автоматически.
        </p>

        <div v-if="error5" class="error-text">⚠️ {{ error5 }}</div>

        <div class="op-action-row">
          <BaseButton
            variant="danger"
            size="sm"
            :loading="specialStore.moveHobbitsLoading"
            @click="handleMoveHobbitsToMordor"
          >
            🌋 Отправить в Мордор
          </BaseButton>

          <div v-if="specialStore.moveHobbitsResult !== null" class="result-badge">
            Перемещено: <strong>{{ specialStore.moveHobbitsResult }}</strong>
          </div>
        </div>
      </div>
    </div>

    <!-- Op 3: Find Attack Less Than (Full Width Card with Table) -->
    <div class="card op-card full-card">
      <div class="op-header">
        <div class="op-icon bg-primary-icon">
          <Search :size="20" />
        </div>
        <div>
          <h3 class="op-title">Поиск существ с атакой меньше заданной</h3>
          <p class="op-desc">
            Возвращает массив существ, значение поля attackLevel которых меньше заданного порога
          </p>
        </div>
      </div>

      <div class="op-body">
        <form class="search-form-block" novalidate @submit.prevent="handleSearchAttackLessThan">
          <div class="search-input-box">
            <BaseInput
              v-model="maxAttackLevel"
              label="Максимальный порог attackLevel"
              type="number"
              step="any"
              placeholder="Например: 50.0"
              :error="error3 || undefined"
            />
          </div>

          <div class="op-action-row">
            <BaseButton
              variant="primary"
              size="sm"
              :loading="specialStore.attackLessThanLoading"
              type="submit"
            >
              <Search :size="14" />
              Найти существ
            </BaseButton>
          </div>
        </form>

        <!-- Results Table -->
        <div v-if="specialStore.attackLessThanResult !== null" class="search-results-section">
          <div class="results-header">
            <h4>Найдено существ: {{ totalSearchResults }}</h4>
            <div v-if="totalSearchResults > 0" class="page-size-selector">
              <span>Показывать по:</span>
              <div class="size-select-wrap">
                <BaseSelect
                  :model-value="searchPageSize"
                  :options="searchPageSizeOptions"
                  size="sm"
                  @update:model-value="
                    (val) => {
                      searchPageSize = Number(val);
                      onSearchPageSizeChange();
                    }
                  "
                />
              </div>
            </div>
          </div>

          <div class="table-container">
            <table class="data-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Имя</th>
                  <th>Тип</th>
                  <th>Координаты</th>
                  <th>Возраст</th>
                  <th>Атака</th>
                  <th>Защита</th>
                  <th>Город</th>
                  <th>Кольцо</th>
                </tr>
              </thead>
              <tbody>
                <tr v-if="totalSearchResults === 0">
                  <td colspan="9" class="empty-results">
                    Существ с атакой меньше {{ maxAttackLevel }} не найдено.
                  </td>
                </tr>
                <tr v-for="c in searchResultsList" :key="c.id">
                  <td>#{{ c.id }}</td>
                  <td>
                    <strong>{{ c.name }}</strong>
                  </td>
                  <td>
                    <BaseBadge :variant="getBadgeVariant(c.creatureType)" size="sm">
                      {{ getTypeLabel(c.creatureType) }}
                    </BaseBadge>
                  </td>
                  <td>({{ c.coordinates.x }}, {{ c.coordinates.y }})</td>
                  <td>{{ c.age }}</td>
                  <td class="stat-attack">⚔️ {{ c.attackLevel }}</td>
                  <td class="stat-defense">🛡️ {{ c.defenseLevel }}</td>
                  <td>
                    {{ c.creatureLocation ? c.creatureLocation.name : '—' }}
                  </td>
                  <td>
                    {{ c.ring ? `${c.ring.name} (${c.ring.power})` : '—' }}
                  </td>
                </tr>
              </tbody>
            </table>
          </div>

          <CreaturePagination
            v-if="totalSearchResults > 0"
            :page="searchPage"
            :size="searchPageSize"
            :total="totalSearchResults"
            @page-change="onSearchPageChange"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.special-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1.5rem;
}

@media (max-width: 900px) {
  .special-grid {
    grid-template-columns: 1fr;
  }
}

.full-card {
  grid-column: 1 / -1;
}

.op-card {
  display: flex;
  flex-direction: column;
  background: var(--bg-surface);
}

.op-header {
  display: flex;
  align-items: flex-start;
  gap: 0.85rem;
  padding-bottom: 1rem;
  border-bottom: 1px solid var(--border-color);
  margin-bottom: 1rem;
}

.op-icon {
  width: 42px;
  height: 42px;
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.bg-danger-icon {
  background: var(--danger-bg);
  color: var(--danger);
}

.bg-info-icon {
  background: var(--info-bg);
  color: var(--info);
}

.bg-warning-icon {
  background: var(--warning-bg);
  color: var(--warning);
}

.bg-primary-icon {
  background: var(--primary-bg);
  color: var(--primary);
}

.op-title {
  font-size: 1.05rem;
  font-weight: 600;
  color: var(--text-main);
  margin: 0;
}

.op-desc {
  font-size: 0.8rem;
  color: var(--text-muted);
  margin-top: 0.15rem;
}

.op-body {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.op-action-row {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.result-badge {
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  padding: 0.35rem 0.75rem;
  border-radius: var(--radius-sm);
  font-size: 0.85rem;
  color: var(--text-main);
}

.calc-display {
  background: var(--bg-card);
  padding: 0.85rem 1rem;
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: space-between;
  border: 1px solid var(--border-subtle);
}

.calc-label {
  font-size: 0.85rem;
  color: var(--text-muted);
}

.calc-value {
  font-size: 1.35rem;
  font-weight: 700;
  color: var(--success);
}

.calc-empty {
  font-size: 0.825rem;
  color: var(--text-dim);
  font-style: italic;
}

.op-hint {
  font-size: 0.825rem;
  color: var(--text-muted);
  line-height: 1.4;
}

.error-text {
  color: var(--danger);
  font-size: 0.8rem;
}

.search-form-block {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.search-input-box {
  max-width: 380px;
}

.search-results-section {
  margin-top: 1rem;
}

.results-header {
  margin-bottom: 0.75rem;
  font-size: 0.9rem;
  color: var(--text-muted);
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 0.75rem;
}

.page-size-selector {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.825rem;
  color: var(--text-muted);
}

.size-select-wrap {
  width: 70px;
}

.size-select-wrap :deep(.form-group) {
  margin-bottom: 0;
}

.empty-results {
  text-align: center;
  padding: 2rem !important;
  color: var(--text-dim);
}

.stat-attack {
  color: var(--danger);
  font-weight: 600;
}

.stat-defense {
  color: var(--success);
  font-weight: 600;
}
</style>
