DO
$do$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = 'poche') THEN
        CREATE ROLE aet WITH LOGIN PASSWORD 'poche';
        GRANT CONNECT ON DATABASE aet TO poche;
    END IF;
END
$do$;

BEGIN;

CREATE SCHEMA IF NOT EXISTS sc_argent_de_poche;
ALTER SCHEMA sc_argent_de_poche OWNER TO poche;

DROP TABLE IF EXISTS
      sc_argent_de_poche.delay_login,
      sc_argent_de_poche.login,
      sc_argent_de_poche.jwt,
      sc_argent_de_poche.role_parent,
      sc_argent_de_poche.role,
      sc_argent_de_poche.parent_family_account,
      sc_argent_de_poche.child_account_movement,
      sc_argent_de_poche.movement_action_code,
      sc_argent_de_poche.child_account_movement_code,
      sc_argent_de_poche.child_account_calendar,
      sc_argent_de_poche.child_account_money,
      sc_argent_de_poche.child_account,
      sc_argent_de_poche.family_account,
      sc_argent_de_poche.family,
      sc_argent_de_poche.child,
      sc_argent_de_poche.child_image,
      sc_argent_de_poche.parent
CASCADE;

DROP SEQUENCE IF EXISTS
sc_argent_de_poche.parent_id_seq,
sc_argent_de_poche.child_id_seq,
sc_argent_de_poche.child_image_id_seq,
sc_argent_de_poche.family_account_id_seq,
sc_argent_de_poche.family_id_seq,
sc_argent_de_poche.child_account_id_seq,
sc_argent_de_poche.child_account_movement_id_seq,
sc_argent_de_poche.parent_family_account_id_seq,
sc_argent_de_poche.role_id_seq,
sc_argent_de_poche.role_parent_id_seq,
sc_argent_de_poche.jwt_id_seq,
sc_argent_de_poche.login_id_seq,
sc_argent_de_poche.delay_login_id_seq,
sc_argent_de_poche.child_account_calendar_id_seq,
sc_argent_de_poche.child_account_money_id_seq,
sc_argent_de_poche.child_account_movement_code_id_seq,
sc_argent_de_poche.movement_action_code_id_seq;


-- parent --
CREATE TABLE if NOT EXISTS sc_argent_de_poche.parent(
    "id" BIGINT PRIMARY KEY,
    "nickname" VARCHAR(255) NOT NULL,
    "email" VARCHAR(255) NOT NULL UNIQUE,
    "password" TEXT NOT NULL,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);
CREATE INDEX IF NOT EXISTS idx_parent_email ON sc_argent_de_poche.parent(email);

-- Compte famille --
CREATE TABLE if NOT EXISTS sc_argent_de_poche.family_account(
    "id" BIGINT PRIMARY KEY,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);

-- Compte famille --
CREATE TABLE if NOT EXISTS sc_argent_de_poche.family(
    "id" BIGINT PRIMARY KEY,
    "family_account_id" BIGINT NOT NULL REFERENCES sc_argent_de_poche."family_account"("id") on delete cascade,
    "name" VARCHAR(255) NOT NULL,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);
CREATE INDEX IF NOT EXISTS idx_family_family_account_id ON sc_argent_de_poche.family(family_account_id);

-- compte argent de poche enfant --
CREATE TABLE if NOT EXISTS sc_argent_de_poche.child_account(
    "id" BIGINT PRIMARY KEY,
    "family_account_id" BIGINT NOT NULL REFERENCES sc_argent_de_poche."family_account"("id") on delete cascade,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);
CREATE INDEX IF NOT EXISTS idx_child_account_family_account_id ON sc_argent_de_poche.child_account(family_account_id);

-- image de l'enfant --
CREATE TABLE if NOT EXISTS sc_argent_de_poche.child_image(
    "id" BIGINT PRIMARY KEY,
    "image_name" VARCHAR(255) NOT NULL,
    "extension" VARCHAR(10) NOT NULL,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);
CREATE INDEX IF NOT EXISTS idx_ci_child_image_name ON sc_argent_de_poche.child_image(image_name);

