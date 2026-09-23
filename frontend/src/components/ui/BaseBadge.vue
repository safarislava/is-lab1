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
  gap: 0.3rem;
  font-weight: 600;
  border-radius: var(--radius-full);
  letter-spacing: 0.02em;
  text-transform: uppercase;
}

.badge-sm {
  padding: 0.15rem 0.5rem;
  font-size: 0.675rem;
}

.badge-md {
  padding: 0.25rem 0.65rem;
  font-size: 0.75rem;
}

.badge-hobbit {
  background: rgba(16, 185, 129, 0.18);
  color: #34d399;
  border: 1px solid rgba(16, 185, 129, 0.35);
}

.badge-elf {
  background: rgba(168, 85, 247, 0.18);
  color: #c084fc;
  border: 1px solid rgba(168, 85, 247, 0.35);
}

.badge-human {
  background: rgba(56, 189, 248, 0.18);
  color: #38bdf8;
  border: 1px solid rgba(56, 189, 248, 0.35);
}

.badge-gollum {
  background: rgba(148, 163, 184, 0.18);
  color: #cbd5e1;
  border: 1px solid rgba(148, 163, 184, 0.35);
}

.badge-orc {
  background: rgba(244, 63, 94, 0.18);
  color: #fb7185;
  border: 1px solid rgba(244, 63, 94, 0.35);
}

.badge-primary {
  background: var(--primary-bg);
  color: var(--primary);
  border: 1px solid var(--primary);
}

.badge-success {
  background: var(--success-bg);
  color: var(--success);
  border: 1px solid var(--success);
}

.badge-warning {
  background: var(--warning-bg);
  color: var(--warning);
  border: 1px solid var(--warning);
}

.badge-danger {
  background: var(--danger-bg);
  color: var(--danger);
  border: 1px solid var(--danger);
}

.badge-info {
  background: var(--info-bg);
  color: var(--info);
  border: 1px solid var(--info);
}

.badge-neutral {
  background: var(--bg-card-hover);
  color: var(--text-muted);
  border: 1px solid var(--border-color);
}
</style>
