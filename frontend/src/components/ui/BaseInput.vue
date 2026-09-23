<script setup lang="ts">
import { computed } from 'vue';

const props = withDefaults(
  defineProps<{
    modelValue?: string | number | null;
    label?: string;
    type?: string;
    placeholder?: string;
    required?: boolean;
    error?: string;
    helpText?: string;
    min?: number | string;
    max?: number | string;
    step?: number | string;
    disabled?: boolean;
  }>(),
  {
    modelValue: '',
    label: '',
    type: 'text',
    placeholder: '',
    required: false,
    error: '',
    helpText: '',
    min: undefined,
    max: undefined,
    step: undefined,
    disabled: false,
  },
);

const computedStep = computed(() => {
  if (props.step !== undefined) return props.step;
  if (props.type === 'number') return 'any';
  return undefined;
});

const emit = defineEmits<{
  (e: 'update:modelValue', value: string | number | null): void;
}>();

function onInput(event: Event) {
  const target = event.target as HTMLInputElement;
  if (props.type === 'number') {
    emit('update:modelValue', target.value === '' ? null : Number(target.value));
  } else {
    emit('update:modelValue', target.value);
  }
}
</script>

<template>
  <div class="form-group">
    <label v-if="label" class="form-label">
      {{ label }}
      <span v-if="required" class="required">*</span>
    </label>
    <input
      :type="type"
      :value="modelValue ?? ''"
      :placeholder="placeholder"
      :required="required"
      :min="min"
      :max="max"
      :step="computedStep"
      :disabled="disabled"
      :class="['form-input', { 'has-error': !!error }]"
      @input="onInput"
    />
    <span v-if="error" class="form-error">{{ error }}</span>
    <span v-else-if="helpText" class="form-help">{{ helpText }}</span>
  </div>
</template>

<style scoped>
.form-help {
  font-size: 0.775rem;
  color: var(--text-dim);
  margin-top: 0.2rem;
}
</style>
