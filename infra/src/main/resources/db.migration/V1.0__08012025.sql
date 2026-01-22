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
      sc_argent_de_poche.child_account_calendar,
      sc_argent_de_poche.child_account_money,
      sc_argent_de_poche.child_account,
      sc_argent_de_poche.family_account,
      sc_argent_de_poche.child,
      sc_argent_de_poche.child_image,
      sc_argent_de_poche.parent
CASCADE;

DROP SEQUENCE IF EXISTS
sc_argent_de_poche.parent_id_seq,
sc_argent_de_poche.child_id_seq,
sc_argent_de_poche.child_image_id_seq,
sc_argent_de_poche.family_account_id_seq,
sc_argent_de_poche.child_account_id_seq,
sc_argent_de_poche.child_account_movement_id_seq,
sc_argent_de_poche.parent_family_account_id_seq,
sc_argent_de_poche.role_id_seq,
sc_argent_de_poche.role_parent_id_seq,
sc_argent_de_poche.jwt_id_seq,
sc_argent_de_poche.login_id_seq,
sc_argent_de_poche.delay_login_id_seq,
sc_argent_de_poche.child_account_calendar_id_seq,
sc_argent_de_poche.child_account_money_id_seq;



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
    "name" VARCHAR(255) NOT NULL,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);
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
    "image_name" VARCHAR(255) NOT NULL UNIQUE,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);
CREATE INDEX IF NOT EXISTS idx_c_child_account_id ON sc_argent_de_poche.child(child_account_id);
CREATE INDEX IF NOT EXISTS idx_c_child_image_id ON sc_argent_de_poche.child(child_image_id);

-- Calendar compte enfant
CREATE TABLE if NOT EXISTS sc_argent_de_poche.child_account_calendar(
    "id" BIGINT PRIMARY KEY,
    "child_account_id" BIGINT NOT NULL REFERENCES sc_argent_de_poche."child_account"("id") on delete cascade,
    "calendar_period" VARCHAR(255) NOT NULL UNIQUE,
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
    "money_at_period_start" NUMERIC(10,2) NOT NULL,
    "remaining_money" NUMERIC(10,2) NOT NULL,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);
CREATE INDEX IF NOT EXISTS idx_cam_child_account_id ON sc_argent_de_poche.child_account_money(child_account_id);

-- movement d'argent --
CREATE TABLE if NOT EXISTS sc_argent_de_poche.child_account_movement(
    "id" BIGINT PRIMARY KEY,
    "child_account_id" BIGINT NOT NULL REFERENCES sc_argent_de_poche."child_account"("id") on delete cascade,
    "add_by" BIGINT NOT NULL REFERENCES sc_argent_de_poche."parent"("id") on delete cascade,
    "fluctuation_price" NUMERIC(10,2) NOT NULL,
    "movement_action_code" VARCHAR(10) NOT NULL,
    "movement_reason_code" VARCHAR(10) NOT NULL,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);
CREATE INDEX IF NOT EXISTS idx_cam_child_account_id ON sc_argent_de_poche.child_account_movement(child_account_id);

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

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_argent_de_poche.parent TO poche;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_argent_de_poche.child TO poche;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_argent_de_poche.child_image TO poche;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_argent_de_poche.family_account TO poche;
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

CREATE SEQUENCE IF NOT EXISTS sc_argent_de_poche.parent_id_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
CREATE SEQUENCE IF NOT EXISTS sc_argent_de_poche.child_id_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
CREATE SEQUENCE IF NOT EXISTS sc_argent_de_poche.child_image_id_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
CREATE SEQUENCE IF NOT EXISTS sc_argent_de_poche.family_account_id_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
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

ALTER SEQUENCE IF EXISTS sc_argent_de_poche.parent_id_seq OWNER TO poche;
ALTER SEQUENCE IF EXISTS sc_argent_de_poche.child_id_seq OWNER TO poche;
ALTER SEQUENCE IF EXISTS sc_argent_de_poche.child_image_id_seq OWNER TO poche;
ALTER SEQUENCE IF EXISTS sc_argent_de_poche.family_account_id_seq OWNER TO poche;
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

ALTER TABLE sc_argent_de_poche.parent ALTER COLUMN id SET DEFAULT nextval('sc_argent_de_poche.parent_id_seq');
ALTER TABLE sc_argent_de_poche.child ALTER COLUMN id SET DEFAULT nextval('sc_argent_de_poche.child_id_seq');
ALTER TABLE sc_argent_de_poche.child_image ALTER COLUMN id SET DEFAULT nextval('sc_argent_de_poche.child_image_id_seq');
ALTER TABLE sc_argent_de_poche.family_account ALTER COLUMN id SET DEFAULT nextval('sc_argent_de_poche.family_account_id_seq');
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

insert into sc_argent_de_poche.family_account (name) values ('family_name');
insert into sc_argent_de_poche.parent(nickname,email, password) values ('nom_parent', 'parent_mail@mail', 'password');
insert into sc_argent_de_poche.parent(nickname,email, password) values ('nom_parent_2', 'parent_mail_2@mail', 'password');
insert into sc_argent_de_poche.parent_family_account (parent_id, family_account_id) values (1, 1);
insert into sc_argent_de_poche.parent_family_account (parent_id, family_account_id) values (2, 1);
insert into sc_argent_de_poche.child_account (family_account_id) values (1);
insert into sc_argent_de_poche.child_account (family_account_id) values (1);
insert into sc_argent_de_poche.child_image (image_name) values ('nom_de_l_image');
insert into sc_argent_de_poche.child (child_account_id,child_image_id, nickname, image_name) values (1, 1, 'enfant', 'nom_de_l_image');
insert into sc_argent_de_poche.child_account_calendar (child_account_id, calendar_period, period_start_day, period_end_day) values (1, 'week', '2026-01-21', '2026-01-28');
insert into sc_argent_de_poche.child_account_money (child_account_id, money_at_period_start, remaining_money) values (1, 2, 1.5);
insert into sc_argent_de_poche.child_account_movement (child_account_id, add_by , fluctuation_price, movement_action_code, movement_reason_code) values (1, 1, 0.5, '+', 'H');

COMMIT;