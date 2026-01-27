-- 1. Create Login User Table
CREATE TABLE IF NOT EXISTS "login_user" (
    "id"          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    "user_name"   VARCHAR(255) UNIQUE,
    "name"        VARCHAR(255),
    "email"       VARCHAR(255) UNIQUE,
    "phone"       VARCHAR(255),
    "roles"       VARCHAR(1000),
    "created_at"  TIMESTAMP DEFAULT NOW(),
    "created_by"  VARCHAR(255),
    "updated_at"  TIMESTAMP DEFAULT NOW(),
    "updated_by"  VARCHAR(255)
    );

-- 2. Create Bank Account Table
CREATE TABLE IF NOT EXISTS bank_account (
    "id"              UUID PRIMARY KEY,
    "first_name"      VARCHAR(100) NOT NULL,
    "last_name"       VARCHAR(100) NOT NULL,
    "phone"           VARCHAR(20),
    "email"           VARCHAR(255) UNIQUE,
    "street"          VARCHAR(150),
    "street_number"   VARCHAR(20),
    "postal_code"     VARCHAR(20),
    "city"            VARCHAR(100),
    "country"         VARCHAR(100),
    "account_number"  VARCHAR(30) NOT NULL UNIQUE,
    "account_type"    VARCHAR(20) NOT NULL,
    "ifsc_code"       VARCHAR(20),
    "balance"         NUMERIC(14, 2),
    "currency"        CHAR(3),
    "status"          VARCHAR(20),
    "created_at"      TIMESTAMP,
    "created_by"      VARCHAR(255),
    "updated_at"      TIMESTAMP,
    "updated_by"      VARCHAR(255)
    );

CREATE SEQUENCE IF NOT EXISTS transaction_number_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 100;

CREATE SEQUENCE IF NOT EXISTS sequence_number
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 100;

-- 3. Create Transaction Detail Table
CREATE TABLE IF NOT EXISTS transaction_detail (
    "id"                    UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    "transaction_number"    VARCHAR(16) NOT NULL DEFAULT ('TRN' || LPAD(nextval('transaction_number_seq')::text, 13, '0')),
    "sequence_number"       BIGINT NOT NULL DEFAULT nextval('sequence_number')::BIGINT,
    "date"                  DATE NULL,
    "domain"                VARCHAR(50) NULL,
    "location"              VARCHAR(50) NULL,
    "value"                 INT NULL,
    "status"                VARCHAR(255) NULL,
    "payment_method"        VARCHAR(255) NULL,
    "tax_amount"            NUMERIC(12, 2) NULL,
    "net_value"             NUMERIC(12, 2) NULL,
    "bank_account_id"       UUID NULL,
    "created_at"            TIMESTAMP NULL,
    "updated_at"            TIMESTAMP NULL,
    "created_by"            VARCHAR(255) NULL,
    "updated_by"            VARCHAR(255) NULL,
    CONSTRAINT uq_transaction_detail_transaction_number
    UNIQUE (transaction_number),
    CONSTRAINT uq_sequence_number
    UNIQUE (sequence_number),
    CONSTRAINT transaction_detail_bank_account_id_fkey
    FOREIGN KEY (bank_account_id) REFERENCES bank_account(id)
    );

INSERT INTO transaction_detail (
    id, "date", domain, location, value,
    status, payment_method, tax_amount, net_value,
    created_at, updated_at, created_by, updated_by
)
SELECT
    gen_random_uuid(),
    v.dt::date,
    (ARRAY['EDUCATION', 'INTERNATIONAL', 'INVESTMENTS', 'MEDICAL', 'PUBLIC', 'RESTAURANT', 'RETAIL'])[floor(random() * 7) + 1],
    (ARRAY['Ahmedabad','Ajmer','Akola','Ambala','Amritsar','Ara','Banglore','Betul','Bhind','Bhopal','Bhuj','Bidar','Bikaner','Bokaro','Bombay','Buxar','Daman','Delhi','Doda','Durg','Goa','Hyderabad','Indore','Jaipur','Kannur','Kanpur','Kochin','Kolkata','Konark','Kota','Kullu','Lucknow','Ludhiana','Lunglei','Madurai','Mathura','Mon','Patiala','Pune','Ranchi','Srinagar','Surat','Tirumala','Trichy','Varanasi','Vellore'])[floor(random() * 46) + 1],

    -- 1. Random Value (Now changes per row)
    v.val,

    -- 2. Status Distribution (Fixed counts)
    CASE
        WHEN g <= 10000 THEN 'FAILED'
        WHEN g <= 18000 THEN 'CANCELLED'
        ELSE 'SUCCESS'
    END,

    (ARRAY['CARD', 'CASH', 'UPI'])[floor(random() * 3) + 1],

    -- 3. Tax Amount (Small realistic tax)
    CASE
        WHEN g <= 18000 THEN 0
        ELSE round((v.val * v.tax_rate), 2)
    END,

    -- 4. Net Value (Ensures value = tax + net)
    CASE
        WHEN g <= 18000 THEN 0
        ELSE round((v.val * (1.0 - v.tax_rate)), 2)
    END,

    v.dt,
    v.dt + (random() * interval '2 hours'),
    'SYSTEM',
    'SYSTEM'
