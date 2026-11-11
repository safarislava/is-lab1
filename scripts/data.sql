-- Очистка таблиц перед заполнением (с каскадным сбросом связей и ID)
TRUNCATE TABLE bookcreature CASCADE;
TRUNCATE TABLE ring CASCADE;
TRUNCATE TABLE magiccity CASCADE;

-- Сброс автоинкремента ID
ALTER SEQUENCE IF EXISTS bookcreature_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS ring_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS magiccity_id_seq RESTART WITH 1;

-- 1. Заполнение Колец Власти (Ring)
INSERT INTO ring (name, power) VALUES
('Единое Кольцо', 1000),
('Нарья (Кольцо Огня)', 350),
('Ненья (Кольцо Воды)', 320),
('Вилья (Кольцо Воздуха)', 380),
('Кольцо Траина (Гномье)', 180),
('Кольцо Короля-чародея', 220),
('Кольцо Камула Истерлинга', 190),
('Малое волшебное кольцо', 50);

-- 2. Заполнение Городов и Локаций Средиземья (MagicCity)
INSERT INTO magiccity (name, area, population, establishmentdate, governor, capital, populationdensity) VALUES
('Шир (Мичел Делвинг)', 45000, 150000, '1601-03-15 00:00:00', 'HOBBIT', true, 3.33),
('Ривенделл (Имладрис)', 1200, 3500, '1697-01-01 00:00:00', 'ELF', false, 2.91),
('Минас Тирит', 8500, 65000, '3320-09-12 00:00:00', 'HUMAN', true, 7.64),
('Лотлориэн (Карас Галадон)', 5000, 12000, '1350-05-20 00:00:00', 'ELF', false, 2.40),
('Мордор', 350000, 500000, '1000-01-01 00:00:00', 'ORC', true, 1.42),
('Эребор (Одинокая гора)', 15000, 45000, '1999-07-07 00:00:00', 'HUMAN', false, 3.00),
('Изенгард (Ортанк)', 3000, 20000, '2759-10-10 00:00:00', 'ORC', false, 6.66),
('Бри (Пригорье)', 2500, 8000, '1300-04-01 00:00:00', 'HUMAN', false, 3.20);

-- 3. Заполнение Персонажей (BookCreature)
-- Хоббиты (с кольцами и без, для тестирования спец. операций)
INSERT INTO bookcreature (name, x, y, creationdate, age, creaturetype, creaturelocation_id, attacklevel, defenselevel, ring_id) VALUES
('Фродо Бэггинс', -120, 150.5, now() - interval '50 days', 50, 'HOBBIT', 
    (SELECT id FROM magiccity WHERE name LIKE 'Шир%'), 25.0, 50.0, (SELECT id FROM ring WHERE name = 'Единое Кольцо')),

('Бильбо Бэггинс', 210, -50.0, now() - interval '120 days', 129, 'HOBBIT', 
    (SELECT id FROM magiccity WHERE name LIKE 'Ривенделл%'), 15.0, 50.0, (SELECT id FROM ring WHERE name = 'Малое волшебное кольцо')),

('Сэмуайз Гэмджи', -115, 148.0, now() - interval '40 days', 38, 'HOBBIT', 
    (SELECT id FROM magiccity WHERE name LIKE 'Шир%'), 30.0, 60.0, NULL),

('Мериадок Брендибак', -110, 140.0, now() - interval '35 days', 36, 'HOBBIT', 
    (SELECT id FROM magiccity WHERE name LIKE 'Шир%'), 35.0, 50.0, NULL),

('Перегрин Тук', -105, 142.0, now() - interval '30 days', 28, 'HOBBIT', 
    (SELECT id FROM magiccity WHERE name LIKE 'Шир%'), 28.0, 45.0, NULL);

-- Существа / Голлум
INSERT INTO bookcreature (name, x, y, creationdate, age, creaturetype, creaturelocation_id, attacklevel, defenselevel, ring_id) VALUES
('Горлум (Смеагол)', 350, -450.0, now() - interval '500 days', 589, 'GOLLUM', 
    (SELECT id FROM magiccity WHERE name = 'Мордор'), 45.0, 30.0, NULL);

