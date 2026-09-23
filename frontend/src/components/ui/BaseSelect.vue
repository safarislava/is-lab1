<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue';
import { ChevronDown, Check } from 'lucide-vue-next';

export interface SelectOption {
  label: string;
  value: string | number | boolean | null;
}

const props = withDefaults(
  defineProps<{
    modelValue?: string | number | boolean | null;
    label?: string;
    options: SelectOption[];
    placeholder?: string;
    required?: boolean;
    error?: string;
    disabled?: boolean;
    size?: 'sm' | 'md';
  }>(),
  {
    modelValue: '',
    label: '',
    placeholder: 'Выберите...',
    required: false,
    error: '',
    disabled: false,
    size: 'md',
  },
);

const emit = defineEmits<{
  (e: 'update:modelValue', value: string | number | boolean | null): void;
}>();

const isOpen = ref(false);
const selectRef = ref<HTMLElement | null>(null);

const selectedOption = computed(() => {
  return props.options.find((opt) => opt.value === props.modelValue);
});

const displayLabel = computed(() => {
  if (selectedOption.value) {
    return selectedOption.value.label;
  }
  return props.placeholder || 'Выберите...';
});

const isPlaceholder = computed(() => {
  return !selectedOption.value;
});

function toggleDropdown() {
  if (props.disabled) return;
  isOpen.value = !isOpen.value;
}

function selectOption(opt: SelectOption) {
  emit('update:modelValue', opt.value);
  isOpen.value = false;
}

function onClickOutside(event: MouseEvent) {
  if (selectRef.value && !selectRef.value.contains(event.target as Node)) {
    isOpen.value = false;
  }
}

function onKeydown(e: KeyboardEvent) {
  if (props.disabled) return;
  if (e.key === 'Escape') {
    isOpen.value = false;
  } else if (e.key === 'Enter' || e.key === ' ') {
    e.preventDefault();
    isOpen.value = !isOpen.value;
  } else if (e.key === 'ArrowDown') {
    e.preventDefault();
    if (!isOpen.value) {
      isOpen.value = true;
    }
  }
}

onMounted(() => {
  document.addEventListener('click', onClickOutside);
});

onUnmounted(() => {
  document.removeEventListener('click', onClickOutside);
});
</script>

<template>
  <div ref="selectRef" class="form-group custom-select-group">
    <label v-if="label" class="form-label" @click="toggleDropdown">
      {{ label }}
      <span v-if="required" class="required">*</span>
    </label>

    <div class="select-trigger-wrapper">
      <button
        type="button"
        :class="[
          'custom-select-trigger',
          {
            'is-open': isOpen,
            'has-error': !!error,
            'is-placeholder': isPlaceholder,
            'size-sm': size === 'sm',
          },
        ]"
        :disabled="disabled"
        aria-haspopup="listbox"
        :aria-expanded="isOpen"
        @click="toggleDropdown"
        @keydown="onKeydown"
      >
        <span class="trigger-text">{{ displayLabel }}</span>
        <ChevronDown :size="14" :class="['trigger-arrow', { 'arrow-up': isOpen }]" />
      </button>

      <Transition name="select-dropdown">
        <ul v-if="isOpen" class="custom-select-menu" role="listbox">
          <li
            v-for="opt in options"
            :key="String(opt.value)"
            :class="[
              'custom-select-option',
              {
                'is-selected': opt.value === modelValue,
              },
            ]"
            role="option"
            :aria-selected="opt.value === modelValue"
            @click.stop="selectOption(opt)"
          >
            <span class="option-label">{{ opt.label }}</span>
            <Check v-if="opt.value === modelValue" :size="14" class="option-check" />
          </li>
        </ul>
      </Transition>
    </div>

    <span v-if="error" class="form-error">{{ error }}</span>
  </div>
</template>

<style scoped>
.custom-select-group {
  position: relative;
}

.select-trigger-wrapper {
  position: relative;
  width: 100%;
}

.custom-select-trigger {
  width: 100%;
  height: 32px;
  background: var(--bg-input);
  border: 1px solid var(--border-input);
  border-radius: var(--radius-sm);
  color: var(--text-main);
  padding: 0 0.75rem;
  font-size: 13px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 0.5rem;
  cursor: pointer;
  text-align: left;
  user-select: none;
  transition:
    border-color 0.12s ease,
    box-shadow 0.12s ease,
    background-color 0.12s ease;
}

.custom-select-trigger.size-sm {
  height: 28px;
  padding: 0 0.6rem;
  font-size: 12px;
}

.custom-select-trigger:focus-visible,
.custom-select-trigger.is-open {
  outline: none;
  border-color: var(--border-focus);
  box-shadow: 0 0 0 2px rgba(53, 116, 240, 0.35);
}

.custom-select-trigger.has-error {
  border-color: var(--danger);
}

.custom-select-trigger:disabled {
  opacity: 0.45;
  cursor: not-allowed;
  background: var(--bg-well);
}

.trigger-text {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  flex: 1;
}

.is-placeholder .trigger-text {
  color: var(--text-dim);
}

.trigger-arrow {
  color: var(--text-muted);
  flex-shrink: 0;
  transition: transform 0.15s ease;
}

.trigger-arrow.arrow-up {
  transform: rotate(180deg);
  color: var(--primary);
}

.custom-select-menu {
  position: absolute;
  top: calc(100% + 3px);
  left: 0;
  right: 0;
  z-index: 1200;
  background: var(--bg-surface);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-sm);
  box-shadow: var(--shadow-lg);
  max-height: 220px;
  overflow-y: auto;
  list-style: none;
  padding: 0.25rem 0;
  margin: 0;
}

.custom-select-option {
  padding: 0.45rem 0.75rem;
  font-size: 13px;
  color: var(--text-main);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 0.5rem;
  transition: background-color 0.1s ease;
  user-select: none;
}

.custom-select-option:hover {
  background: var(--bg-card-hover);
}

.custom-select-option.is-selected {
  background: var(--primary-bg);
  color: #589df6;
  font-weight: 500;
}

.option-label {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.option-check {
  color: var(--primary);
  flex-shrink: 0;
}

.select-dropdown-enter-active,
.select-dropdown-leave-active {
  transition:
    opacity 0.12s ease,
    transform 0.12s ease;
}

.select-dropdown-enter-from,
.select-dropdown-leave-to {
  opacity: 0;
  transform: translateY(-4px);
}
</style>
