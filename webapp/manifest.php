<?php
header('Content-Type: text/cache-manifest');

$filesToCache = array(
    './categories.html',
	'./category-posts.html',
	'./index.html',
	'./settings.html',
	'./settings-info.html',
	'./settings-vp.html',
	'./single-post.html',
	'./termine.html',
	
	'./css/app.css',
	'./css/f7.3d.min.css',
	'./css/f7.ios.min.css',
	'./css/f7.ios.colors.min.css',
	
	'./js/app.js',
	'./js/f7.3d.min.js',
	'./js/f7.min.js',
	'./js/f7.min.js.map',
	'./js/functions.js',
	'./js/ios_splash.js',
	'./js/jquery-1.11.1.min.js',
	
	'./favicon.ico',
	'./img/archive.png',
	'./img/calendar.png',
	'./img/list_placeholder.png',
	'./img/menu.png',
	'./img/news.png',
	'./img/news-b.png',
	'./img/news-s.png',
	'./img/phone.png',
	'./img/photo.png',
	'./img/photo-b.png',
	'./img/photo-s.png',
	'./img/settings.png',
	'./img/share-b.png',
	'./img/share-s.png',
	'./img/vp.png',
	'./img/ios/icon_ipad_ios6.png',
	'./img/ios/icon_ipad_ios6_retina.png',
	'./img/ios/icon_ipad_ios7.png',
	'./img/ios/icon_ipad_ios7_retina.png',
	'./img/ios/icon_iphone_ios6.png',
	'./img/ios/icon_iphone_ios6_retina.png',
	'./img/ios/icon_iphone_ios7.png',
	'./img/ios/icon_iphone_ios7_retina.png',
	'./img/ios/ipad_2x_ios6_landscape.jpg',
	'./img/ios/ipad_2x_ios6_portrait.jpg',
	'./img/ios/ipad_2x_ios7_landscape.jpg',
	'./img/ios/ipad_2x_ios7_portrait.jpg',
	'./img/ios/ipad_ios6_landscape.jpg',
	'./img/ios/ipad_ios6_portrait.jpg',
	'./img/ios/ipad_ios7_landscape.jpg',
	'./img/ios/ipad_ios7_portrait.jpg',
	'./img/ios/iphone3G.jpg',
	'./img/ios/iphone4_ios6.jpg',
	'./img/ios/iphone4_ios7.jpg',
	'./img/ios/iphone5_ios6.jpg',
	'./img/ios/iphone5_ios7.jpg',
);
?>

CACHE MANIFEST

CACHE:
<?php

$hashes = '';
foreach($filesToCache as $file) {
    echo $file."\n";
    $hashes.=md5_file($file);
};
?>

NETWORK:
*

# Hash Version: <?=md5($hashes)?>