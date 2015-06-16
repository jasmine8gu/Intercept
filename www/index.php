<?php
	error_reporting(E_ALL ^ E_DEPRECATED);
	
	function connect() {
		$myconn=mysql_connect("localhost", "interce", "Tianwawa1+");
		mysql_select_db("interce");
	
		mysql_query("set names 'utf8'");
		
		return $myconn;
	}

	function disconnect($myconn) {
		mysql_close($myconn);
	}
?>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>

<body>
	<?php
		$myconn = connect();
	?>

	<div class='mainpage'>
		<div class="pagebody">
		<form>
			<table class="list" cellspacing=0 cellpadding=0 border=1>
				<tr>
					<td class="listtitle">id</td>
					<td class="listtitle">sender number</td>
					<td class="listtitle">message</td>
					<td class="listtitle">datetime</td>
				</tr>
			<?php
				$strSql = "select * from sms order by id desc";
				$result = mysql_query($strSql, $myconn);
				while ($row = mysql_fetch_array($result)) {
			?>
				<tr>
					<td><?php echo $row['id'];?></td>
					<td><?php echo $row['number'];?></td>
					<td><?php echo $row['message'];?></td>
					<td><?php echo $row['datetime'];?></td>
				</tr>
			<?php
				}
			?>
			</table>
		</form>		
		</div>
		
	</div>
	
	<?php
		disconnect($myconn);
	?>
</body>
</html>	