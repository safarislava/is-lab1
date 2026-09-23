<script setup lang="ts">
export type BadgeVariant =
  | 'hobbit'
  | 'elf'
  | 'human'
  | 'gollum'
  | 'orc'
  | 'primary'
  | 'success'
  | 'warning'
  | 'danger'
  | 'info'
  | 'neutral';

withDefaults(
  defineProps<{
    variant?: BadgeVariant;
    size?: 'sm' | 'md';
  }>(),
  {
    variant: 'neutral',
    size: 'md',
  },
);
</script>

<template>
  <span :class="['badge', `badge-${variant}`, `badge-${size}`]">
    <slot />
  </span>
</template>

<style scoped>
.badge {
  display: inline-flex;
  align-items: center;
  gap: 0.25rem;
  font-weight: 500;
  border-radius: var(--radius-xs);
  letter-spacing: 0.01em;
  font-size: 11.5px;
  line-height: 1.2;
}

.badge-sm {
  padding: 0.15rem 0.45rem;
  font-size: 11px;
}

.badge-md {
  padding: 0.2rem 0.55rem;
  font-size: 12px;
}

.badge-hobbit {
  background: rgba(89, 168, 105, 0.15);
  color: #59a869;
  border: 1px solid rgba(89, 168, 105, 0.35);
}

.badge-elf {
  background: rgba(199, 125, 187, 0.15);
  color: #c77dbb;
  border: 1px solid rgba(199, 125, 187, 0.35);
}

.badge-human {
  background: rgba(86, 182, 194, 0.15);
  color: #56b6c2;
  border: 1px solid rgba(86, 182, 194, 0.35);
}

.badge-gollum {
  background: rgba(140, 146, 158, 0.15);
  color: #8c929e;
  border: 1px solid rgba(140, 146, 158, 0.35);
}

.badge-orc {
  background: rgba(229, 83, 75, 0.15);
  color: #e5534b;
  border: 1px solid rgba(229, 83, 75, 0.35);
}

.badge-primary {
  background: var(--primary-bg);
  color: #589df6;
  border: 1px solid rgba(53, 116, 240, 0.4);
}

.badge-success {
  background: var(--success-bg);
  color: #59a869;
  border: 1px solid rgba(89, 168, 105, 0.4);
}

.badge-warning {
  background: var(--warning-bg);
  color: #f2c55c;
  border: 1px solid rgba(242, 197, 92, 0.4);
}

.badge-danger {
  background: var(--danger-bg);
  color: #e5534b;
  border: 1px solid rgba(229, 83, 75, 0.4);
}

.badge-info {
  background: var(--info-bg);
  color: #56b6c2;
  border: 1px solid rgba(86, 182, 194, 0.4);
}

.badge-neutral {
  background: var(--bg-well);
  color: var(--text-muted);
  border: 1px solid var(--border-color);
}
</style>
