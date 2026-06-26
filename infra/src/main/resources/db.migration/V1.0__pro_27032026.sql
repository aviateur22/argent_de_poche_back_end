-- CREATE ROLE poche WITH LOGIN PASSWORD 'xxxx';
-- CREATE DATABASE poche OWNER poche;
-- CREATE ROLE poche_pro_app WITH LOGIN PASSWORD 'xxxx';
-- Le role poche permets la création des tables/ schémas/ sequences. Par default poche est le proprietaire de la base argent_de_poche
-- Le role: poche_pro_app est créé en tant que user applicatif avec des droits limités

BEGIN;

CREATE SCHEMA IF NOT EXISTS sc_argent_de_poche;

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
    "is_account_active" BOOLEAN NOT NULL DEFAULT FALSE,
    "created_at" TIMESTAMP NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_parent_email ON sc_argent_de_poche.parent(email);

-- Compte famille --
CREATE TABLE if NOT EXISTS sc_argent_de_poche.family_account(
    "id" BIGINT PRIMARY KEY,
    "created_at" TIMESTAMP NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMP
);

-- Compte famille --
CREATE TABLE if NOT EXISTS sc_argent_de_poche.family(
    "id" BIGINT PRIMARY KEY,
    "family_account_id" BIGINT NOT NULL REFERENCES sc_argent_de_poche."family_account"("id") on delete cascade,
    "name" VARCHAR(255) NOT NULL,
    "created_at" TIMESTAMP NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_family_family_account_id ON sc_argent_de_poche.family(family_account_id);

-- compte argent de poche enfant --
CREATE TABLE if NOT EXISTS sc_argent_de_poche.child_account(
    "id" BIGINT PRIMARY KEY,
    "family_account_id" BIGINT NOT NULL REFERENCES sc_argent_de_poche."family_account"("id") on delete cascade,
    "is_account_active" BOOLEAN NOT NULL DEFAULT true,
    "created_at" TIMESTAMP NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_child_account_family_account_id ON sc_argent_de_poche.child_account(family_account_id);

-- image de l'enfant --
CREATE TABLE if NOT EXISTS sc_argent_de_poche.child_image(
    "id" BIGINT PRIMARY KEY,
    "image_name" VARCHAR(255) NOT NULL,
    "extension" VARCHAR(10) NOT NULL,
    "created_at" TIMESTAMP NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_ci_child_image_name ON sc_argent_de_poche.child_image(image_name);

-- enfant --
CREATE TABLE if NOT EXISTS sc_argent_de_poche.child(
    "id" BIGINT PRIMARY KEY,
    "child_account_id" BIGINT NOT NULL REFERENCES sc_argent_de_poche."child_account"("id") on delete cascade,
    "child_image_id" BIGINT NOT NULL REFERENCES sc_argent_de_poche."child_image"("id"),
    "nickname" VARCHAR(255) NOT NULL,
    "created_at" TIMESTAMP NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_c_child_account_id ON sc_argent_de_poche.child(child_account_id);
CREATE INDEX IF NOT EXISTS idx_c_child_image_id ON sc_argent_de_poche.child(child_image_id);

-- Calendar compte enfant
CREATE TABLE if NOT EXISTS sc_argent_de_poche.child_account_calendar(
    "id" BIGINT PRIMARY KEY,
    "child_account_id" BIGINT NOT NULL REFERENCES sc_argent_de_poche."child_account"("id") on delete cascade,
    "calendar_period" VARCHAR(255) NOT NULL,
    "period_start_day" TIMESTAMP NOT NULL,
    "period_end_day" TIMESTAMP NOT NULL,
    "created_at" TIMESTAMP NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_cac_child_account_id ON sc_argent_de_poche.child_account_calendar(child_account_id);

-- Argent compte enfant
CREATE TABLE if NOT EXISTS sc_argent_de_poche.child_account_money(
    "id" BIGINT PRIMARY KEY,
    "child_account_id" BIGINT NOT NULL REFERENCES sc_argent_de_poche."child_account"("id") on delete cascade,
    "account_calendar_id" BIGINT NOT NULL REFERENCES sc_argent_de_poche."child_account_calendar"("id") on delete cascade,
    "money_at_period_start" NUMERIC(10,2) NOT NULL,
    "remaining_money" NUMERIC(10,2) NOT NULL,
    "created_at" TIMESTAMP NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_cam_child_account_id ON sc_argent_de_poche.child_account_money(child_account_id);
CREATE INDEX IF NOT EXISTS idx_cam_account_calendar_id ON sc_argent_de_poche.child_account_money(account_calendar_id);

-- code des mouvement d'argent --
CREATE TABLE if NOT EXISTS sc_argent_de_poche.movement_action_code(
    "id" INT PRIMARY KEY,
    "movement_code" VARCHAR(5) NOT NULL,
    "movement_name" VARCHAR(255) NOT NULL,
    "created_at" TIMESTAMP NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMP
);

-- Liaison mouvement_action_code et child_account
CREATE TABLE if NOT EXISTS sc_argent_de_poche.child_account_movement_code(
    "id" BIGINT PRIMARY KEY,
    "movement_code_id" BIGINT NOT NULL REFERENCES sc_argent_de_poche."movement_action_code"("id") on delete cascade,
    "child_account_id" BIGINT NOT NULL REFERENCES sc_argent_de_poche."child_account"("id") on delete cascade,
    "movement_fluctuation_price" NUMERIC(10,2) NOT NULL,
    "created_at" TIMESTAMP NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMP
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
    "movement_add_at"  TIMESTAMP NOT NULL,
    "created_at" TIMESTAMP NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_cam_child_account_id ON sc_argent_de_poche.child_account_movement(child_account_id);
CREATE INDEX IF NOT EXISTS idx_cam_child_account_movement_code_id ON sc_argent_de_poche.child_account_movement(child_account_movement_code_id);

-- Liaison parent - compte de famille
CREATE TABLE if NOT EXISTS sc_argent_de_poche.parent_family_account(
    "id" BIGINT PRIMARY KEY,
    "parent_id" BIGINT NOT NULL REFERENCES sc_argent_de_poche."parent"("id") on delete cascade,
    "family_account_id" BIGINT NOT NULL REFERENCES sc_argent_de_poche."family_account"("id") on delete cascade,
    "created_at" TIMESTAMP NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMP
);

-- Role --
create table IF NOT EXISTS sc_argent_de_poche.role(
    "id" INT PRIMARY KEY,
    "role" TEXT NOT NULL,
    "created_at" TIMESTAMP NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMP
);

-- Liasion role - parent --
create table IF NOT EXISTS sc_argent_de_poche.role_parent(
    "id" BIGINT PRIMARY KEY,
    "parent_id" BIGINT NOT NULL REFERENCES sc_argent_de_poche."parent"("id") on delete cascade,
    "role_id" INT NOT NULL REFERENCES sc_argent_de_poche."role"("id") on delete cascade,
    "created_at" TIMESTAMP NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMP
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
    "expired_at" TIMESTAMP NOT NULL DEFAULT now(),
    "created_at" TIMESTAMP NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_jwt_paren_id ON sc_argent_de_poche.jwt(parent_id);

-- Connexion utilisateur --
create table IF NOT EXISTS sc_argent_de_poche.login(
    "id" BIGINT PRIMARY KEY,
    "parent_id" BIGINT NOT NULL REFERENCES sc_argent_de_poche."parent"("id") on delete cascade,
    "is_login_success" BOOLEAN NOT NULL,
    "has_to_be_check" BOOLEAN NOT NULL DEFAULT TRUE,
    "login_at" TIMESTAMP NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_login_parent_id ON sc_argent_de_poche.login(parent_id);

-- Delai de connexion au compte --
create table IF NOT EXISTS sc_argent_de_poche.delay_login(
    "id" BIGINT PRIMARY KEY,
    "parent_id" BIGINT NOT NULL REFERENCES sc_argent_de_poche."parent"("id") on delete cascade,
    "delay_login_until" TIMESTAMP NOT NULL DEFAULT NOW() + INTERVAL '5 minutes',
    "created_at" TIMESTAMP NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_dl_parent_id ON sc_argent_de_poche.delay_login(parent_id);

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

-- Attribution des droits pour le user applicatif poche_pro_app
GRANT CONNECT ON DATABASE db_argent_de_poche_pro TO poche_pro_app;
GRANT usage ON SCHEMA sc_argent_de_poche TO poche_pro_app;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_argent_de_poche.parent TO poche_pro_app;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_argent_de_poche.child TO poche_pro_app;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_argent_de_poche.child_image TO poche_pro_app;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_argent_de_poche.family_account TO poche_pro_app;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_argent_de_poche.family TO poche_pro_app;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_argent_de_poche.child_account TO poche_pro_app;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_argent_de_poche.child_account_movement TO poche_pro_app;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_argent_de_poche.parent_family_account TO poche_pro_app;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_argent_de_poche.role TO poche_pro_app;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_argent_de_poche.role_parent TO poche_pro_app;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_argent_de_poche.jwt TO poche_pro_app;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_argent_de_poche.login TO poche_pro_app;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_argent_de_poche.delay_login TO poche_pro_app;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_argent_de_poche.child_account_calendar TO poche_pro_app;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_argent_de_poche.child_account_money TO poche_pro_app;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_argent_de_poche.movement_action_code TO poche_pro_app;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_argent_de_poche.child_account_movement_code TO poche_pro_app;
GRANT USAGE, SELECT ON SEQUENCE sc_argent_de_poche.parent_id_seq TO poche_pro_app;
GRANT USAGE, SELECT ON SEQUENCE sc_argent_de_poche.child_id_seq TO poche_pro_app;
GRANT USAGE, SELECT ON SEQUENCE sc_argent_de_poche.child_image_id_seq TO poche_pro_app;
GRANT USAGE, SELECT ON SEQUENCE sc_argent_de_poche.family_account_id_seq TO poche_pro_app;
GRANT USAGE, SELECT ON SEQUENCE sc_argent_de_poche.family_id_seq TO poche_pro_app;
GRANT USAGE, SELECT ON SEQUENCE sc_argent_de_poche.child_account_id_seq TO poche_pro_app;
GRANT USAGE, SELECT ON SEQUENCE sc_argent_de_poche.child_account_movement_id_seq TO poche_pro_app;
GRANT USAGE, SELECT ON SEQUENCE sc_argent_de_poche.parent_family_account_id_seq TO poche_pro_app;
GRANT USAGE, SELECT ON SEQUENCE sc_argent_de_poche.role_id_seq TO poche_pro_app;
GRANT USAGE, SELECT ON SEQUENCE sc_argent_de_poche.role_parent_id_seq TO poche_pro_app;
GRANT USAGE, SELECT ON SEQUENCE sc_argent_de_poche.jwt_id_seq TO poche_pro_app;
GRANT USAGE, SELECT ON SEQUENCE sc_argent_de_poche.login_id_seq TO poche_pro_app;
GRANT USAGE, SELECT ON SEQUENCE sc_argent_de_poche.delay_login_id_seq TO poche_pro_app;
GRANT USAGE, SELECT ON SEQUENCE sc_argent_de_poche.child_account_calendar_id_seq TO poche_pro_app;
GRANT USAGE, SELECT ON SEQUENCE sc_argent_de_poche.child_account_money_id_seq TO poche_pro_app;
GRANT USAGE, SELECT ON SEQUENCE sc_argent_de_poche.movement_action_code_id_seq TO poche_pro_app;
GRANT USAGE, SELECT ON SEQUENCE sc_argent_de_poche.child_account_movement_code_id_seq TO poche_pro_app;

--Insertion des codes d'action disponible
insert into sc_argent_de_poche.movement_action_code (movement_code, movement_name)
 values
 ('hih', 'Aide à la maison'),
 ('rs', 'Etat de la chambre'),
 ('cb', 'Comportement'),
 ('sh', 'Travail de classe'),
 ('m', 'Repas');

--Insertion des roles disponibles
insert into sc_argent_de_poche.role (id, role)
 values
 (1, 'ROLE_PARENT'),
 (2, 'ROLE_ADMIN');


COMMIT;