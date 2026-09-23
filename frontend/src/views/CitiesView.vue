<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useCitiesStore } from '@/stores/citiesStore';
import type { MagicCityResponse } from '@/types/city';
import CityTable from '@/components/cities/CityTable.vue';
import CityFormDialog from '@/components/cities/CityFormDialog.vue';
import CityDeleteDialog from '@/components/cities/CityDeleteDialog.vue';
import BaseButton from '@/components/ui/BaseButton.vue';
import { Plus } from 'lucide-vue-next';

const citiesStore = useCitiesStore();

const isFormOpen = ref(false);
const isDeleteOpen = ref(false);
const activeCity = ref<MagicCityResponse | null>(null);

onMounted(() => {
  citiesStore.fetchCities().catch(() => {});
});

function openCreateDialog() {
  activeCity.value = null;
  isFormOpen.value = true;
}

function openEditDialog(city: MagicCityResponse) {
  activeCity.value = city;
  isFormOpen.value = true;
}

function openDeleteDialog(city: MagicCityResponse) {
  activeCity.value = city;
  isDeleteOpen.value = true;
}
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <div>
        <h1 class="page-title"><span>🏙️</span> Волшебные города</h1>
        <p class="page-subtitle">
          Управление волшебными локациями, столицами, населением и губернаторами
        </p>
      </div>

      <BaseButton variant="primary" @click="openCreateDialog">
        <Plus :size="16" />
        Создать город
      </BaseButton>
    </div>

    <CityTable :cities="citiesStore.cities" :loading="citiesStore.loading" @edit="openEditDialog" />

    <!-- Dialogs -->
    <CityFormDialog
      :is-open="isFormOpen"
      :city="activeCity"
      @close="isFormOpen = false"
      @saved="() => {}"
      @delete="openDeleteDialog"
    />

    <CityDeleteDialog
      :is-open="isDeleteOpen"
      :city="activeCity"
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
