<?php

require_once 'include/db.php';

class dbTestCase Extends PHPUnit_Framework_TestCase
{
	var $db;
		
	public function setUp()
	{
		parent::setUp();
		
		global $config;
	
		$this->db = new db();
		$this->db->connect($config);
			
		if(strtolower($config['dbName']) == "zabbixnotifiertest" )
		{
			$this->db->purgeDatabase();
			$this->db->initiateDb();		}
		else
		{
			die('Bad db name');
		}
	}
		
	public function testDbConnection()
	{
		GLOBAL $config;
		 
		$db = new db();
		$db->connect($config);
		 
		$this->assertTrue($db->isConnected(), 'Connection Failed');
	
	}
	
	
	protected function dumpTable($table)
	{
		$query = "SELECT * FROM {$table}";
		$res = $this->db->query($query);
		while($row = $res->fetch())
		{
			var_dump($row);
		}
	}
	
	protected function assertQueryReturnsRows($expectedCount, $sql, $failMessage)
	{
		$res = $this->db->query($sql);
		$this->assertEquals($expectedCount, $res->rowCount(), $failMessage);
	}
}

?>