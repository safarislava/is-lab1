<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useCreaturesStore } from '@/stores/creaturesStore';
import type { BookCreatureResponse } from '@/types/creature';
import CreatureTable from '@/components/creatures/CreatureTable.vue';
import CreatureFilters from '@/components/creatures/CreatureFilters.vue';
import CreaturePagination from '@/components/creatures/CreaturePagination.vue';
import CreatureFormDialog from '@/components/creatures/CreatureFormDialog.vue';
import CreatureDetailsDialog from '@/components/creatures/CreatureDetailsDialog.vue';
import CreatureDeleteDialog from '@/components/creatures/CreatureDeleteDialog.vue';
import BaseButton from '@/components/ui/BaseButton.vue';
import { Plus, Upload } from 'lucide-vue-next';

const router = useRouter();
const creaturesStore = useCreaturesStore();

const isFormOpen = ref(false);
const isDetailsOpen = ref(false);
const isDeleteOpen = ref(false);

const activeCreature = ref<BookCreatureResponse | null>(null);

onMounted(() => {
  creaturesStore.fetchCreatures().catch(() => {});
});

function openCreateDialog() {
  activeCreature.value = null;
  isFormOpen.value = true;
}

function openViewDialog(creature: BookCreatureResponse) {
  activeCreature.value = creature;
  isDetailsOpen.value = true;
}

function openEditDialog(creature: BookCreatureResponse) {
  activeCreature.value = creature;
  isFormOpen.value = true;
}

function openDeleteDialog(creature: BookCreatureResponse) {
  activeCreature.value = creature;
  isDeleteOpen.value = true;
}

function onSort(col: string) {
  creaturesStore.toggleSort(col);
}

function onPageChange(newPage: number) {
  creaturesStore.setPage(newPage);
}
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h1 class="page-title"><span>✨</span> Книжные существа</h1>
      <div class="header-buttons">
        <BaseButton variant="secondary" @click="router.push('/import')">
          <Upload :size="16" />
          Импорт
        </BaseButton>
        <BaseButton variant="primary" @click="openCreateDialog">
          <Plus :size="16" />
          Создать существо
        </BaseButton>
      </div>
    </div>

    <CreatureFilters />

    <div class="table-view-wrapper">
      <CreatureTable
        :creatures="creaturesStore.creatures"
        :loading="creaturesStore.loading"
        :sort-by="creaturesStore.sortBy"
        :sort-direction="creaturesStore.sortDirection"
        @sort="onSort"
        @view="openViewDialog"
        @edit="openEditDialog"
        @delete="openDeleteDialog"
      />

      <CreaturePagination
        :page="creaturesStore.page"
        :size="creaturesStore.size"
        :total="creaturesStore.total"
        @page-change="onPageChange"
      />
    </div>

    <CreatureFormDialog
      :is-open="isFormOpen"
      :creature="activeCreature"
      @close="isFormOpen = false"
      @saved="() => {}"
    />

    <CreatureDetailsDialog
      :is-open="isDetailsOpen"
      :creature="activeCreature"
      @close="isDetailsOpen = false"
      @edit="openEditDialog"
      @delete="openDeleteDialog"
    />

    <CreatureDeleteDialog
      :is-open="isDeleteOpen"
      :creature="activeCreature"
      @close="isDeleteOpen = false"
      @deleted="() => {}"
    />
  </div>
</template>

<style scoped>
.page-container {
  display: flex;
  flex-direction: column;
  width: 100%;
  min-width: 0;
}

.table-view-wrapper {
  width: 100%;
  min-width: 0;
}

.header-buttons {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}
</style>
