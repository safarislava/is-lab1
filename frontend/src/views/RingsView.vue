<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRingsStore } from '@/stores/ringsStore';
import type { RingResponse } from '@/types/ring';
import RingTable from '@/components/rings/RingTable.vue';
import RingFormDialog from '@/components/rings/RingFormDialog.vue';
import RingDeleteDialog from '@/components/rings/RingDeleteDialog.vue';
import BaseButton from '@/components/ui/BaseButton.vue';
import { Plus } from 'lucide-vue-next';

const ringsStore = useRingsStore();

const isFormOpen = ref(false);
const isDeleteOpen = ref(false);
const activeRing = ref<RingResponse | null>(null);

onMounted(() => {
  ringsStore.fetchRings().catch(() => {});
});

function openCreateDialog() {
  activeRing.value = null;
  isFormOpen.value = true;
}

function openEditDialog(ring: RingResponse) {
  activeRing.value = ring;
  isFormOpen.value = true;
}

function openDeleteDialog(ring: RingResponse) {
  activeRing.value = ring;
  isDeleteOpen.value = true;
}
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <div>
        <h1 class="page-title"><span>💍</span> Магические кольца</h1>
      </div>

      <BaseButton variant="primary" @click="openCreateDialog">
        <Plus :size="16" />
        Создать кольцо
      </BaseButton>
    </div>

    <RingTable :rings="ringsStore.rings" :loading="ringsStore.loading" @edit="openEditDialog" />

    <!-- Dialogs -->
    <RingFormDialog
      :is-open="isFormOpen"
      :ring="activeRing"
      @close="isFormOpen = false"
      @saved="() => {}"
      @delete="openDeleteDialog"
    />

    <RingDeleteDialog
      :is-open="isDeleteOpen"
      :ring="activeRing"
      @close="isDeleteOpen = false"
      @deleted="() => {}"
    />
  </div>
</template>

<style scoped>
.page-container {
  display: flex;
  flex-direction: column;
}
</style>
