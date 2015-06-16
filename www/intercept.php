<?php
	$fp = fopen("log.txt", "w+");
	fputs($fp, "Begin\n"); 

	require_once('360_safe3.php');
	require_once('CryptAES.php');	
	
	error_reporting(E_ALL ^ E_DEPRECATED);

	$myconn=mysql_connect("localhost", "interce", "Tianwawa1+");
	mysql_select_db("interce");
	mysql_query("set names 'utf8'");
	$curdate = date("Y-m-d H:i:s");
	
	$keyStr = 'UITN25LMUQC436IM';
	 
	$aes = new CryptAES();
	$aes->set_key($keyStr);
	$aes->require_pkcs5();
 
	$number = $aes->decrypt($_REQUEST['number']);
	$message = $aes->decrypt($_REQUEST['message']);
	$type = $_REQUEST['type'];
	
	fputs($fp, "number: " . $number . "\n");
	fputs($fp, "message: " . $message . "\n");
	fputs($fp, "type: " . $type . "\n");

	if ($type == '1') {
		$strSql = "insert into sms(number, message, datetime) values('" . $number . "', '" . $message . "', '" . $curdate . "');";
		fputs($fp, $strSql . "\n");
		$result = mysql_query($strSql, $myconn);
		if ($result) {
			$smsid = mysql_insert_id();
			$output[]=$smsid;
		}
		else {
			$output[]=-1;
		}
	}
	else if ($type == '2') {
		$strSql = "delete from sms;";
		fputs($fp, $strSql . "\n");
		$result = mysql_query($strSql, $myconn);
		if ($result) {
			$output[]=1;
		}
		else {
			$output[]=-1;
		}
	}
	
	print(json_encode($output));

	fputs($fp, "End\n");
	
	mysql_close();  
	fclose($fp);
?>
