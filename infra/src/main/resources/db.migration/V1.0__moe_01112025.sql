DO
$do$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = 'aet_moe') THEN
        CREATE ROLE aet_moe WITH LOGIN PASSWORD 'aet_moe';
        GRANT CONNECT ON DATABASE aet_moe TO aet_moe;
    END IF;
END
$do$;

BEGIN;

CREATE SCHEMA IF NOT EXISTS sc_aet;
ALTER SCHEMA sc_aet OWNER TO aet_moe;

DROP TABLE IF EXISTS sc_aet."login", sc_aet."jwt", sc_aet."delay_login", sc_aet."memory_card_game_image_family", sc_aet."memory_card_game_image_face", sc_aet."memory_card_game_image", sc_aet."game_image", sc_aet."game", sc_aet."image", sc_aet."role_player", sc_aet."role", sc_aet."family_player", sc_aet."family",  sc_aet."owner_account", sc_aet."player" CASCADE;

-- Joueur --
CREATE TABLE if NOT EXISTS sc_aet.player(
    "id" BIGINT PRIMARY KEY,
    "nickname" VARCHAR(255) NOT NULL,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);

-- PROPRIETAIRE --
CREATE TABLE if NOT EXISTS sc_aet.owner_account(
    "owner_id" BIGINT NOT NULL REFERENCES sc_aet."player"("id") on delete cascade,
    "email" VARCHAR(255) NOT NULL UNIQUE,
    "password" TEXT NOT NULL,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);
CREATE INDEX IF NOT EXISTS idx_owner_account_email ON sc_aet.owner_account(email);
CREATE INDEX IF NOT EXISTS idx_owner_account_id ON sc_aet.owner_account(owner_id);

-- FAMILLE --
CREATE TABLE if NOT EXISTS sc_aet.family(
    "id" BIGINT PRIMARY KEY,
    "owner_id" BIGINT NOT NULL REFERENCES sc_aet."player"("id") on delete cascade,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);
CREATE INDEX IF NOT EXISTS idx_family_owner ON sc_aet.family(owner_id);

-- LIAISON JOEUR AVEC FAMILLE  --
CREATE TABLE if NOT EXISTS sc_aet.family_player(
    "id" BIGINT PRIMARY KEY,
    "family_id" BIGINT NOT NULL REFERENCES sc_aet."family"("id") on delete cascade,
    "player_id" BIGINT NOT NULL REFERENCES sc_aet."player"("id") on delete cascade,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);
CREATE INDEX IF NOT EXISTS idx_family_player_family ON sc_aet.family_player(family_id);
CREATE INDEX IF NOT EXISTS idx_family_player_player ON sc_aet.family_player(player_id);

-- Role --
create table IF NOT EXISTS sc_aet.role(
    "id" INT PRIMARY KEY,
    "role" TEXT NOT NULL,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);

-- Liasion Role AVEC  JOEUR --
create table IF NOT EXISTS sc_aet.role_player(
    "id" BIGINT PRIMARY KEY,
    "player_id" BIGINT NOT NULL REFERENCES sc_aet."player"("id") on delete cascade,
    "role_id" INT NOT NULL REFERENCES sc_aet."role"("id") on delete cascade,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);
CREATE INDEX IF NOT EXISTS idx_role_user ON sc_aet.role_player(player_id, role_id);

-- JEU --
CREATE TABLE if NOT EXISTS sc_aet.game(
    "id" INT PRIMARY KEY,
    "name" VARCHAR(255),
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);

-- IMAGE
CREATE TABLE if NOT EXISTS sc_aet.image(
  "id" BIGINT PRIMARY KEY,
  "image_path" VARCHAR(355) NOT NULL,
  "random_name" VARCHAR(355) NOT NULL,
  "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
  "updated_at" TIMESTAMPTZ
);
CREATE INDEX IF NOT EXISTS idx_image_random_name ON sc_aet.image(random_name);

-- LIAISON IMAGE JEUX --
CREATE TABLE if NOT EXISTS sc_aet.game_image(
    "id" BIGINT PRIMARY KEY,
    "game_id" INT NOT NULL REFERENCES sc_aet."game"("id") on delete cascade,
    "image_id" BIGINT NOT NULL REFERENCES sc_aet."image"("id") on delete cascade,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);
CREATE INDEX IF NOT EXISTS idx_image_game_game_id ON sc_aet.game_image(game_id);
CREATE INDEX IF NOT EXISTS idx_image_game_image_id ON sc_aet.game_image(image_id);

