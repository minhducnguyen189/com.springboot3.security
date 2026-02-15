
CREATE SEQUENCE IF NOT EXISTS bank_account_sequence_number
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 100;

ALTER TABLE bank_account
ADD COLUMN IF NOT EXISTS sequence_number BIGINT UNIQUE NOT NULL DEFAULT nextval('bank_account_sequence_number');

-- Populate existing rows with nextval from the sequence
UPDATE bank_account
SET sequence_number = nextval('bank_account_sequence_number')
WHERE sequence_number IS NULL;

