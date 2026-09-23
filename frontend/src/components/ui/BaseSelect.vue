<script setup lang="ts">
interface SelectOption {
  label: string;
  value: string | number | boolean | null;
}

withDefaults(
  defineProps<{
    modelValue?: string | number | boolean | null;
    label?: string;
    options: SelectOption[];
    placeholder?: string;
    required?: boolean;
    error?: string;
    disabled?: boolean;
  }>(),
  {
    modelValue: '',
    label: '',
    placeholder: 'Выберите...',
    required: false,
    error: '',
    disabled: false,
  },
);

const emit = defineEmits<{
  (e: 'update:modelValue', value: string | number | boolean | null): void;
}>();

function onChange(event: Event) {
  const target = event.target as HTMLSelectElement;
  const val = target.value;
  if (val === '__NULL__') {
    emit('update:modelValue', null);
  } else if (val === 'true') {
    emit('update:modelValue', true);
  } else if (val === 'false') {
    emit('update:modelValue', false);
  } else if (!isNaN(Number(val)) && val !== '') {
    emit('update:modelValue', Number(val));
  } else {
    emit('update:modelValue', val);
  }
}
</script>

<template>
  <div class="form-group">
    <label v-if="label" class="form-label">
      {{ label }}
      <span v-if="required" class="required">*</span>
    </label>
    <select
      :value="modelValue === null ? '__NULL__' : modelValue"
      :disabled="disabled"
      :class="['form-select', { 'has-error': !!error }]"
      @change="onChange"
    >
      <option v-if="placeholder" value="" disabled>
        {{ placeholder }}
      </option>
      <option
        v-for="opt in options"
        :key="String(opt.value)"
        :value="opt.value === null ? '__NULL__' : opt.value"
      >
        {{ opt.label }}
      </option>
    </select>
    <span v-if="error" class="form-error">{{ error }}</span>
  </div>
</template>
