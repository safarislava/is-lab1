TRUNCATE TABLE bookcreature, ring, magiccity CASCADE;

ALTER SEQUENCE IF EXISTS bookcreature_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS ring_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS magiccity_id_seq RESTART WITH 1;

SELECT
    (SELECT count(*) FROM bookcreature) AS bookcreature_count,
    (SELECT count(*) FROM ring) AS ring_count,
    (SELECT count(*) FROM magiccity) AS magiccity_count;
