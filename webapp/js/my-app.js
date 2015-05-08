var myApp = new Framework7({
	cache: true,
	cacheIgnore: ['vp.html'],
	cacheIgnoreGetParameters: true,
	swipePanel: 'left',
	pushState: true,
	init: false,
	swipeBack: true,
	uniqueHistory: true,
	modalButtonCancel: 'Abrechen',
	modalPreloaderTitle: 'Lade ..',
	onAjaxStart: function (xhr) {
        myApp.showIndicator();
    },
    onAjaxComplete: function (xhr) {
        myApp.hideIndicator();
    },
	
});

// For older Browser without .contains() support
if (!String.prototype.contains) {
    String.prototype.contains = function() {
        return String.prototype.indexOf.apply(this, arguments) !== -1;
    };
}

var $$ = Dom7;
var mainView = myApp.addView('.view-main', {dynamicNavbar: true});
var ptrContent = $$('.pull-to-refresh-content');



/** NEWS PAGE **/
myApp.onPageInit('news-list', function(page) {
	
	var d_page = 1;
	var d_pages = 1;		
	
	// LIST
	myApp.showPreloader();
	var url = 'http://jgg-mannheim.de/jggapi/get_recent_posts/?date_format=d.%20M%20Y,%20H:i&include=title,date,thumbnail,author&callback=?';
	var singleUri = 'single-post.html?id=';
	var imgUrl = 'img/list_placeholder.png';
	var output = '<div class="list-block media-list">' +
				  '   <ul>';
						
	$$.getJSON(url, function(data){
		for(var i = 0; i < data.posts.length; i++) {
			if(data.posts[i].thumbnail != null) {
				imgUrl = data.posts[i].thumbnail_images.thumbnail.url;
			} else {
				imgUrl = 'img/list_placeholder.png';
			}
			singleUri = null;
			singleUri = 'single-post.html?id=';
			singleUri += data.posts[i].id;
			output+= '     <li>';
			output+= '	      <a href="'+ singleUri +'" class="item-link item-content">';
			output+= '          <div class="item-media"><img src="'+ imgUrl +'"width="70px"></div>';
			output+= '          <div class="item-inner">';
			output+= '            <div class="item-title-row">';
			output+= '              <div class="item-title"><b>' + data.posts[i].title + '</b></div>';
			output+= '              <div class="item-after">' + '&nbsp;' + '</div>';
			output+= '            </div>';
			output+= '            <div class="item-text" align="right">Am&nbsp;<i>' + data.posts[i].date + '</i><br>Von&nbsp;<i>'+ data.posts[i].author.name + '</i></div>';
			output+= '         </div>';
			output+= '       </a>';
			output+= '    </li>';
			
		}
		d_pages = data.pages;
		output+= '   </ul>';
		output+= '</div>';
		output+= '<div class="infinite-scroll-preloader">'+
				 '   <div class="preloader"></div>'+
				 '</div>';
		
		$$(page.container).find('.page-content').append(output);
		myApp.hidePreloader();
	});
		
	$$('.infinite-scroll').on('infinite', function () {
		if(d_page <= d_pages) {
			d_page++;
			var url = 'http://jgg-mannheim.de/jggapi/get_recent_posts/?date_format=d.%20M%20Y,%20H:i&include=title,date,thumbnail,author&callback=?&page='+d_page;
			var singleUri = 'single-post.html?id=';
			var imgUrl = 'img/list_placeholder.png';
			var output = '';
						
			$$.getJSON(url, function(data){
				for(var i = 0; i < data.posts.length; i++) {
					if(data.posts[i].thumbnail != null) {
						imgUrl = data.posts[i].thumbnail_images.thumbnail.url;
					} else {
						imgUrl = 'img/list_placeholder.png';
					}
					singleUri = null;
					singleUri = 'single-post.html?id=';
					singleUri += data.posts[i].id;
					output+= '     <li>';
					output+= '	      <a href="'+ singleUri +'" class="item-link item-content">';
					output+= '          <div class="item-media"><img src="'+ imgUrl +'"width="70px"></div>';
					output+= '          <div class="item-inner">';
					output+= '            <div class="item-title-row">';
					output+= '              <div class="item-title"><b>' + data.posts[i].title + '</b></div>';
					output+= '              <div class="item-after">' + '&nbsp;' + '</div>';
					output+= '            </div>';
					output+= '            <div class="item-text" align="right">Am&nbsp;<i>' + data.posts[i].date + '</i><br>Von&nbsp;<i>'+ data.posts[i].author.name + '</i></div>';
					output+= '         </div>';
					output+= '       </a>';
					output+= '    </li>';
					
				}
		
				$$(page.container).find('.page-content .list-block ul').append(output);
			});				
		}else {
			var nomore = '<p>Keine weiteren Beitr&auml;ge.</p>';
		}
	});
});

