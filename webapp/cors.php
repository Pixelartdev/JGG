<?php

if(isset($_GET['url'])) {
	$uri = $_GET['url'];
	$content = file_get_contents($uri);
	
	echo $content;
}


?>