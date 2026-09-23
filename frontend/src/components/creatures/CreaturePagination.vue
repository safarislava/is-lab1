<script setup lang="ts">
import { computed } from 'vue';
import { ChevronLeft, ChevronRight, ChevronsLeft, ChevronsRight } from 'lucide-vue-next';

const props = defineProps<{
  page: number; // 0-indexed
  size: number;
  total: number;
}>();

const emit = defineEmits<{
  (e: 'pageChange', page: number): void;
}>();

const totalPages = computed(() => Math.max(1, Math.ceil(props.total / props.size)));
const currentPage1 = computed(() => props.page + 1);

const startRecord = computed(() => (props.total === 0 ? 0 : props.page * props.size + 1));
const endRecord = computed(() => Math.min(props.total, (props.page + 1) * props.size));

const visiblePages = computed(() => {
  const current = currentPage1.value;
  const last = totalPages.value;
  const delta = 2;
  const range: (number | string)[] = [];

  for (let i = Math.max(2, current - delta); i <= Math.min(last - 1, current + delta); i++) {
    range.push(i);
  }

  if (current - delta > 2) {
    range.unshift('...');
  }
  range.unshift(1);

  if (current + delta < last - 1) {
    range.push('...');
  }
  if (last > 1) {
    range.push(last);
  }

  return range;
});

function goToPage(p: number) {
  if (p >= 0 && p < totalPages.value && p !== props.page) {
    emit('pageChange', p);
  }
}
</script>

<template>
  <div class="pagination-wrapper">
    <div class="pagination-info">
      Показано <span class="highlight">{{ startRecord }}-{{ endRecord }}</span> из
      <span class="highlight">{{ total }}</span> записей
    </div>

    <div class="pagination-controls">
      <button class="page-btn" :disabled="page === 0" title="Первая страница" @click="goToPage(0)">
        <ChevronsLeft :size="16" />
      </button>

      <button
        class="page-btn"
        :disabled="page === 0"
        title="Предыдущая"
        @click="goToPage(page - 1)"
      >
        <ChevronLeft :size="16" />
      </button>

      <template v-for="(p, idx) in visiblePages" :key="idx">
        <span v-if="p === '...'" class="page-ellipsis">...</span>
        <button
          v-else
          :class="['page-btn page-number', { active: Number(p) === currentPage1 }]"
          @click="goToPage(Number(p) - 1)"
        >
          {{ p }}
        </button>
      </template>

      <button
        class="page-btn"
        :disabled="page >= totalPages - 1"
        title="Следующая"
        @click="goToPage(page + 1)"
      >
        <ChevronRight :size="16" />
      </button>

      <button
        class="page-btn"
        :disabled="page >= totalPages - 1"
        title="Последняя страница"
        @click="goToPage(totalPages - 1)"
      >
        <ChevronsRight :size="16" />
      </button>
    </div>
  </div>
</template>

<style scoped>
.pagination-wrapper {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1rem 0.5rem 0.5rem 0.5rem;
  flex-wrap: wrap;
  gap: 1rem;
}

.pagination-info {
  font-size: 0.85rem;
  color: var(--text-muted);
}

.highlight {
  color: var(--text-main);
  font-weight: 600;
}

.pagination-controls {
  display: flex;
  align-items: center;
  gap: 0.35rem;
}

.page-btn {
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  color: var(--text-muted);
  min-width: 32px;
  height: 32px;
  padding: 0 0.4rem;
  border-radius: var(--radius-sm);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 0.825rem;
  font-weight: 500;
  transition: all 0.15s ease;
}

.page-btn:hover:not(:disabled) {
  background: var(--bg-card-hover);
  color: var(--text-main);
  border-color: var(--text-dim);
}

.page-btn:disabled {
  opacity: 0.35;
  cursor: not-allowed;
}

.page-number.active {
  background: var(--primary);
  color: #ffffff;
  border-color: var(--primary);
  font-weight: 600;
}

.page-ellipsis {
  color: var(--text-dim);
  padding: 0 0.35rem;
  font-size: 0.85rem;
}
</style>