myApp.init();



/** SINGLE POST **/
myApp.onPageInit('single-post', function(page) {
	myApp.showPreloader();
	myApp.params.swipePanel= false;
	
	
	// BEITRAG
	var url1 = 'http://jgg-mannheim.de/jggapi/get_post/?date_format=d.%20M%20Y,%20H:i&include=content,title,date,author&callback=?&id='+page.query.id;
	var output1 = '<div class="content-block">';
	
	$$.getJSON(url1, function(data){		
		output1+= '  <p><b><h3>'+ data.post.title +'</h3></b></p>';
		output1+= '  <div class="content-block-inner">';
		output1+= '    <p>'+ data.post.content +'</p>';
		output1+= '  </div>';
		output1+= '  <p>Am:&nbsp;<b>'+ data.post.date +'</b>&nbsp;&nbsp;Von:&nbsp;<b>'+ data.post.author.name +'</b></p>';
		output1+= '</div>';
		
		output1 = output1.replace(/<img[^>]*>/g,"");
			
		$$(page.container).find('.inhalt').append(output1);
		$$(page.container).find('.gallery').remove('div');
		$$(page.container).find('.content-block').find('a').addClass('external');
		myApp.hidePreloader();
	});
	
	
	
	// FOTOS
	myApp.showPreloader();
	var index = 0;
	var bilder = [];
	var fotoSlider;
	var url2 = 'http://jgg-mannheim.de/jggapi/get_post/?include=attachments&callback=?&id='+page.query.id;
	var output2 = '<div class="content-block">'+
	              '  <div class="row">';
	              
	$$.getJSON(url2, function(data) {
		
		if(data.post.attachments.length > 0 && data.post.attachments != null) {
		   var uri = null;
		   for(var i = 0; i< data.post.attachments.length; i++) {
		   	 if(data.post.attachments[i].images != null) {  
		     	uri = null;
			 	uri = data.post.attachments[i].images.medium.url;
			 	uriFull = data.post.attachments[i].images.full.url;
			 	bilder.push(uriFull);
			 
			 	if(i && (i % 3 === 0) ) {
			    	output2+= '  </div>';
					output2+= ' <div class="row">';
			 	}
			 
			 	output2+= '     <div class="col-33">';
		     	output2+= '      <a href="#"><img id="'+index+'" class="bild" src="'+uri+'" /></a>';
			 	output2+= '     </div>';
			 	index++;
			 }else { 
			 	console.log('no image'); 
			 }
		   }
		   output2+= '  </div>';
		   output2+= '</div>';
		}
		$$(page.container).find('.fotos').append(output2);
		$$(page.container).find('.num-pic').html(index);
		
		fotoSlider = myApp.photoBrowser({
		   photos : bilder,
		   theme: 'dark',
		   initialSlide: 0,
		   lazyLoading: true,
		   backLinkText: 'Schlie&szlig;en',
		   ofText: ' von ',
		   type: 'popup'
		});
		
		myApp.hidePreloader();
	});
	
	
	$$(document).on('click', '.bild', function () {
	   var id = $$(this).attr('id');
	   fotoSlider.open(id);
    });
  
});



