import { defineStore } from 'pinia';
import { ref } from 'vue';
import { creaturesApi } from '@/api/creaturesApi';
import type { BookCreatureResponse } from '@/types/creature';
import { useNotificationStore } from './notificationStore';

export const useSpecialOperationsStore = defineStore('specialOperations', () => {
  const notificationStore = useNotificationStore();

  // Operation 1: Delete by defense level
  const deleteByDefenseLoading = ref(false);
  const deleteByDefenseResult = ref<number | null>(null);

  // Operation 2: Average defense level
  const avgDefenseLoading = ref(false);
  const avgDefenseResult = ref<number | null>(null);

  // Operation 3: Attack less than
  const attackLessThanLoading = ref(false);
  const attackLessThanResult = ref<BookCreatureResponse[] | null>(null);

  // Operation 4: Take rings from hobbits
  const takeRingsLoading = ref(false);
  const takeRingsResult = ref<number | null>(null);

  // Operation 5: Move hobbits to Mordor
  const moveHobbitsLoading = ref(false);
  const moveHobbitsResult = ref<number | null>(null);

  async function deleteByDefense(defenseLevel: number): Promise<number> {
    deleteByDefenseLoading.value = true;
    try {
      const res = await creaturesApi.deleteByDefenseLevel(defenseLevel);
      deleteByDefenseResult.value = res.count;
      notificationStore.addToast({
        type: 'success',
        title: 'Операция выполнена',
        message: `Удалено существ с защитой ${defenseLevel}: ${res.count}`,
      });
      return res.count;
    } finally {
      deleteByDefenseLoading.value = false;
    }
  }

  async function calculateAvgDefense(): Promise<number> {
    avgDefenseLoading.value = true;
    try {
      const res = await creaturesApi.calculateAverageDefenseLevel();
      avgDefenseResult.value = res.average;
      return res.average;
    } finally {
      avgDefenseLoading.value = false;
    }
  }

  async function searchAttackLessThan(maxAttackLevel: number): Promise<BookCreatureResponse[]> {
    attackLessThanLoading.value = true;
    try {
      const res = await creaturesApi.findCreaturesWithAttackLevelLessThan(maxAttackLevel);
      attackLessThanResult.value = res;
      return res;
    } finally {
      attackLessThanLoading.value = false;
    }
  }

  async function takeRingsFromHobbits(): Promise<number> {
    takeRingsLoading.value = true;
    try {
      const res = await creaturesApi.takeAllRingsFromHobbits();
      takeRingsResult.value = res.count;
      notificationStore.addToast({
        type: 'success',
        title: 'Кольца отобраны',
        message: `Успешно отобрано колец у хоббитов: ${res.count}`,
      });
      return res.count;
    } finally {
      takeRingsLoading.value = false;
    }
  }

  async function moveHobbitsToMordor(): Promise<number> {
    moveHobbitsLoading.value = true;
    try {
      const res = await creaturesApi.moveHobbitsWithRingsToMordor();
      moveHobbitsResult.value = res.count;
      notificationStore.addToast({
        type: 'success',
        title: 'Перемещение в Мордор',
        message: `Хоббитов с кольцами перемещено в Мордор: ${res.count}`,
      });
      return res.count;
    } finally {
      moveHobbitsLoading.value = false;
    }
  }

  return {
    deleteByDefenseLoading,
    deleteByDefenseResult,
    avgDefenseLoading,
    avgDefenseResult,
    attackLessThanLoading,
    attackLessThanResult,
    takeRingsLoading,
    takeRingsResult,
    moveHobbitsLoading,
    moveHobbitsResult,
    deleteByDefense,
    calculateAvgDefense,
    searchAttackLessThan,
    takeRingsFromHobbits,
    moveHobbitsToMordor,
  };
});
