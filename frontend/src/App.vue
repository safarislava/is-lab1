<script setup lang="ts">
import { ref } from 'vue';

const title = ref('Information System Lab 1');
</script>

<template>
  <main class="container">
    <h1>{{ title }}</h1>
  </main>
</template>

<style scoped>
.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
  font-family:
    system-ui,
    -apple-system,
    sans-serif;
}
</style>
