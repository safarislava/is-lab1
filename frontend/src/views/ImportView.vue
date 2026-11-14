<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useImportStore } from '@/stores/importStore';
import { SAMPLE_JSON } from '@/utils/importParser';
import { formatDateTime } from '@/utils/formatDate';
import BaseButton from '@/components/ui/BaseButton.vue';
import BaseBadge, { type BadgeVariant } from '@/components/ui/BaseBadge.vue';
import CreaturePagination from '@/components/creatures/CreaturePagination.vue';
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
  History,
  CheckCircle2,
  XCircle,
  Clock,
} from 'lucide-vue-next';

const importStore = useImportStore();

const activeTab = ref<'creatures' | 'cities' | 'rings'>('creatures');
const fileInput = ref<HTMLInputElement | null>(null);
const isDragging = ref(false);

const previewCreatures = computed(() => importStore.parsedRequest?.creatures ?? []);
const previewCities = computed(() => importStore.parsedRequest?.cities ?? []);
const previewRings = computed(() => importStore.parsedRequest?.rings ?? []);

onMounted(() => {
  importStore.fetchHistory();
});

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

function onPageChange(page: number) {
  importStore.fetchHistory(page);
}

function formatBytes(bytes: number | null): string {
  if (!bytes) return '';
  if (bytes < 1024) return `${bytes} B`;
  return `${(bytes / 1024).toFixed(1)} KB`;
}

function formatDate(dateVal: string | number | null | undefined): string {
  return formatDateTime(dateVal);
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

    <div class="import-columns-grid">
      <div class="editor-column card">
        <div class="column-header">
          <div class="column-title-wrap">
            <FileText :size="16" class="text-muted" />
            <span class="column-title">JSON файл</span>
            <span v-if="importStore.fileName" class="file-name-tag">
              {{ importStore.fileName }}
              <span v-if="importStore.fileSize">({{ formatBytes(importStore.fileSize) }})</span>
            </span>
          </div>

          <BaseButton variant="ghost" size="sm" @click="importStore.loadSample()">
            Пример
          </BaseButton>
        </div>

        <div
          :class="['dropzone', { active: isDragging }]"
          @click="triggerFilePicker"
          @dragover="onDragOver"
          @dragleave="onDragLeave"
          @drop="onDrop"
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

        <div class="preview-tab-content">
          <div v-if="activeTab === 'creatures'" class="tab-pane">
            <div v-if="previewCreatures.length === 0" class="empty-preview">
              Нет существ в запросе
            </div>
            <div v-else class="table-container mini-table">
              <table class="data-table">
                <thead>
                  <tr>
                    <th>Имя</th>
                    <th>Тип</th>
                    <th>Возраст</th>
                    <th>Атака/Защита</th>
                    <th>Локация</th>
                    <th>Кольцо</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(c, idx) in previewCreatures" :key="idx">
                    <td>{{ c.name }}</td>
                    <td>
                      <BaseBadge :variant="getBadgeVariant(c.creatureType)" size="sm">
                        {{ getTypeLabel(c.creatureType) }}
                      </BaseBadge>
                    </td>
                    <td>{{ c.age }}</td>
                    <td>{{ c.attackLevel }} / {{ c.defenseLevel }}</td>
                    <td>
                      <span v-if="c.tempCreatureLocationId != null" class="ref-badge city-ref">
                        город #{{ c.tempCreatureLocationId }}
                      </span>
                      <span v-else-if="c.creatureLocationId != null" class="ref-badge existing-ref">
                        город #{{ c.creatureLocationId }}
                      </span>
                      <span v-else class="text-dim">—</span>
                    </td>
                    <td>
                      <span v-if="c.tempRingId != null" class="ref-badge ring-ref">
                        кольцо #{{ c.tempRingId }}
                      </span>
                      <span v-else-if="c.ringId != null" class="ref-badge existing-ref">
                        кольцо #{{ c.ringId }}
                      </span>
                      <span v-else class="text-dim">—</span>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>

          <div v-if="activeTab === 'cities'" class="tab-pane">
            <div v-if="previewCities.length === 0" class="empty-preview">
              Нет городов в запросе
            </div>
            <div v-else class="table-container mini-table">
              <table class="data-table">
                <thead>
                  <tr>
                    <th>Temp ID</th>
                    <th>Название</th>
                    <th>Площадь</th>
                    <th>Население</th>
                    <th>Столица</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(city, idx) in previewCities" :key="idx">
                    <td>
                      <span class="temp-id-pill">#{{ city.tempId ?? '—' }}</span>
                    </td>
                    <td>{{ city.name }}</td>
                    <td>{{ city.area }}</td>
                    <td>{{ city.population }}</td>
                    <td>{{ city.capital ? 'Да' : 'Нет' }}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>

          <div v-if="activeTab === 'rings'" class="tab-pane">
            <div v-if="previewRings.length === 0" class="empty-preview">
              Нет колец в запросе
            </div>
            <div v-else class="table-container mini-table">
              <table class="data-table">
                <thead>
                  <tr>
                    <th>Temp ID</th>
                    <th>Название</th>
                    <th>Сила</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(ring, idx) in previewRings" :key="idx">
                    <td>
                      <span class="temp-id-pill">#{{ ring.tempId ?? '—' }}</span>
                    </td>
                    <td>{{ ring.name }}</td>
                    <td>{{ ring.power }}</td>
                  </tr>
                </tbody>
              </table>
            </div>
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

    <div class="history-card card">
      <div class="column-header">
        <div class="column-title-wrap">
          <History :size="16" class="text-muted" />
          <span class="column-title">История операций импорта</span>
        </div>

        <BaseButton
          variant="ghost"
          size="sm"
          :loading="importStore.historyLoading"
          @click="importStore.fetchHistory()"
        >
          <RotateCcw :size="12" />
          Обновить
        </BaseButton>
      </div>

      <div v-if="importStore.history.length === 0" class="empty-history">
        <Clock :size="24" class="text-dim" />
        <p>История операций импорта пуста</p>
      </div>

      <div v-else class="table-container">
        <table class="data-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Статус</th>
              <th>Пользователь</th>
              <th>Добавлено объектов</th>
              <th>Время создания</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="op in importStore.history" :key="op.id">
              <td>
                <span class="temp-id-pill">#{{ op.id }}</span>
              </td>
              <td>
                <span
                  :class="[
                    'status-badge',
                    op.status === 'SUCCESS' ? 'status-success' : 'status-failed',
                  ]"
                >
                  <CheckCircle2 v-if="op.status === 'SUCCESS'" :size="13" />
                  <XCircle v-else :size="13" />
                  {{ op.status === 'SUCCESS' ? 'Успешно' : 'Ошибка' }}
                </span>
              </td>
              <td>
                <strong>{{ op.username }}</strong>
              </td>
              <td>
                <span v-if="op.status === 'SUCCESS' && op.addedObjectsCount != null">
                  {{ op.addedObjectsCount }}
                </span>
                <span v-else class="text-dim">—</span>
              </td>
              <td>
                <span class="text-muted">{{ formatDate(op.creationDate) }}</span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <CreaturePagination
        v-if="importStore.historyTotal > 0"
        :page="importStore.historyPage"
        :size="importStore.historySize"
        :total="importStore.historyTotal"
        @page-change="onPageChange"
      />
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
  margin-bottom: 0.25rem;
}

