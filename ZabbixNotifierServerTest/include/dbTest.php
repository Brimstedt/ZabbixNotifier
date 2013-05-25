<?php

require_once "testsetup.php";
require_once "dbTestCase.php";

class dbTest extends dbTestCase
{
	public function setUp()
	{
		parent::setUp();
		 
	}

	public function testDbInit()
	{
	
		
		$result = $this->db->purgeDatabase();
		$this->assertEquals(0, $this->db->getDbVersion(), 'Database version not 0 from start');
			
		$result = $this->db->initiateDb();
			
		$this->assertTrue($result, 'Database initialization failed');
		$this->assertEquals(DB_VERSION, $this->db->getDbVersion(), 'Database not properly upgraded');
			
	}
    
}
?>