/** Kategorien **/
myApp.onPageInit('categories', function(page) {
	myApp.showPreloader();
	var url = 'http://jgg-mannheim.de/jggapi/get_category_index/?callback=?';
	var singleUri = 'category-posts.html?cid=';
	var output = '<div class="list-block">' +
				  '   <ul>';
						
	$$.getJSON(url, function(data){
		for(var i = 0; i < data.categories.length; i++) {
			singleUri = null;
			singleUri = 'category-posts.html?cid=';
			singleUri += data.categories[i].id;
			singleUri += '&name='+ data.categories[i].title;
			
			output+= '     <li>';
			output+= '	      <a href="'+ singleUri +'" class="item-link item-content">';
			output+= '          <div class="item-inner">';
			output+= '            <div class="item-title-row">';
			output+= '              <div class="item-title"><b>' + data.categories[i].title + '</b></div>';
			output+= '              <div class="item-after">' + '&nbsp;' + '</div>';
			output+= '            </div>';
			output+= '            <div class="item-text" align="right">'+ data.categories[i].post_count + '&nbsp; Artikel</div>';
			output+= '         </div>';
			output+= '       </a>';
			output+= '    </li>';
		}
		
	$$(page.container).find('.page-content').append(output);
		myApp.hidePreloader();
	
	});
});



/** Single Kategorie **/
myApp.onPageInit('category', function(page) {
	myApp.showPreloader();
	
	var title = page.query.name;
	var url = 'http://jgg-mannheim.de/jggapi/get_category_posts/?date_format=d.%20M%20Y,%20H:i&include=title,date,thumbnail,author&id='+page.query.cid+'&callback=?';
	var singleUri = 'single-post.html?id=';
	var imgUrl = 'img/list_placeholder.png';
	var output = '<div class="list-block media-list">' +
				  '   <ul>';
				  
	$$(page.container).find('.center .sliding div').append(title);				
	$$.getJSON(url, function(data){
		for(var i = 0; i < data.posts.length; i++) {
			if(data.posts[i].thumbnail != null) {
				imgUrl = data.posts[i].thumbnail_images.thumbnail.url;
			} else {
				imgUrl = 'img/list_placeholder.png';
			}
			singleUri = null;
			singleUri = 'single-post.html?id=';
			singleUri += data.posts[i].id;
			output+= '     <li>';
			output+= '	      <a href="'+ singleUri +'" class="item-link item-content">';
			output+= '          <div class="item-media"><img src="'+ imgUrl +'"width="70px"></div>';
			output+= '          <div class="item-inner">';
			output+= '            <div class="item-title-row">';
			output+= '              <div class="item-title"><b>' + data.posts[i].title + '</b></div>';
			output+= '              <div class="item-after">' + '&nbsp;' + '</div>';
			output+= '            </div>';
			output+= '            <div class="item-text" align="right">Am&nbsp;<i>' + data.posts[i].date + '</i><br>Von&nbsp;<i>'+ data.posts[i].author.name + '</i></div>';
			output+= '         </div>';
			output+= '       </a>';
			output+= '    </li>';
			
		}
		
		d_pages = data.pages;
		output+= '   </ul>';
		output+= '</div>';
		output+= '<div class="infinite-scroll-preloader">'+
				 '   <div class="preloader"></div>'+
				 '</div>';
		
		$$(page.container).find('.page-content').append(output);
		myApp.hidePreloader();
		
	});
		
	$$('.infinite-scroll').on('infinite', function () {
		if(d_page <= d_pages) {
			d_page++;
			var url = 'http://jgg-mannheim.de/jggapi/get_category_posts/?date_format=d.%20M%20Y,%20H:i&include=title,date,thumbnail,author&callback=?&id='+page.query.cid+'&page='+d_page;
			var singleUri = 'single-post.html?id=';
			var imgUrl = 'img/list_placeholder.png';
			var output = '';
						
			$$.getJSON(url, function(data){
				for(var i = 0; i < data.posts.length; i++) {
					if(data.posts[i].thumbnail != null) {
						imgUrl = data.posts[i].thumbnail_images.thumbnail.url;
					} else {
						imgUrl = 'img/list_placeholder.png';
					}
					singleUri = null;
					singleUri = 'single-post.html?id=';
					singleUri += data.posts[i].id;
					output+= '     <li>';
					output+= '	      <a href="'+ singleUri +'" class="item-link item-content">';
					output+= '          <div class="item-media"><img src="'+ imgUrl +'"width="70px"></div>';
					output+= '          <div class="item-inner">';
					output+= '            <div class="item-title-row">';
					output+= '              <div class="item-title"><b>' + data.posts[i].title + '</b></div>';
					output+= '              <div class="item-after">' + '&nbsp;' + '</div>';
					output+= '            </div>';
					output+= '            <div class="item-text" align="right">Am&nbsp;<i>' + data.posts[i].date + '</i><br>Von&nbsp;<i>'+ data.posts[i].author.name + '</i></div>';
					output+= '         </div>';
					output+= '       </a>';
					output+= '    </li>';
					
				}
		
				$$(page.container).find('.page-content .list-block ul').append(output);
			});				
		}else {
			var nomore = '<p>Keine weiteren Beitr&auml;ge.</p>';
		}
	});
});
 

 
/** Vertretungsplan **/
myApp.onPageInit('vp', function(page) {
	myApp.showPreloader();
	
	var auth = '0da147a6-48cd-4a50-8b85-5f15b9e884e6';
	var url = 'https://iphone.dsbcontrol.de/iPhoneService.svc/DSB/timetables/';
	
	url+= auth;
	
	$$.getJSON('http://webapp.pixelartdev.com/cors.php?url='+url, function(data) {			
		var nameH = data[0].timetablegroupname;
		var nameM = data[1].timetablegroupname;
		var conH;
		var conM;
		
		if (nameH.indexOf("morgen") > -1) {
			// save values temporaly
			var temp1 = nameH;
			var temp2 = nameM;
			
			nameH = temp2;
			nameM = temp1;
			conH = data[1].timetableurl;
			conM = data[0].timetableurl;
			
		}else {
			// save values temporaly
			var temp1 = nameH;
			var temp2 = nameM;
			
			nameH = temp1;
			nameM = temp2;
			conH = data[0].timetableurl;
			conM = data[1].timetableurl;
		}
		
		conH = httpGet('http://webapp.pixelartdev.com/cors.php?url='+conH);
		conM = httpGet('http://webapp.pixelartdev.com/cors.php?url='+conM);
		
		nameH = jQuery(conH).find(".mon_title").text();
		nameM = jQuery(conM).find(".mon_title").text();
		
		conH = jQuery(conH).find(".mon_list")[0].outerHTML;
		conM = jQuery(conM).find(".mon_list")[0].outerHTML;
		
		
		$$(page.container).find('.dateH').append(nameH);
		$$(page.container).find('.dateM').append(nameM);
		$$(page.container).find('.vpH').append(conH);
		$$(page.container).find('.vpM').append(conM);
		
	/*	var metas = document.getElementsByTagName('meta');
		for(i = 0; i < metas.length; i++) {
			if(metas[i].name == "viewport") {
				metas[i].setAttribute("content", "");
			}
		}
		*/
		
		myApp.hidePreloader();			
	});
});



