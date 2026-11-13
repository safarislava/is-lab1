<script setup lang="ts">
import { ref, computed } from 'vue';
import { useImportStore } from '@/stores/importStore';
import { SAMPLE_JSON } from '@/utils/importParser';
import BaseButton from '@/components/ui/BaseButton.vue';
import BaseBadge, { type BadgeVariant } from '@/components/ui/BaseBadge.vue';
import {
  Upload,
  FileUp,
  FileText,
  AlertTriangle,
  Sparkles,
  Building2,
  CircleDot,
  Trash2,
  Download,
  RotateCcw,
} from 'lucide-vue-next';

const importStore = useImportStore();

const activeTab = ref<'creatures' | 'cities' | 'rings'>('creatures');
const fileInput = ref<HTMLInputElement | null>(null);
const isDragging = ref(false);

const previewCreatures = computed(() => importStore.parsedRequest?.creatures ?? []);
const previewCities = computed(() => importStore.parsedRequest?.cities ?? []);
const previewRings = computed(() => importStore.parsedRequest?.rings ?? []);

function triggerFilePicker() {
  fileInput.value?.click();
}

function onFileSelected(event: Event) {
  const target = event.target as HTMLInputElement;
  const file = target.files?.[0];
  if (file) {
    readFile(file);
  }
  target.value = '';
}

function onDragOver(e: DragEvent) {
  e.preventDefault();
  isDragging.value = true;
}

function onDragLeave(e: DragEvent) {
  e.preventDefault();
  isDragging.value = false;
}

function onDrop(e: DragEvent) {
  e.preventDefault();
  isDragging.value = false;
  const file = e.dataTransfer?.files?.[0];
  if (file) {
    readFile(file);
  }
}

function readFile(file: File) {
  const reader = new FileReader();
  reader.onload = (e) => {
    const text = String(e.target?.result ?? '');
    importStore.setContent(text, file.name, file.size);
  };
  reader.readAsText(file);
}

function onContentChange(e: Event) {
  const target = e.target as HTMLTextAreaElement;
  importStore.setContent(target.value);
}

function downloadSample() {
  const sample = importStore.rawContent.trim() || SAMPLE_JSON;
  const blob = new Blob([sample], { type: 'application/json;charset=utf-8' });
  const url = URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = 'import_template.json';
  a.click();
  URL.revokeObjectURL(url);
}

async function handleImport() {
  try {
    await importStore.executeImport();
  } catch (err) {
    void err;
  }
}

function formatBytes(bytes: number | null): string {
  if (!bytes) return '';
  if (bytes < 1024) return `${bytes} B`;
  return `${(bytes / 1024).toFixed(1)} KB`;
}

function getBadgeVariant(type: string): BadgeVariant {
  const map: Record<string, BadgeVariant> = {
    HOBBIT: 'hobbit',
    ELF: 'elf',
    HUMAN: 'human',
    GOLLUM: 'gollum',
    ORC: 'orc',
  };
  return map[type] || 'neutral';
}

