<?php
/**
 *
 * @package zabbixNotifierServer
 * @version $Id$
 * @copyright (c) 2012 Brimstedt Technology
 * @abstract Provides persistence
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

define('DB_VERSION', 4);

class db
{

	var $dbh = false;
	
	/**
	 * Connects to database
	 */
	function connect($config)
	{
		$connectionString = "${config['dbDriver']}:host=${config['dbServer']};dbname=${config['dbName']}";
		
		$this->dbh = new PDO($connectionString, $config['dbUsername'], $config['dbPassword']);
		if($this->dbh->errorCode() != 0)
		{
			echo $connectionString . ', user: ' . $config['dbUsername'] . "\n";
 			die('Unable to connect to db: ' . $this->dbh->errorInfo());
		}
	}
	
	/**
	 * Checks connection state
	 */
	function isConnected()
	{
		return  $this->dbh && $this->dbh->getAttribute(PDO::ATTR_CONNECTION_STATUS) !== false;
	}
	
	/**
	 * Drops all zabbix notifier tables
	 */
	function purgeDatabase()
	{
		$this->dbh->exec("DROP TABLE dbSettings");
		$this->dbh->exec("DROP TABLE device_server");
		$this->dbh->exec("DROP TABLE device");
		
	}
	
	/**
	 * Creates necessary tables, upgrades database, etc
	 */
	function initiateDb()
	{
		$dbVersion = $this->getDbVersion();
		while($dbVersion < DB_VERSION)
		{
			$dbVersion++;
			$upgrade = $this->applyDbUpgrade($dbVersion);
			if(!$upgrade)
			{
				var_dump($this->dbh->errorInfo());
				return false;
			}
			
		}
		return true;
	}
	
	/**
	 * Apply one database upgrade to database
	 */
	function applyDbUpgrade($revision)
	{
		$filename = $this->getDatabaseRevisionName($revision);
		if($filename == "")
		{
			return false;
		}
		$sql = file_get_contents($filename);
		if($this->dbh->exec($sql) === false)
		{
			return false;
		}
		
		$stmt = $this->dbh->prepare("UPDATE	dbSettings SET dbVersion = :revision");
		$stmt->bindparam(':revision', $revision, PDO::PARAM_INT);
		return $stmt->execute();
	}
	
	/**
	 * Finds the filename for a revision upgrade
	 * 
	 */
	function getDatabaseRevisionName($revision)
	{
		if(!is_numeric($revision))
		{
			return "";
		}
		// We could consider query engine here if we wanted..
		$filename = "sql/dbschema/initRevision_{$revision}.sql";
		$filename = stream_resolve_include_path($filename);

		return $filename;
	}
	/** 
	 * Returns the current db version number
	 */
	function getDbVersion()
	{
		$stmt = @$this->dbh->query("SELECT dbVersion FROM dbSettings");
		if($stmt)
		{
			$row = $stmt->fetch(PDO::FETCH_NUM);
			return $row[0];
		}
		return 0;
		
	}
	
	/**
	 * Stores the C2DM registration id for a certain device. Google supplies you with this..
	 * @param unknown_type $deviceId
	 * @param unknown_type $registrationId
	 */
	function registerDevice($deviceId, $registrationId)
	{
		// Check if we have registration already, in that case we may update instead
		$oldReg = $this->getDeviceRegistrationId($deviceId);
		
		// If we have registration, and new registration is blank, exit
		if($oldReg !== false && $registrationId === "")
		{
			return "";
		}
		
		// If we dont have registration, insert
		if($oldReg === false)
		{
			$stmt = $this->dbh->prepare("
					INSERT INTO device (deviceId, registrationId, registrationDate)
					VALUES (:deviceId, :registrationId, now())");
			$stmt->bindParam(':deviceId', $deviceId);
			$stmt->bindParam(':registrationId', $registrationId);
			@$stmt->execute();
			return $this->checkError($stmt);
		}

		$stmt = $this->dbh->prepare("
				UPDATE device 
				SET	registrationId = :registrationId
				,	registrationDate = now()
				WHERE	deviceId = :deviceId
				AND		registrationId <> :registrationId
			");
		$stmt->bindParam(':deviceId', $deviceId);
		$stmt->bindParam(':registrationId', $registrationId);
		@$stmt->execute();
		return $this->checkError($stmt);
		}
		
	/**
	 * Connects a device to a zabbix server
	 * @param unknown_type $deviceId
	 * @param unknown_type $serverHash
	 */
	function registerDeviceToServer($deviceId, $serverHash)
	{
		// Ensure device is registered
		$this->registerDevice($deviceId, "");
		
		$stmt = $this->dbh->prepare("
				INSERT INTO device_server (deviceId, serverHash, registrationDate)
				SELECT 	:deviceId, :serverHash, now()
				FROM	device d
				WHERE 	d.deviceId = :deviceId
				AND		NOT EXISTS 
				(
					SELECT	1
					FROM	device_server
					WHERE	deviceId = :deviceId
					AND		serverHash = :serverHash
				)
			");
		$stmt->bindParam(':deviceId', $deviceId);
		$stmt->bindParam(':serverHash', $serverHash);
		@$stmt->execute();
		return $this->checkError($stmt);
	}

	private function checkError($obj)
	{
		if($obj->errorCode() == "00000")
		{
			return "";
		}
		else
		{
			$err = $obj->errorInfo();
			return $err[2];
		}
		
	}
	
	/**
	 * Gets the C2DM id for a device.
	 *
	 * @param unknown_type $deviceId
	 * @return mixed
	 */
	function getDeviceRegistrationId($deviceId)
	{
	
		$stmt = $this->dbh->prepare("
				SELECT 	registrationId
				FROM	device d
				WHERE	d.deviceId = :deviceId
				");
		$stmt->bindParam(':deviceId', $deviceId);
		$stmt->execute();
		
		if($stmt->rowCount() == 0)
		{
			return false;
		}
		
		$row = $stmt->fetch();
		
		return $row['registrationId'];
	}
	
	
	/**
	 * Checks if a device is connected to a server
	 *
	 * @param unknown_type $deviceId
	 * @return mixed
	 */
	function isDeviceRegistererdForServer($deviceId, $serverHash)
	{
	
		$stmt = $this->dbh->prepare("
				SELECT 	1
				FROM	device_server d
				WHERE	d.deviceId = :deviceId
				AND		d.serverHash = :serverHash
				");
		$stmt->bindParam(':deviceId', $deviceId);
		$stmt->bindParam(':serverHash', $serverHash);
		$stmt->execute();
		
		return $stmt->rowCount() > 0 ? true : false;

	}
	
	
	/**
	 * Gets the number of sent messages for a device/server pair
	 *
	 * @param unknown_type $deviceId
	 * @return mixed
	 */
	function getMessageCount($deviceId, $serverHash)
	{
	
		$stmt = $this->dbh->prepare("
				SELECT 	messageCount
				FROM	device_server d
				WHERE	d.deviceId = :deviceId
				AND		d.serverHash = :serverHash
				");
		$stmt->bindParam(':deviceId', $deviceId);
		$stmt->bindParam(':serverHash', $serverHash);
		$stmt->execute();

		$row = $stmt->fetch();
		return $row['messageCount'];
	}
	
	
	/**
	 * Increases the number of sent messages for a device/server pair
	 *
	 * @param unknown_type $deviceId
	 * @return mixed
	 */
	function registerSentMessage($deviceId, $serverHash)
	{
	
		$stmt = $this->dbh->prepare("
				UPDATE 	device_server
				SET		messageCount = messageCount + 1
				WHERE	deviceId = :deviceId
				AND		serverHash = :serverHash
				");
 		$stmt->bindParam(':deviceId', $deviceId);
 		$stmt->bindParam(':serverHash', $serverHash);
		$stmt->execute();
	
		return $stmt->rowCount() > 0 ? true : false;				
	}
	
	
	public function query($sql)
	{
		return $this->dbh->query($sql);
	}
}
?>
