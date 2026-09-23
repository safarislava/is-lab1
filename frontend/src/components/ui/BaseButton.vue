<script setup lang="ts">
withDefaults(
  defineProps<{
    variant?: 'primary' | 'secondary' | 'danger' | 'ghost';
    size?: 'sm' | 'md' | 'lg';
    loading?: boolean;
    disabled?: boolean;
    type?: 'button' | 'submit' | 'reset';
  }>(),
  {
    variant: 'secondary',
    size: 'md',
    loading: false,
    disabled: false,
    type: 'button',
  },
);
</script>

<template>
  <button
    :type="type"
    :class="['btn', `btn-${variant}`, size === 'sm' ? 'btn-sm' : size === 'lg' ? 'btn-lg' : '']"
    :disabled="disabled || loading"
  >
    <span v-if="loading" class="spinner" />
    <slot />
  </button>
</template>

<style scoped>
.btn-lg {
  padding: 0.75rem 1.4rem;
  font-size: 1rem;
}
</style>