-- enfant --
CREATE TABLE if NOT EXISTS sc_argent_de_poche.child(
    "id" BIGINT PRIMARY KEY,
    "child_account_id" BIGINT NOT NULL REFERENCES sc_argent_de_poche."child_account"("id") on delete cascade,
    "child_image_id" BIGINT NOT NULL REFERENCES sc_argent_de_poche."child_image"("id"),
    "nickname" VARCHAR(255) NOT NULL,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);
CREATE INDEX IF NOT EXISTS idx_c_child_account_id ON sc_argent_de_poche.child(child_account_id);
CREATE INDEX IF NOT EXISTS idx_c_child_image_id ON sc_argent_de_poche.child(child_image_id);

-- Calendar compte enfant
CREATE TABLE if NOT EXISTS sc_argent_de_poche.child_account_calendar(
    "id" BIGINT PRIMARY KEY,
    "child_account_id" BIGINT NOT NULL REFERENCES sc_argent_de_poche."child_account"("id") on delete cascade,
    "calendar_period" VARCHAR(255) NOT NULL,
    "period_start_day" TIMESTAMPTZ NOT NULL,
    "period_end_day" TIMESTAMPTZ NOT NULL,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);
CREATE INDEX IF NOT EXISTS idx_cac_child_account_id ON sc_argent_de_poche.child_account_calendar(child_account_id);

-- Argent compte enfant
CREATE TABLE if NOT EXISTS sc_argent_de_poche.child_account_money(
    "id" BIGINT PRIMARY KEY,
    "child_account_id" BIGINT NOT NULL REFERENCES sc_argent_de_poche."child_account"("id") on delete cascade,
    "account_calendar_id" BIGINT NOT NULL REFERENCES sc_argent_de_poche."child_account_calendar"("id") on delete cascade,
    "money_at_period_start" NUMERIC(10,2) NOT NULL,
    "remaining_money" NUMERIC(10,2) NOT NULL,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);
CREATE INDEX IF NOT EXISTS idx_cam_child_account_id ON sc_argent_de_poche.child_account_money(child_account_id);
CREATE INDEX IF NOT EXISTS idx_cam_account_calendar_id ON sc_argent_de_poche.child_account_money(account_calendar_id);

-- code des mouvement d'argent --
CREATE TABLE if NOT EXISTS sc_argent_de_poche.movement_action_code(
    "id" INT PRIMARY KEY,
    "movement_code" VARCHAR(5) NOT NULL,
    "movement_name" VARCHAR(255) NOT NULL,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);

-- Liaison mouvement_action_code et child_account
CREATE TABLE if NOT EXISTS sc_argent_de_poche.child_account_movement_code(
    "id" BIGINT PRIMARY KEY,
    "movement_code_id" BIGINT NOT NULL REFERENCES sc_argent_de_poche."movement_action_code"("id") on delete cascade,
    "child_account_id" BIGINT NOT NULL REFERENCES sc_argent_de_poche."child_account"("id") on delete cascade,
    "movement_fluctuation_price" NUMERIC(10,2) NOT NULL,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);
CREATE INDEX IF NOT EXISTS idx_camc_movement_code_id ON sc_argent_de_poche.child_account_movement_code(movement_code_id);
CREATE INDEX IF NOT EXISTS idx_camc_child_account_id ON sc_argent_de_poche.child_account_movement_code(child_account_id);

-- movement d'argent --
CREATE TABLE if NOT EXISTS sc_argent_de_poche.child_account_movement(
    "id" BIGINT PRIMARY KEY,
    "child_account_id" BIGINT NOT NULL REFERENCES sc_argent_de_poche."child_account"("id") on delete cascade,
    "child_account_movement_code_id" BIGINT NOT NULL REFERENCES sc_argent_de_poche."child_account_movement_code"("id") on delete cascade,
    "add_by" BIGINT NOT NULL REFERENCES sc_argent_de_poche."parent"("id") on delete cascade,
    "movement_action_code" VARCHAR(10) NOT NULL,
    "movement_add_at"  TIMESTAMPTZ NOT NULL,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);