FROM generate_series(1, 10000000) AS g
CROSS JOIN LATERAL (
    SELECT
        NOW() - (random() * interval '5 years') AS dt,
        -- Generate random value between 500 and 50000
        floor(random() * 49501 + 500)::int AS val,
        -- Define a single tax rate per row (5% to 12%) so math is consistent
        (0.05 + (random() * 0.07))::numeric AS tax_rate
    WHERE g IS NOT NULL -- FORCES randomization per row
) AS v;

DO $$
DECLARE
current_account_count INT;
    account_array UUID[];
BEGIN
    -- A. Check current account count
SELECT COUNT(*) INTO current_account_count FROM bank_account;

-- B. Insert accounts if we have fewer than 200,000
IF current_account_count < 200000 THEN
        RAISE NOTICE 'Inserting % new accounts...', (200000 - current_account_count);

INSERT INTO bank_account (
    id, first_name, last_name, phone, email, street, street_number,
    postal_code, city, country, account_number, account_type,
    ifsc_code, balance, currency, status, created_at, created_by, updated_at, updated_by
)
SELECT
    gen_random_uuid(),
    (ARRAY['James','John','Robert','Michael','William','David','Richard','Joseph','Thomas','Charles','Christopher','Daniel','Matthew','Anthony','Mark','Paul','Steven','Andrew','Kenneth','George','Edward','Brian','Kevin','Jason','Jeffrey','Ryan','Jacob','Gary','Nicholas','Eric','Emily','Emma','Olivia','Sophia','Isabella','Mia','Charlotte','Amelia','Harper','Evelyn','Abigail','Ella','Avery','Scarlett','Grace','Chloe'])[FLOOR(RANDOM() * 46) + 1],
            (ARRAY['Smith','Johnson','Williams','Brown','Jones','Garcia','Miller','Davis','Rodriguez','Martinez','Hernandez','Lopez','Gonzalez','Wilson','Anderson','Thomas','Taylor','Moore','Jackson','Martin','Lee','Perez','Thompson','White','Harris','Sanchez','Clark','Ramirez','Lewis','Robinson','Walker','Young','Allen','King','Wright','Scott','Torres','Nguyen','Hill','Flores','Green','Adams','Nelson','Baker','Hall'])[FLOOR(RANDOM() * 45) + 1],
            '+1-' || FLOOR(RANDOM() * (999 -200) + 200)::TEXT || '-' || FLOOR(RANDOM() * (999 -200) + 200)::TEXT || '-' || LPAD(FLOOR(RANDOM() * 9999)::TEXT, 4, '0'),
            'user' || (current_account_count + g) || '@example.com',
            (ARRAY['Main Street','High Street','Broadway','Oak Street','Maple Avenue','Cedar Road','Pine Street','Elm Street','Washington Avenue','Lakeview Drive','Sunset Boulevard','Hillcrest Road','Riverside Drive','Park Avenue','Church Street','Mill Road'])[FLOOR(RANDOM() * 16) + 1],
            (FLOOR(RANDOM() * 999) + 1)::TEXT,
            LPAD((FLOOR(RANDOM() * 89999) + 10000)::TEXT, 5, '0'),
            (ARRAY['New York','Los Angeles','Chicago','Houston','Phoenix','Philadelphia','San Antonio','San Diego','Dallas','San Jose','Austin','Seattle','Denver','Boston','San Francisco','Oakland','Berkeley','Palo Alto','Mountain View','Sunnyvale','Santa Clara','Redwood City','Fremont','San Mateo','Cupertino'])[FLOOR(RANDOM() * 25) + 1],
            (ARRAY['United States','United Kingdom','Canada','Australia'])[FLOOR(RANDOM() * 4) + 1],
            'ACCT' || LPAD((current_account_count + g)::TEXT, 10, '0'),
            (ARRAY['SAVINGS', 'CURRENT'])[FLOOR(RANDOM() * 2) + 1],
            'BANK' || '0' || LPAD(FLOOR(RANDOM() * 999999)::TEXT, 6, '0'),
            ROUND((RANDOM() * 750000)::NUMERIC, 2),
            'USD',
            (ARRAY['ACTIVE', 'INACTIVE', 'CLOSED'])[FLOOR(RANDOM() * 3) + 1],
            t.created_date,
            'system',
            t.created_date + (RANDOM() * INTERVAL '30 days'),
            'system'
FROM GENERATE_SERIES(1, (200000 - current_account_count)) AS g, -- Added comma here
    LATERAL (SELECT TIMESTAMP '2022-01-01' + RANDOM() * (NOW() - TIMESTAMP '2022-01-01') AS created_date) AS t;
END IF;

    -- C. Load all 200,000 IDs into memory
    account_array := ARRAY(SELECT id FROM bank_account);

    -- D. Update transaction_detail with random IDs
    RAISE NOTICE 'Linking transactions to accounts...';

UPDATE "transaction_detail"
SET bank_account_id = account_array[floor(random() * array_length(account_array, 1)) + 1]
WHERE bank_account_id IS NULL;

RAISE NOTICE 'Process Complete.';
END $$;

-- 4. Final Constraint
ALTER TABLE "transaction_detail" ALTER COLUMN bank_account_id SET NOT NULL;


