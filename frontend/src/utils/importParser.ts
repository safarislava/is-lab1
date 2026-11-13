import type {
  BatchBookCreatureCreateRequest,
  BatchMagicCityCreateRequest,
  BatchObjectsCreateRequest,
  BatchRingCreateRequest,
} from '@/types/batch';

export interface ParseResult {
  request: BatchObjectsCreateRequest | null;
  errors: string[];
  rawParsed: unknown;
}

export function parseAndValidateImport(content: string): ParseResult {
  const trimmed = content.trim();
  if (!trimmed) {
    return {
      request: null,
      errors: [],
      rawParsed: null,
    };
  }

  let raw: unknown = null;
  try {
    raw = JSON.parse(trimmed);
  } catch (err) {
    return {
      request: null,
      errors: [`Синтаксическая ошибка JSON: ${err instanceof Error ? err.message : String(err)}`],
      rawParsed: null,
    };
  }

  const request = normalize(raw);
  return {
    request,
    errors: [],
    rawParsed: raw,
  };
}

function normalize(raw: unknown): BatchObjectsCreateRequest {
  if (raw && typeof raw === 'object' && !Array.isArray(raw)) {
    const obj = raw as Record<string, unknown>;
    return {
      creatures: Array.isArray(obj.creatures)
        ? (obj.creatures as BatchBookCreatureCreateRequest[])
        : [],
      cities: Array.isArray(obj.cities) ? (obj.cities as BatchMagicCityCreateRequest[]) : [],
      rings: Array.isArray(obj.rings) ? (obj.rings as BatchRingCreateRequest[]) : [],
    };
  }

  if (Array.isArray(raw)) {
    return {
      creatures: raw as BatchBookCreatureCreateRequest[],
      cities: [],
      rings: [],
    };
  }

  return {
    creatures: [],
    cities: [],
    rings: [],
  };
}

export const SAMPLE_JSON = JSON.stringify(
  {
    rings: [
      {
        tempId: 1,
        name: 'Единое Кольцо Власти',
        power: 1000,
      },
      {
        tempId: 2,
        name: 'Малое лесное кольцо',
        power: 120,
      },
    ],
    cities: [
      {
        tempId: 1,
        name: 'Шир (Заселье)',
        area: 45000,
        population: 150000,
        establishmentDate: '1601-03-15T00:00:00',
        governor: 'HOBBIT',
        capital: true,
        populationDensity: 3.33,
      },
      {
        tempId: 2,
        name: 'Лихолесье',
        area: 32000,
        population: 40000,
        establishmentDate: '1050-01-01T00:00:00',
        governor: 'ELF',
        capital: false,
        populationDensity: 1.25,
      },
    ],
    creatures: [
      {
        name: 'Фродо Бэггинс',
        coordinates: {
          x: 100,
          y: 250.5,
        },
        age: 50,
        creatureType: 'HOBBIT',
        attackLevel: 25.0,
        defenseLevel: 50.0,
        tempCreatureLocationId: 1,
        tempRingId: 1,
      },
      {
        name: 'Леголас Зеленолист',
        coordinates: {
          x: 215,
          y: -40.0,
        },
        age: 2931,
        creatureType: 'ELF',
        attackLevel: 95.0,
        defenseLevel: 80.0,
        tempCreatureLocationId: 2,
        tempRingId: 2,
      },
    ],
  },
  null,
  2,
);