CREATE INDEX IF NOT EXISTS idx_cam_child_account_id ON sc_argent_de_poche.child_account_movement(child_account_id);
CREATE INDEX IF NOT EXISTS idx_cam_child_account_movement_code_id ON sc_argent_de_poche.child_account_movement(child_account_movement_code_id);

-- Liaison parent - compte de famille
CREATE TABLE if NOT EXISTS sc_argent_de_poche.parent_family_account(
    "id" BIGINT PRIMARY KEY,
    "parent_id" BIGINT NOT NULL REFERENCES sc_argent_de_poche."parent"("id") on delete cascade,
    "family_account_id" BIGINT NOT NULL REFERENCES sc_argent_de_poche."family_account"("id") on delete cascade,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);

-- Role --
create table IF NOT EXISTS sc_argent_de_poche.role(
    "id" INT PRIMARY KEY,
    "role" TEXT NOT NULL,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);

-- Liasion role - parent --
create table IF NOT EXISTS sc_argent_de_poche.role_parent(
    "id" BIGINT PRIMARY KEY,
    "parent_id" BIGINT NOT NULL REFERENCES sc_argent_de_poche."parent"("id") on delete cascade,
    "role_id" INT NOT NULL REFERENCES sc_argent_de_poche."role"("id") on delete cascade,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);
CREATE INDEX IF NOT EXISTS idx_rp_parent_id_role_id ON sc_argent_de_poche.role_parent(parent_id, role_id);

