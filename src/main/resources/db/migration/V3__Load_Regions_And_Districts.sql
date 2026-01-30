-- Insert Regions
INSERT INTO regions (code, name, description, status, created_at, updated_at) VALUES
('ARU', 'Arusha', 'Arusha Region', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('DAR', 'Dar es Salaam', 'Dar es Salaam Region', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('DOD', 'Dodoma', 'Dodoma Region', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('GEI', 'Geita', 'Geita Region', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('IRI', 'Iringa', 'Iringa Region', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('KAG', 'Kagera', 'Kagera Region', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('KPN', 'Kaskazini Pemba', 'North Pemba Region', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('KUN', 'Kaskazini Unguja', 'North Zanzibar Region', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('KAT', 'Katavi', 'Katavi Region', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('KIG', 'Kigoma', 'Kigoma Region', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('KIL', 'Kilimanjaro', 'Kilimanjaro Region', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('KSP', 'Kusini Pemba', 'South Pemba Region', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('KSU', 'Kusini Unguja', 'South Zanzibar Region', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('LIN', 'Lindi', 'Lindi Region', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('MAN', 'Manyara', 'Manyara Region', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('MAR', 'Mara', 'Mara Region', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('MBE', 'Mbeya', 'Mbeya Region', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('MJI', 'Mjini Magharibi', 'Urban West Region', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('MOR', 'Morogoro', 'Morogoro Region', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('MTW', 'Mtwara', 'Mtwara Region', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('MWA', 'Mwanza', 'Mwanza Region', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('NJO', 'Njombe', 'Njombe Region', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PWA', 'Pwani', 'Pwani Region', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('RUK', 'Rukwa', 'Rukwa Region', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('RUV', 'Ruvuma', 'Ruvuma Region', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SHI', 'Shinyanga', 'Shinyanga Region', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SIM', 'Simiyu', 'Simiyu Region', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SIN', 'Singida', 'Singida Region', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SON', 'Songwe', 'Songwe Region', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('TAB', 'Tabora', 'Tabora Region', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('TAN', 'Tanga', 'Tanga Region', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Districts for Arusha Region
INSERT INTO districts (code, name, region_id, description, status, created_at, updated_at)
SELECT 'ARU-CC', 'Arusha City', r.id, 'Arusha City Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'ARU'
UNION ALL SELECT 'ARU-RD', 'Arusha Rural', r.id, 'Arusha Rural District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'ARU'
UNION ALL SELECT 'ARU-KT', 'Karatu', r.id, 'Karatu District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'ARU'
UNION ALL SELECT 'ARU-LG', 'Longido', r.id, 'Longido District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'ARU'
UNION ALL SELECT 'ARU-MR', 'Meru', r.id, 'Meru District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'ARU'
UNION ALL SELECT 'ARU-MD', 'Monduli', r.id, 'Monduli District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'ARU'
UNION ALL SELECT 'ARU-NG', 'Ngorongoro', r.id, 'Ngorongoro District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'ARU';

-- Insert Districts for Dar es Salaam Region
INSERT INTO districts (code, name, region_id, description, status, created_at, updated_at)
SELECT 'DAR-IL', 'Ilala', r.id, 'Ilala Municipal Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'DAR'
UNION ALL SELECT 'DAR-KG', 'Kigamboni', r.id, 'Kigamboni Municipal Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'DAR'
UNION ALL SELECT 'DAR-KN', 'Kinondoni', r.id, 'Kinondoni Municipal Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'DAR'
UNION ALL SELECT 'DAR-TM', 'Temeke', r.id, 'Temeke Municipal Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'DAR'
UNION ALL SELECT 'DAR-UB', 'Ubungo', r.id, 'Ubungo Municipal Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'DAR';

-- Insert Districts for Dodoma Region
INSERT INTO districts (code, name, region_id, description, status, created_at, updated_at)
SELECT 'DOD-BH', 'Bahi', r.id, 'Bahi District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'DOD'
UNION ALL SELECT 'DOD-CW', 'Chamwino', r.id, 'Chamwino District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'DOD'
UNION ALL SELECT 'DOD-CB', 'Chemba', r.id, 'Chemba District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'DOD'
UNION ALL SELECT 'DOD-CC', 'Dodoma City', r.id, 'Dodoma City Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'DOD'
UNION ALL SELECT 'DOD-KR', 'Kondoa Rural', r.id, 'Kondoa Rural District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'DOD'
UNION ALL SELECT 'DOD-KT', 'Kondoa Town', r.id, 'Kondoa Town Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'DOD'
UNION ALL SELECT 'DOD-KG', 'Kongwa', r.id, 'Kongwa District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'DOD'
UNION ALL SELECT 'DOD-MP', 'Mpwapwa', r.id, 'Mpwapwa District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'DOD';

-- Insert Districts for Geita Region
INSERT INTO districts (code, name, region_id, description, status, created_at, updated_at)
SELECT 'GEI-BK', 'Bukombe', r.id, 'Bukombe District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'GEI'
UNION ALL SELECT 'GEI-CH', 'Chato', r.id, 'Chato District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'GEI'
UNION ALL SELECT 'GEI-GR', 'Geita Rural', r.id, 'Geita Rural District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'GEI'
UNION ALL SELECT 'GEI-GT', 'Geita Town', r.id, 'Geita Town Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'GEI'
UNION ALL SELECT 'GEI-MB', 'Mbogwe', r.id, 'Mbogwe District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'GEI'
UNION ALL SELECT 'GEI-NY', 'Nyang\'hwale', r.id, 'Nyang\'hwale District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'GEI';

-- Insert Districts for Iringa Region
INSERT INTO districts (code, name, region_id, description, status, created_at, updated_at)
SELECT 'IRI-MC', 'Iringa Municipal', r.id, 'Iringa Municipal Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'IRI'
UNION ALL SELECT 'IRI-RD', 'Iringa Rural', r.id, 'Iringa Rural District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'IRI'
UNION ALL SELECT 'IRI-KL', 'Kilolo', r.id, 'Kilolo District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'IRI'
UNION ALL SELECT 'IRI-MF', 'Mafinga Town', r.id, 'Mafinga Town Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'IRI'
UNION ALL SELECT 'IRI-MU', 'Mufindi', r.id, 'Mufindi District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'IRI';

-- Insert Districts for Kagera Region
INSERT INTO districts (code, name, region_id, description, status, created_at, updated_at)
SELECT 'KAG-BH', 'Biharamulo', r.id, 'Biharamulo District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'KAG'
UNION ALL SELECT 'KAG-BM', 'Bukoba Municipal', r.id, 'Bukoba Municipal Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'KAG'
UNION ALL SELECT 'KAG-BR', 'Bukoba Rural', r.id, 'Bukoba Rural District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'KAG'
UNION ALL SELECT 'KAG-KR', 'Karagwe', r.id, 'Karagwe District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'KAG'
UNION ALL SELECT 'KAG-KY', 'Kyerwa', r.id, 'Kyerwa District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'KAG'
UNION ALL SELECT 'KAG-MS', 'Missenyi', r.id, 'Missenyi District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'KAG'
UNION ALL SELECT 'KAG-ML', 'Muleba', r.id, 'Muleba District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'KAG'
UNION ALL SELECT 'KAG-NG', 'Ngara', r.id, 'Ngara District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'KAG';

-- Insert Districts for Kaskazini Pemba Region
INSERT INTO districts (code, name, region_id, description, status, created_at, updated_at)
SELECT 'KPN-MC', 'Micheweni', r.id, 'Micheweni District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'KPN'
UNION ALL SELECT 'KPN-WT', 'Wete', r.id, 'Wete District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'KPN';

-- Insert Districts for Kaskazini Unguja Region
INSERT INTO districts (code, name, region_id, description, status, created_at, updated_at)
SELECT 'KUN-NA', 'Kaskazini A Town', r.id, 'North Unguja A Town Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'KUN'
UNION ALL SELECT 'KUN-NB', 'Kaskazini B', r.id, 'North Unguja B District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'KUN';

-- Insert Districts for Katavi Region
INSERT INTO districts (code, name, region_id, description, status, created_at, updated_at)
SELECT 'KAT-ML', 'Mlele', r.id, 'Mlele District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'KAT'
UNION ALL SELECT 'KAT-MM', 'Mpanda Municipal', r.id, 'Mpanda Municipal Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'KAT'
UNION ALL SELECT 'KAT-MP', 'Mpimbwe', r.id, 'Mpimbwe District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'KAT'
UNION ALL SELECT 'KAT-NS', 'Nsimbo', r.id, 'Nsimbo District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'KAT'
UNION ALL SELECT 'KAT-TG', 'Tanganyika', r.id, 'Tanganyika District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'KAT';

-- Insert Districts for Kigoma Region
INSERT INTO districts (code, name, region_id, description, status, created_at, updated_at)
SELECT 'KIG-BH', 'Buhigwe', r.id, 'Buhigwe District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'KIG'
UNION ALL SELECT 'KIG-KK', 'Kakonko', r.id, 'Kakonko District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'KIG'
UNION ALL SELECT 'KIG-KR', 'Kasulu Rural', r.id, 'Kasulu Rural District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'KIG'
UNION ALL SELECT 'KIG-KT', 'Kasulu Town', r.id, 'Kasulu Town Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'KIG'
UNION ALL SELECT 'KIG-KB', 'Kibondo', r.id, 'Kibondo District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'KIG'
UNION ALL SELECT 'KIG-MC', 'Kigoma Municipal', r.id, 'Kigoma Municipal Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'KIG'
UNION ALL SELECT 'KIG-RD', 'Kigoma Rural', r.id, 'Kigoma Rural District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'KIG'
UNION ALL SELECT 'KIG-UV', 'Uvinza', r.id, 'Uvinza District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'KIG';

-- Insert Districts for Kilimanjaro Region
INSERT INTO districts (code, name, region_id, description, status, created_at, updated_at)
SELECT 'KIL-HA', 'Hai', r.id, 'Hai District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'KIL'
UNION ALL SELECT 'KIL-MC', 'Moshi Municipal', r.id, 'Moshi Municipal Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'KIL'
UNION ALL SELECT 'KIL-MR', 'Moshi Rural', r.id, 'Moshi Rural District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'KIL'
UNION ALL SELECT 'KIL-MW', 'Mwanga', r.id, 'Mwanga District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'KIL'
UNION ALL SELECT 'KIL-RO', 'Rombo', r.id, 'Rombo District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'KIL'
UNION ALL SELECT 'KIL-SA', 'Same', r.id, 'Same District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'KIL'
UNION ALL SELECT 'KIL-SI', 'Siha', r.id, 'Siha District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'KIL';

-- Insert Districts for Kusini Pemba Region
INSERT INTO districts (code, name, region_id, description, status, created_at, updated_at)
SELECT 'KSP-CK', 'Chake Chake', r.id, 'Chake Chake District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'KSP'
UNION ALL SELECT 'KSP-MK', 'Mkoani Town', r.id, 'Mkoani Town Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'KSP';

-- Insert Districts for Kusini Unguja Region
INSERT INTO districts (code, name, region_id, description, status, created_at, updated_at)
SELECT 'KSU-KT', 'Kati', r.id, 'Central Unguja District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'KSU'
UNION ALL SELECT 'KSU-KS', 'Kusini', r.id, 'South Unguja District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'KSU';

-- Insert Districts for Lindi Region
INSERT INTO districts (code, name, region_id, description, status, created_at, updated_at)
SELECT 'LIN-KL', 'Kilwa', r.id, 'Kilwa District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'LIN'
UNION ALL SELECT 'LIN-MC', 'Lindi Municipal', r.id, 'Lindi Municipal Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'LIN'
UNION ALL SELECT 'LIN-LW', 'Liwale', r.id, 'Liwale District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'LIN'
UNION ALL SELECT 'LIN-MT', 'Mtama', r.id, 'Mtama District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'LIN'
UNION ALL SELECT 'LIN-NC', 'Nachingwea', r.id, 'Nachingwea District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'LIN'
UNION ALL SELECT 'LIN-RU', 'Ruangwa', r.id, 'Ruangwa District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'LIN';

-- Insert Districts for Manyara Region
INSERT INTO districts (code, name, region_id, description, status, created_at, updated_at)
SELECT 'MAN-BR', 'Babati Rural', r.id, 'Babati Rural District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MAN'
UNION ALL SELECT 'MAN-BT', 'Babati Town', r.id, 'Babati Town Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MAN'
UNION ALL SELECT 'MAN-HN', 'Hanang', r.id, 'Hanang District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MAN'
UNION ALL SELECT 'MAN-KT', 'Kiteto', r.id, 'Kiteto District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MAN'
UNION ALL SELECT 'MAN-MR', 'Mbulu Rural', r.id, 'Mbulu Rural District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MAN'
UNION ALL SELECT 'MAN-MT', 'Mbulu Town', r.id, 'Mbulu Town Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MAN'
UNION ALL SELECT 'MAN-SM', 'Simanjiro', r.id, 'Simanjiro District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MAN';

-- Insert Districts for Mara Region
INSERT INTO districts (code, name, region_id, description, status, created_at, updated_at)
SELECT 'MAR-BR', 'Bunda Rural', r.id, 'Bunda Rural District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MAR'
UNION ALL SELECT 'MAR-BT', 'Bunda Town', r.id, 'Bunda Town Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MAR'
UNION ALL SELECT 'MAR-BU', 'Butiama', r.id, 'Butiama District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MAR'
UNION ALL SELECT 'MAR-MM', 'Musoma Municipal', r.id, 'Musoma Municipal Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MAR'
UNION ALL SELECT 'MAR-MR', 'Musoma Rural', r.id, 'Musoma Rural District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MAR'
UNION ALL SELECT 'MAR-RO', 'Rorya', r.id, 'Rorya District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MAR'
UNION ALL SELECT 'MAR-SE', 'Serengeti', r.id, 'Serengeti District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MAR'
UNION ALL SELECT 'MAR-TR', 'Tarime Rural', r.id, 'Tarime Rural District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MAR'
UNION ALL SELECT 'MAR-TT', 'Tarime Town', r.id, 'Tarime Town Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MAR';

-- Insert Districts for Mbeya Region
INSERT INTO districts (code, name, region_id, description, status, created_at, updated_at)
SELECT 'MBE-BS', 'Busekelo', r.id, 'Busekelo District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MBE'
UNION ALL SELECT 'MBE-CH', 'Chunya', r.id, 'Chunya District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MBE'
UNION ALL SELECT 'MBE-KY', 'Kyela', r.id, 'Kyela District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MBE'
UNION ALL SELECT 'MBE-MB', 'Mbarali', r.id, 'Mbarali District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MBE'
UNION ALL SELECT 'MBE-CC', 'Mbeya City', r.id, 'Mbeya City Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MBE'
UNION ALL SELECT 'MBE-RD', 'Mbeya Rural', r.id, 'Mbeya Rural District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MBE'
UNION ALL SELECT 'MBE-RU', 'Rungwe', r.id, 'Rungwe District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MBE';

-- Insert Districts for Mjini Magharibi Region
INSERT INTO districts (code, name, region_id, description, status, created_at, updated_at)
SELECT 'MJI-MA', 'Magharibi A Municipal', r.id, 'Magharibi A Municipal Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MJI'
UNION ALL SELECT 'MJI-MB', 'Magharibi B Municipal', r.id, 'Magharibi B Municipal Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MJI'
UNION ALL SELECT 'MJI-ZC', 'Mjini Municipal', r.id, 'Zanzibar City Municipal Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MJI';

-- Insert Districts for Morogoro Region
INSERT INTO districts (code, name, region_id, description, status, created_at, updated_at)
SELECT 'MOR-GR', 'Gairo', r.id, 'Gairo District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MOR'
UNION ALL SELECT 'MOR-IF', 'Ifakara Town', r.id, 'Ifakara Town Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MOR'
UNION ALL SELECT 'MOR-KL', 'Kilosa', r.id, 'Kilosa District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MOR'
UNION ALL SELECT 'MOR-ML', 'Malinyi', r.id, 'Malinyi District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MOR'
UNION ALL SELECT 'MOR-MB', 'Mlimba', r.id, 'Mlimba District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MOR'
UNION ALL SELECT 'MOR-MC', 'Morogoro Municipal', r.id, 'Morogoro Municipal Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MOR'
UNION ALL SELECT 'MOR-RD', 'Morogoro Rural', r.id, 'Morogoro Rural District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MOR'
UNION ALL SELECT 'MOR-MV', 'Mvomero', r.id, 'Mvomero District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MOR'
UNION ALL SELECT 'MOR-UL', 'Ulanga', r.id, 'Ulanga District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MOR';

-- Insert Districts for Mtwara Region
INSERT INTO districts (code, name, region_id, description, status, created_at, updated_at)
SELECT 'MTW-MR', 'Masasi Rural', r.id, 'Masasi Rural District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MTW'
UNION ALL SELECT 'MTW-MT', 'Masasi Town', r.id, 'Masasi Town Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MTW'
UNION ALL SELECT 'MTW-MC', 'Mtwara Municipal', r.id, 'Mtwara Municipal Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MTW'
UNION ALL SELECT 'MTW-RD', 'Mtwara Rural', r.id, 'Mtwara Rural District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MTW'
UNION ALL SELECT 'MTW-NY', 'Nanyamba Town', r.id, 'Nanyamba Town Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MTW'
UNION ALL SELECT 'MTW-NU', 'Nanyumbu', r.id, 'Nanyumbu District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MTW'
UNION ALL SELECT 'MTW-NR', 'Newala Rural', r.id, 'Newala Rural District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MTW'
UNION ALL SELECT 'MTW-NT', 'Newala Town', r.id, 'Newala Town Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MTW'
UNION ALL SELECT 'MTW-TH', 'Tandahimba', r.id, 'Tandahimba District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MTW';

-- Insert Districts for Mwanza Region
INSERT INTO districts (code, name, region_id, description, status, created_at, updated_at)
SELECT 'MWA-BC', 'Buchosa', r.id, 'Buchosa District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MWA'
UNION ALL SELECT 'MWA-IL', 'Ilemela Municipal', r.id, 'Ilemela Municipal Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MWA'
UNION ALL SELECT 'MWA-KW', 'Kwimba', r.id, 'Kwimba District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MWA'
UNION ALL SELECT 'MWA-MG', 'Magu', r.id, 'Magu District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MWA'
UNION ALL SELECT 'MWA-MS', 'Misungwi', r.id, 'Misungwi District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MWA'
UNION ALL SELECT 'MWA-CC', 'Nyamagana Municipal', r.id, 'Nyamagana Municipal Council (Mwanza City)', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MWA'
UNION ALL SELECT 'MWA-SE', 'Sengerema', r.id, 'Sengerema District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MWA'
UNION ALL SELECT 'MWA-UK', 'Ukerewe', r.id, 'Ukerewe District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'MWA';

-- Insert Districts for Njombe Region
INSERT INTO districts (code, name, region_id, description, status, created_at, updated_at)
SELECT 'NJO-LD', 'Ludewa', r.id, 'Ludewa District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'NJO'
UNION ALL SELECT 'NJO-MK', 'Makambako Town', r.id, 'Makambako Town Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'NJO'
UNION ALL SELECT 'NJO-MT', 'Makete', r.id, 'Makete District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'NJO'
UNION ALL SELECT 'NJO-RD', 'Njombe Rural', r.id, 'Njombe Rural District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'NJO'
UNION ALL SELECT 'NJO-TC', 'Njombe Town', r.id, 'Njombe Town Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'NJO'
UNION ALL SELECT 'NJO-WN', 'Wanging\'ombe', r.id, 'Wanging\'ombe District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'NJO';

-- Insert Districts for Pwani Region
INSERT INTO districts (code, name, region_id, description, status, created_at, updated_at)
SELECT 'PWA-BG', 'Bagamoyo', r.id, 'Bagamoyo District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'PWA'
UNION ALL SELECT 'PWA-CH', 'Chalinze', r.id, 'Chalinze District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'PWA'
UNION ALL SELECT 'PWA-KB', 'Kibaha', r.id, 'Kibaha District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'PWA'
UNION ALL SELECT 'PWA-KT', 'Kibaha Town', r.id, 'Kibaha Town Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'PWA'
UNION ALL SELECT 'PWA-KI', 'Kibiti', r.id, 'Kibiti District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'PWA'
UNION ALL SELECT 'PWA-KS', 'Kisarawe', r.id, 'Kisarawe District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'PWA'
UNION ALL SELECT 'PWA-MF', 'Mafia', r.id, 'Mafia District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'PWA'
UNION ALL SELECT 'PWA-MK', 'Mkuranga', r.id, 'Mkuranga District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'PWA'
UNION ALL SELECT 'PWA-RF', 'Rufiji', r.id, 'Rufiji District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'PWA';

-- Insert Districts for Rukwa Region
INSERT INTO districts (code, name, region_id, description, status, created_at, updated_at)
SELECT 'RUK-KL', 'Kalambo', r.id, 'Kalambo District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'RUK'
UNION ALL SELECT 'RUK-NK', 'Nkasi', r.id, 'Nkasi District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'RUK'
UNION ALL SELECT 'RUK-MC', 'Sumbawanga Municipal', r.id, 'Sumbawanga Municipal Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'RUK'
UNION ALL SELECT 'RUK-RD', 'Sumbawanga Rural', r.id, 'Sumbawanga Rural District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'RUK';

-- Insert Districts for Ruvuma Region
INSERT INTO districts (code, name, region_id, description, status, created_at, updated_at)
SELECT 'RUV-MD', 'Madaba', r.id, 'Madaba District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'RUV'
UNION ALL SELECT 'RUV-MR', 'Mbinga Rural', r.id, 'Mbinga Rural District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'RUV'
UNION ALL SELECT 'RUV-MT', 'Mbinga Town', r.id, 'Mbinga Town Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'RUV'
UNION ALL SELECT 'RUV-NM', 'Namtumbo', r.id, 'Namtumbo District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'RUV'
UNION ALL SELECT 'RUV-NY', 'Nyasa', r.id, 'Nyasa District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'RUV'
UNION ALL SELECT 'RUV-MC', 'Songea Municipal', r.id, 'Songea Municipal Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'RUV'
UNION ALL SELECT 'RUV-SR', 'Songea Rural', r.id, 'Songea Rural District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'RUV'
UNION ALL SELECT 'RUV-TU', 'Tunduru', r.id, 'Tunduru District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'RUV';

-- Insert Districts for Shinyanga Region
INSERT INTO districts (code, name, region_id, description, status, created_at, updated_at)
SELECT 'SHI-KM', 'Kahama Municipality', r.id, 'Kahama Municipal Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'SHI'
UNION ALL SELECT 'SHI-KS', 'Kishapu', r.id, 'Kishapu District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'SHI'
UNION ALL SELECT 'SHI-MS', 'Msalala', r.id, 'Msalala District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'SHI'
UNION ALL SELECT 'SHI-MC', 'Shinyanga Municipal', r.id, 'Shinyanga Municipal Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'SHI'
UNION ALL SELECT 'SHI-RD', 'Shinyanga Rural', r.id, 'Shinyanga Rural District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'SHI'
UNION ALL SELECT 'SHI-US', 'Ushetu', r.id, 'Ushetu District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'SHI';

-- Insert Districts for Simiyu Region
INSERT INTO districts (code, name, region_id, description, status, created_at, updated_at)
SELECT 'SIM-BR', 'Bariadi Rural', r.id, 'Bariadi Rural District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'SIM'
UNION ALL SELECT 'SIM-BT', 'Bariadi Town', r.id, 'Bariadi Town Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'SIM'
UNION ALL SELECT 'SIM-BU', 'Busega', r.id, 'Busega District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'SIM'
UNION ALL SELECT 'SIM-IT', 'Itilima', r.id, 'Itilima District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'SIM'
UNION ALL SELECT 'SIM-MA', 'Maswa', r.id, 'Maswa District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'SIM'
UNION ALL SELECT 'SIM-ME', 'Meatu', r.id, 'Meatu District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'SIM';

-- Insert Districts for Singida Region
INSERT INTO districts (code, name, region_id, description, status, created_at, updated_at)
SELECT 'SIN-IK', 'Ikungi', r.id, 'Ikungi District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'SIN'
UNION ALL SELECT 'SIN-IR', 'Iramba', r.id, 'Iramba District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'SIN'
UNION ALL SELECT 'SIN-IT', 'Itigi', r.id, 'Itigi District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'SIN'
UNION ALL SELECT 'SIN-MN', 'Manyoni', r.id, 'Manyoni District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'SIN'
UNION ALL SELECT 'SIN-MK', 'Mkalama', r.id, 'Mkalama District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'SIN'
UNION ALL SELECT 'SIN-MC', 'Singida Municipal', r.id, 'Singida Municipal Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'SIN'
UNION ALL SELECT 'SIN-RD', 'Singida Rural', r.id, 'Singida Rural District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'SIN';

-- Insert Districts for Songwe Region
INSERT INTO districts (code, name, region_id, description, status, created_at, updated_at)
SELECT 'SON-IL', 'Ileje', r.id, 'Ileje District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'SON'
UNION ALL SELECT 'SON-MB', 'Mbozi', r.id, 'Mbozi District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'SON'
UNION ALL SELECT 'SON-MM', 'Momba', r.id, 'Momba District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'SON'
UNION ALL SELECT 'SON-SO', 'Songwe', r.id, 'Songwe District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'SON'
UNION ALL SELECT 'SON-TU', 'Tunduma Town', r.id, 'Tunduma Town Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'SON';

-- Insert Districts for Tabora Region
INSERT INTO districts (code, name, region_id, description, status, created_at, updated_at)
SELECT 'TAB-IG', 'Igunga', r.id, 'Igunga District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'TAB'
UNION ALL SELECT 'TAB-KL', 'Kaliua', r.id, 'Kaliua District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'TAB'
UNION ALL SELECT 'TAB-NR', 'Nzega Rural', r.id, 'Nzega Rural District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'TAB'
UNION ALL SELECT 'TAB-NT', 'Nzega Town', r.id, 'Nzega Town Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'TAB'
UNION ALL SELECT 'TAB-SI', 'Sikonge', r.id, 'Sikonge District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'TAB'
UNION ALL SELECT 'TAB-MC', 'Tabora Municipal', r.id, 'Tabora Municipal Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'TAB'
UNION ALL SELECT 'TAB-UR', 'Urambo', r.id, 'Urambo District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'TAB'
UNION ALL SELECT 'TAB-UY', 'Uyui', r.id, 'Uyui District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'TAB';

-- Insert Districts for Tanga Region
INSERT INTO districts (code, name, region_id, description, status, created_at, updated_at)
SELECT 'TAN-BU', 'Bumbuli', r.id, 'Bumbuli District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'TAN'
UNION ALL SELECT 'TAN-HR', 'Handeni Rural', r.id, 'Handeni Rural District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'TAN'
UNION ALL SELECT 'TAN-HT', 'Handeni Town', r.id, 'Handeni Town Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'TAN'
UNION ALL SELECT 'TAN-KL', 'Kilindi', r.id, 'Kilindi District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'TAN'
UNION ALL SELECT 'TAN-KR', 'Korogwe Rural', r.id, 'Korogwe Rural District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'TAN'
UNION ALL SELECT 'TAN-KT', 'Korogwe Town', r.id, 'Korogwe Town Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'TAN'
UNION ALL SELECT 'TAN-LU', 'Lushoto', r.id, 'Lushoto District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'TAN'
UNION ALL SELECT 'TAN-MK', 'Mkinga', r.id, 'Mkinga District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'TAN'
UNION ALL SELECT 'TAN-MU', 'Muheza', r.id, 'Muheza District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'TAN'
UNION ALL SELECT 'TAN-PA', 'Pangani', r.id, 'Pangani District', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'TAN'
UNION ALL SELECT 'TAN-CC', 'Tanga City', r.id, 'Tanga City Council', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP FROM regions r WHERE r.code = 'TAN';
