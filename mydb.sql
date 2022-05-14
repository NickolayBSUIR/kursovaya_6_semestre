USE crypto;

DROP TABLE IF EXISTS `currencies`, `users`, `transactions`, `user_currencies`, `session`, `session_attributes`;

CREATE TABLE currencies (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(45) UNIQUE NOT NULL,
  rate DOUBLE NOT NULL,
  change_hour DOUBLE NOT NULL,
  change_day DOUBLE NOT NULL
);

CREATE TABLE users (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name varchar(45) UNIQUE NOT NULL,
  password varchar(64) NOT NULL,
  money DOUBLE DEFAULT 1000,
  role varchar(45) NOT NULL,
  enabled BOOLEAN DEFAULT TRUE 
);

CREATE TABLE transactions (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  currency_count DOUBLE NOT NULL,
  usd_count DOUBLE NOT NULL,
  buying BOOLEAN DEFAULT 1,
  time DATETIME NOT NULL,
  user_id INT NOT NULL,
  currency_id INT NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
  FOREIGN KEY (currency_id) REFERENCES currencies (id) ON DELETE CASCADE
);

CREATE TABLE user_currencies (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  count DOUBLE NOT NULL,
  user_id INT NOT NULL,
  currency_id INT NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
  FOREIGN KEY (currency_id) REFERENCES currencies (id) ON DELETE CASCADE
);

INSERT users(name,password,role,money)
VALUES ('admin', '$2a$10$hkfkoiTLh16jMgXbQkbq5uYoGWeNqFbP55Uq9GXFQGndpC8DGNyES', 'ROLE_ADMIN',10000);

CREATE TABLE session (
	primary_id CHAR(36) NOT NULL,
	session_id CHAR(36) NOT NULL,
	creation_time BIGINT NOT NULL,
	last_access_time BIGINT NOT NULL,
	max_inactive_interval INT NOT NULL,
	expiry_time BIGINT NOT NULL,
	principal_name VARCHAR(100),
	CONSTRAINT session_pk PRIMARY KEY (primary_id)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

CREATE UNIQUE INDEX session_ix1 ON session (session_id);
CREATE INDEX session_ix2 ON session (expiry_time);
CREATE INDEX session_ix3 ON session (principal_name);

CREATE TABLE session_attributes (
	session_primary_id CHAR(36) NOT NULL,
	attribute_name VARCHAR(200) NOT NULL,
	attribute_bytes BLOB NOT NULL,
	CONSTRAINT session_attributes_pk PRIMARY KEY (session_primary_id, attribute_name),
	CONSTRAINT session_attributes_fk FOREIGN KEY (session_primary_id) REFERENCES session(primary_id) ON DELETE CASCADE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;