-- JWT --
CREATE TABLE if NOT EXISTS sc_argent_de_poche.jwt(
    "id" BIGINT PRIMARY KEY,
    "parent_id" BIGINT NOT NULL REFERENCES sc_argent_de_poche."parent"("id") on delete cascade,
    "email" TEXT NOT NULL,
    "jwt_token" TEXT NOT NULL,
    "jwt_id" TEXT NOT NULL,
    "is_valid" BOOLEAN NOT NULL DEFAULT FALSE,
    "expired_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);
CREATE INDEX IF NOT EXISTS idx_jwt_paren_id ON sc_argent_de_poche.jwt(parent_id);

-- Connexion utilisateur --
create table IF NOT EXISTS sc_argent_de_poche.login(
    "id" BIGINT PRIMARY KEY,
    "parent_id" BIGINT NOT NULL REFERENCES sc_argent_de_poche."parent"("id") on delete cascade,
    "is_login_success" BOOLEAN NOT NULL,
    "has_to_be_check" BOOLEAN NOT NULL DEFAULT TRUE,
    "login_at" TIMESTAMPTZ NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_login_parent_id ON sc_argent_de_poche.login(parent_id);

-- Delai de connexion au compte --
create table IF NOT EXISTS sc_argent_de_poche.delay_login(
    "id" BIGINT PRIMARY KEY,
    "parent_id" BIGINT NOT NULL REFERENCES sc_argent_de_poche."parent"("id") on delete cascade,
    "delay_login_until" TIMESTAMPTZ NOT NULL DEFAULT NOW() + INTERVAL '5 minutes',
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);
CREATE INDEX IF NOT EXISTS idx_dl_parent_id ON sc_argent_de_poche.delay_login(parent_id);

ALTER TABLE IF EXISTS sc_argent_de_poche.parent OWNER TO poche;
ALTER TABLE IF EXISTS sc_argent_de_poche.child OWNER TO poche;
ALTER TABLE IF EXISTS sc_argent_de_poche.child_image OWNER TO poche;
ALTER TABLE IF EXISTS sc_argent_de_poche.family_account OWNER TO poche;
ALTER TABLE IF EXISTS sc_argent_de_poche.family OWNER TO poche;
ALTER TABLE IF EXISTS sc_argent_de_poche.child_account OWNER TO poche;
ALTER TABLE IF EXISTS sc_argent_de_poche.child_account_movement OWNER TO poche;
ALTER TABLE IF EXISTS sc_argent_de_poche.parent_family_account OWNER TO poche;
ALTER TABLE IF EXISTS sc_argent_de_poche.role OWNER TO poche;
ALTER TABLE IF EXISTS sc_argent_de_poche.role_parent OWNER TO poche;
ALTER TABLE IF EXISTS sc_argent_de_poche.jwt OWNER TO poche;
ALTER TABLE IF EXISTS sc_argent_de_poche.login OWNER TO poche;
ALTER TABLE IF EXISTS sc_argent_de_poche.delay_login OWNER TO poche;
ALTER TABLE IF EXISTS sc_argent_de_poche.child_account_calendar OWNER TO poche;
ALTER TABLE IF EXISTS sc_argent_de_poche.child_account_money OWNER TO poche;
ALTER TABLE IF EXISTS sc_argent_de_poche.movement_action_code OWNER TO poche;
ALTER TABLE IF EXISTS sc_argent_de_poche.child_account_movement_code OWNER TO poche;

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_argent_de_poche.parent TO poche;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_argent_de_poche.child TO poche;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_argent_de_poche.child_image TO poche;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_argent_de_poche.family_account TO poche;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_argent_de_poche.family TO poche;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_argent_de_poche.child_account TO poche;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_argent_de_poche.child_account_movement TO poche;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_argent_de_poche.parent_family_account TO poche;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_argent_de_poche.role TO poche;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_argent_de_poche.role_parent TO poche;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_argent_de_poche.jwt TO poche;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_argent_de_poche.login TO poche;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_argent_de_poche.delay_login TO poche;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_argent_de_poche.child_account_calendar TO poche;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_argent_de_poche.child_account_money TO poche;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_argent_de_poche.movement_action_code TO poche;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_argent_de_poche.child_account_movement_code TO poche;

CREATE SEQUENCE IF NOT EXISTS sc_argent_de_poche.parent_id_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
CREATE SEQUENCE IF NOT EXISTS sc_argent_de_poche.child_id_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
CREATE SEQUENCE IF NOT EXISTS sc_argent_de_poche.child_image_id_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
CREATE SEQUENCE IF NOT EXISTS sc_argent_de_poche.family_account_id_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
CREATE SEQUENCE IF NOT EXISTS sc_argent_de_poche.family_id_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
CREATE SEQUENCE IF NOT EXISTS sc_argent_de_poche.child_account_id_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
CREATE SEQUENCE IF NOT EXISTS sc_argent_de_poche.child_account_movement_id_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
CREATE SEQUENCE IF NOT EXISTS sc_argent_de_poche.parent_family_account_id_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
CREATE SEQUENCE IF NOT EXISTS sc_argent_de_poche.role_id_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
CREATE SEQUENCE IF NOT EXISTS sc_argent_de_poche.role_parent_id_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
CREATE SEQUENCE IF NOT EXISTS sc_argent_de_poche.jwt_id_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
CREATE SEQUENCE IF NOT EXISTS sc_argent_de_poche.login_id_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
CREATE SEQUENCE IF NOT EXISTS sc_argent_de_poche.delay_login_id_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
CREATE SEQUENCE IF NOT EXISTS sc_argent_de_poche.child_account_calendar_id_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
CREATE SEQUENCE IF NOT EXISTS sc_argent_de_poche.child_account_money_id_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
CREATE SEQUENCE IF NOT EXISTS sc_argent_de_poche.movement_action_code_id_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
CREATE SEQUENCE IF NOT EXISTS sc_argent_de_poche.child_account_movement_code_id_seq START WITH 1 INCREMENT BY 1 NO CYCLE;

ALTER SEQUENCE IF EXISTS sc_argent_de_poche.parent_id_seq OWNER TO poche;
ALTER SEQUENCE IF EXISTS sc_argent_de_poche.child_id_seq OWNER TO poche;
ALTER SEQUENCE IF EXISTS sc_argent_de_poche.child_image_id_seq OWNER TO poche;
ALTER SEQUENCE IF EXISTS sc_argent_de_poche.family_account_id_seq OWNER TO poche;
ALTER SEQUENCE IF EXISTS sc_argent_de_poche.family_id_seq OWNER TO poche;
ALTER SEQUENCE IF EXISTS sc_argent_de_poche.child_account_id_seq OWNER TO poche;
ALTER SEQUENCE IF EXISTS sc_argent_de_poche.child_account_movement_id_seq OWNER TO poche;
ALTER SEQUENCE IF EXISTS sc_argent_de_poche.parent_family_account_id_seq OWNER TO poche;
ALTER SEQUENCE IF EXISTS sc_argent_de_poche.role_id_seq OWNER TO poche;
ALTER SEQUENCE IF EXISTS sc_argent_de_poche.role_parent_id_seq OWNER TO poche;
ALTER SEQUENCE IF EXISTS sc_argent_de_poche.jwt_id_seq OWNER TO poche;
ALTER SEQUENCE IF EXISTS sc_argent_de_poche.login_id_seq OWNER TO poche;
ALTER SEQUENCE IF EXISTS sc_argent_de_poche.delay_login_id_seq OWNER TO poche;
ALTER SEQUENCE IF EXISTS sc_argent_de_poche.child_account_calendar_id_seq OWNER TO poche;
ALTER SEQUENCE IF EXISTS sc_argent_de_poche.child_account_money_id_seq OWNER TO poche;
ALTER SEQUENCE IF EXISTS sc_argent_de_poche.child_account_movement_code_id_seq OWNER TO poche;
ALTER SEQUENCE IF EXISTS sc_argent_de_poche.movement_action_code_id_seq OWNER TO poche;

ALTER TABLE sc_argent_de_poche.parent ALTER COLUMN id SET DEFAULT nextval('sc_argent_de_poche.parent_id_seq');
ALTER TABLE sc_argent_de_poche.child ALTER COLUMN id SET DEFAULT nextval('sc_argent_de_poche.child_id_seq');
ALTER TABLE sc_argent_de_poche.child_image ALTER COLUMN id SET DEFAULT nextval('sc_argent_de_poche.child_image_id_seq');
ALTER TABLE sc_argent_de_poche.family_account ALTER COLUMN id SET DEFAULT nextval('sc_argent_de_poche.family_account_id_seq');
ALTER TABLE sc_argent_de_poche.family ALTER COLUMN id SET DEFAULT nextval('sc_argent_de_poche.family_id_seq');
ALTER TABLE sc_argent_de_poche.child_account ALTER COLUMN id SET DEFAULT nextval('sc_argent_de_poche.child_account_id_seq');
ALTER TABLE sc_argent_de_poche.child_account_movement ALTER COLUMN id SET DEFAULT nextval('sc_argent_de_poche.child_account_movement_id_seq');
ALTER TABLE sc_argent_de_poche.parent_family_account ALTER COLUMN id SET DEFAULT nextval('sc_argent_de_poche.parent_family_account_id_seq');
ALTER TABLE sc_argent_de_poche.role ALTER COLUMN id SET DEFAULT nextval('sc_argent_de_poche.role_id_seq');
ALTER TABLE sc_argent_de_poche.role_parent ALTER COLUMN id SET DEFAULT nextval('sc_argent_de_poche.role_parent_id_seq');
ALTER TABLE sc_argent_de_poche.jwt ALTER COLUMN id SET DEFAULT nextval('sc_argent_de_poche.jwt_id_seq');
ALTER TABLE sc_argent_de_poche.login ALTER COLUMN id SET DEFAULT nextval('sc_argent_de_poche.login_id_seq');
ALTER TABLE sc_argent_de_poche.delay_login ALTER COLUMN id SET DEFAULT nextval('sc_argent_de_poche.delay_login_id_seq');
ALTER TABLE sc_argent_de_poche.child_account_calendar ALTER COLUMN id SET DEFAULT nextval('sc_argent_de_poche.child_account_calendar_id_seq');
ALTER TABLE sc_argent_de_poche.child_account_money ALTER COLUMN id SET DEFAULT nextval('sc_argent_de_poche.child_account_money_id_seq');
ALTER TABLE sc_argent_de_poche.movement_action_code ALTER COLUMN id SET DEFAULT nextval('sc_argent_de_poche.movement_action_code_id_seq');
ALTER TABLE sc_argent_de_poche.child_account_movement_code ALTER COLUMN id SET DEFAULT nextval('sc_argent_de_poche.child_account_movement_code_id_seq');

insert into sc_argent_de_poche.movement_action_code (movement_code, movement_name)
 values
 ('hih', 'Aide à la maison'),
 ('rs', 'Etat de la chambre'),
 ('cb', 'Comportement'),
 ('sh', 'Travail de classe'),
 ('m', 'Repas');

--insert into sc_argent_de_poche.family (family_account_id, name) values (1, 'family_name');
--insert into sc_argent_de_poche.parent(nickname,email, password) values ('nom_parent', 'parent_mail@mail', 'password');
--insert into sc_argent_de_poche.parent(nickname,email, password) values ('nom_parent_2', 'parent_mail_2@mail', 'password');
--insert into sc_argent_de_poche.parent_family_account (parent_id, family_account_id) values (1, 1);
--insert into sc_argent_de_poche.parent_family_account (parent_id, family_account_id) values (2, 1);
--insert into sc_argent_de_poche.child_account (family_account_id) values (1);
--insert into sc_argent_de_poche.child_account (family_account_id) values (1);
--insert into sc_argent_de_poche.child_image (image_name) values ('nom_de_l_image-1');
--insert into sc_argent_de_poche.child_image (image_name) values ('nom_de_l_image-2');
--insert into sc_argent_de_poche.child (child_account_id,child_image_id, nickname) values (1, 1, 'nom-1');
--insert into sc_argent_de_poche.child (child_account_id,child_image_id, nickname) values (2, 2, 'nom-2');
--insert into sc_argent_de_poche.child_account_calendar (child_account_id, calendar_period, period_start_day, period_end_day) values (1, 'week', '2026-01-05', '2026-01-11');
--insert into sc_argent_de_poche.child_account_calendar (child_account_id, calendar_period, period_start_day, period_end_day) values (1, 'week', '2026-01-12', '2026-01-18');
--insert into sc_argent_de_poche.child_account_calendar (child_account_id, calendar_period, period_start_day, period_end_day) values (1, 'week', '2026-01-21', '2026-01-28');
--insert into sc_argent_de_poche.child_account_calendar (child_account_id, calendar_period, period_start_day, period_end_day) values (2, 'week', '2026-01-21', '2026-01-28');
--insert into sc_argent_de_poche.child_account_money (child_account_id, account_calendar_id, money_at_period_start, remaining_money) values (1, 1, 2, 1.5);
--insert into sc_argent_de_poche.child_account_money (child_account_id, account_calendar_id, money_at_period_start, remaining_money) values (1, 3, 2.5, 2);
--insert into sc_argent_de_poche.child_account_money (child_account_id, account_calendar_id, money_at_period_start, remaining_money) values (1, 4, 3.5, 3);
--insert into sc_argent_de_poche.child_account_money (child_account_id, account_calendar_id, money_at_period_start, remaining_money) values (2, 2, 2, 0.5);
--
--insert into sc_argent_de_poche.child_account_movement_code(child_account_id, movement_code_id, movement_fluctuation_price)
-- values
-- (1, 1, 0.5),
-- (1, 2, 0.5),
-- (1, 3, 0.5),
-- (1, 4, 0.5),
-- (1, 5, 0.5),
-- (2, 1, 1.2),
-- (2, 2, 1.2),
-- (2, 3, 1.2),
-- (2, 4, 1.2),
-- (2, 5, 1.2);
--
--insert into sc_argent_de_poche.child_account_movement (child_account_id, child_account_movement_code_id, add_by , movement_action_code)
--values
--(1, 1, 1, '+'),
--(1, 1, 1, '-'),
--(1, 2, 1, '+'),
--(1, 2, 1, '+'),
--(1, 3, 1, '-'),
--(1, 3, 1, '+'),
--(1, 4, 1, '+'),
--(1, 4, 1, '+');

COMMIT;