function getTypeLabel(type: string): string {
  const map: Record<string, string> = {
    HOBBIT: 'Хоббит',
    ELF: 'Эльф',
    HUMAN: 'Человек',
    GOLLUM: 'Голлум',
    ORC: 'Орк',
  };
  return map[type] || type;
}
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <div>
        <h1 class="page-title">
          <Upload :size="20" class="page-title-icon" />
          Импорт объектов
        </h1>
      </div>

      <div class="header-actions">
        <BaseButton variant="secondary" size="sm" @click="downloadSample">
          <Download :size="14" />
          Скачать шаблон
        </BaseButton>
        <BaseButton variant="ghost" size="sm" @click="importStore.reset()">
          <Trash2 :size="14" />
          Очистить
        </BaseButton>
      </div>
    </div>

    <div v-if="importStore.serverError" class="server-error-callout">
      <div class="error-callout-icon">
        <AlertTriangle :size="22" />
      </div>
      <div class="error-callout-body">
        <div class="error-title">Ошибка импорта</div>
        <div class="error-message">
          {{ importStore.serverError }}
        </div>
        <ul v-if="importStore.serverErrorDetails.length > 0" class="error-details-list">
          <li v-for="(detail, idx) in importStore.serverErrorDetails" :key="idx">
            {{ detail }}
          </li>
        </ul>
      </div>
    </div>

    <div class="import-layout">
      <div class="editor-column card">
        <div class="column-header">
          <div class="column-title-wrap">
            <FileText :size="16" class="text-muted" />
            <span class="column-title">JSON файл</span>
            <span v-if="importStore.fileName" class="file-chip">
              {{ importStore.fileName }} ({{ formatBytes(importStore.fileSize) }})
            </span>
          </div>

          <BaseButton variant="ghost" size="sm" @click="importStore.loadSample()">
            Загрузить шаблон
          </BaseButton>
        </div>

        <div
          class="dropzone"
          :class="{ 'is-dragging': isDragging }"
          @dragover="onDragOver"
          @dragleave="onDragLeave"
          @drop="onDrop"
          @click="triggerFilePicker"
        >
          <input
            ref="fileInput"
            type="file"
            accept=".json"
            class="hidden-file-input"
            @change="onFileSelected"
          />
          <FileUp :size="28" class="dropzone-icon" />
          <div class="dropzone-text">
            <strong>Перетащите JSON файл сюда</strong> или
            <span class="browse-link">выберите на компьютере</span>
          </div>
        </div>

        <div class="editor-wrapper">
          <div class="editor-toolbar">
            <span class="editor-length">{{ importStore.rawContent.length }} симв.</span>
          </div>
          <textarea
            class="raw-textarea"
            :value="importStore.rawContent"
            placeholder="Вставьте JSON содержимое..."
            spellcheck="false"
            @input="onContentChange"
          ></textarea>
        </div>
      </div>

      <div class="preview-column card">
        <div class="column-header">
          <div class="column-title-wrap">
            <Sparkles :size="16" class="text-muted" />
            <span class="column-title">Предпросмотр</span>
          </div>

          <BaseButton variant="ghost" size="sm" @click="importStore.validate()">
            <RotateCcw :size="12" />
            Обновить
          </BaseButton>
        </div>

        <div v-if="importStore.validationErrors.length > 0" class="validation-errors-box">
          <div class="validation-header">
            <AlertTriangle :size="16" class="text-danger" />
            <strong>Ошибка в JSON</strong>
          </div>
          <ul class="validation-list">
            <li v-for="(err, idx) in importStore.validationErrors" :key="idx">
              {{ err }}
            </li>
          </ul>
        </div>

        <div class="preview-tabs">
          <button
            type="button"
            :class="['preview-tab-btn', { active: activeTab === 'creatures' }]"
            @click="activeTab = 'creatures'"
          >
            <Sparkles :size="14" />
            Существа ({{ previewCreatures.length }})
          </button>
          <button
            type="button"
            :class="['preview-tab-btn', { active: activeTab === 'cities' }]"
            @click="activeTab = 'cities'"
          >
            <Building2 :size="14" />
            Города ({{ previewCities.length }})
          </button>
          <button
            type="button"
            :class="['preview-tab-btn', { active: activeTab === 'rings' }]"
            @click="activeTab = 'rings'"
          >
            <CircleDot :size="14" />
            Кольца ({{ previewRings.length }})
          </button>
        </div>

        <div v-if="activeTab === 'creatures'" class="preview-tab-content">
          <div v-if="previewCreatures.length === 0" class="empty-preview">Нет существ</div>
          <div v-else class="table-container mini-table">
            <table class="data-table">
              <thead>
                <tr>
                  <th>#</th>
                  <th>Имя</th>
                  <th>Тип</th>
                  <th>Координаты</th>
                  <th>Возраст</th>
                  <th>Атака</th>
                  <th>Защита</th>
                  <th>Город</th>
                  <th>Кольцо</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(c, idx) in previewCreatures" :key="idx">
                  <td>{{ idx + 1 }}</td>
                  <td>
                    <strong>{{ c.name }}</strong>
                  </td>
                  <td>
                    <BaseBadge :variant="getBadgeVariant(c.creatureType)" size="sm">
                      {{ getTypeLabel(c.creatureType) }}
                    </BaseBadge>
                  </td>
                  <td>({{ c.coordinates?.x ?? '—' }}, {{ c.coordinates?.y ?? '—' }})</td>
                  <td>{{ c.age }}</td>
                  <td>⚔️ {{ c.attackLevel }}</td>
                  <td>🛡️ {{ c.defenseLevel }}</td>
                  <td>
                    <span v-if="c.tempCreatureLocationId" class="ref-badge city-ref">
                      Новый [tempId: {{ c.tempCreatureLocationId }}]
                    </span>
                    <span v-else-if="c.creatureLocationId" class="ref-badge existing-ref">
                      #{{ c.creatureLocationId }}
                    </span>
                    <span v-else class="text-dim">—</span>
                  </td>
                  <td>
                    <span v-if="c.tempRingId" class="ref-badge ring-ref">
                      Новое [tempId: {{ c.tempRingId }}]
                    </span>
                    <span v-else-if="c.ringId" class="ref-badge existing-ref">
                      #{{ c.ringId }}
                    </span>
                    <span v-else class="text-dim">—</span>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <div v-if="activeTab === 'cities'" class="preview-tab-content">
          <div v-if="previewCities.length === 0" class="empty-preview">Нет городов</div>
          <div v-else class="table-container mini-table">
            <table class="data-table">
              <thead>
                <tr>
                  <th>tempId</th>
                  <th>Название</th>
                  <th>Площадь</th>
                  <th>Население</th>
                  <th>Столица</th>
                  <th>Губернатор</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(city, idx) in previewCities" :key="idx">
                  <td>
                    <span v-if="city.tempId" class="temp-id-pill">#{{ city.tempId }}</span>
                    <span v-else class="text-dim">—</span>
                  </td>
                  <td>
                    <strong>{{ city.name }}</strong>
                  </td>
                  <td>{{ city.area }} км²</td>
                  <td>{{ city.population }} чел.</td>
                  <td>{{ city.capital ? '👑 Да' : 'Нет' }}</td>
                  <td>{{ city.governor ? getTypeLabel(city.governor) : '—' }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <div v-if="activeTab === 'rings'" class="preview-tab-content">
          <div v-if="previewRings.length === 0" class="empty-preview">Нет колец</div>
          <div v-else class="table-container mini-table">
            <table class="data-table">
              <thead>
                <tr>
                  <th>tempId</th>
                  <th>Название</th>
                  <th>Сила</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(ring, idx) in previewRings" :key="idx">
                  <td>
                    <span v-if="ring.tempId" class="temp-id-pill">#{{ ring.tempId }}</span>
                    <span v-else class="text-dim">—</span>
                  </td>
                  <td>
                    <strong>{{ ring.name }}</strong>
                  </td>
                  <td>💍 {{ ring.power }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <div class="submit-section">
          <div class="submit-summary">
            Всего:
            <strong>
              {{ previewCreatures.length + previewCities.length + previewRings.length }}
            </strong>
          </div>

          <BaseButton
            variant="primary"
            size="md"
            :loading="importStore.loading"
            :disabled="!importStore.isParsedValid || importStore.loading"
            @click="handleImport"
          >
            <Upload :size="16" />
            Импортировать
          </BaseButton>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.page-container {
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
}

.page-title-icon {
  color: var(--primary);
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.server-error-callout {
  display: flex;
  align-items: flex-start;
  gap: 0.85rem;
  background: var(--danger-bg);
  border: 1px solid var(--danger);
  border-radius: var(--radius-md);
  padding: 1rem;
  color: #ff9b96;
}

.error-callout-icon {
  color: var(--danger);
  flex-shrink: 0;
}

.error-callout-body {
  flex: 1;
}

.error-title {
  font-weight: 600;
  font-size: 13.5px;
  color: #ffffff;
  margin-bottom: 0.25rem;
}

.error-message {
  font-size: 13px;
  margin-bottom: 0.5rem;
}

.error-details-list {
  padding-left: 1.2rem;
  margin: 0.5rem 0;
  font-size: 12px;
  color: #ffcfcc;
}

.import-layout {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1.25rem;
  align-items: start;
}

@media (max-width: 1024px) {
  .import-layout {
    grid-template-columns: 1fr;
  }
}

.editor-column,
.preview-column {
  display: flex;
  flex-direction: column;
  gap: 0.85rem;
  min-height: 580px;
}

.column-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-bottom: 0.65rem;
  border-bottom: 1px solid var(--border-subtle);
}

.column-title-wrap {
  display: flex;
  align-items: center;
  gap: 0.45rem;
}

.column-title {
  font-weight: 600;
  font-size: 13.5px;
  color: var(--text-main);
}

.file-chip {
  font-size: 11px;
  color: var(--text-muted);
  background: var(--bg-well);
  border: 1px solid var(--border-color);
  padding: 0.15rem 0.45rem;
  border-radius: var(--radius-xs);
}

.dropzone {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 1.25rem;
  border: 2px dashed var(--border-color);
  border-radius: var(--radius-sm);
  background: var(--bg-well);
  cursor: pointer;
  transition: all 0.15s ease;
  text-align: center;
}

.dropzone:hover,
.dropzone.is-dragging {
  border-color: var(--primary);
  background: var(--bg-card-hover);
}

.dropzone-icon {
  color: var(--primary);
  margin-bottom: 0.35rem;
}

.dropzone-text {
  font-size: 12.5px;
  color: var(--text-main);
}

.browse-link {
  color: var(--primary);
  text-decoration: underline;
}

.hidden-file-input {
  display: none;
}

.editor-wrapper {
  display: flex;
  flex-direction: column;
  flex: 1;
}

.editor-toolbar {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  font-size: 11px;
  color: var(--text-dim);
  margin-bottom: 0.35rem;
}

.raw-textarea {
  width: 100%;
  min-height: 280px;
  flex: 1;
  background: var(--bg-input);
  border: 1px solid var(--border-input);
  border-radius: var(--radius-sm);
  color: var(--text-main);
  font-family: 'JetBrains Mono', 'Fira Code', Menlo, Monaco, Consolas, monospace;
  font-size: 12px;
  line-height: 1.5;
  padding: 0.75rem;
  resize: vertical;
  tab-size: 2;
  white-space: pre;
}

.raw-textarea:focus {
  outline: none;
  border-color: var(--primary);
  box-shadow: 0 0 0 1px var(--primary);
}

.validation-errors-box {
  background: var(--danger-bg);
  border: 1px solid var(--danger);
  border-radius: var(--radius-sm);
  padding: 0.75rem 0.85rem;
  color: #ff9b96;
}

.validation-header {
  display: flex;
  align-items: center;
  gap: 0.4rem;
  font-size: 12.5px;
  margin-bottom: 0.4rem;
  color: #ffffff;
}

.validation-list {
  padding-left: 1.25rem;
  font-size: 11.5px;
  line-height: 1.45;
  max-height: 140px;
  overflow-y: auto;
}

.validation-success-box {
  display: flex;
  align-items: center;
  gap: 0.65rem;
  background: var(--success-bg);
  border: 1px solid var(--success);
  border-radius: var(--radius-sm);
  padding: 0.65rem 0.85rem;
  color: var(--text-main);
  font-size: 12px;
}

.preview-tabs {
  display: flex;
  border-bottom: 1px solid var(--border-color);
  gap: 0.25rem;
}

.preview-tab-btn {
  display: flex;
  align-items: center;
  gap: 0.35rem;
  background: none;
  border: none;
  border-bottom: 2px solid transparent;
  padding: 0.45rem 0.75rem;
  color: var(--text-muted);
  font-size: 12px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.12s ease;
}

.preview-tab-btn:hover {
  color: var(--text-main);
}

.preview-tab-btn.active {
  color: var(--primary);
  border-bottom-color: var(--primary);
  font-weight: 600;
}

.preview-tab-content {
  flex: 1;
  min-height: 220px;
}

.mini-table {
  max-height: 250px;
  overflow-y: auto;
}

.empty-preview {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 120px;
  color: var(--text-dim);
  font-size: 12.5px;
}

.ref-badge {
  font-size: 10.5px;
  padding: 0.15rem 0.45rem;
  border-radius: var(--radius-xs);
  display: inline-block;
}

.city-ref {
  background: rgba(86, 182, 194, 0.15);
  color: #56b6c2;
  border: 1px solid rgba(86, 182, 194, 0.35);
}

.ring-ref {
  background: rgba(242, 197, 92, 0.15);
  color: #f2c55c;
  border: 1px solid rgba(242, 197, 92, 0.35);
}

.existing-ref {
  background: var(--bg-well);
  color: var(--text-muted);
  border: 1px solid var(--border-color);
}

.temp-id-pill {
  font-family: monospace;
  background: var(--bg-well);
  border: 1px solid var(--border-color);
  padding: 0.1rem 0.35rem;
  border-radius: var(--radius-xs);
  color: var(--text-main);
  font-size: 11px;
}

.submit-section {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-top: 0.85rem;
  border-top: 1px solid var(--border-subtle);
  margin-top: auto;
}

.submit-summary {
  font-size: 12.5px;
  color: var(--text-muted);
}

.submit-summary strong {
  color: var(--text-main);
}
</style>