/** Termine **/
myApp.onPageInit('termine', function(page) {
	myApp.showPreloader();
	var url = 'http://webapp.pixelartdev.com/cors.php?url=http://jgg-mannheim.de/termine';
	var output = '<div class="content-block-inner">';
	
/**	$$.getJSON(url, function(data){
			output+= '        <script type="text/javascript" class="external" src="http://www.jgg-mannheim.de/wp-content/plugins/ajax-event-calendar/js/jquery.init_show_calendar.js"></script>';
			output+= '        <link rel="stylesheet" id="jq_ui_css-css" class="external" href="http://www.jgg-mannheim.de/wp-content/plugins/ajax-event-calendar/css/jquery-ui-1.8.16.custom.css" type="text/css" media="all">';
			output+= '        <link rel="stylesheet" id="custom-css" class="external" href="http://www.jgg-mannheim.de/wp-content/plugins/ajax-event-calendar/css/custom.css" type="text/css" media="all">';
			output+= '  <p>'+ data.page.content +'</p>';
		    output+= '</div>';
		
		$$(page.container).find('.content-block').append(output);
		$$(page.container).find('div[id="aec-menu"]').remove('div');
		$$(page.container).find('ul[id="aec-filter"]').remove('ul');
		myApp.hidePreloader();
	});	
	
 **/
});


// get url contents
function httpGet(theUrl) {
	var xmlHttp = null;
	xmlHttp = new XMLHttpRequest();
	xmlHttp.open( "GET", theUrl, false );
	xmlHttp.send( null );
	return xmlHttp.responseText;
}


/** REFRESH **/
ptrContent.on('refresh', function (e) {
	window.location.reload();
	myApp.pullToRefreshDone();
});