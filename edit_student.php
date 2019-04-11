<?php

require_once __DIR__ . '/db_connect.php';
$response = array();

if(isset($_POST['Name'])){

	$id = $_POST['ID'];
	$name = $_POST['Name'];
	$email = $_POST['Email'];
	$mobile = $_POST['Mobile'];

	$db = new DB_CONNECT();

	mysql_query("SET NAMES utf8");
	$result=mysql_query("UPDATE student SET `Name`='$name',`Email`='$email',`Mobile`='$mobile' WHERE `ID`='$id'");
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