
CREATE TABLE device 
(
	deviceId		VARCHAR(16)  
,	registrationId		VARCHAR(184) 
,	registrationDate	TIMESTAMP    DEFAULT 	CURRENT_TIMESTAMP
,	PRIMARY KEY (deviceId)
) ENGINE=INNODB;

CREATE TABLE device_server
(
	deviceId		VARCHAR(16) 
,	serverHash		VARCHAR(32) 
,	PRIMARY KEY (deviceId, serverHash)
,	FOREIGN KEY (deviceId) REFERENCES device(deviceId) ON DELETE CASCADE
) ENGINE=INNODB;

CREATE TABLE dbSettings
(
	dbVersion		INT
) ENGINE=INNODB;

INSERT INTO dbSettings(dbVersion)
VALUES(0)
;
