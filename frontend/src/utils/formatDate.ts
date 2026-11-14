export function parseDate(val?: string | number | null): Date | null {
  if (val == null || val === '') return null;
  if (typeof val === 'number') {
    return new Date(val < 1e11 ? val * 1000 : val);
  }
  const str = String(val).trim();
  const num = Number(str);
  if (!isNaN(num) && isFinite(num) && str !== '') {
    return new Date(num < 1e11 ? num * 1000 : num);
  }
  const d = new Date(str);
  return isNaN(d.getTime()) ? null : d;
}

export function formatDateTime(val?: string | number | null, fallback = '—'): string {
  const d = parseDate(val);
  if (!d) return fallback;
  return new Intl.DateTimeFormat('ru-RU', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
  }).format(d);
}

export function formatDateShort(val?: string | number | null, fallback = '—'): string {
  const d = parseDate(val);
  if (!d) return fallback;
  return new Intl.DateTimeFormat('ru-RU', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  }).format(d);
}