-- MEMORY GAME CARD FAMYLY IMAGE --
CREATE TABLE if NOT EXISTS sc_aet.memory_card_game_image_family (
    "id" INT PRIMARY KEY,
    "family_name" VARCHAR NOT NULL,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);

-- MEMORY GAME CARD IMAGE Face --
CREATE TABLE if NOT EXISTS sc_aet.memory_card_game_image_face(
    "id" INT PRIMARY KEY,
    "face_name" VARCHAR NOT NULL,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);

-- MEMORY GAME CARD IMAGE PER Face --
CREATE TABLE if NOT EXISTS sc_aet.memory_card_game_image(
    "id" BIGINT PRIMARY KEY,
    "image_id" BIGINT NOT NULL REFERENCES sc_aet."image"("id") on delete cascade,
    "card_face_id" INT NOT NULL REFERENCES sc_aet."memory_card_game_image_face"("id") on delete cascade,
    "card_family_id" INT REFERENCES sc_aet."memory_card_game_image_family"("id") on delete cascade,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);
CREATE INDEX IF NOT EXISTS idx_card_id ON sc_aet.memory_card_game_image(image_id);
CREATE INDEX IF NOT EXISTS idx_card_face ON sc_aet.memory_card_game_image(card_face_id);
CREATE INDEX IF NOT EXISTS idx_card_face ON sc_aet.memory_card_game_image(card_family_id);