-- Эльфы
INSERT INTO bookcreature (name, x, y, creationdate, age, creaturetype, creaturelocation_id, attacklevel, defenselevel, ring_id) VALUES
('Элронд Полуэльф', 220, -45.0, now() - interval '1000 days', 6520, 'ELF', 
    (SELECT id FROM magiccity WHERE name LIKE 'Ривенделл%'), 85.0, 95.0, (SELECT id FROM ring WHERE name LIKE 'Вилья%')),

('Владычица Галадриэль', 180, -200.0, now() - interval '2000 days', 8372, 'ELF', 
    (SELECT id FROM magiccity WHERE name LIKE 'Лотлориэн%'), 90.0, 100.0, (SELECT id FROM ring WHERE name LIKE 'Ненья%')),

('Леголас Зеленолист', 190, -190.0, now() - interval '300 days', 2931, 'ELF', 
    (SELECT id FROM magiccity WHERE name LIKE 'Лотлориэн%'), 80.0, 70.0, NULL),

('Глорфиндель', 215, -48.0, now() - interval '800 days', 5000, 'ELF', 
    (SELECT id FROM magiccity WHERE name LIKE 'Ривенделл%'), 92.0, 85.0, NULL);

-- Люди
INSERT INTO bookcreature (name, x, y, creationdate, age, creaturetype, creaturelocation_id, attacklevel, defenselevel, ring_id) VALUES
('Гэндальф Серый (Олорин)', 0, 0.0, now() - interval '900 days', 2019, 'HUMAN', 
    (SELECT id FROM magiccity WHERE name = 'Минас Тирит'), 95.0, 90.0, (SELECT id FROM ring WHERE name LIKE 'Нарья%')),

('Арагорн (Элессар)', 50, -300.0, now() - interval '100 days', 87, 'HUMAN', 
    (SELECT id FROM magiccity WHERE name = 'Минас Тирит'), 90.0, 85.0, NULL),

('Боромир Гондорский', 45, -310.0, now() - interval '90 days', 40, 'HUMAN', 
    (SELECT id FROM magiccity WHERE name = 'Минас Тирит'), 85.0, 65.0, NULL),

('Фарамир', 40, -305.0, now() - interval '80 days', 36, 'HUMAN', 
    (SELECT id FROM magiccity WHERE name = 'Минас Тирит'), 78.0, 65.0, NULL),

('Король-чародей Ангмара', 400, -500.0, now() - interval '1500 days', 4200, 'HUMAN', 
    (SELECT id FROM magiccity WHERE name = 'Мордор'), 92.0, 88.0, (SELECT id FROM ring WHERE name = 'Кольцо Короля-чародея')),

('Камул Истерлинг', 380, -490.0, now() - interval '1200 days', 3500, 'HUMAN', 
    (SELECT id FROM magiccity WHERE name = 'Мордор'), 75.0, 70.0, (SELECT id FROM ring WHERE name = 'Кольцо Камула Истерлинга'));

-- Орки
INSERT INTO bookcreature (name, x, y, creationdate, age, creaturetype, creaturelocation_id, attacklevel, defenselevel, ring_id) VALUES
('Готмог (Военачальник)', 390, -480.0, now() - interval '60 days', 150, 'ORC', 
    (SELECT id FROM magiccity WHERE name = 'Мордор'), 70.0, 60.0, NULL),

('Углук (Урук-хай)', 150, -250.0, now() - interval '20 days', 65, 'ORC', 
    (SELECT id FROM magiccity WHERE name LIKE 'Изенгард%'), 65.0, 55.0, NULL),

('Гришнак', 370, -470.0, now() - interval '15 days', 45, 'ORC', 
    (SELECT id FROM magiccity WHERE name = 'Мордор'), 55.0, 40.0, NULL),

('Шаграт', 360, -460.0, now() - interval '25 days', 80, 'ORC', 
    (SELECT id FROM magiccity WHERE name = 'Мордор'), 60.0, 50.0, NULL);
