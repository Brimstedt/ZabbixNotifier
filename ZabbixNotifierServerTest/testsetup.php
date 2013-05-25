<?php

	// Set include path to contain original sources
	set_include_path(get_include_path() . PATH_SEPARATOR . dirname(__FILE__)."\..\ZabbixNotifierServer");

	// Include configuration
	require_once "config/test_config.php";

?>