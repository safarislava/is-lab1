DROP TABLE IF EXISTS bookcreature CASCADE;
DROP TABLE IF EXISTS ring CASCADE;
DROP TABLE IF EXISTS magiccity CASCADE;

DROP FUNCTION IF EXISTS delete_ring_with_detach(INTEGER);
DROP FUNCTION IF EXISTS delete_by_defense_level(REAL);
DROP FUNCTION IF EXISTS calculate_avg_defense_level();
DROP FUNCTION IF EXISTS take_rings_from_hobbits();
DROP FUNCTION IF EXISTS move_hobbits_with_rings_to_mordor();