.error-message {
  font-size: 13px;
  line-height: 1.4;
}

.error-details-list {
  margin-top: 0.5rem;
  padding-left: 1.25rem;
  font-size: 12px;
}

.import-columns-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1.25rem;
  align-items: stretch;
}

@media (max-width: 1024px) {
  .import-columns-grid {
    grid-template-columns: 1fr;
  }
}

.column-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 0.85rem;
  padding-bottom: 0.5rem;
  border-bottom: 1px solid var(--border-subtle);
}

.column-title-wrap {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.column-title {
  font-size: 14px;
  font-weight: 600;
  color: var(--text-main);
}

.file-name-tag {
  font-size: 12px;
  color: var(--text-muted);
  background: var(--bg-well);
  padding: 0.15rem 0.5rem;
  border-radius: var(--radius-xs);
  border: 1px solid var(--border-color);
}

.dropzone {
  border: 2px dashed var(--border-color);
  border-radius: var(--radius-md);
  padding: 1.25rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  cursor: pointer;
  background: var(--bg-well);
  transition: all 0.2s ease;
  margin-bottom: 0.85rem;
}

.dropzone:hover,
.dropzone.active {
  border-color: var(--primary);
  background: rgba(120, 119, 198, 0.08);
}

.dropzone-icon {
  color: var(--text-muted);
}

.dropzone-text {
  font-size: 12.5px;
  color: var(--text-muted);
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
  min-height: 260px;
}

.editor-toolbar {
  display: flex;
  justify-content: flex-end;
  padding: 0.25rem 0.5rem;
  font-size: 11px;
  color: var(--text-dim);
}

.raw-textarea {
  width: 100%;
  flex: 1;
  min-height: 240px;
  font-family: monospace;
  font-size: 12px;
  line-height: 1.45;
  background: var(--bg-well);
  color: var(--text-main);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-sm);
  padding: 0.75rem;
  resize: vertical;
}

.raw-textarea:focus {
  outline: none;
  border-color: var(--primary);
}

.validation-errors-box {
  background: rgba(224, 108, 117, 0.1);
  border: 1px solid rgba(224, 108, 117, 0.35);
  border-radius: var(--radius-sm);
  padding: 0.65rem 0.85rem;
  margin-bottom: 0.75rem;
}

.validation-header {
  display: flex;
  align-items: center;
  gap: 0.4rem;
  font-size: 12.5px;
  color: #ff9b96;
  margin-bottom: 0.35rem;
}

.validation-list {
  padding-left: 1.25rem;
  font-size: 11.5px;
  color: #ff9b96;
}

.preview-tabs {
  display: flex;
  gap: 0.25rem;
  border-bottom: 1px solid var(--border-subtle);
  margin-bottom: 0.75rem;
}

.preview-tab-btn {
  display: flex;
  align-items: center;
  gap: 0.35rem;
  background: none;
  border: none;
  border-bottom: 2px solid transparent;
  padding: 0.45rem 0.75rem;
  font-size: 12.5px;
  color: var(--text-muted);
  cursor: pointer;
  transition: all 0.15s ease;
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

.history-card {
  margin-top: 0.5rem;
}

.empty-history {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  padding: 2.5rem 1rem;
  color: var(--text-dim);
  font-size: 13px;
}

.status-badge {
  display: inline-flex;
  align-items: center;
  gap: 0.35rem;
  padding: 0.2rem 0.55rem;
  border-radius: var(--radius-xs);
  font-size: 11.5px;
  font-weight: 600;
}

.status-success {
  background: rgba(152, 195, 121, 0.15);
  color: #98c379;
  border: 1px solid rgba(152, 195, 121, 0.35);
}

.status-failed {
  background: rgba(224, 108, 117, 0.15);
  color: #e06c75;
  border: 1px solid rgba(224, 108, 117, 0.35);
}
</style>
