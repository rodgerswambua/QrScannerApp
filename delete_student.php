<?php

require_once __DIR__ . '/db_connect.php';
$response = array();

if(isset($_POST['ID'])){

	$id = $_POST['ID'];


	$db = new DB_CONNECT();

	mysql_query("SET NAMES utf8");
	$result=mysql_query("DELETE FROM student WHERE `ID`='$id'");
	if($result){
		$response['value']=1;

	}else{
		$response['value']=0;
	}
}else{
	$response['value']=-1;
	
}
echo json_encode($response);

?>