-- void delete_ring_with_detach
CREATE OR REPLACE FUNCTION delete_ring_with_detach(arg_id INTEGER)
RETURNS VOID AS $$
BEGIN
    UPDATE bookcreature SET ring_id = NULL WHERE bookcreature.ring_id = arg_id;
    DELETE FROM ring WHERE id = arg_id;
END;
$$ LANGUAGE plpgsql;

-- int delete_by_defense_level
CREATE OR REPLACE FUNCTION delete_by_defense_level(arg_level REAL)
RETURNS INTEGER AS $$
DECLARE
    v_count INTEGER;
BEGIN
    DELETE FROM bookcreature WHERE defenselevel = arg_level;
    GET DIAGNOSTICS v_count = ROW_COUNT;
    RETURN v_count;
END;
$$ LANGUAGE plpgsql;

-- double calculate_avg_defense_level
CREATE OR REPLACE FUNCTION calculate_avg_defense_level()
RETURNS REAL AS $$
DECLARE
    v_avg_level REAL;
BEGIN
    SELECT COALESCE(AVG(defenselevel), 0) INTO v_avg_level FROM bookcreature;
    RETURN v_avg_level;
END;
$$ LANGUAGE plpgsql;

-- int take_rings_from_hobbits
CREATE OR REPLACE FUNCTION take_rings_from_hobbits()
    RETURNS INTEGER AS $$
DECLARE
    v_count INTEGER;
BEGIN
    UPDATE bookcreature SET ring_id = NULL WHERE creaturetype = 'HOBBIT' AND ring_id IS NOT NULL;
    GET DIAGNOSTICS v_count = ROW_COUNT;
    RETURN v_count;
END;
$$ LANGUAGE plpgsql;

-- int move_hobbits_with_rings_to_mordor
CREATE OR REPLACE FUNCTION move_hobbits_with_rings_to_mordor()
RETURNS INTEGER AS $$
DECLARE
    v_mordor_id INTEGER;
    v_count INTEGER;
BEGIN
    SELECT id INTO v_mordor_id FROM magiccity WHERE name = 'Мордор' LIMIT 1;
    IF v_mordor_id IS NULL THEN
        INSERT INTO magiccity (name, area, population, establishmentdate, governor, capital, populationDensity)
        VALUES ("Мордор", "1000", "10000000", '20-09-06 00:00:05', 'ORC', false, 10000.0)
        RETURNING id INTO v_mordor_id;
    END IF;
    UPDATE bookcreature SET creaturelocation_id = v_mordor_id WHERE creaturetype = 'HOBBIT' AND ring_id IS NOT NULL;
    GET DIAGNOSTICS v_count = ROW_COUNT;
    RETURN v_count;
END;
$$ LANGUAGE plpgsql;