-- JWT --
CREATE TABLE if NOT EXISTS sc_aet.jwt(
    "id" BIGINT PRIMARY KEY,
    "owner_id" BIGINT NOT NULL REFERENCES sc_aet."player"("id") on delete cascade,
    "email" TEXT NOT NULL,
    "jwt_token" TEXT NOT NULL,
    "jwt_id" TEXT NOT NULL,
    "is_valid" BOOLEAN NOT NULL DEFAULT FALSE,
    "expired_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);
CREATE INDEX IF NOT EXISTS idx_jwt ON sc_aet.jwt(owner_id);

-- Connexion utilisateur --
create table IF NOT EXISTS sc_aet.login(
    "id" BIGINT PRIMARY KEY,
    "owner_id" BIGINT NOT NULL REFERENCES sc_aet."player"("id") on delete cascade,
    "is_login_success" BOOLEAN NOT NULL,
    "has_to_be_check" BOOLEAN NOT NULL DEFAULT TRUE,
    "login_at" TIMESTAMPTZ NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_login ON sc_aet.login(owner_id);

-- Delai de connexion au compte --
create table IF NOT EXISTS sc_aet.delay_login(
    "id" BIGINT PRIMARY KEY,
    "owner_id" BIGINT NOT NULL REFERENCES sc_aet."player"("id") on delete cascade,
    "delay_login_until" TIMESTAMPTZ NOT NULL DEFAULT NOW() + INTERVAL '5 minutes',
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);
CREATE INDEX IF NOT EXISTS idx_delay_login ON sc_aet.delay_login(owner_id);

-- Image pour le jeu d calcul mental --
create table IF NOT EXISTS sc_aet.mental_calcul_game_image(
    "image_id" BIGINT PRIMARY KEY NOT NULL REFERENCES sc_aet."image"("id") on delete cascade,
    "created_at" TIMESTAMPTZ NOT NULL DEFAULT now(),
    "updated_at" TIMESTAMPTZ
);

ALTER TABLE IF EXISTS sc_aet.player OWNER TO aet_moe;
ALTER TABLE IF EXISTS sc_aet.owner_account OWNER TO aet_moe;
ALTER TABLE IF EXISTS sc_aet.family OWNER TO aet_moe;
ALTER TABLE IF EXISTS sc_aet.family_player OWNER TO aet_moe;
ALTER TABLE IF EXISTS sc_aet.role OWNER TO aet_moe;
ALTER TABLE IF EXISTS sc_aet.role_player OWNER TO aet_moe;
ALTER TABLE IF EXISTS sc_aet.game OWNER TO aet_moe;
ALTER TABLE IF EXISTS sc_aet.image OWNER TO aet_moe;
ALTER TABLE IF EXISTS sc_aet.game_image OWNER TO aet_moe;
ALTER TABLE IF EXISTS sc_aet.jwt OWNER TO aet_moe;
ALTER TABLE IF EXISTS sc_aet.login OWNER TO aet_moe;
ALTER TABLE IF EXISTS sc_aet.delay_login OWNER TO aet_moe;
ALTER TABLE IF EXISTS sc_aet.memory_card_game_image OWNER TO aet_moe;
ALTER TABLE IF EXISTS sc_aet.memory_card_game_image_face OWNER TO aet_moe;
ALTER TABLE IF EXISTS sc_aet.memory_card_game_image_family OWNER TO aet_moe;
ALTER TABLE IF EXISTS sc_aet.mental_calcul_game_image OWNER TO aet_moe;


GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_aet.player TO aet_moe;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_aet.owner_account TO aet_moe;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_aet.family TO aet_moe;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_aet.family_player TO aet_moe;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_aet.role TO aet_moe;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_aet.role_player TO aet_moe;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_aet.game TO aet_moe;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_aet.image TO aet_moe;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_aet.game_image TO aet_moe;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_aet.jwt TO aet_moe;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_aet.login TO aet_moe;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_aet.delay_login TO aet_moe;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_aet.memory_card_game_image TO aet_moe;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_aet.memory_card_game_image_face TO aet_moe;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_aet.memory_card_game_image_family TO aet_moe;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE sc_aet.mental_calcul_game_image TO aet_moe;


CREATE SEQUENCE IF NOT EXISTS sc_aet.player_id_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
CREATE SEQUENCE IF NOT EXISTS sc_aet.family_id_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
CREATE SEQUENCE IF NOT EXISTS sc_aet.family_player_id_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
CREATE SEQUENCE IF NOT EXISTS sc_aet.role_player_id_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
CREATE SEQUENCE IF NOT EXISTS sc_aet.game_id_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
CREATE SEQUENCE IF NOT EXISTS sc_aet.image_id_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
CREATE SEQUENCE IF NOT EXISTS sc_aet.game_image_id_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
CREATE SEQUENCE IF NOT EXISTS sc_aet.jwt_id_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
CREATE SEQUENCE IF NOT EXISTS sc_aet.login_id_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
CREATE SEQUENCE IF NOT EXISTS sc_aet.delay_login_id_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
CREATE SEQUENCE IF NOT EXISTS sc_aet.memory_card_game_image_id_seq START WITH 1 INCREMENT BY 1 NO CYCLE;
CREATE SEQUENCE IF NOT EXISTS sc_aet.memory_card_game_image_family_id_seq START WITH 1 INCREMENT BY 1 NO CYCLE;

ALTER SEQUENCE IF EXISTS sc_aet.player_id_seq OWNER TO aet_moe;
ALTER SEQUENCE IF EXISTS sc_aet.family_id_seq OWNER TO aet_moe;
ALTER SEQUENCE IF EXISTS sc_aet.family_player_id_seq OWNER TO aet_moe;
ALTER SEQUENCE IF EXISTS sc_aet.role_player_id_seq OWNER TO aet_moe;
ALTER SEQUENCE IF EXISTS sc_aet.game_id_seq OWNER TO aet_moe;
ALTER SEQUENCE IF EXISTS sc_aet.image_id_seq OWNER TO aet_moe;
ALTER SEQUENCE IF EXISTS sc_aet.game_image_id_seq OWNER TO aet_moe;
ALTER SEQUENCE IF EXISTS sc_aet.jwt_id_seq OWNER TO aet_moe;
ALTER SEQUENCE IF EXISTS sc_aet.login_id_seq OWNER TO aet_moe;
ALTER SEQUENCE IF EXISTS sc_aet.delay_login_id_seq OWNER TO aet_moe;
ALTER SEQUENCE IF EXISTS sc_aet.memory_card_game_image_id_seq OWNER TO aet_moe;
ALTER SEQUENCE IF EXISTS sc_aet.memory_card_game_image_family_id_seq OWNER TO aet_moe;

ALTER SEQUENCE IF EXISTS sc_aet.player_id_seq OWNED BY sc_aet.player.id;
ALTER SEQUENCE IF EXISTS sc_aet.family_id_seq OWNED BY sc_aet.family.id;
ALTER SEQUENCE IF EXISTS sc_aet.family_player_id_seq OWNED BY sc_aet.family_player.id;
ALTER SEQUENCE IF EXISTS sc_aet.role_player_id_seq OWNED BY sc_aet.role_player.id;
ALTER SEQUENCE IF EXISTS sc_aet.game_id_seq OWNED BY sc_aet.game.id;
ALTER SEQUENCE IF EXISTS sc_aet.image_id_seq OWNED BY sc_aet.image.id;
ALTER SEQUENCE IF EXISTS sc_aet.game_image_id_seq OWNED BY sc_aet.game_image.id;
ALTER SEQUENCE IF EXISTS sc_aet.jwt_id_seq OWNED BY sc_aet.jwt.id;
ALTER SEQUENCE IF EXISTS sc_aet.login_id_seq OWNED BY sc_aet.login.id;
ALTER SEQUENCE IF EXISTS sc_aet.delay_login_id_seq OWNED BY sc_aet.delay_login.id;
ALTER SEQUENCE IF EXISTS sc_aet.memory_card_game_image_id_seq OWNED BY sc_aet.memory_card_game_image.id;
ALTER SEQUENCE IF EXISTS sc_aet.memory_card_game_image_family_id_seq OWNED BY sc_aet.memory_card_game_image_family.id;

ALTER TABLE sc_aet.player ALTER COLUMN id SET DEFAULT nextval('sc_aet.player_id_seq');
ALTER TABLE sc_aet.family ALTER COLUMN id SET DEFAULT nextval('sc_aet.family_id_seq');
ALTER TABLE sc_aet.family_player ALTER COLUMN id SET DEFAULT nextval('sc_aet.family_player_id_seq');
ALTER TABLE sc_aet.role_player ALTER COLUMN id SET DEFAULT nextval('sc_aet.role_player_id_seq');
ALTER TABLE sc_aet.game ALTER COLUMN id SET DEFAULT nextval('sc_aet.game_id_seq');
ALTER TABLE sc_aet.image ALTER COLUMN id SET DEFAULT nextval('sc_aet.image_id_seq');
ALTER TABLE sc_aet.game_image ALTER COLUMN id SET DEFAULT nextval('sc_aet.game_image_id_seq');
ALTER TABLE sc_aet.jwt ALTER COLUMN id SET DEFAULT nextval('sc_aet.jwt_id_seq');
ALTER TABLE sc_aet.login ALTER COLUMN id SET DEFAULT nextval('sc_aet.login_id_seq');
ALTER TABLE sc_aet.delay_login ALTER COLUMN id SET DEFAULT nextval('sc_aet.delay_login_id_seq');
ALTER TABLE sc_aet.memory_card_game_image ALTER COLUMN id SET DEFAULT nextval('sc_aet.memory_card_game_image_id_seq');
ALTER TABLE sc_aet.memory_card_game_image_family ALTER COLUMN id SET DEFAULT nextval('sc_aet.memory_card_game_image_family_id_seq');

INSERT INTO sc_aet.player ("nickname") VALUES
('playerA'),
('playerB'),
('playerC'),
('playerD'),
('Admin1');

INSERT INTO sc_aet.owner_account ("owner_id", "email" , "password") VALUES
(2, 'owner1@hotmail.fr', '$2y$10$9PSCTWQiEIbXulYGOZi7.u6x5S6.8XuM0dL3EH72sigNHLlUW2wzy'),
(4, 'owner2@hotmail.fr', '$2y$10$9PSCTWQiEIbXulYGOZi7.u6x5S6.8XuM0dL3EH72sigNHLlUW2wzy'),
(5, 'owner3@hotmail.fr', '$2y$10$9PSCTWQiEIbXulYGOZi7.u6x5S6.8XuM0dL3EH72sigNHLlUW2wzy');

INSERT INTO sc_aet.role ("id", "role") VALUES
(1, 'ROLE_PLAYER'),
(2, 'ROLE_OWNER'),
(3, 'ROLE_ADMIN');

INSERT INTO sc_aet.role_player ("player_id", "role_id") VALUES
(1, 1),
(2, 1),
(2, 2),
(3, 1),
(4, 1),
(4, 2),
(5, 1),
(5, 2),
(5, 3);

INSERT INTO sc_aet.game ("name") VALUES
('memory_card_game');

INSERT INTO sc_aet.memory_card_game_image_face ("id", "face_name") VALUES
(1, 'front'),
(2, 'back');

INSERT INTO sc_aet.memory_card_game_image_family ("family_name") VALUES
('hiboux'),
('arbres'),
('enfant'),
('fleur');

INSERT INTO sc_aet.image ("image_path", "random_name") VALUES
('C:/Programmation/test/images/hiboux.png', 'abcdegfd1236571'),
('C:/Programmation/test/images/hiboux-1.png', 'abcdegfd1236572'),
('C:/Programmation/test/images/hiboux-2.png', 'abcdegfd1236573'),
('C:/Programmation/test/images/hiboux-3.png', 'abcdegfd1236574'),
('C:/Programmation/test/images/hiboux-4.png', 'abcdegfd1236575'),
('C:/Programmation/test/images/hiboux-5.png', 'abcdegfd1236576'),
('C:/Programmation/test/images/hiboux-6.png', 'abcdegfd1236577'),
('C:/Programmation/test/images/hiboux-7.png', 'abcdegfd1236578'),
('C:/Programmation/test/images/hiboux-8.png', 'abcdegfd1236579'),
('C:/Programmation/test/images/back.svg', 'abcdegfd12365710'),
('C:/Programmation/test/images/arbre complet.png', 'abcdegfd12365711'),
('C:/Programmation/test/images/arbre complet-1.png', 'abcdegfd12365712'),
('C:/Programmation/test/images/arbre complet-2.png', 'abcdegfd12365713'),
('C:/Programmation/test/images/arbre complet-3.png', 'abcdegfd12365714'),
('C:/Programmation/test/images/arbre complet-4.png', 'abcdegfd12365715'),
('C:/Programmation/test/images/arbre complet-5.png', 'abcdegfd12365716'),
('C:/Programmation/test/images/arbre complet-6.png', 'abcdegfd12365717'),
('C:/Programmation/test/images/arbre complet-7.png', 'abcdegfd12365718'),
('C:/Programmation/test/images/arbre complet-8.png', 'abcdegfd12365719'),
('C:/Programmation/test/images/enfant.png', 'abcdegfd12365720'),
('C:/Programmation/test/images/enfant-1.png', 'abcdegfd12365721'),
('C:/Programmation/test/images/enfant-2.png', 'abcdegfd12365722'),
('C:/Programmation/test/images/enfant-3.png', 'abcdegfd12365723'),
('C:/Programmation/test/images/enfant-4.png', 'abcdegfd12365724'),
('C:/Programmation/test/images/enfant-5.png', 'abcdegfd12365725'),
('C:/Programmation/test/images/enfant-6.png', 'abcdegfd12365726'),
('C:/Programmation/test/images/enfant-7.png', 'abcdegfd12365727'),
('C:/Programmation/test/images/enfant-8.png', 'abcdegfd12365728'),
('C:/Programmation/test/images/enfant-9.png', 'abcdegfd12365729'),
('C:/Programmation/test/images/fleur-1.png', 'abcdegfd12365730'),
('C:/Programmation/test/images/fleur-2.png', 'abcdegfd12365731'),
('C:/Programmation/test/images/fleur-3.png', 'abcdegfd12365732'),
('C:/Programmation/test/images/fleur-4.png', 'abcdegfd12365733'),
('C:/Programmation/test/images/fleur-5.png', 'abcdegfd12365734');


INSERT INTO sc_aet.game_image ("game_id", "image_id") VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(1, 6),
(1, 7),
(1, 8),
(1, 9),
(1, 10),
(1, 11),
(1, 12),
(1, 13),
(1, 14),
(1, 15),
(1, 16),
(1, 17),
(1, 18),
(1, 19),
(1, 20),
(1, 21),
(1, 22),
(1, 23),
(1, 24),
(1, 25),
(1, 26),
(1, 27),
(1, 28),
(1, 29),
(1, 30),
(1, 31),
(1, 32),
(1, 33),
(1, 34);


INSERT INTO sc_aet.memory_card_game_image ("image_id", "card_face_id", "card_family_id") VALUES
(1, 1, 1),
(2, 1, 1),
(3, 1, 1),
(4, 1, 1),
(5, 1, 1),
(6, 1, 1),
(7, 1, 1),
(8, 1, 1),
(9, 1, 1),
(10, 2, null),
(11, 1, 2),
(12, 1, 2),
(13, 1, 2),
(14, 1, 2),
(15, 1, 2),
(16, 1, 2),
(17, 1, 2),
(18, 1, 2),
(19, 1, 2),
(20, 1, 3),
(21, 1, 3),
(22, 1, 3),
(23, 1, 3),
(24, 1, 3),
(25, 1, 3),
(26, 1, 3),
(27, 1, 3),
(28, 1, 3),
(29, 1, 3),
(30, 1, 4),
(31, 1, 4),
(32, 1, 4),
(33, 1, 4),
(34, 1, 4);

INSERT INTO sc_aet.mental_calcul_game_image ("image_id") VALUES (10);

-- Mise a jour du path des images --
UPDATE sc_aet.image
SET image_path = REPLACE(image_path, 'C:/Programmation/test/images', '/opt/images/aet')
WHERE image_path LIKE 'C:/Programmation/test/images%';

COMMIT;