USE finance_app;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
DELETE FROM files WHERE entity_type_id = ( SELECT id FROM entity_types WHERE code='countries' );
TRUNCATE TABLE country_languages;
TRUNCATE TABLE country_timezones;
TRUNCATE TABLE currency_countries;
TRUNCATE TABLE languages;
TRUNCATE TABLE timezones;
TRUNCATE TABLE currencies;
TRUNCATE TABLE countries;
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('AF', 'AFG', '004', NULL, 'Afghanistan', 'افغانستان', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'af.png',
            NULL,
            'https://flagcdn.com/w320/af.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'AF';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('AX', 'ALA', '248', NULL, 'Aland Islands', 'Åland', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'ax.png',
            NULL,
            'https://flagcdn.com/w320/ax.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'AX';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('AL', 'ALB', '008', NULL, 'Albania', 'Shqipëria', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'al.png',
            NULL,
            'https://flagcdn.com/w320/al.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'AL';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('DZ', 'DZA', '012', NULL, 'Algeria', 'الجزائر', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'dz.png',
            NULL,
            'https://flagcdn.com/w320/dz.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'DZ';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('AS', 'ASM', '016', NULL, 'American Samoa', 'American Samoa', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'as.png',
            NULL,
            'https://flagcdn.com/w320/as.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'AS';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('AD', 'AND', '020', NULL, 'Andorra', 'Andorra', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'ad.png',
            NULL,
            'https://flagcdn.com/w320/ad.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'AD';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('AO', 'AGO', '024', NULL, 'Angola', 'Angola', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'ao.png',
            NULL,
            'https://flagcdn.com/w320/ao.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'AO';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('AI', 'AIA', '660', NULL, 'Anguilla', 'Anguilla', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'ai.png',
            NULL,
            'https://flagcdn.com/w320/ai.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'AI';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('AQ', 'ATA', '010', NULL, 'Antarctica', 'Antarctica', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'aq.png',
            NULL,
            'https://flagcdn.com/w320/aq.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'AQ';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('AG', 'ATG', '028', NULL, 'Antigua and Barbuda', 'Antigua and Barbuda', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'ag.png',
            NULL,
            'https://flagcdn.com/w320/ag.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'AG';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('AR', 'ARG', '032', NULL, 'Argentina', 'Argentina', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'ar.png',
            NULL,
            'https://flagcdn.com/w320/ar.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'AR';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('AM', 'ARM', '051', NULL, 'Armenia', 'Հայաստան', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'am.png',
            NULL,
            'https://flagcdn.com/w320/am.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'AM';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('AW', 'ABW', '533', NULL, 'Aruba', 'Aruba', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'aw.png',
            NULL,
            'https://flagcdn.com/w320/aw.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'AW';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('AU', 'AUS', '036', NULL, 'Australia', 'Australia', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'au.png',
            NULL,
            'https://flagcdn.com/w320/au.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'AU';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('AT', 'AUT', '040', NULL, 'Austria', 'Österreich', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'at.png',
            NULL,
            'https://flagcdn.com/w320/at.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'AT';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('AZ', 'AZE', '031', NULL, 'Azerbaijan', 'Azərbaycan', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'az.png',
            NULL,
            'https://flagcdn.com/w320/az.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'AZ';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('BH', 'BHR', '048', NULL, 'Bahrain', '‏البحرين', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'bh.png',
            NULL,
            'https://flagcdn.com/w320/bh.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'BH';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('BD', 'BGD', '050', NULL, 'Bangladesh', 'Bangladesh', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'bd.png',
            NULL,
            'https://flagcdn.com/w320/bd.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'BD';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('BB', 'BRB', '052', NULL, 'Barbados', 'Barbados', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'bb.png',
            NULL,
            'https://flagcdn.com/w320/bb.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'BB';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('BY', 'BLR', '112', NULL, 'Belarus', 'Белару́сь', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'by.png',
            NULL,
            'https://flagcdn.com/w320/by.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'BY';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('BE', 'BEL', '056', NULL, 'Belgium', 'België', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'be.png',
            NULL,
            'https://flagcdn.com/w320/be.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'BE';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('BZ', 'BLZ', '084', NULL, 'Belize', 'Belize', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'bz.png',
            NULL,
            'https://flagcdn.com/w320/bz.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'BZ';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('BJ', 'BEN', '204', NULL, 'Benin', 'Bénin', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'bj.png',
            NULL,
            'https://flagcdn.com/w320/bj.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'BJ';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('BM', 'BMU', '060', NULL, 'Bermuda', 'Bermuda', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'bm.png',
            NULL,
            'https://flagcdn.com/w320/bm.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'BM';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('BT', 'BTN', '064', NULL, 'Bhutan', 'ʼbrug-yul', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'bt.png',
            NULL,
            'https://flagcdn.com/w320/bt.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'BT';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('BO', 'BOL', '068', NULL, 'Bolivia', 'Bolivia', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'bo.png',
            NULL,
            'https://flagcdn.com/w320/bo.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'BO';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('BQ', 'BES', '535', NULL, 'Bonaire, Sint Eustatius and Saba', 'Caribisch Nederland', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'bq.png',
            NULL,
            'https://flagcdn.com/w320/bq.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'BQ';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('BA', 'BIH', '070', NULL, 'Bosnia and Herzegovina', 'Bosna i Hercegovina', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'ba.png',
            NULL,
            'https://flagcdn.com/w320/ba.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'BA';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('BW', 'BWA', '072', NULL, 'Botswana', 'Botswana', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'bw.png',
            NULL,
            'https://flagcdn.com/w320/bw.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'BW';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('BV', 'BVT', '074', NULL, 'Bouvet Island', 'Bouvetøya', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'bv.png',
            NULL,
            'https://flagcdn.com/w320/bv.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'BV';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('BR', 'BRA', '076', NULL, 'Brazil', 'Brasil', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'br.png',
            NULL,
            'https://flagcdn.com/w320/br.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'BR';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('IO', 'IOT', '086', NULL, 'British Indian Ocean Territory', 'British Indian Ocean Territory', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'io.png',
            NULL,
            'https://flagcdn.com/w320/io.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'IO';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('BN', 'BRN', '096', NULL, 'Brunei', 'Negara Brunei Darussalam', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'bn.png',
            NULL,
            'https://flagcdn.com/w320/bn.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'BN';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('BG', 'BGR', '100', NULL, 'Bulgaria', 'България', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'bg.png',
            NULL,
            'https://flagcdn.com/w320/bg.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'BG';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('BF', 'BFA', '854', NULL, 'Burkina Faso', 'Burkina Faso', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'bf.png',
            NULL,
            'https://flagcdn.com/w320/bf.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'BF';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('BI', 'BDI', '108', NULL, 'Burundi', 'Burundi', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'bi.png',
            NULL,
            'https://flagcdn.com/w320/bi.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'BI';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('KH', 'KHM', '116', NULL, 'Cambodia', 'Kâmpŭchéa', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'kh.png',
            NULL,
            'https://flagcdn.com/w320/kh.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'KH';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('CM', 'CMR', '120', NULL, 'Cameroon', 'Cameroon', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'cm.png',
            NULL,
            'https://flagcdn.com/w320/cm.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'CM';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('CA', 'CAN', '124', NULL, 'Canada', 'Canada', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'ca.png',
            NULL,
            'https://flagcdn.com/w320/ca.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'CA';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('CV', 'CPV', '132', NULL, 'Cape Verde', 'Cabo Verde', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'cv.png',
            NULL,
            'https://flagcdn.com/w320/cv.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'CV';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('KY', 'CYM', '136', NULL, 'Cayman Islands', 'Cayman Islands', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'ky.png',
            NULL,
            'https://flagcdn.com/w320/ky.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'KY';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('CF', 'CAF', '140', NULL, 'Central African Republic', 'Ködörösêse tî Bêafrîka', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'cf.png',
            NULL,
            'https://flagcdn.com/w320/cf.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'CF';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('TD', 'TCD', '148', NULL, 'Chad', 'Tchad', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'td.png',
            NULL,
            'https://flagcdn.com/w320/td.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'TD';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('CL', 'CHL', '152', NULL, 'Chile', 'Chile', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'cl.png',
            NULL,
            'https://flagcdn.com/w320/cl.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'CL';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('CN', 'CHN', '156', NULL, 'China', '中国', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'cn.png',
            NULL,
            'https://flagcdn.com/w320/cn.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'CN';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('CX', 'CXR', '162', NULL, 'Christmas Island', 'Christmas Island', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'cx.png',
            NULL,
            'https://flagcdn.com/w320/cx.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'CX';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('CC', 'CCK', '166', NULL, 'Cocos (Keeling) Islands', 'Cocos (Keeling) Islands', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'cc.png',
            NULL,
            'https://flagcdn.com/w320/cc.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'CC';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('CO', 'COL', '170', NULL, 'Colombia', 'Colombia', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'co.png',
            NULL,
            'https://flagcdn.com/w320/co.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'CO';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('KM', 'COM', '174', NULL, 'Comoros', 'Komori', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'km.png',
            NULL,
            'https://flagcdn.com/w320/km.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'KM';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('CG', 'COG', '178', NULL, 'Congo', 'République du Congo', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'cg.png',
            NULL,
            'https://flagcdn.com/w320/cg.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'CG';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('CK', 'COK', '184', NULL, 'Cook Islands', 'Cook Islands', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'ck.png',
            NULL,
            'https://flagcdn.com/w320/ck.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'CK';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('CR', 'CRI', '188', NULL, 'Costa Rica', 'Costa Rica', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'cr.png',
            NULL,
            'https://flagcdn.com/w320/cr.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'CR';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('HR', 'HRV', '191', NULL, 'Croatia', 'Hrvatska', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'hr.png',
            NULL,
            'https://flagcdn.com/w320/hr.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'HR';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('CU', 'CUB', '192', NULL, 'Cuba', 'Cuba', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'cu.png',
            NULL,
            'https://flagcdn.com/w320/cu.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'CU';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('CW', 'CUW', '531', NULL, 'Curaçao', 'Curaçao', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'cw.png',
            NULL,
            'https://flagcdn.com/w320/cw.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'CW';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('CY', 'CYP', '196', NULL, 'Cyprus', 'Κύπρος', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'cy.png',
            NULL,
            'https://flagcdn.com/w320/cy.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'CY';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('CZ', 'CZE', '203', NULL, 'Czech Republic', 'Česká republika', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'cz.png',
            NULL,
            'https://flagcdn.com/w320/cz.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'CZ';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('CD', 'COD', '180', NULL, 'Democratic Republic of the Congo', 'République démocratique du Congo', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'cd.png',
            NULL,
            'https://flagcdn.com/w320/cd.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'CD';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('DK', 'DNK', '208', NULL, 'Denmark', 'Danmark', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'dk.png',
            NULL,
            'https://flagcdn.com/w320/dk.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'DK';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('DJ', 'DJI', '262', NULL, 'Djibouti', 'Djibouti', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'dj.png',
            NULL,
            'https://flagcdn.com/w320/dj.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'DJ';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('DM', 'DMA', '212', NULL, 'Dominica', 'Dominica', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'dm.png',
            NULL,
            'https://flagcdn.com/w320/dm.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'DM';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('DO', 'DOM', '214', NULL, 'Dominican Republic', 'República Dominicana', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'do.png',
            NULL,
            'https://flagcdn.com/w320/do.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'DO';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('EC', 'ECU', '218', NULL, 'Ecuador', 'Ecuador', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'ec.png',
            NULL,
            'https://flagcdn.com/w320/ec.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'EC';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('EG', 'EGY', '818', NULL, 'Egypt', 'مصر‎', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'eg.png',
            NULL,
            'https://flagcdn.com/w320/eg.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'EG';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('SV', 'SLV', '222', NULL, 'El Salvador', 'El Salvador', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'sv.png',
            NULL,
            'https://flagcdn.com/w320/sv.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'SV';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('GQ', 'GNQ', '226', NULL, 'Equatorial Guinea', 'Guinea Ecuatorial', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'gq.png',
            NULL,
            'https://flagcdn.com/w320/gq.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'GQ';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('ER', 'ERI', '232', NULL, 'Eritrea', 'ኤርትራ', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'er.png',
            NULL,
            'https://flagcdn.com/w320/er.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'ER';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('EE', 'EST', '233', NULL, 'Estonia', 'Eesti', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'ee.png',
            NULL,
            'https://flagcdn.com/w320/ee.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'EE';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('SZ', 'SWZ', '748', NULL, 'Eswatini', 'Swaziland', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'sz.png',
            NULL,
            'https://flagcdn.com/w320/sz.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'SZ';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('ET', 'ETH', '231', NULL, 'Ethiopia', 'ኢትዮጵያ', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'et.png',
            NULL,
            'https://flagcdn.com/w320/et.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'ET';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('FK', 'FLK', '238', NULL, 'Falkland Islands', 'Falkland Islands', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'fk.png',
            NULL,
            'https://flagcdn.com/w320/fk.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'FK';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('FO', 'FRO', '234', NULL, 'Faroe Islands', 'Føroyar', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'fo.png',
            NULL,
            'https://flagcdn.com/w320/fo.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'FO';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('FJ', 'FJI', '242', NULL, 'Fiji Islands', 'Fiji', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'fj.png',
            NULL,
            'https://flagcdn.com/w320/fj.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'FJ';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('FI', 'FIN', '246', NULL, 'Finland', 'Suomi', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'fi.png',
            NULL,
            'https://flagcdn.com/w320/fi.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'FI';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('FR', 'FRA', '250', NULL, 'France', 'France', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'fr.png',
            NULL,
            'https://flagcdn.com/w320/fr.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'FR';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('GF', 'GUF', '254', NULL, 'French Guiana', 'Guyane française', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'gf.png',
            NULL,
            'https://flagcdn.com/w320/gf.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'GF';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('PF', 'PYF', '258', NULL, 'French Polynesia', 'Polynésie française', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'pf.png',
            NULL,
            'https://flagcdn.com/w320/pf.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'PF';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('TF', 'ATF', '260', NULL, 'French Southern Territories', 'Territoire des Terres australes et antarctiques fr', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'tf.png',
            NULL,
            'https://flagcdn.com/w320/tf.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'TF';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('GA', 'GAB', '266', NULL, 'Gabon', 'Gabon', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'ga.png',
            NULL,
            'https://flagcdn.com/w320/ga.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'GA';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('GE', 'GEO', '268', NULL, 'Georgia', 'საქართველო', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'ge.png',
            NULL,
            'https://flagcdn.com/w320/ge.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'GE';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('DE', 'DEU', '276', NULL, 'Germany', 'Deutschland', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'de.png',
            NULL,
            'https://flagcdn.com/w320/de.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'DE';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('GH', 'GHA', '288', NULL, 'Ghana', 'Ghana', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'gh.png',
            NULL,
            'https://flagcdn.com/w320/gh.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'GH';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('GI', 'GIB', '292', NULL, 'Gibraltar', 'Gibraltar', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'gi.png',
            NULL,
            'https://flagcdn.com/w320/gi.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'GI';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('GR', 'GRC', '300', NULL, 'Greece', 'Ελλάδα', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'gr.png',
            NULL,
            'https://flagcdn.com/w320/gr.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'GR';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('GL', 'GRL', '304', NULL, 'Greenland', 'Kalaallit Nunaat', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'gl.png',
            NULL,
            'https://flagcdn.com/w320/gl.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'GL';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('GD', 'GRD', '308', NULL, 'Grenada', 'Grenada', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'gd.png',
            NULL,
            'https://flagcdn.com/w320/gd.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'GD';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('GP', 'GLP', '312', NULL, 'Guadeloupe', 'Guadeloupe', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'gp.png',
            NULL,
            'https://flagcdn.com/w320/gp.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'GP';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('GU', 'GUM', '316', NULL, 'Guam', 'Guam', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'gu.png',
            NULL,
            'https://flagcdn.com/w320/gu.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'GU';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('GT', 'GTM', '320', NULL, 'Guatemala', 'Guatemala', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'gt.png',
            NULL,
            'https://flagcdn.com/w320/gt.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'GT';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('GG', 'GGY', '831', NULL, 'Guernsey', 'Guernsey', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'gg.png',
            NULL,
            'https://flagcdn.com/w320/gg.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'GG';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('GN', 'GIN', '324', NULL, 'Guinea', 'Guinée', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'gn.png',
            NULL,
            'https://flagcdn.com/w320/gn.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'GN';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('GW', 'GNB', '624', NULL, 'Guinea-Bissau', 'Guiné-Bissau', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'gw.png',
            NULL,
            'https://flagcdn.com/w320/gw.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'GW';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('GY', 'GUY', '328', NULL, 'Guyana', 'Guyana', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'gy.png',
            NULL,
            'https://flagcdn.com/w320/gy.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'GY';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('HT', 'HTI', '332', NULL, 'Haiti', 'Haïti', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'ht.png',
            NULL,
            'https://flagcdn.com/w320/ht.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'HT';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('HM', 'HMD', '334', NULL, 'Heard Island and McDonald Islands', 'Heard Island and McDonald Islands', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'hm.png',
            NULL,
            'https://flagcdn.com/w320/hm.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'HM';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('HN', 'HND', '340', NULL, 'Honduras', 'Honduras', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'hn.png',
            NULL,
            'https://flagcdn.com/w320/hn.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'HN';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('HK', 'HKG', '344', NULL, 'Hong Kong S.A.R.', '香港', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'hk.png',
            NULL,
            'https://flagcdn.com/w320/hk.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'HK';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('HU', 'HUN', '348', NULL, 'Hungary', 'Magyarország', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'hu.png',
            NULL,
            'https://flagcdn.com/w320/hu.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'HU';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('IS', 'ISL', '352', NULL, 'Iceland', 'Ísland', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'is.png',
            NULL,
            'https://flagcdn.com/w320/is.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'IS';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('IN', 'IND', '356', NULL, 'India', 'भारत', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'in.png',
            NULL,
            'https://flagcdn.com/w320/in.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'IN';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('ID', 'IDN', '360', NULL, 'Indonesia', 'Indonesia', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'id.png',
            NULL,
            'https://flagcdn.com/w320/id.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'ID';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('IR', 'IRN', '364', NULL, 'Iran', 'ایران', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'ir.png',
            NULL,
            'https://flagcdn.com/w320/ir.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'IR';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('IQ', 'IRQ', '368', NULL, 'Iraq', 'العراق', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'iq.png',
            NULL,
            'https://flagcdn.com/w320/iq.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'IQ';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('IE', 'IRL', '372', NULL, 'Ireland', 'Éire', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'ie.png',
            NULL,
            'https://flagcdn.com/w320/ie.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'IE';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('IL', 'ISR', '376', NULL, 'Israel', 'יִשְׂרָאֵל', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'il.png',
            NULL,
            'https://flagcdn.com/w320/il.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'IL';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('IT', 'ITA', '380', NULL, 'Italy', 'Italia', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'it.png',
            NULL,
            'https://flagcdn.com/w320/it.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'IT';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('CI', 'CIV', '384', NULL, 'Ivory Coast', 'Cote D''Ivoire', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'ci.png',
            NULL,
            'https://flagcdn.com/w320/ci.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'CI';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('JM', 'JAM', '388', NULL, 'Jamaica', 'Jamaica', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'jm.png',
            NULL,
            'https://flagcdn.com/w320/jm.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'JM';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('JP', 'JPN', '392', NULL, 'Japan', '日本', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'jp.png',
            NULL,
            'https://flagcdn.com/w320/jp.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'JP';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('JE', 'JEY', '832', NULL, 'Jersey', 'Jersey', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'je.png',
            NULL,
            'https://flagcdn.com/w320/je.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'JE';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('JO', 'JOR', '400', NULL, 'Jordan', 'الأردن', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'jo.png',
            NULL,
            'https://flagcdn.com/w320/jo.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'JO';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('KZ', 'KAZ', '398', NULL, 'Kazakhstan', 'Қазақстан', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'kz.png',
            NULL,
            'https://flagcdn.com/w320/kz.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'KZ';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('KE', 'KEN', '404', NULL, 'Kenya', 'Kenya', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'ke.png',
            NULL,
            'https://flagcdn.com/w320/ke.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'KE';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('KI', 'KIR', '296', NULL, 'Kiribati', 'Kiribati', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'ki.png',
            NULL,
            'https://flagcdn.com/w320/ki.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'KI';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('XK', 'XKX', '926', NULL, 'Kosovo', 'Republika e Kosovës', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'xk.png',
            NULL,
            'https://flagcdn.com/w320/xk.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'XK';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('KW', 'KWT', '414', NULL, 'Kuwait', 'الكويت', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'kw.png',
            NULL,
            'https://flagcdn.com/w320/kw.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'KW';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('KG', 'KGZ', '417', NULL, 'Kyrgyzstan', 'Кыргызстан', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'kg.png',
            NULL,
            'https://flagcdn.com/w320/kg.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'KG';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('LA', 'LAO', '418', NULL, 'Laos', 'ສປປລາວ', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'la.png',
            NULL,
            'https://flagcdn.com/w320/la.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'LA';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('LV', 'LVA', '428', NULL, 'Latvia', 'Latvija', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'lv.png',
            NULL,
            'https://flagcdn.com/w320/lv.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'LV';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('LB', 'LBN', '422', NULL, 'Lebanon', 'لبنان', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'lb.png',
            NULL,
            'https://flagcdn.com/w320/lb.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'LB';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('LS', 'LSO', '426', NULL, 'Lesotho', 'Lesotho', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'ls.png',
            NULL,
            'https://flagcdn.com/w320/ls.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'LS';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('LR', 'LBR', '430', NULL, 'Liberia', 'Liberia', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'lr.png',
            NULL,
            'https://flagcdn.com/w320/lr.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'LR';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('LY', 'LBY', '434', NULL, 'Libya', '‏ليبيا', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'ly.png',
            NULL,
            'https://flagcdn.com/w320/ly.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'LY';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('LI', 'LIE', '438', NULL, 'Liechtenstein', 'Liechtenstein', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'li.png',
            NULL,
            'https://flagcdn.com/w320/li.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'LI';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('LT', 'LTU', '440', NULL, 'Lithuania', 'Lietuva', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'lt.png',
            NULL,
            'https://flagcdn.com/w320/lt.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'LT';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('LU', 'LUX', '442', NULL, 'Luxembourg', 'Luxembourg', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'lu.png',
            NULL,
            'https://flagcdn.com/w320/lu.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'LU';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('MO', 'MAC', '446', NULL, 'Macau S.A.R.', '澳門', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'mo.png',
            NULL,
            'https://flagcdn.com/w320/mo.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'MO';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('MG', 'MDG', '450', NULL, 'Madagascar', 'Madagasikara', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'mg.png',
            NULL,
            'https://flagcdn.com/w320/mg.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'MG';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('MW', 'MWI', '454', NULL, 'Malawi', 'Malawi', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'mw.png',
            NULL,
            'https://flagcdn.com/w320/mw.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'MW';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('MY', 'MYS', '458', NULL, 'Malaysia', 'Malaysia', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'my.png',
            NULL,
            'https://flagcdn.com/w320/my.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'MY';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('MV', 'MDV', '462', NULL, 'Maldives', 'Maldives', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'mv.png',
            NULL,
            'https://flagcdn.com/w320/mv.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'MV';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('ML', 'MLI', '466', NULL, 'Mali', 'Mali', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'ml.png',
            NULL,
            'https://flagcdn.com/w320/ml.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'ML';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('MT', 'MLT', '470', NULL, 'Malta', 'Malta', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'mt.png',
            NULL,
            'https://flagcdn.com/w320/mt.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'MT';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('IM', 'IMN', '833', NULL, 'Man (Isle of)', 'Isle of Man', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'im.png',
            NULL,
            'https://flagcdn.com/w320/im.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'IM';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('MH', 'MHL', '584', NULL, 'Marshall Islands', 'M̧ajeļ', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'mh.png',
            NULL,
            'https://flagcdn.com/w320/mh.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'MH';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('MQ', 'MTQ', '474', NULL, 'Martinique', 'Martinique', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'mq.png',
            NULL,
            'https://flagcdn.com/w320/mq.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'MQ';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('MR', 'MRT', '478', NULL, 'Mauritania', 'موريتانيا', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'mr.png',
            NULL,
            'https://flagcdn.com/w320/mr.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'MR';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('MU', 'MUS', '480', NULL, 'Mauritius', 'Maurice', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'mu.png',
            NULL,
            'https://flagcdn.com/w320/mu.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'MU';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('YT', 'MYT', '175', NULL, 'Mayotte', 'Mayotte', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'yt.png',
            NULL,
            'https://flagcdn.com/w320/yt.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'YT';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('MX', 'MEX', '484', NULL, 'Mexico', 'México', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'mx.png',
            NULL,
            'https://flagcdn.com/w320/mx.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'MX';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('FM', 'FSM', '583', NULL, 'Micronesia', 'Micronesia', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'fm.png',
            NULL,
            'https://flagcdn.com/w320/fm.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'FM';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('MD', 'MDA', '498', NULL, 'Moldova', 'Moldova', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'md.png',
            NULL,
            'https://flagcdn.com/w320/md.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'MD';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('MC', 'MCO', '492', NULL, 'Monaco', 'Monaco', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'mc.png',
            NULL,
            'https://flagcdn.com/w320/mc.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'MC';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('MN', 'MNG', '496', NULL, 'Mongolia', 'Монгол улс', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'mn.png',
            NULL,
            'https://flagcdn.com/w320/mn.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'MN';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('ME', 'MNE', '499', NULL, 'Montenegro', 'Црна Гора', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'me.png',
            NULL,
            'https://flagcdn.com/w320/me.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'ME';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('MS', 'MSR', '500', NULL, 'Montserrat', 'Montserrat', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'ms.png',
            NULL,
            'https://flagcdn.com/w320/ms.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'MS';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('MA', 'MAR', '504', NULL, 'Morocco', 'المغرب', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'ma.png',
            NULL,
            'https://flagcdn.com/w320/ma.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'MA';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('MZ', 'MOZ', '508', NULL, 'Mozambique', 'Moçambique', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'mz.png',
            NULL,
            'https://flagcdn.com/w320/mz.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'MZ';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('MM', 'MMR', '104', NULL, 'Myanmar', 'မြန်မာ', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'mm.png',
            NULL,
            'https://flagcdn.com/w320/mm.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'MM';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('NA', 'NAM', '516', NULL, 'Namibia', 'Namibia', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'na.png',
            NULL,
            'https://flagcdn.com/w320/na.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'NA';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('NR', 'NRU', '520', NULL, 'Nauru', 'Nauru', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'nr.png',
            NULL,
            'https://flagcdn.com/w320/nr.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'NR';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('NP', 'NPL', '524', NULL, 'Nepal', 'नेपाल', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'np.png',
            NULL,
            'https://flagcdn.com/w320/np.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'NP';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('NL', 'NLD', '528', NULL, 'Netherlands', 'Nederland', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'nl.png',
            NULL,
            'https://flagcdn.com/w320/nl.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'NL';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('NC', 'NCL', '540', NULL, 'New Caledonia', 'Nouvelle-Calédonie', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'nc.png',
            NULL,
            'https://flagcdn.com/w320/nc.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'NC';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('NZ', 'NZL', '554', NULL, 'New Zealand', 'New Zealand', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'nz.png',
            NULL,
            'https://flagcdn.com/w320/nz.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'NZ';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('NI', 'NIC', '558', NULL, 'Nicaragua', 'Nicaragua', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'ni.png',
            NULL,
            'https://flagcdn.com/w320/ni.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'NI';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('NE', 'NER', '562', NULL, 'Niger', 'Niger', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'ne.png',
            NULL,
            'https://flagcdn.com/w320/ne.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'NE';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('NG', 'NGA', '566', NULL, 'Nigeria', 'Nigeria', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'ng.png',
            NULL,
            'https://flagcdn.com/w320/ng.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'NG';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('NU', 'NIU', '570', NULL, 'Niue', 'Niuē', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'nu.png',
            NULL,
            'https://flagcdn.com/w320/nu.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'NU';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('NF', 'NFK', '574', NULL, 'Norfolk Island', 'Norfolk Island', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'nf.png',
            NULL,
            'https://flagcdn.com/w320/nf.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'NF';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('KP', 'PRK', '408', NULL, 'North Korea', '북한', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'kp.png',
            NULL,
            'https://flagcdn.com/w320/kp.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'KP';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('MK', 'MKD', '807', NULL, 'North Macedonia', 'Северна Македонија', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'mk.png',
            NULL,
            'https://flagcdn.com/w320/mk.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'MK';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('MP', 'MNP', '580', NULL, 'Northern Mariana Islands', 'Northern Mariana Islands', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'mp.png',
            NULL,
            'https://flagcdn.com/w320/mp.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'MP';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('NO', 'NOR', '578', NULL, 'Norway', 'Norge', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'no.png',
            NULL,
            'https://flagcdn.com/w320/no.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'NO';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('OM', 'OMN', '512', NULL, 'Oman', 'عمان', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'om.png',
            NULL,
            'https://flagcdn.com/w320/om.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'OM';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('PK', 'PAK', '586', NULL, 'Pakistan', 'پاکستان', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'pk.png',
            NULL,
            'https://flagcdn.com/w320/pk.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'PK';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('PW', 'PLW', '585', NULL, 'Palau', 'Palau', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'pw.png',
            NULL,
            'https://flagcdn.com/w320/pw.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'PW';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('PS', 'PSE', '275', NULL, 'Palestinian Territory Occupied', 'فلسطين', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'ps.png',
            NULL,
            'https://flagcdn.com/w320/ps.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'PS';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('PA', 'PAN', '591', NULL, 'Panama', 'Panamá', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'pa.png',
            NULL,
            'https://flagcdn.com/w320/pa.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'PA';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('PG', 'PNG', '598', NULL, 'Papua New Guinea', 'Papua Niugini', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'pg.png',
            NULL,
            'https://flagcdn.com/w320/pg.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'PG';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('PY', 'PRY', '600', NULL, 'Paraguay', 'Paraguay', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'py.png',
            NULL,
            'https://flagcdn.com/w320/py.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'PY';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('PE', 'PER', '604', NULL, 'Peru', 'Perú', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'pe.png',
            NULL,
            'https://flagcdn.com/w320/pe.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'PE';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('PH', 'PHL', '608', NULL, 'Philippines', 'Pilipinas', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'ph.png',
            NULL,
            'https://flagcdn.com/w320/ph.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'PH';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('PN', 'PCN', '612', NULL, 'Pitcairn Island', 'Pitcairn Islands', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'pn.png',
            NULL,
            'https://flagcdn.com/w320/pn.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'PN';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('PL', 'POL', '616', NULL, 'Poland', 'Polska', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'pl.png',
            NULL,
            'https://flagcdn.com/w320/pl.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'PL';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('PT', 'PRT', '620', NULL, 'Portugal', 'Portugal', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'pt.png',
            NULL,
            'https://flagcdn.com/w320/pt.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'PT';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('PR', 'PRI', '630', NULL, 'Puerto Rico', 'Puerto Rico', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'pr.png',
            NULL,
            'https://flagcdn.com/w320/pr.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'PR';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('QA', 'QAT', '634', NULL, 'Qatar', 'قطر', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'qa.png',
            NULL,
            'https://flagcdn.com/w320/qa.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'QA';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('RE', 'REU', '638', NULL, 'Reunion', 'La Réunion', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            're.png',
            NULL,
            'https://flagcdn.com/w320/re.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'RE';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('RO', 'ROU', '642', NULL, 'Romania', 'România', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'ro.png',
            NULL,
            'https://flagcdn.com/w320/ro.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'RO';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('RU', 'RUS', '643', NULL, 'Russia', 'Россия', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'ru.png',
            NULL,
            'https://flagcdn.com/w320/ru.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'RU';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('RW', 'RWA', '646', NULL, 'Rwanda', 'Rwanda', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'rw.png',
            NULL,
            'https://flagcdn.com/w320/rw.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'RW';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('SH', 'SHN', '654', NULL, 'Saint Helena', 'Saint Helena', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'sh.png',
            NULL,
            'https://flagcdn.com/w320/sh.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'SH';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('KN', 'KNA', '659', NULL, 'Saint Kitts and Nevis', 'Saint Kitts and Nevis', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'kn.png',
            NULL,
            'https://flagcdn.com/w320/kn.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'KN';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('LC', 'LCA', '662', NULL, 'Saint Lucia', 'Saint Lucia', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'lc.png',
            NULL,
            'https://flagcdn.com/w320/lc.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'LC';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('PM', 'SPM', '666', NULL, 'Saint Pierre and Miquelon', 'Saint-Pierre-et-Miquelon', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'pm.png',
            NULL,
            'https://flagcdn.com/w320/pm.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'PM';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('VC', 'VCT', '670', NULL, 'Saint Vincent and the Grenadines', 'Saint Vincent and the Grenadines', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'vc.png',
            NULL,
            'https://flagcdn.com/w320/vc.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'VC';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('BL', 'BLM', '652', NULL, 'Saint-Barthelemy', 'Saint-Barthélemy', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'bl.png',
            NULL,
            'https://flagcdn.com/w320/bl.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'BL';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('MF', 'MAF', '663', NULL, 'Saint-Martin (French part)', 'Saint-Martin', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'mf.png',
            NULL,
            'https://flagcdn.com/w320/mf.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'MF';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('WS', 'WSM', '882', NULL, 'Samoa', 'Samoa', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'ws.png',
            NULL,
            'https://flagcdn.com/w320/ws.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'WS';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('SM', 'SMR', '674', NULL, 'San Marino', 'San Marino', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'sm.png',
            NULL,
            'https://flagcdn.com/w320/sm.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'SM';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('ST', 'STP', '678', NULL, 'Sao Tome and Principe', 'São Tomé e Príncipe', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'st.png',
            NULL,
            'https://flagcdn.com/w320/st.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'ST';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('SA', 'SAU', '682', NULL, 'Saudi Arabia', 'المملكة العربية السعودية', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'sa.png',
            NULL,
            'https://flagcdn.com/w320/sa.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'SA';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('SN', 'SEN', '686', NULL, 'Senegal', 'Sénégal', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'sn.png',
            NULL,
            'https://flagcdn.com/w320/sn.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'SN';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('RS', 'SRB', '688', NULL, 'Serbia', 'Србија', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'rs.png',
            NULL,
            'https://flagcdn.com/w320/rs.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'RS';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('SC', 'SYC', '690', NULL, 'Seychelles', 'Seychelles', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'sc.png',
            NULL,
            'https://flagcdn.com/w320/sc.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'SC';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('SL', 'SLE', '694', NULL, 'Sierra Leone', 'Sierra Leone', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'sl.png',
            NULL,
            'https://flagcdn.com/w320/sl.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'SL';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('SG', 'SGP', '702', NULL, 'Singapore', 'Singapore', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'sg.png',
            NULL,
            'https://flagcdn.com/w320/sg.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'SG';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('SX', 'SXM', '534', NULL, 'Sint Maarten (Dutch part)', 'Sint Maarten', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'sx.png',
            NULL,
            'https://flagcdn.com/w320/sx.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'SX';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('SK', 'SVK', '703', NULL, 'Slovakia', 'Slovensko', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'sk.png',
            NULL,
            'https://flagcdn.com/w320/sk.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'SK';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('SI', 'SVN', '705', NULL, 'Slovenia', 'Slovenija', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'si.png',
            NULL,
            'https://flagcdn.com/w320/si.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'SI';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('SB', 'SLB', '090', NULL, 'Solomon Islands', 'Solomon Islands', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'sb.png',
            NULL,
            'https://flagcdn.com/w320/sb.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'SB';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('SO', 'SOM', '706', NULL, 'Somalia', 'Soomaaliya', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'so.png',
            NULL,
            'https://flagcdn.com/w320/so.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'SO';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('ZA', 'ZAF', '710', NULL, 'South Africa', 'South Africa', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'za.png',
            NULL,
            'https://flagcdn.com/w320/za.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'ZA';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('GS', 'SGS', '239', NULL, 'South Georgia', 'South Georgia', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'gs.png',
            NULL,
            'https://flagcdn.com/w320/gs.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'GS';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('KR', 'KOR', '410', NULL, 'South Korea', '대한민국', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'kr.png',
            NULL,
            'https://flagcdn.com/w320/kr.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'KR';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('SS', 'SSD', '728', NULL, 'South Sudan', 'South Sudan', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'ss.png',
            NULL,
            'https://flagcdn.com/w320/ss.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'SS';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('ES', 'ESP', '724', NULL, 'Spain', 'España', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'es.png',
            NULL,
            'https://flagcdn.com/w320/es.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'ES';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('LK', 'LKA', '144', NULL, 'Sri Lanka', 'śrī laṃkāva', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'lk.png',
            NULL,
            'https://flagcdn.com/w320/lk.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'LK';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('SD', 'SDN', '729', NULL, 'Sudan', 'السودان', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'sd.png',
            NULL,
            'https://flagcdn.com/w320/sd.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'SD';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('SR', 'SUR', '740', NULL, 'Suriname', 'Suriname', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'sr.png',
            NULL,
            'https://flagcdn.com/w320/sr.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'SR';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('SJ', 'SJM', '744', NULL, 'Svalbard and Jan Mayen Islands', 'Svalbard og Jan Mayen', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'sj.png',
            NULL,
            'https://flagcdn.com/w320/sj.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'SJ';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('SE', 'SWE', '752', NULL, 'Sweden', 'Sverige', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'se.png',
            NULL,
            'https://flagcdn.com/w320/se.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'SE';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('CH', 'CHE', '756', NULL, 'Switzerland', 'Schweiz', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'ch.png',
            NULL,
            'https://flagcdn.com/w320/ch.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'CH';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('SY', 'SYR', '760', NULL, 'Syria', 'سوريا', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'sy.png',
            NULL,
            'https://flagcdn.com/w320/sy.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'SY';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('TW', 'TWN', '158', NULL, 'Taiwan', '臺灣', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'tw.png',
            NULL,
            'https://flagcdn.com/w320/tw.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'TW';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('TJ', 'TJK', '762', NULL, 'Tajikistan', 'Тоҷикистон', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'tj.png',
            NULL,
            'https://flagcdn.com/w320/tj.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'TJ';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('TZ', 'TZA', '834', NULL, 'Tanzania', 'Tanzania', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'tz.png',
            NULL,
            'https://flagcdn.com/w320/tz.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'TZ';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('TH', 'THA', '764', NULL, 'Thailand', 'ประเทศไทย', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'th.png',
            NULL,
            'https://flagcdn.com/w320/th.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'TH';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('BS', 'BHS', '044', NULL, 'The Bahamas', 'Bahamas', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'bs.png',
            NULL,
            'https://flagcdn.com/w320/bs.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'BS';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('GM', 'GMB', '270', NULL, 'The Gambia', 'Gambia', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'gm.png',
            NULL,
            'https://flagcdn.com/w320/gm.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'GM';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('TL', 'TLS', '626', NULL, 'Timor-Leste', 'Timor-Leste', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'tl.png',
            NULL,
            'https://flagcdn.com/w320/tl.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'TL';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('TG', 'TGO', '768', NULL, 'Togo', 'Togo', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'tg.png',
            NULL,
            'https://flagcdn.com/w320/tg.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'TG';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('TK', 'TKL', '772', NULL, 'Tokelau', 'Tokelau', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'tk.png',
            NULL,
            'https://flagcdn.com/w320/tk.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'TK';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('TO', 'TON', '776', NULL, 'Tonga', 'Tonga', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'to.png',
            NULL,
            'https://flagcdn.com/w320/to.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'TO';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('TT', 'TTO', '780', NULL, 'Trinidad and Tobago', 'Trinidad and Tobago', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'tt.png',
            NULL,
            'https://flagcdn.com/w320/tt.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'TT';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('TN', 'TUN', '788', NULL, 'Tunisia', 'تونس', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'tn.png',
            NULL,
            'https://flagcdn.com/w320/tn.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'TN';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('TR', 'TUR', '792', NULL, 'Turkey', 'Türkiye', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'tr.png',
            NULL,
            'https://flagcdn.com/w320/tr.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'TR';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('TM', 'TKM', '795', NULL, 'Turkmenistan', 'Türkmenistan', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'tm.png',
            NULL,
            'https://flagcdn.com/w320/tm.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'TM';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('TC', 'TCA', '796', NULL, 'Turks and Caicos Islands', 'Turks and Caicos Islands', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'tc.png',
            NULL,
            'https://flagcdn.com/w320/tc.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'TC';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('TV', 'TUV', '798', NULL, 'Tuvalu', 'Tuvalu', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'tv.png',
            NULL,
            'https://flagcdn.com/w320/tv.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'TV';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('UG', 'UGA', '800', NULL, 'Uganda', 'Uganda', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'ug.png',
            NULL,
            'https://flagcdn.com/w320/ug.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'UG';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('UA', 'UKR', '804', NULL, 'Ukraine', 'Україна', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'ua.png',
            NULL,
            'https://flagcdn.com/w320/ua.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'UA';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('AE', 'ARE', '784', NULL, 'United Arab Emirates', 'دولة الإمارات العربية المتحدة', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'ae.png',
            NULL,
            'https://flagcdn.com/w320/ae.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'AE';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('GB', 'GBR', '826', NULL, 'United Kingdom', 'United Kingdom', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'gb.png',
            NULL,
            'https://flagcdn.com/w320/gb.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'GB';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('US', 'USA', '840', NULL, 'United States', 'United States', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'us.png',
            NULL,
            'https://flagcdn.com/w320/us.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'US';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('UM', 'UMI', '581', NULL, 'United States Minor Outlying Islands', 'United States Minor Outlying Islands', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'um.png',
            NULL,
            'https://flagcdn.com/w320/um.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'UM';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('UY', 'URY', '858', NULL, 'Uruguay', 'Uruguay', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'uy.png',
            NULL,
            'https://flagcdn.com/w320/uy.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'UY';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('UZ', 'UZB', '860', NULL, 'Uzbekistan', 'O‘zbekiston', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'uz.png',
            NULL,
            'https://flagcdn.com/w320/uz.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'UZ';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('VU', 'VUT', '548', NULL, 'Vanuatu', 'Vanuatu', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'vu.png',
            NULL,
            'https://flagcdn.com/w320/vu.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'VU';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('VA', 'VAT', '336', NULL, 'Vatican City State (Holy See)', 'Vaticano', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'va.png',
            NULL,
            'https://flagcdn.com/w320/va.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'VA';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('VE', 'VEN', '862', NULL, 'Venezuela', 'Venezuela', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            've.png',
            NULL,
            'https://flagcdn.com/w320/ve.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'VE';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('VN', 'VNM', '704', NULL, 'Vietnam', 'Việt Nam', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'vn.png',
            NULL,
            'https://flagcdn.com/w320/vn.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'VN';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('VG', 'VGB', '092', NULL, 'Virgin Islands (British)', 'British Virgin Islands', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'vg.png',
            NULL,
            'https://flagcdn.com/w320/vg.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'VG';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('VI', 'VIR', '850', NULL, 'Virgin Islands (US)', 'United States Virgin Islands', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'vi.png',
            NULL,
            'https://flagcdn.com/w320/vi.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'VI';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('WF', 'WLF', '876', NULL, 'Wallis and Futuna Islands', 'Wallis et Futuna', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'wf.png',
            NULL,
            'https://flagcdn.com/w320/wf.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'WF';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('EH', 'ESH', '732', NULL, 'Western Sahara', 'الصحراء الغربية', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'eh.png',
            NULL,
            'https://flagcdn.com/w320/eh.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'EH';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('YE', 'YEM', '887', NULL, 'Yemen', 'اليَمَن', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'ye.png',
            NULL,
            'https://flagcdn.com/w320/ye.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'YE';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('ZM', 'ZMB', '894', NULL, 'Zambia', 'Zambia', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'zm.png',
            NULL,
            'https://flagcdn.com/w320/zm.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'ZM';
        
INSERT INTO countries (iso2, iso3, iso_numeric, phone_code, name, native_name, created_date, updated_date) VALUES ('ZW', 'ZWE', '716', NULL, 'Zimbabwe', 'Zimbabwe', NOW(), NOW());

        INSERT INTO files (
            user_id,
            resource_provider_id,
            storage_provider_id,
            entity_type_id,
            entity_id,
            file_role_id,
            original_name,
            storage_key,
            file_path,
            file_type_id,
            is_public,
            is_system,
            is_active,
            created_date,
            updated_date
        )
        SELECT
            NULL,
            (SELECT id FROM resource_providers WHERE code='FLAGCDN' LIMIT 1),
            (SELECT id FROM storage_providers WHERE code='EXTERNAL' LIMIT 1),
            (SELECT id FROM entity_types WHERE code='countries' LIMIT 1),
            c.id,
            (SELECT id FROM file_roles WHERE code='FLAG' LIMIT 1),
            'zw.png',
            NULL,
            'https://flagcdn.com/w320/zw.png',
            (SELECT id FROM file_types WHERE code='PNG' LIMIT 1),
            TRUE,
            TRUE,
            TRUE,
            NOW(),
            NOW()
        FROM countries c
        WHERE c.iso2 = 'ZW';
        

-- Insert Currencies
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('AFN', 'Afghan afghani','؋', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('EUR', 'Euro','€', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('ALL', 'Albanian lek','Lek', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('DZD', 'Algerian dinar','دج', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('USD', 'United States dollar','$', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('AOA', 'Angolan kwanza','Kz', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('XCD', 'Eastern Caribbean dollar','$', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('AAD', 'Antarctican dollar','$', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('ARS', 'Argentine peso','$', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('AMD', 'Armenian dram','֏', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('AWG', 'Aruban florin','ƒ', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('AUD', 'Australian dollar','$', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('AZN', 'Azerbaijani manat','m', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('BHD', 'Bahraini dinar','.د.ب', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('BDT', 'Bangladeshi taka','৳', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('BBD', 'Barbadian dollar','Bds$', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('BYN', 'Belarusian ruble','Br', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('BZD', 'Belize dollar','$', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('XOF', 'West African CFA franc','CFA', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('BMD', 'Bermudian dollar','$', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('BTN', 'Bhutanese ngultrum','Nu.', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('BOB', 'Bolivian boliviano','Bs.', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('BAM', 'Bosnia and Herzegovina convertible mark','KM', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('BWP', 'Botswana pula','P', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('NOK', 'Norwegian krone','ko', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('BRL', 'Brazilian real','R$', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('BND', 'Brunei dollar','B$', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('BGN', 'Bulgarian lev','Лв.', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('BIF', 'Burundian franc','FBu', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('KHR', 'Cambodian riel','KHR', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('XAF', 'Central African CFA franc','FCFA', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('CAD', 'Canadian dollar','$', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('CVE', 'Cape Verdean escudo','$', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('KYD', 'Cayman Islands dollar','$', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('CLP', 'Chilean peso','$', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('CNY', 'Chinese yuan','¥', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('COP', 'Colombian peso','$', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('KMF', 'Comorian franc','CF', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('CDF', 'Congolese Franc','FC', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('NZD', 'New Zealand dollar','$', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('CRC', 'Costa Rican colón','₡', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('CUP', 'Cuban peso','$', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('ANG', 'Netherlands Antillean guilder','ƒ', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('CZK', 'Czech koruna','Kč', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('DKK', 'Danish krone','Kr.', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('DJF', 'Djiboutian franc','Fdj', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('DOP', 'Dominican peso','$', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('EGP', 'Egyptian pound','ج.م', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('ERN', 'Eritrean nakfa','Nfk', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('SZL', 'Lilangeni','E', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('ETB', 'Ethiopian birr','Nkf', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('FKP', 'Falkland Islands pound','£', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('FJD', 'Fijian dollar','FJ$', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('XPF', 'CFP franc','₣', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('GEL', 'Georgian lari','ლ', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('GHS', 'Ghanaian cedi','GH₵', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('GIP', 'Gibraltar pound','£', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('GTQ', 'Guatemalan quetzal','Q', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('GBP', 'British pound','£', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('GNF', 'Guinean franc','FG', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('GYD', 'Guyanese dollar','$', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('HTG', 'Haitian gourde','G', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('HNL', 'Honduran lempira','L', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('HKD', 'Hong Kong dollar','$', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('HUF', 'Hungarian forint','Ft', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('ISK', 'Icelandic króna','ko', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('INR', 'Indian rupee','₹', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('IDR', 'Indonesian rupiah','Rp', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('IRR', 'Iranian rial','﷼', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('IQD', 'Iraqi dinar','د.ع', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('ILS', 'Israeli new shekel','₪', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('JMD', 'Jamaican dollar','J$', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('JPY', 'Japanese yen','¥', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('JOD', 'Jordanian dinar','ا.د', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('KZT', 'Kazakhstani tenge','лв', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('KES', 'Kenyan shilling','KSh', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('KWD', 'Kuwaiti dinar','ك.د', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('KGS', 'Kyrgyzstani som','лв', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('LAK', 'Lao kip','₭', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('LBP', 'Lebanese pound','£', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('LSL', 'Lesotho loti','L', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('LRD', 'Liberian dollar','$', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('LYD', 'Libyan dinar','د.ل', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('CHF', 'Swiss franc','CHf', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('MOP', 'Macanese pataca','$', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('MGA', 'Malagasy ariary','Ar', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('MWK', 'Malawian kwacha','MK', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('MYR', 'Malaysian ringgit','RM', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('MVR', 'Maldivian rufiyaa','Rf', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('MRU', 'Mauritanian ouguiya','UM', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('MUR', 'Mauritian rupee','₨', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('MXN', 'Mexican peso','$', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('MDL', 'Moldovan leu','L', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('MNT', 'Mongolian tögrög','₮', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('MAD', 'Moroccan dirham','DH', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('MZN', 'Mozambican metical','MT', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('MMK', 'Burmese kyat','K', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('NAD', 'Namibian dollar','$', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('NPR', 'Nepalese rupee','₨', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('NIO', 'Nicaraguan córdoba','C$', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('NGN', 'Nigerian naira','₦', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('KPW', 'North Korean Won','₩', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('MKD', 'Denar','ден', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('OMR', 'Omani rial','.ع.ر', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('PKR', 'Pakistani rupee','₨', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('PAB', 'Panamanian balboa','B/.', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('PGK', 'Papua New Guinean kina','K', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('PYG', 'Paraguayan guarani','₲', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('PEN', 'Peruvian sol','S/.', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('PHP', 'Philippine peso','₱', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('PLN', 'Polish złoty','zł', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('QAR', 'Qatari riyal','ق.ر', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('RON', 'Romanian leu','lei', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('RUB', 'Russian ruble','₽', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('RWF', 'Rwandan franc','FRw', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('SHP', 'Saint Helena pound','£', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('WST', 'Samoan tālā','SAT', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('STN', 'Dobra','Db', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('SAR', 'Saudi riyal','﷼', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('RSD', 'Serbian dinar','din', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('SCR', 'Seychellois rupee','SRe', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('SLL', 'Sierra Leonean leone','Le', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('SGD', 'Singapore dollar','$', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('SBD', 'Solomon Islands dollar','Si$', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('SOS', 'Somali shilling','Sh.so.', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('ZAR', 'South African rand','R', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('KRW', 'Won','₩', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('SSP', 'South Sudanese pound','£', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('LKR', 'Sri Lankan rupee','Rs', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('SDG', 'Sudanese pound','.س.ج', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('SRD', 'Surinamese dollar','$', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('SEK', 'Swedish krona','ko', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('SYP', 'Syrian pound','LS', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('TWD', 'New Taiwan dollar','$', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('TJS', 'Tajikistani somoni','SM', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('TZS', 'Tanzanian shilling','TSh', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('THB', 'Thai baht','฿', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('BSD', 'Bahamian dollar','B$', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('GMD', 'Gambian dalasi','D', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('TOP', 'Tongan paʻanga','$', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('TTD', 'Trinidad and Tobago dollar','$', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('TND', 'Tunisian dinar','ت.د', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('TRY', 'Turkish lira','₺', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('TMT', 'Turkmenistan manat','T', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('UGX', 'Ugandan shilling','USh', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('UAH', 'Ukrainian hryvnia','₴', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('AED', 'United Arab Emirates dirham','إ.د', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('UYU', 'Uruguayan peso','$', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('UZS', 'Uzbekistani soʻm','лв', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('VUV', 'Vanuatu vatu','VT', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('VES', 'Bolívar','Bs', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('VND', 'Vietnamese đồng','₫', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('YER', 'Yemeni rial','﷼', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('ZMW', 'Zambian kwacha','ZK', NOW(), NOW());
INSERT INTO currencies (code, name, symbol, created_date, updated_date) VALUES ('ZWL', 'Zimbabwe Dollar','$', NOW(), NOW());

-- Insert Languages

-- Insert Timezones
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AFT', 'Asia/Kabul', 'Afghanistan Time', 16200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EET', 'Europe/Mariehamn', 'Eastern European Time', 7200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CET', 'Europe/Tirane', 'Central European Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CET', 'Africa/Algiers', 'Central European Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('SST', 'Pacific/Pago_Pago', 'Samoa Standard Time', -39600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CET', 'Europe/Andorra', 'Central European Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('WAT', 'Africa/Luanda', 'West Africa Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AST', 'America/Anguilla', 'Atlantic Standard Time', -14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AWST', 'Antarctica/Casey', 'Australian Western Standard Time', 39600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('DAVT', 'Antarctica/Davis', 'Davis Time', 25200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('DDUT', 'Antarctica/DumontDUrville', 'Dumont d''Urville Time', 36000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('MAWT', 'Antarctica/Mawson', 'Mawson Station Time', 18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('NZDT', 'Antarctica/McMurdo', 'New Zealand Daylight Time', 46800, TRUE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CLST', 'Antarctica/Palmer', 'Chile Summer Time', -10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('ROTT', 'Antarctica/Rothera', 'Rothera Research Station Time', -10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('SYOT', 'Antarctica/Syowa', 'Showa Station Time', 10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('GMT', 'Antarctica/Troll', 'Greenwich Mean Time', 0, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('VOST', 'Antarctica/Vostok', 'Vostok Station Time', 21600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AST', 'America/Antigua', 'Atlantic Standard Time', -14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('ART', 'America/Argentina/Buenos_Aires', 'Argentina Time', -10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('ART', 'America/Argentina/Catamarca', 'Argentina Time', -10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('ART', 'America/Argentina/Cordoba', 'Argentina Time', -10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('ART', 'America/Argentina/Jujuy', 'Argentina Time', -10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('ART', 'America/Argentina/La_Rioja', 'Argentina Time', -10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('ART', 'America/Argentina/Mendoza', 'Argentina Time', -10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('ART', 'America/Argentina/Rio_Gallegos', 'Argentina Time', -10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('ART', 'America/Argentina/Salta', 'Argentina Time', -10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('ART', 'America/Argentina/San_Juan', 'Argentina Time', -10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('ART', 'America/Argentina/San_Luis', 'Argentina Time', -10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('ART', 'America/Argentina/Tucuman', 'Argentina Time', -10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('ART', 'America/Argentina/Ushuaia', 'Argentina Time', -10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AMT', 'Asia/Yerevan', 'Armenia Time', 14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AST', 'America/Aruba', 'Atlantic Standard Time', -14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('MIST', 'Antarctica/Macquarie', 'Macquarie Island Station Time', 39600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('ACDT', 'Australia/Adelaide', 'Australian Central Daylight Saving Time', 37800, TRUE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AEST', 'Australia/Brisbane', 'Australian Eastern Standard Time', 36000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('ACDT', 'Australia/Broken_Hill', 'Australian Central Daylight Saving Time', 37800, TRUE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AEDT', 'Australia/Currie', 'Australian Eastern Daylight Saving Time', 39600, TRUE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('ACST', 'Australia/Darwin', 'Australian Central Standard Time', 34200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('ACWST', 'Australia/Eucla', 'Australian Central Western Standard Time (Unofficial)', 31500, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AEDT', 'Australia/Hobart', 'Australian Eastern Daylight Saving Time', 39600, TRUE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AEST', 'Australia/Lindeman', 'Australian Eastern Standard Time', 36000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('LHST', 'Australia/Lord_Howe', 'Lord Howe Summer Time', 39600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AEDT', 'Australia/Melbourne', 'Australian Eastern Daylight Saving Time', 39600, TRUE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AWST', 'Australia/Perth', 'Australian Western Standard Time', 28800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AEDT', 'Australia/Sydney', 'Australian Eastern Daylight Saving Time', 39600, TRUE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CET', 'Europe/Vienna', 'Central European Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AZT', 'Asia/Baku', 'Azerbaijan Time', 14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AST', 'Asia/Bahrain', 'Arabia Standard Time', 10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('BDT', 'Asia/Dhaka', 'Bangladesh Standard Time', 21600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AST', 'America/Barbados', 'Atlantic Standard Time', -14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('MSK', 'Europe/Minsk', 'Moscow Time', 10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CET', 'Europe/Brussels', 'Central European Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CST', 'America/Belize', 'Central Standard Time (North America)', -21600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('WAT', 'Africa/Porto-Novo', 'West Africa Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AST', 'Atlantic/Bermuda', 'Atlantic Standard Time', -14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('BTT', 'Asia/Thimphu', 'Bhutan Time', 21600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('BOT', 'America/La_Paz', 'Bolivia Time', -14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AST', 'America/Kralendijk', 'Atlantic Standard Time', -14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CET', 'Europe/Sarajevo', 'Central European Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CAT', 'Africa/Gaborone', 'Central Africa Time', 7200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CET', 'Europe/Oslo', 'Central European Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('BRT', 'America/Araguaina', 'Brasília Time', -10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('BRT', 'America/Bahia', 'Brasília Time', -10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('BRT', 'America/Belem', 'Brasília Time', -10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AMT', 'America/Boa_Vista', 'Amazon Time (Brazil)[3', -14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AMT', 'America/Campo_Grande', 'Amazon Time (Brazil)[3', -14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('BRT', 'America/Cuiaba', 'Brasilia Time', -14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('ACT', 'America/Eirunepe', 'Acre Time', -18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('BRT', 'America/Fortaleza', 'Brasília Time', -10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('BRT', 'America/Maceio', 'Brasília Time', -10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AMT', 'America/Manaus', 'Amazon Time (Brazil)', -14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('FNT', 'America/Noronha', 'Fernando de Noronha Time', -7200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AMT', 'America/Porto_Velho', 'Amazon Time (Brazil)[3', -14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('BRT', 'America/Recife', 'Brasília Time', -10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('ACT', 'America/Rio_Branco', 'Acre Time', -18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('BRT', 'America/Santarem', 'Brasília Time', -10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('BRT', 'America/Sao_Paulo', 'Brasília Time', -10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('IOT', 'Indian/Chagos', 'Indian Ocean Time', 21600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('BNT', 'Asia/Brunei', 'Brunei Darussalam Time', 28800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EET', 'Europe/Sofia', 'Eastern European Time', 7200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('GMT', 'Africa/Ouagadougou', 'Greenwich Mean Time', 0, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CAT', 'Africa/Bujumbura', 'Central Africa Time', 7200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('ICT', 'Asia/Phnom_Penh', 'Indochina Time', 25200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('WAT', 'Africa/Douala', 'West Africa Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EST', 'America/Atikokan', 'Eastern Standard Time (North America)', -18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AST', 'America/Blanc-Sablon', 'Atlantic Standard Time', -14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('MST', 'America/Cambridge_Bay', 'Mountain Standard Time (North America)', -25200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('MST', 'America/Creston', 'Mountain Standard Time (North America)', -25200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('MST', 'America/Dawson', 'Mountain Standard Time (North America)', -25200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('MST', 'America/Dawson_Creek', 'Mountain Standard Time (North America)', -25200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('MST', 'America/Edmonton', 'Mountain Standard Time (North America)', -25200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('MST', 'America/Fort_Nelson', 'Mountain Standard Time (North America)', -25200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AST', 'America/Glace_Bay', 'Atlantic Standard Time', -14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AST', 'America/Goose_Bay', 'Atlantic Standard Time', -14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AST', 'America/Halifax', 'Atlantic Standard Time', -14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('MST', 'America/Inuvik', 'Mountain Standard Time (North America)', -25200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EST', 'America/Iqaluit', 'Eastern Standard Time (North America', -18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AST', 'America/Moncton', 'Atlantic Standard Time', -14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EST', 'America/Nipigon', 'Eastern Standard Time (North America', -18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EST', 'America/Pangnirtung', 'Eastern Standard Time (North America', -18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CST', 'America/Rainy_River', 'Central Standard Time (North America)', -21600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CST', 'America/Rankin_Inlet', 'Central Standard Time (North America)', -21600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CST', 'America/Regina', 'Central Standard Time (North America)', -21600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CST', 'America/Resolute', 'Central Standard Time (North America)', -21600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('NST', 'America/St_Johns', 'Newfoundland Standard Time', -12600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CST', 'America/Swift_Current', 'Central Standard Time (North America)', -21600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EST', 'America/Thunder_Bay', 'Eastern Standard Time (North America)', -18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EST', 'America/Toronto', 'Eastern Standard Time (North America)', -18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('PST', 'America/Vancouver', 'Pacific Standard Time (North America)', -28800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('MST', 'America/Whitehorse', 'Mountain Standard Time (North America)', -25200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CST', 'America/Winnipeg', 'Central Standard Time (North America)', -21600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('MST', 'America/Yellowknife', 'Mountain Standard Time (North America)', -25200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CVT', 'Atlantic/Cape_Verde', 'Cape Verde Time', -3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EST', 'America/Cayman', 'Eastern Standard Time (North America', -18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('WAT', 'Africa/Bangui', 'West Africa Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('WAT', 'Africa/Ndjamena', 'West Africa Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CLST', 'America/Punta_Arenas', 'Chile Summer Time', -10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CLST', 'America/Santiago', 'Chile Summer Time', -10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EASST', 'Pacific/Easter', 'Easter Island Summer Time', -18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CLST', 'America/Coyhaique', 'Chile Summer Time', -10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CST', 'Asia/Shanghai', 'China Standard Time', 28800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('XJT', 'Asia/Urumqi', 'China Standard Time', 21600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CXT', 'Indian/Christmas', 'Christmas Island Time', 25200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CCT', 'Indian/Cocos', 'Cocos Islands Time', 23400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('COT', 'America/Bogota', 'Colombia Time', -18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EAT', 'Indian/Comoro', 'East Africa Time', 10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('WAT', 'Africa/Brazzaville', 'West Africa Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CKT', 'Pacific/Rarotonga', 'Cook Island Time', -36000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CST', 'America/Costa_Rica', 'Central Standard Time (North America)', -21600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CET', 'Europe/Zagreb', 'Central European Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CST', 'America/Havana', 'Cuba Standard Time', -18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AST', 'America/Curacao', 'Atlantic Standard Time', -14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EET', 'Asia/Famagusta', 'Eastern European Time', 7200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EET', 'Asia/Nicosia', 'Eastern European Time', 7200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CET', 'Europe/Prague', 'Central European Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('WAT', 'Africa/Kinshasa', 'West Africa Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CAT', 'Africa/Lubumbashi', 'Central Africa Time', 7200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CET', 'Europe/Copenhagen', 'Central European Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EAT', 'Africa/Djibouti', 'East Africa Time', 10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AST', 'America/Dominica', 'Atlantic Standard Time', -14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AST', 'America/Santo_Domingo', 'Atlantic Standard Time', -14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('ECT', 'America/Guayaquil', 'Ecuador Time', -18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('GALT', 'Pacific/Galapagos', 'Galápagos Time', -21600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EET', 'Africa/Cairo', 'Eastern European Time', 7200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CST', 'America/El_Salvador', 'Central Standard Time (North America)', -21600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('WAT', 'Africa/Malabo', 'West Africa Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EAT', 'Africa/Asmara', 'East Africa Time', 10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EET', 'Europe/Tallinn', 'Eastern European Time', 7200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('SAST', 'Africa/Mbabane', 'South African Standard Time', 7200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EAT', 'Africa/Addis_Ababa', 'East Africa Time', 10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('FKST', 'Atlantic/Stanley', 'Falkland Islands Summer Time', -10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('WET', 'Atlantic/Faroe', 'Western European Time', 0, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('FJT', 'Pacific/Fiji', 'Fiji Time', 43200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EET', 'Europe/Helsinki', 'Eastern European Time', 7200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CET', 'Europe/Paris', 'Central European Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('GFT', 'America/Cayenne', 'French Guiana Time', -10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('GAMT', 'Pacific/Gambier', 'Gambier Islands Time', -32400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('MART', 'Pacific/Marquesas', 'Marquesas Islands Time', -34200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('TAHT', 'Pacific/Tahiti', 'Tahiti Time', -36000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('TFT', 'Indian/Kerguelen', 'French Southern and Antarctic Time', 18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('WAT', 'Africa/Libreville', 'West Africa Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('GET', 'Asia/Tbilisi', 'Georgia Standard Time', 14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CET', 'Europe/Berlin', 'Central European Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CET', 'Europe/Busingen', 'Central European Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('GMT', 'Africa/Accra', 'Greenwich Mean Time', 0, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CET', 'Europe/Gibraltar', 'Central European Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EET', 'Europe/Athens', 'Eastern European Time', 7200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('GMT', 'America/Danmarkshavn', 'Greenwich Mean Time', 0, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('WGT', 'America/Nuuk', 'West Greenland Time', -10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EGT', 'America/Scoresbysund', 'Eastern Greenland Time', -3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AST', 'America/Thule', 'Atlantic Standard Time', -14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AST', 'America/Grenada', 'Atlantic Standard Time', -14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AST', 'America/Guadeloupe', 'Atlantic Standard Time', -14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CHST', 'Pacific/Guam', 'Chamorro Standard Time', 36000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CST', 'America/Guatemala', 'Central Standard Time (North America)', -21600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('GMT', 'Europe/Guernsey', 'Greenwich Mean Time', 0, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('GMT', 'Africa/Conakry', 'Greenwich Mean Time', 0, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('GMT', 'Africa/Bissau', 'Greenwich Mean Time', 0, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('GYT', 'America/Guyana', 'Guyana Time', -14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EST', 'America/Port-au-Prince', 'Eastern Standard Time (North America', -18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CST', 'America/Tegucigalpa', 'Central Standard Time (North America)', -21600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('HKT', 'Asia/Hong_Kong', 'Hong Kong Time', 28800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CET', 'Europe/Budapest', 'Central European Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('GMT', 'Atlantic/Reykjavik', 'Greenwich Mean Time', 0, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('IST', 'Asia/Kolkata', 'Indian Standard Time', 19800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('WIB', 'Asia/Jakarta', 'Western Indonesian Time', 25200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('WIT', 'Asia/Jayapura', 'Eastern Indonesian Time', 32400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('WITA', 'Asia/Makassar', 'Central Indonesia Time', 28800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('WIB', 'Asia/Pontianak', 'Western Indonesian Time', 25200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('IRDT', 'Asia/Tehran', 'Iran Daylight Time', 12600, TRUE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AST', 'Asia/Baghdad', 'Arabia Standard Time', 10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('GMT', 'Europe/Dublin', 'Greenwich Mean Time', 0, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('IST', 'Asia/Jerusalem', 'Israel Standard Time', 7200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CET', 'Europe/Rome', 'Central European Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('GMT', 'Africa/Abidjan', 'Greenwich Mean Time', 0, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EST', 'America/Jamaica', 'Eastern Standard Time (North America', -18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('JST', 'Asia/Tokyo', 'Japan Standard Time', 32400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('GMT', 'Europe/Jersey', 'Greenwich Mean Time', 0, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EET', 'Asia/Amman', 'Eastern European Time', 7200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('ALMT', 'Asia/Almaty', 'Alma-Ata Time[1', 21600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AQTT', 'Asia/Aqtau', 'Aqtobe Time', 18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AQTT', 'Asia/Aqtobe', 'Aqtobe Time', 18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('MSD+1', 'Asia/Atyrau', 'Moscow Daylight Time+1', 18000, TRUE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('ORAT', 'Asia/Oral', 'Oral Time', 18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('QYZST', 'Asia/Qostanay', 'Qyzylorda Summer Time', 21600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('QYZT', 'Asia/Qyzylorda', 'Qyzylorda Summer Time', 18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EAT', 'Africa/Nairobi', 'East Africa Time', 10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('PHOT', 'Pacific/Enderbury', 'Phoenix Island Time', 46800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('LINT', 'Pacific/Kiritimati', 'Line Islands Time', 50400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('GILT', 'Pacific/Tarawa', 'Gilbert Island Time', 43200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CET', 'Europe/Belgrade', 'Central European Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AST', 'Asia/Kuwait', 'Arabia Standard Time', 10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('KGT', 'Asia/Bishkek', 'Kyrgyzstan Time', 21600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('ICT', 'Asia/Vientiane', 'Indochina Time', 25200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EET', 'Europe/Riga', 'Eastern European Time', 7200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EET', 'Asia/Beirut', 'Eastern European Time', 7200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('SAST', 'Africa/Maseru', 'South African Standard Time', 7200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('GMT', 'Africa/Monrovia', 'Greenwich Mean Time', 0, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EET', 'Africa/Tripoli', 'Eastern European Time', 7200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CET', 'Europe/Vaduz', 'Central European Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EET', 'Europe/Vilnius', 'Eastern European Time', 7200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CET', 'Europe/Luxembourg', 'Central European Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CST', 'Asia/Macau', 'China Standard Time', 28800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EAT', 'Indian/Antananarivo', 'East Africa Time', 10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CAT', 'Africa/Blantyre', 'Central Africa Time', 7200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('MYT', 'Asia/Kuala_Lumpur', 'Malaysia Time', 28800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('MYT', 'Asia/Kuching', 'Malaysia Time', 28800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('MVT', 'Indian/Maldives', 'Maldives Time', 18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('GMT', 'Africa/Bamako', 'Greenwich Mean Time', 0, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CET', 'Europe/Malta', 'Central European Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('GMT', 'Europe/Isle_of_Man', 'Greenwich Mean Time', 0, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('MHT', 'Pacific/Kwajalein', 'Marshall Islands Time', 43200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('MHT', 'Pacific/Majuro', 'Marshall Islands Time', 43200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AST', 'America/Martinique', 'Atlantic Standard Time', -14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('GMT', 'Africa/Nouakchott', 'Greenwich Mean Time', 0, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('MUT', 'Indian/Mauritius', 'Mauritius Time', 14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EAT', 'Indian/Mayotte', 'East Africa Time', 10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CST', 'America/Bahia_Banderas', 'Central Standard Time (North America)', -21600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EST', 'America/Cancun', 'Eastern Standard Time (North America', -18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('MST', 'America/Chihuahua', 'Mountain Standard Time (North America)', -25200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('MST', 'America/Hermosillo', 'Mountain Standard Time (North America)', -25200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CST', 'America/Matamoros', 'Central Standard Time (North America)', -21600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('MST', 'America/Mazatlan', 'Mountain Standard Time (North America)', -25200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CST', 'America/Merida', 'Central Standard Time (North America)', -21600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CST', 'America/Mexico_City', 'Central Standard Time (North America)', -21600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CST', 'America/Monterrey', 'Central Standard Time (North America)', -21600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('MST', 'America/Ojinaga', 'Mountain Standard Time (North America)', -25200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('PST', 'America/Tijuana', 'Pacific Standard Time (North America)', -28800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('MST', 'America/Ciudad_Juarez', 'Mountain Standard Time (North America)', -25200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CHUT', 'Pacific/Chuuk', 'Chuuk Time', 36000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('KOST', 'Pacific/Kosrae', 'Kosrae Time', 39600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('PONT', 'Pacific/Pohnpei', 'Pohnpei Standard Time', 39600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EET', 'Europe/Chisinau', 'Eastern European Time', 7200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CET', 'Europe/Monaco', 'Central European Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CHOT', 'Asia/Choibalsan', 'Choibalsan Standard Time', 28800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('HOVT', 'Asia/Hovd', 'Hovd Time', 25200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('ULAT', 'Asia/Ulaanbaatar', 'Ulaanbaatar Standard Time', 28800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CET', 'Europe/Podgorica', 'Central European Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AST', 'America/Montserrat', 'Atlantic Standard Time', -14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('WEST', 'Africa/Casablanca', 'Western European Summer Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('WEST', 'Africa/El_Aaiun', 'Western Sahara Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CAT', 'Africa/Maputo', 'Central Africa Time', 7200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('MMT', 'Asia/Yangon', 'Myanmar Standard Time', 23400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('WAST', 'Africa/Windhoek', 'West Africa Summer Time', 7200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('NRT', 'Pacific/Nauru', 'Nauru Time', 43200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('NPT', 'Asia/Kathmandu', 'Nepal Time', 20700, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CET', 'Europe/Amsterdam', 'Central European Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('NCT', 'Pacific/Noumea', 'New Caledonia Time', 39600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('NZDT', 'Pacific/Auckland', 'New Zealand Daylight Time', 46800, TRUE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CHAST', 'Pacific/Chatham', 'Chatham Standard Time', 49500, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CST', 'America/Managua', 'Central Standard Time (North America)', -21600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('WAT', 'Africa/Niamey', 'West Africa Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('WAT', 'Africa/Lagos', 'West Africa Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('NUT', 'Pacific/Niue', 'Niue Time', -39600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('NFT', 'Pacific/Norfolk', 'Norfolk Time', 43200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('KST', 'Asia/Pyongyang', 'Korea Standard Time', 32400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CET', 'Europe/Skopje', 'Central European Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('ChST', 'Pacific/Saipan', 'Chamorro Standard Time', 36000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CET', 'Arctic/Longyearbyen', 'Central European Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('GST', 'Asia/Muscat', 'Gulf Standard Time', 14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('PKT', 'Asia/Karachi', 'Pakistan Standard Time', 18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('PWT', 'Pacific/Palau', 'Palau Time', 32400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EET', 'Asia/Gaza', 'Eastern European Time', 7200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EET', 'Asia/Hebron', 'Eastern European Time', 7200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EST', 'America/Panama', 'Eastern Standard Time (North America', -18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('BST', 'Pacific/Bougainville', 'Bougainville Standard Time[6', 39600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('PGT', 'Pacific/Port_Moresby', 'Papua New Guinea Time', 36000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('PYST', 'America/Asuncion', 'Paraguay Summer Time', -10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('PET', 'America/Lima', 'Peru Time', -18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('PHT', 'Asia/Manila', 'Philippine Time', 28800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('PST', 'Pacific/Pitcairn', 'Pacific Standard Time (North America)', -28800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CET', 'Europe/Warsaw', 'Central European Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AZOT', 'Atlantic/Azores', 'Azores Standard Time', -3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('WET', 'Atlantic/Madeira', 'Western European Time', 0, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('WET', 'Europe/Lisbon', 'Western European Time', 0, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AST', 'America/Puerto_Rico', 'Atlantic Standard Time', -14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AST', 'Asia/Qatar', 'Arabia Standard Time', 10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('RET', 'Indian/Reunion', 'Réunion Time', 14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EET', 'Europe/Bucharest', 'Eastern European Time', 7200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('ANAT', 'Asia/Anadyr', 'Anadyr Time[4', 43200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('KRAT', 'Asia/Barnaul', 'Krasnoyarsk Time', 25200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('YAKT', 'Asia/Chita', 'Yakutsk Time', 32400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('IRKT', 'Asia/Irkutsk', 'Irkutsk Time', 28800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('PETT', 'Asia/Kamchatka', 'Kamchatka Time', 43200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('YAKT', 'Asia/Khandyga', 'Yakutsk Time', 32400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('KRAT', 'Asia/Krasnoyarsk', 'Krasnoyarsk Time', 25200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('MAGT', 'Asia/Magadan', 'Magadan Time', 39600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('KRAT', 'Asia/Novokuznetsk', 'Krasnoyarsk Time', 25200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('NOVT', 'Asia/Novosibirsk', 'Novosibirsk Time', 25200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('OMST', 'Asia/Omsk', 'Omsk Time', 21600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('SAKT', 'Asia/Sakhalin', 'Sakhalin Island Time', 39600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('SRET', 'Asia/Srednekolymsk', 'Srednekolymsk Time', 39600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('MSD+3', 'Asia/Tomsk', 'Moscow Daylight Time+3', 25200, TRUE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('VLAT', 'Asia/Ust-Nera', 'Vladivostok Time', 36000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('VLAT', 'Asia/Vladivostok', 'Vladivostok Time', 36000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('YAKT', 'Asia/Yakutsk', 'Yakutsk Time', 32400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('YEKT', 'Asia/Yekaterinburg', 'Yekaterinburg Time', 18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('SAMT', 'Europe/Astrakhan', 'Samara Time', 14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EET', 'Europe/Kaliningrad', 'Eastern European Time', 7200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('MSK', 'Europe/Kirov', 'Moscow Time', 10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('MSK', 'Europe/Moscow', 'Moscow Time', 10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('SAMT', 'Europe/Samara', 'Samara Time', 14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('MSD', 'Europe/Saratov', 'Moscow Daylight Time+4', 14400, TRUE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('SAMT', 'Europe/Ulyanovsk', 'Samara Time', 14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('MSK', 'Europe/Volgograd', 'Moscow Standard Time', 14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CAT', 'Africa/Kigali', 'Central Africa Time', 7200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('GMT', 'Atlantic/St_Helena', 'Greenwich Mean Time', 0, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AST', 'America/St_Kitts', 'Atlantic Standard Time', -14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AST', 'America/St_Lucia', 'Atlantic Standard Time', -14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('PMDT', 'America/Miquelon', 'Pierre & Miquelon Daylight Time', -10800, TRUE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AST', 'America/St_Vincent', 'Atlantic Standard Time', -14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AST', 'America/St_Barthelemy', 'Atlantic Standard Time', -14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AST', 'America/Marigot', 'Atlantic Standard Time', -14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('WST', 'Pacific/Apia', 'West Samoa Time', 50400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CET', 'Europe/San_Marino', 'Central European Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('GMT', 'Africa/Sao_Tome', 'Greenwich Mean Time', 0, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AST', 'Asia/Riyadh', 'Arabia Standard Time', 10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('GMT', 'Africa/Dakar', 'Greenwich Mean Time', 0, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('SCT', 'Indian/Mahe', 'Seychelles Time', 14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('GMT', 'Africa/Freetown', 'Greenwich Mean Time', 0, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('SGT', 'Asia/Singapore', 'Singapore Time', 28800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CET', 'Europe/Bratislava', 'Central European Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CET', 'Europe/Ljubljana', 'Central European Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('SBT', 'Pacific/Guadalcanal', 'Solomon Islands Time', 39600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EAT', 'Africa/Mogadishu', 'East Africa Time', 10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('SAST', 'Africa/Johannesburg', 'South African Standard Time', 7200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('GST', 'Atlantic/South_Georgia', 'South Georgia and the South Sandwich Islands Time', -7200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('KST', 'Asia/Seoul', 'Korea Standard Time', 32400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EAT', 'Africa/Juba', 'East Africa Time', 10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CET', 'Africa/Ceuta', 'Central European Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('WET', 'Atlantic/Canary', 'Western European Time', 0, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CET', 'Europe/Madrid', 'Central European Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('IST', 'Asia/Colombo', 'Indian Standard Time', 19800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EAT', 'Africa/Khartoum', 'Eastern African Time', 7200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('SRT', 'America/Paramaribo', 'Suriname Time', -10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CET', 'Europe/Stockholm', 'Central European Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CET', 'Europe/Zurich', 'Central European Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EET', 'Asia/Damascus', 'Eastern European Time', 7200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CST', 'Asia/Taipei', 'China Standard Time', 28800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('TJT', 'Asia/Dushanbe', 'Tajikistan Time', 18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EAT', 'Africa/Dar_es_Salaam', 'East Africa Time', 10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('ICT', 'Asia/Bangkok', 'Indochina Time', 25200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EST', 'America/Nassau', 'Eastern Standard Time (North America)', -18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('GMT', 'Africa/Banjul', 'Greenwich Mean Time', 0, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('TLT', 'Asia/Dili', 'Timor Leste Time', 32400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('GMT', 'Africa/Lome', 'Greenwich Mean Time', 0, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('TKT', 'Pacific/Fakaofo', 'Tokelau Time', 46800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('TOT', 'Pacific/Tongatapu', 'Tonga Time', 46800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AST', 'America/Port_of_Spain', 'Atlantic Standard Time', -14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CET', 'Africa/Tunis', 'Central European Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EET', 'Europe/Istanbul', 'Eastern European Time', 10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('TMT', 'Asia/Ashgabat', 'Turkmenistan Time', 18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EST', 'America/Grand_Turk', 'Eastern Standard Time (North America', -18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('TVT', 'Pacific/Funafuti', 'Tuvalu Time', 43200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EAT', 'Africa/Kampala', 'East Africa Time', 10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EET', 'Europe/Kyiv', 'Eastern European Time', 7200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('MSK', 'Europe/Simferopol', 'Moscow Time', 10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EET', 'Europe/Uzhgorod', 'Eastern European Time', 7200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EET', 'Europe/Zaporozhye', 'Eastern European Time', 7200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('GST', 'Asia/Dubai', 'Gulf Standard Time', 14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('GMT', 'Europe/London', 'Greenwich Mean Time', 0, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('HST', 'America/Adak', 'Hawaii–Aleutian Standard Time', -36000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AKST', 'America/Anchorage', 'Alaska Standard Time', -32400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('MST', 'America/Boise', 'Mountain Standard Time (North America)', -25200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CST', 'America/Chicago', 'Central Standard Time (North America)', -21600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('MST', 'America/Denver', 'Mountain Standard Time (North America)', -25200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EST', 'America/Detroit', 'Eastern Standard Time (North America', -18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EST', 'America/Indiana/Indianapolis', 'Eastern Standard Time (North America', -18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CST', 'America/Indiana/Knox', 'Central Standard Time (North America)', -21600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EST', 'America/Indiana/Marengo', 'Eastern Standard Time (North America', -18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EST', 'America/Indiana/Petersburg', 'Eastern Standard Time (North America', -18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CST', 'America/Indiana/Tell_City', 'Central Standard Time (North America)', -21600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EST', 'America/Indiana/Vevay', 'Eastern Standard Time (North America', -18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EST', 'America/Indiana/Vincennes', 'Eastern Standard Time (North America', -18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EST', 'America/Indiana/Winamac', 'Eastern Standard Time (North America', -18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AKST', 'America/Juneau', 'Alaska Standard Time', -32400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EST', 'America/Kentucky/Louisville', 'Eastern Standard Time (North America', -18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EST', 'America/Kentucky/Monticello', 'Eastern Standard Time (North America', -18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('PST', 'America/Los_Angeles', 'Pacific Standard Time (North America)', -28800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CST', 'America/Menominee', 'Central Standard Time (North America)', -21600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AKST', 'America/Metlakatla', 'Alaska Standard Time', -32400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('EST', 'America/New_York', 'Eastern Standard Time (North America', -18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AKST', 'America/Nome', 'Alaska Standard Time', -32400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CST', 'America/North_Dakota/Beulah', 'Central Standard Time (North America)', -21600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CST', 'America/North_Dakota/Center', 'Central Standard Time (North America)', -21600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CST', 'America/North_Dakota/New_Salem', 'Central Standard Time (North America)', -21600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('MST', 'America/Phoenix', 'Mountain Standard Time (North America)', -25200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AKST', 'America/Sitka', 'Alaska Standard Time', -32400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AKST', 'America/Yakutat', 'Alaska Standard Time', -32400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('HST', 'Pacific/Honolulu', 'Hawaii–Aleutian Standard Time', -36000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('SST', 'Pacific/Midway', 'Samoa Standard Time', -39600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('WAKT', 'Pacific/Wake', 'Wake Island Time', 43200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('UYT', 'America/Montevideo', 'Uruguay Standard Time', -10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('UZT', 'Asia/Samarkand', 'Uzbekistan Time', 18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('UZT', 'Asia/Tashkent', 'Uzbekistan Time', 18000, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('VUT', 'Pacific/Efate', 'Vanuatu Time', 39600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CET', 'Europe/Vatican', 'Central European Time', 3600, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('VET', 'America/Caracas', 'Venezuelan Standard Time', -14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('ICT', 'Asia/Ho_Chi_Minh', 'Indochina Time', 25200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AST', 'America/Tortola', 'Atlantic Standard Time', -14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AST', 'America/St_Thomas', 'Atlantic Standard Time', -14400, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('WFT', 'Pacific/Wallis', 'Wallis & Futuna Time', 43200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('AST', 'Asia/Aden', 'Arabia Standard Time', 10800, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CAT', 'Africa/Lusaka', 'Central Africa Time', 7200, FALSE, NOW(), NOW());
INSERT INTO timezones (code, iana_name, name, utc_offset, has_dst, created_date, updated_date) VALUES ('CAT', 'Africa/Harare', 'Central Africa Time', 7200, FALSE, NOW(), NOW());

-- Insert Currency-Country Mapping
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='AFN' LIMIT 1), (SELECT id FROM countries WHERE iso2='AF' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='EUR' LIMIT 1), (SELECT id FROM countries WHERE iso2='AX' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='ALL' LIMIT 1), (SELECT id FROM countries WHERE iso2='AL' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='DZD' LIMIT 1), (SELECT id FROM countries WHERE iso2='DZ' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='USD' LIMIT 1), (SELECT id FROM countries WHERE iso2='AS' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='EUR' LIMIT 1), (SELECT id FROM countries WHERE iso2='AD' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='AOA' LIMIT 1), (SELECT id FROM countries WHERE iso2='AO' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='XCD' LIMIT 1), (SELECT id FROM countries WHERE iso2='AI' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='AAD' LIMIT 1), (SELECT id FROM countries WHERE iso2='AQ' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='XCD' LIMIT 1), (SELECT id FROM countries WHERE iso2='AG' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='ARS' LIMIT 1), (SELECT id FROM countries WHERE iso2='AR' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='AMD' LIMIT 1), (SELECT id FROM countries WHERE iso2='AM' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='AWG' LIMIT 1), (SELECT id FROM countries WHERE iso2='AW' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='AUD' LIMIT 1), (SELECT id FROM countries WHERE iso2='AU' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='EUR' LIMIT 1), (SELECT id FROM countries WHERE iso2='AT' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='AZN' LIMIT 1), (SELECT id FROM countries WHERE iso2='AZ' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='BHD' LIMIT 1), (SELECT id FROM countries WHERE iso2='BH' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='BDT' LIMIT 1), (SELECT id FROM countries WHERE iso2='BD' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='BBD' LIMIT 1), (SELECT id FROM countries WHERE iso2='BB' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='BYN' LIMIT 1), (SELECT id FROM countries WHERE iso2='BY' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='EUR' LIMIT 1), (SELECT id FROM countries WHERE iso2='BE' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='BZD' LIMIT 1), (SELECT id FROM countries WHERE iso2='BZ' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='XOF' LIMIT 1), (SELECT id FROM countries WHERE iso2='BJ' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='BMD' LIMIT 1), (SELECT id FROM countries WHERE iso2='BM' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='BTN' LIMIT 1), (SELECT id FROM countries WHERE iso2='BT' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='BOB' LIMIT 1), (SELECT id FROM countries WHERE iso2='BO' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='USD' LIMIT 1), (SELECT id FROM countries WHERE iso2='BQ' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='BAM' LIMIT 1), (SELECT id FROM countries WHERE iso2='BA' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='BWP' LIMIT 1), (SELECT id FROM countries WHERE iso2='BW' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='NOK' LIMIT 1), (SELECT id FROM countries WHERE iso2='BV' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='BRL' LIMIT 1), (SELECT id FROM countries WHERE iso2='BR' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='USD' LIMIT 1), (SELECT id FROM countries WHERE iso2='IO' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='BND' LIMIT 1), (SELECT id FROM countries WHERE iso2='BN' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='BGN' LIMIT 1), (SELECT id FROM countries WHERE iso2='BG' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='XOF' LIMIT 1), (SELECT id FROM countries WHERE iso2='BF' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='BIF' LIMIT 1), (SELECT id FROM countries WHERE iso2='BI' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='KHR' LIMIT 1), (SELECT id FROM countries WHERE iso2='KH' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='XAF' LIMIT 1), (SELECT id FROM countries WHERE iso2='CM' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='CAD' LIMIT 1), (SELECT id FROM countries WHERE iso2='CA' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='CVE' LIMIT 1), (SELECT id FROM countries WHERE iso2='CV' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='KYD' LIMIT 1), (SELECT id FROM countries WHERE iso2='KY' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='XAF' LIMIT 1), (SELECT id FROM countries WHERE iso2='CF' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='XAF' LIMIT 1), (SELECT id FROM countries WHERE iso2='TD' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='CLP' LIMIT 1), (SELECT id FROM countries WHERE iso2='CL' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='CNY' LIMIT 1), (SELECT id FROM countries WHERE iso2='CN' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='AUD' LIMIT 1), (SELECT id FROM countries WHERE iso2='CX' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='AUD' LIMIT 1), (SELECT id FROM countries WHERE iso2='CC' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='COP' LIMIT 1), (SELECT id FROM countries WHERE iso2='CO' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='KMF' LIMIT 1), (SELECT id FROM countries WHERE iso2='KM' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='CDF' LIMIT 1), (SELECT id FROM countries WHERE iso2='CG' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='NZD' LIMIT 1), (SELECT id FROM countries WHERE iso2='CK' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='CRC' LIMIT 1), (SELECT id FROM countries WHERE iso2='CR' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='EUR' LIMIT 1), (SELECT id FROM countries WHERE iso2='HR' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='CUP' LIMIT 1), (SELECT id FROM countries WHERE iso2='CU' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='ANG' LIMIT 1), (SELECT id FROM countries WHERE iso2='CW' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='EUR' LIMIT 1), (SELECT id FROM countries WHERE iso2='CY' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='CZK' LIMIT 1), (SELECT id FROM countries WHERE iso2='CZ' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='CDF' LIMIT 1), (SELECT id FROM countries WHERE iso2='CD' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='DKK' LIMIT 1), (SELECT id FROM countries WHERE iso2='DK' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='DJF' LIMIT 1), (SELECT id FROM countries WHERE iso2='DJ' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='XCD' LIMIT 1), (SELECT id FROM countries WHERE iso2='DM' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='DOP' LIMIT 1), (SELECT id FROM countries WHERE iso2='DO' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='USD' LIMIT 1), (SELECT id FROM countries WHERE iso2='EC' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='EGP' LIMIT 1), (SELECT id FROM countries WHERE iso2='EG' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='USD' LIMIT 1), (SELECT id FROM countries WHERE iso2='SV' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='XAF' LIMIT 1), (SELECT id FROM countries WHERE iso2='GQ' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='ERN' LIMIT 1), (SELECT id FROM countries WHERE iso2='ER' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='EUR' LIMIT 1), (SELECT id FROM countries WHERE iso2='EE' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='SZL' LIMIT 1), (SELECT id FROM countries WHERE iso2='SZ' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='ETB' LIMIT 1), (SELECT id FROM countries WHERE iso2='ET' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='FKP' LIMIT 1), (SELECT id FROM countries WHERE iso2='FK' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='DKK' LIMIT 1), (SELECT id FROM countries WHERE iso2='FO' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='FJD' LIMIT 1), (SELECT id FROM countries WHERE iso2='FJ' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='EUR' LIMIT 1), (SELECT id FROM countries WHERE iso2='FI' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='EUR' LIMIT 1), (SELECT id FROM countries WHERE iso2='FR' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='EUR' LIMIT 1), (SELECT id FROM countries WHERE iso2='GF' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='XPF' LIMIT 1), (SELECT id FROM countries WHERE iso2='PF' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='EUR' LIMIT 1), (SELECT id FROM countries WHERE iso2='TF' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='XAF' LIMIT 1), (SELECT id FROM countries WHERE iso2='GA' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='GEL' LIMIT 1), (SELECT id FROM countries WHERE iso2='GE' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='EUR' LIMIT 1), (SELECT id FROM countries WHERE iso2='DE' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='GHS' LIMIT 1), (SELECT id FROM countries WHERE iso2='GH' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='GIP' LIMIT 1), (SELECT id FROM countries WHERE iso2='GI' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='EUR' LIMIT 1), (SELECT id FROM countries WHERE iso2='GR' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='DKK' LIMIT 1), (SELECT id FROM countries WHERE iso2='GL' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='XCD' LIMIT 1), (SELECT id FROM countries WHERE iso2='GD' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='EUR' LIMIT 1), (SELECT id FROM countries WHERE iso2='GP' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='USD' LIMIT 1), (SELECT id FROM countries WHERE iso2='GU' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='GTQ' LIMIT 1), (SELECT id FROM countries WHERE iso2='GT' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='GBP' LIMIT 1), (SELECT id FROM countries WHERE iso2='GG' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='GNF' LIMIT 1), (SELECT id FROM countries WHERE iso2='GN' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='XOF' LIMIT 1), (SELECT id FROM countries WHERE iso2='GW' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='GYD' LIMIT 1), (SELECT id FROM countries WHERE iso2='GY' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='HTG' LIMIT 1), (SELECT id FROM countries WHERE iso2='HT' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='AUD' LIMIT 1), (SELECT id FROM countries WHERE iso2='HM' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='HNL' LIMIT 1), (SELECT id FROM countries WHERE iso2='HN' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='HKD' LIMIT 1), (SELECT id FROM countries WHERE iso2='HK' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='HUF' LIMIT 1), (SELECT id FROM countries WHERE iso2='HU' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='ISK' LIMIT 1), (SELECT id FROM countries WHERE iso2='IS' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='INR' LIMIT 1), (SELECT id FROM countries WHERE iso2='IN' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='IDR' LIMIT 1), (SELECT id FROM countries WHERE iso2='ID' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='IRR' LIMIT 1), (SELECT id FROM countries WHERE iso2='IR' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='IQD' LIMIT 1), (SELECT id FROM countries WHERE iso2='IQ' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='EUR' LIMIT 1), (SELECT id FROM countries WHERE iso2='IE' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='ILS' LIMIT 1), (SELECT id FROM countries WHERE iso2='IL' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='EUR' LIMIT 1), (SELECT id FROM countries WHERE iso2='IT' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='XOF' LIMIT 1), (SELECT id FROM countries WHERE iso2='CI' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='JMD' LIMIT 1), (SELECT id FROM countries WHERE iso2='JM' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='JPY' LIMIT 1), (SELECT id FROM countries WHERE iso2='JP' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='GBP' LIMIT 1), (SELECT id FROM countries WHERE iso2='JE' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='JOD' LIMIT 1), (SELECT id FROM countries WHERE iso2='JO' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='KZT' LIMIT 1), (SELECT id FROM countries WHERE iso2='KZ' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='KES' LIMIT 1), (SELECT id FROM countries WHERE iso2='KE' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='AUD' LIMIT 1), (SELECT id FROM countries WHERE iso2='KI' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='EUR' LIMIT 1), (SELECT id FROM countries WHERE iso2='XK' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='KWD' LIMIT 1), (SELECT id FROM countries WHERE iso2='KW' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='KGS' LIMIT 1), (SELECT id FROM countries WHERE iso2='KG' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='LAK' LIMIT 1), (SELECT id FROM countries WHERE iso2='LA' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='EUR' LIMIT 1), (SELECT id FROM countries WHERE iso2='LV' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='LBP' LIMIT 1), (SELECT id FROM countries WHERE iso2='LB' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='LSL' LIMIT 1), (SELECT id FROM countries WHERE iso2='LS' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='LRD' LIMIT 1), (SELECT id FROM countries WHERE iso2='LR' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='LYD' LIMIT 1), (SELECT id FROM countries WHERE iso2='LY' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='CHF' LIMIT 1), (SELECT id FROM countries WHERE iso2='LI' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='EUR' LIMIT 1), (SELECT id FROM countries WHERE iso2='LT' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='EUR' LIMIT 1), (SELECT id FROM countries WHERE iso2='LU' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='MOP' LIMIT 1), (SELECT id FROM countries WHERE iso2='MO' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='MGA' LIMIT 1), (SELECT id FROM countries WHERE iso2='MG' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='MWK' LIMIT 1), (SELECT id FROM countries WHERE iso2='MW' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='MYR' LIMIT 1), (SELECT id FROM countries WHERE iso2='MY' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='MVR' LIMIT 1), (SELECT id FROM countries WHERE iso2='MV' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='XOF' LIMIT 1), (SELECT id FROM countries WHERE iso2='ML' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='EUR' LIMIT 1), (SELECT id FROM countries WHERE iso2='MT' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='GBP' LIMIT 1), (SELECT id FROM countries WHERE iso2='IM' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='USD' LIMIT 1), (SELECT id FROM countries WHERE iso2='MH' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='EUR' LIMIT 1), (SELECT id FROM countries WHERE iso2='MQ' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='MRU' LIMIT 1), (SELECT id FROM countries WHERE iso2='MR' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='MUR' LIMIT 1), (SELECT id FROM countries WHERE iso2='MU' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='EUR' LIMIT 1), (SELECT id FROM countries WHERE iso2='YT' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='MXN' LIMIT 1), (SELECT id FROM countries WHERE iso2='MX' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='USD' LIMIT 1), (SELECT id FROM countries WHERE iso2='FM' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='MDL' LIMIT 1), (SELECT id FROM countries WHERE iso2='MD' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='EUR' LIMIT 1), (SELECT id FROM countries WHERE iso2='MC' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='MNT' LIMIT 1), (SELECT id FROM countries WHERE iso2='MN' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='EUR' LIMIT 1), (SELECT id FROM countries WHERE iso2='ME' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='XCD' LIMIT 1), (SELECT id FROM countries WHERE iso2='MS' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='MAD' LIMIT 1), (SELECT id FROM countries WHERE iso2='MA' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='MZN' LIMIT 1), (SELECT id FROM countries WHERE iso2='MZ' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='MMK' LIMIT 1), (SELECT id FROM countries WHERE iso2='MM' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='NAD' LIMIT 1), (SELECT id FROM countries WHERE iso2='NA' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='AUD' LIMIT 1), (SELECT id FROM countries WHERE iso2='NR' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='NPR' LIMIT 1), (SELECT id FROM countries WHERE iso2='NP' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='EUR' LIMIT 1), (SELECT id FROM countries WHERE iso2='NL' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='XPF' LIMIT 1), (SELECT id FROM countries WHERE iso2='NC' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='NZD' LIMIT 1), (SELECT id FROM countries WHERE iso2='NZ' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='NIO' LIMIT 1), (SELECT id FROM countries WHERE iso2='NI' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='XOF' LIMIT 1), (SELECT id FROM countries WHERE iso2='NE' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='NGN' LIMIT 1), (SELECT id FROM countries WHERE iso2='NG' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='NZD' LIMIT 1), (SELECT id FROM countries WHERE iso2='NU' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='AUD' LIMIT 1), (SELECT id FROM countries WHERE iso2='NF' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='KPW' LIMIT 1), (SELECT id FROM countries WHERE iso2='KP' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='MKD' LIMIT 1), (SELECT id FROM countries WHERE iso2='MK' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='USD' LIMIT 1), (SELECT id FROM countries WHERE iso2='MP' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='NOK' LIMIT 1), (SELECT id FROM countries WHERE iso2='NO' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='OMR' LIMIT 1), (SELECT id FROM countries WHERE iso2='OM' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='PKR' LIMIT 1), (SELECT id FROM countries WHERE iso2='PK' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='USD' LIMIT 1), (SELECT id FROM countries WHERE iso2='PW' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='ILS' LIMIT 1), (SELECT id FROM countries WHERE iso2='PS' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='PAB' LIMIT 1), (SELECT id FROM countries WHERE iso2='PA' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='PGK' LIMIT 1), (SELECT id FROM countries WHERE iso2='PG' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='PYG' LIMIT 1), (SELECT id FROM countries WHERE iso2='PY' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='PEN' LIMIT 1), (SELECT id FROM countries WHERE iso2='PE' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='PHP' LIMIT 1), (SELECT id FROM countries WHERE iso2='PH' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='NZD' LIMIT 1), (SELECT id FROM countries WHERE iso2='PN' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='PLN' LIMIT 1), (SELECT id FROM countries WHERE iso2='PL' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='EUR' LIMIT 1), (SELECT id FROM countries WHERE iso2='PT' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='USD' LIMIT 1), (SELECT id FROM countries WHERE iso2='PR' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='QAR' LIMIT 1), (SELECT id FROM countries WHERE iso2='QA' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='EUR' LIMIT 1), (SELECT id FROM countries WHERE iso2='RE' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='RON' LIMIT 1), (SELECT id FROM countries WHERE iso2='RO' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='RUB' LIMIT 1), (SELECT id FROM countries WHERE iso2='RU' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='RWF' LIMIT 1), (SELECT id FROM countries WHERE iso2='RW' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='SHP' LIMIT 1), (SELECT id FROM countries WHERE iso2='SH' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='XCD' LIMIT 1), (SELECT id FROM countries WHERE iso2='KN' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='XCD' LIMIT 1), (SELECT id FROM countries WHERE iso2='LC' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='EUR' LIMIT 1), (SELECT id FROM countries WHERE iso2='PM' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='XCD' LIMIT 1), (SELECT id FROM countries WHERE iso2='VC' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='EUR' LIMIT 1), (SELECT id FROM countries WHERE iso2='BL' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='EUR' LIMIT 1), (SELECT id FROM countries WHERE iso2='MF' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='WST' LIMIT 1), (SELECT id FROM countries WHERE iso2='WS' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='EUR' LIMIT 1), (SELECT id FROM countries WHERE iso2='SM' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='STN' LIMIT 1), (SELECT id FROM countries WHERE iso2='ST' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='SAR' LIMIT 1), (SELECT id FROM countries WHERE iso2='SA' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='XOF' LIMIT 1), (SELECT id FROM countries WHERE iso2='SN' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='RSD' LIMIT 1), (SELECT id FROM countries WHERE iso2='RS' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='SCR' LIMIT 1), (SELECT id FROM countries WHERE iso2='SC' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='SLL' LIMIT 1), (SELECT id FROM countries WHERE iso2='SL' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='SGD' LIMIT 1), (SELECT id FROM countries WHERE iso2='SG' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='ANG' LIMIT 1), (SELECT id FROM countries WHERE iso2='SX' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='EUR' LIMIT 1), (SELECT id FROM countries WHERE iso2='SK' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='EUR' LIMIT 1), (SELECT id FROM countries WHERE iso2='SI' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='SBD' LIMIT 1), (SELECT id FROM countries WHERE iso2='SB' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='SOS' LIMIT 1), (SELECT id FROM countries WHERE iso2='SO' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='ZAR' LIMIT 1), (SELECT id FROM countries WHERE iso2='ZA' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='GBP' LIMIT 1), (SELECT id FROM countries WHERE iso2='GS' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='KRW' LIMIT 1), (SELECT id FROM countries WHERE iso2='KR' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='SSP' LIMIT 1), (SELECT id FROM countries WHERE iso2='SS' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='EUR' LIMIT 1), (SELECT id FROM countries WHERE iso2='ES' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='LKR' LIMIT 1), (SELECT id FROM countries WHERE iso2='LK' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='SDG' LIMIT 1), (SELECT id FROM countries WHERE iso2='SD' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='SRD' LIMIT 1), (SELECT id FROM countries WHERE iso2='SR' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='NOK' LIMIT 1), (SELECT id FROM countries WHERE iso2='SJ' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='SEK' LIMIT 1), (SELECT id FROM countries WHERE iso2='SE' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='CHF' LIMIT 1), (SELECT id FROM countries WHERE iso2='CH' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='SYP' LIMIT 1), (SELECT id FROM countries WHERE iso2='SY' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='TWD' LIMIT 1), (SELECT id FROM countries WHERE iso2='TW' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='TJS' LIMIT 1), (SELECT id FROM countries WHERE iso2='TJ' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='TZS' LIMIT 1), (SELECT id FROM countries WHERE iso2='TZ' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='THB' LIMIT 1), (SELECT id FROM countries WHERE iso2='TH' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='BSD' LIMIT 1), (SELECT id FROM countries WHERE iso2='BS' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='GMD' LIMIT 1), (SELECT id FROM countries WHERE iso2='GM' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='USD' LIMIT 1), (SELECT id FROM countries WHERE iso2='TL' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='XOF' LIMIT 1), (SELECT id FROM countries WHERE iso2='TG' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='NZD' LIMIT 1), (SELECT id FROM countries WHERE iso2='TK' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='TOP' LIMIT 1), (SELECT id FROM countries WHERE iso2='TO' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='TTD' LIMIT 1), (SELECT id FROM countries WHERE iso2='TT' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='TND' LIMIT 1), (SELECT id FROM countries WHERE iso2='TN' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='TRY' LIMIT 1), (SELECT id FROM countries WHERE iso2='TR' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='TMT' LIMIT 1), (SELECT id FROM countries WHERE iso2='TM' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='USD' LIMIT 1), (SELECT id FROM countries WHERE iso2='TC' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='AUD' LIMIT 1), (SELECT id FROM countries WHERE iso2='TV' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='UGX' LIMIT 1), (SELECT id FROM countries WHERE iso2='UG' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='UAH' LIMIT 1), (SELECT id FROM countries WHERE iso2='UA' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='AED' LIMIT 1), (SELECT id FROM countries WHERE iso2='AE' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='GBP' LIMIT 1), (SELECT id FROM countries WHERE iso2='GB' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='USD' LIMIT 1), (SELECT id FROM countries WHERE iso2='US' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='USD' LIMIT 1), (SELECT id FROM countries WHERE iso2='UM' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='UYU' LIMIT 1), (SELECT id FROM countries WHERE iso2='UY' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='UZS' LIMIT 1), (SELECT id FROM countries WHERE iso2='UZ' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='VUV' LIMIT 1), (SELECT id FROM countries WHERE iso2='VU' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='EUR' LIMIT 1), (SELECT id FROM countries WHERE iso2='VA' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='VES' LIMIT 1), (SELECT id FROM countries WHERE iso2='VE' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='VND' LIMIT 1), (SELECT id FROM countries WHERE iso2='VN' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='USD' LIMIT 1), (SELECT id FROM countries WHERE iso2='VG' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='USD' LIMIT 1), (SELECT id FROM countries WHERE iso2='VI' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='XPF' LIMIT 1), (SELECT id FROM countries WHERE iso2='WF' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='MAD' LIMIT 1), (SELECT id FROM countries WHERE iso2='EH' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='YER' LIMIT 1), (SELECT id FROM countries WHERE iso2='YE' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='ZMW' LIMIT 1), (SELECT id FROM countries WHERE iso2='ZM' LIMIT 1), NOW(), NOW();
INSERT INTO currency_countries (currency_id, country_id, created_date, updated_date) SELECT (SELECT id FROM currencies WHERE code='ZWL' LIMIT 1), (SELECT id FROM countries WHERE iso2='ZW' LIMIT 1), NOW(), NOW();

-- Insert Country-Timezone Mapping
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AF' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Kabul' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AX' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Mariehamn' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AL' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Tirane' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='DZ' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Algiers' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AS' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Pacific/Pago_Pago' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AD' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Andorra' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AO' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Luanda' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AI' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Anguilla' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AQ' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Antarctica/Casey' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AQ' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Antarctica/Davis' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AQ' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Antarctica/DumontDUrville' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AQ' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Antarctica/Mawson' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AQ' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Antarctica/McMurdo' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AQ' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Antarctica/Palmer' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AQ' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Antarctica/Rothera' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AQ' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Antarctica/Syowa' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AQ' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Antarctica/Troll' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AQ' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Antarctica/Vostok' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AG' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Antigua' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AR' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Argentina/Buenos_Aires' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AR' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Argentina/Catamarca' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AR' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Argentina/Cordoba' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AR' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Argentina/Jujuy' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AR' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Argentina/La_Rioja' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AR' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Argentina/Mendoza' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AR' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Argentina/Rio_Gallegos' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AR' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Argentina/Salta' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AR' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Argentina/San_Juan' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AR' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Argentina/San_Luis' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AR' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Argentina/Tucuman' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AR' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Argentina/Ushuaia' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AM' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Yerevan' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AW' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Aruba' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Antarctica/Macquarie' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Australia/Adelaide' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Australia/Brisbane' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Australia/Broken_Hill' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Australia/Currie' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Australia/Darwin' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Australia/Eucla' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Australia/Hobart' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Australia/Lindeman' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Australia/Lord_Howe' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Australia/Melbourne' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Australia/Perth' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Australia/Sydney' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AT' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Vienna' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AZ' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Baku' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='BH' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Bahrain' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='BD' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Dhaka' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='BB' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Barbados' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='BY' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Minsk' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='BE' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Brussels' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='BZ' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Belize' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='BJ' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Porto-Novo' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='BM' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Atlantic/Bermuda' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='BT' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Thimphu' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='BO' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/La_Paz' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='BQ' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Kralendijk' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='BA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Sarajevo' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='BW' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Gaborone' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='BV' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Oslo' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='BR' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Araguaina' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='BR' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Bahia' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='BR' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Belem' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='BR' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Boa_Vista' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='BR' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Campo_Grande' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='BR' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Cuiaba' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='BR' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Eirunepe' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='BR' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Fortaleza' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='BR' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Maceio' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='BR' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Manaus' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='BR' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Noronha' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='BR' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Porto_Velho' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='BR' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Recife' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='BR' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Rio_Branco' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='BR' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Santarem' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='BR' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Sao_Paulo' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='IO' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Indian/Chagos' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='BN' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Brunei' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='BG' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Sofia' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='BF' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Ouagadougou' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='BI' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Bujumbura' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='KH' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Phnom_Penh' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CM' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Douala' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Atikokan' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Blanc-Sablon' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Cambridge_Bay' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Creston' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Dawson' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Dawson_Creek' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Edmonton' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Fort_Nelson' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Glace_Bay' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Goose_Bay' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Halifax' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Inuvik' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Iqaluit' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Moncton' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Nipigon' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Pangnirtung' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Rainy_River' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Rankin_Inlet' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Regina' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Resolute' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/St_Johns' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Swift_Current' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Thunder_Bay' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Toronto' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Vancouver' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Whitehorse' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Winnipeg' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Yellowknife' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CV' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Atlantic/Cape_Verde' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='KY' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Cayman' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CF' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Bangui' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='TD' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Ndjamena' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CL' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Punta_Arenas' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CL' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Santiago' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CL' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Pacific/Easter' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CL' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Coyhaique' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CN' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Shanghai' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CN' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Urumqi' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CX' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Indian/Christmas' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CC' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Indian/Cocos' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CO' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Bogota' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='KM' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Indian/Comoro' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CG' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Brazzaville' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CK' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Pacific/Rarotonga' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CR' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Costa_Rica' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='HR' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Zagreb' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Havana' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CW' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Curacao' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CY' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Famagusta' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CY' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Nicosia' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CZ' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Prague' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CD' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Kinshasa' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CD' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Lubumbashi' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='DK' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Copenhagen' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='DJ' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Djibouti' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='DM' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Dominica' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='DO' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Santo_Domingo' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='EC' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Guayaquil' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='EC' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Pacific/Galapagos' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='EG' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Cairo' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='SV' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/El_Salvador' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='GQ' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Malabo' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='ER' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Asmara' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='EE' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Tallinn' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='SZ' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Mbabane' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='ET' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Addis_Ababa' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='FK' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Atlantic/Stanley' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='FO' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Atlantic/Faroe' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='FJ' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Pacific/Fiji' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='FI' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Helsinki' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='FR' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Paris' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='GF' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Cayenne' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='PF' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Pacific/Gambier' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='PF' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Pacific/Marquesas' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='PF' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Pacific/Tahiti' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='TF' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Indian/Kerguelen' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='GA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Libreville' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='GE' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Tbilisi' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='DE' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Berlin' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='DE' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Busingen' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='GH' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Accra' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='GI' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Gibraltar' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='GR' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Athens' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='GL' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Danmarkshavn' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='GL' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Nuuk' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='GL' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Scoresbysund' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='GL' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Thule' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='GD' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Grenada' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='GP' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Guadeloupe' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='GU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Pacific/Guam' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='GT' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Guatemala' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='GG' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Guernsey' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='GN' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Conakry' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='GW' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Bissau' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='GY' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Guyana' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='HT' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Port-au-Prince' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='HM' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Indian/Kerguelen' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='HN' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Tegucigalpa' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='HK' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Hong_Kong' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='HU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Budapest' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='IS' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Atlantic/Reykjavik' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='IN' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Kolkata' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='ID' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Jakarta' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='ID' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Jayapura' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='ID' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Makassar' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='ID' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Pontianak' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='IR' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Tehran' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='IQ' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Baghdad' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='IE' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Dublin' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='IL' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Jerusalem' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='IT' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Rome' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CI' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Abidjan' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='JM' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Jamaica' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='JP' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Tokyo' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='JE' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Jersey' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='JO' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Amman' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='KZ' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Almaty' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='KZ' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Aqtau' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='KZ' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Aqtobe' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='KZ' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Atyrau' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='KZ' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Oral' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='KZ' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Qostanay' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='KZ' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Qyzylorda' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='KE' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Nairobi' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='KI' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Pacific/Enderbury' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='KI' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Pacific/Kiritimati' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='KI' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Pacific/Tarawa' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='XK' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Belgrade' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='KW' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Kuwait' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='KG' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Bishkek' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='LA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Vientiane' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='LV' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Riga' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='LB' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Beirut' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='LS' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Maseru' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='LR' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Monrovia' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='LY' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Tripoli' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='LI' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Vaduz' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='LT' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Vilnius' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='LU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Luxembourg' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='MO' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Macau' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='MG' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Indian/Antananarivo' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='MW' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Blantyre' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='MY' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Kuala_Lumpur' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='MY' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Kuching' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='MV' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Indian/Maldives' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='ML' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Bamako' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='MT' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Malta' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='IM' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Isle_of_Man' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='MH' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Pacific/Kwajalein' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='MH' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Pacific/Majuro' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='MQ' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Martinique' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='MR' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Nouakchott' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='MU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Indian/Mauritius' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='YT' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Indian/Mayotte' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='MX' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Bahia_Banderas' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='MX' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Cancun' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='MX' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Chihuahua' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='MX' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Hermosillo' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='MX' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Matamoros' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='MX' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Mazatlan' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='MX' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Merida' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='MX' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Mexico_City' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='MX' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Monterrey' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='MX' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Ojinaga' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='MX' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Tijuana' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='MX' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Ciudad_Juarez' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='FM' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Pacific/Chuuk' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='FM' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Pacific/Kosrae' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='FM' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Pacific/Pohnpei' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='MD' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Chisinau' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='MC' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Monaco' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='MN' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Choibalsan' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='MN' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Hovd' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='MN' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Ulaanbaatar' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='ME' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Podgorica' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='MS' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Montserrat' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='MA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Casablanca' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='MA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/El_Aaiun' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='MZ' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Maputo' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='MM' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Yangon' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='NA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Windhoek' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='NR' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Pacific/Nauru' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='NP' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Kathmandu' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='NL' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Amsterdam' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='NC' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Pacific/Noumea' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='NZ' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Pacific/Auckland' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='NZ' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Pacific/Chatham' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='NI' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Managua' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='NE' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Niamey' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='NG' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Lagos' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='NU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Pacific/Niue' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='NF' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Pacific/Norfolk' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='KP' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Pyongyang' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='MK' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Skopje' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='MP' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Pacific/Saipan' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='NO' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Oslo' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='NO' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Arctic/Longyearbyen' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='OM' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Muscat' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='PK' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Karachi' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='PW' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Pacific/Palau' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='PS' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Gaza' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='PS' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Hebron' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='PA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Panama' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='PG' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Pacific/Bougainville' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='PG' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Pacific/Port_Moresby' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='PY' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Asuncion' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='PE' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Lima' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='PH' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Manila' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='PN' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Pacific/Pitcairn' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='PL' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Warsaw' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='PT' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Atlantic/Azores' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='PT' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Atlantic/Madeira' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='PT' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Lisbon' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='PR' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Puerto_Rico' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='QA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Qatar' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='RE' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Indian/Reunion' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='RO' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Bucharest' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='RU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Anadyr' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='RU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Barnaul' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='RU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Chita' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='RU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Irkutsk' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='RU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Kamchatka' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='RU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Khandyga' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='RU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Krasnoyarsk' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='RU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Magadan' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='RU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Novokuznetsk' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='RU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Novosibirsk' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='RU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Omsk' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='RU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Sakhalin' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='RU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Srednekolymsk' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='RU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Tomsk' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='RU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Ust-Nera' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='RU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Vladivostok' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='RU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Yakutsk' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='RU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Yekaterinburg' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='RU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Astrakhan' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='RU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Kaliningrad' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='RU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Kirov' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='RU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Moscow' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='RU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Samara' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='RU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Saratov' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='RU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Ulyanovsk' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='RU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Volgograd' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='RW' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Kigali' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='SH' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Atlantic/St_Helena' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='KN' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/St_Kitts' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='LC' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/St_Lucia' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='PM' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Miquelon' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='VC' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/St_Vincent' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='BL' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/St_Barthelemy' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='MF' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Marigot' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='WS' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Pacific/Apia' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='SM' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/San_Marino' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='ST' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Sao_Tome' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='SA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Riyadh' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='SN' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Dakar' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='RS' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Belgrade' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='SC' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Indian/Mahe' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='SL' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Freetown' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='SG' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Singapore' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='SX' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Anguilla' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='SK' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Bratislava' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='SI' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Ljubljana' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='SB' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Pacific/Guadalcanal' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='SO' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Mogadishu' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='ZA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Johannesburg' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='GS' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Atlantic/South_Georgia' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='KR' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Seoul' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='SS' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Juba' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='ES' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Ceuta' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='ES' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Atlantic/Canary' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='ES' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Madrid' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='LK' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Colombo' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='SD' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Khartoum' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='SR' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Paramaribo' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='SJ' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Arctic/Longyearbyen' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='SE' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Stockholm' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='CH' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Zurich' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='SY' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Damascus' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='TW' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Taipei' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='TJ' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Dushanbe' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='TZ' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Dar_es_Salaam' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='TH' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Bangkok' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='BS' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Nassau' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='GM' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Banjul' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='TL' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Dili' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='TG' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Lome' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='TK' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Pacific/Fakaofo' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='TO' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Pacific/Tongatapu' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='TT' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Port_of_Spain' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='TN' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Tunis' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='TR' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Istanbul' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='TM' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Ashgabat' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='TC' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Grand_Turk' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='TV' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Pacific/Funafuti' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='UG' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Kampala' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='UA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Kyiv' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='UA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Simferopol' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='UA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Uzhgorod' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='UA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Zaporozhye' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='AE' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Dubai' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='GB' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/London' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='US' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Adak' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='US' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Anchorage' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='US' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Boise' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='US' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Chicago' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='US' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Denver' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='US' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Detroit' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='US' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Indiana/Indianapolis' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='US' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Indiana/Knox' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='US' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Indiana/Marengo' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='US' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Indiana/Petersburg' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='US' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Indiana/Tell_City' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='US' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Indiana/Vevay' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='US' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Indiana/Vincennes' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='US' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Indiana/Winamac' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='US' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Juneau' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='US' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Kentucky/Louisville' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='US' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Kentucky/Monticello' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='US' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Los_Angeles' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='US' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Menominee' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='US' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Metlakatla' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='US' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/New_York' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='US' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Nome' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='US' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/North_Dakota/Beulah' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='US' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/North_Dakota/Center' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='US' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/North_Dakota/New_Salem' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='US' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Phoenix' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='US' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Sitka' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='US' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Yakutat' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='US' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Pacific/Honolulu' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='UM' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Pacific/Midway' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='UM' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Pacific/Wake' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='UY' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Montevideo' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='UZ' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Samarkand' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='UZ' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Tashkent' LIMIT 1), FALSE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='VU' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Pacific/Efate' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='VA' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Europe/Vatican' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='VE' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Caracas' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='VN' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Ho_Chi_Minh' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='VG' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/Tortola' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='VI' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='America/St_Thomas' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='WF' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Pacific/Wallis' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='EH' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/El_Aaiun' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='YE' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Asia/Aden' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='ZM' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Lusaka' LIMIT 1), TRUE, NOW(), NOW();
INSERT INTO country_timezones (country_id, timezone_id, is_default, created_date, updated_date) SELECT (SELECT id FROM countries WHERE iso2='ZW' LIMIT 1), (SELECT id FROM timezones WHERE iana_name='Africa/Harare' LIMIT 1), TRUE, NOW(), NOW();

-- Insert Country-Language Mapping