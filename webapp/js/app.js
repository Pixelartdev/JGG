var app = new Framework7({
	cache: true,
	cacheIgnore: ['vp.html'],
	cacheIgnoreGetParameters: true,
	swipePanel: 'left',
	pushState: true,
	scrollTopOnStatusbarClick: true,
	init: false,
	swipeBack: true,
	uniqueHistory: true,
	modalButtonCancel: 'Abrechen',
	modalPreloaderTitle: 'Lade ..',
	onAjaxStart: function (xhr) {
        app.showPreloader();
    },
    onAjaxComplete: function (xhr) {
        app.hidePreloader();
    },
});

var $$ = Dom7;
var mainView = app.addView('.view-main', {dynamicNavbar: true, domCache: true});

/** NEWS PAGE **/
app.onPageInit('news-list', function(page) {
	
	startup();
	
	var d_page = 1;
	var d_pages = 1;		
	
	// LIST
	app.showPreloader();
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
			output += '     <li>';
			output += '	      <a href="'+ singleUri +'" class="item-link item-content">';
			output += '          <div class="item-media"><img src="'+ imgUrl +'"width="70px"></div>';
			output += '          <div class="item-inner">';
			output += '            <div class="item-title-row">';
			output += '              <div class="item-title"><b>' + data.posts[i].title + '</b></div>';
			output += '              <div class="item-after">' + '&nbsp;' + '</div>';
			output += '            </div>';
			output += '            <div class="item-text" align="right">Am&nbsp;<i>' + data.posts[i].date + '</i><br>Von&nbsp;<i>'+ data.posts[i].author.name + '</i></div>';
			output += '         </div>';
			output += '       </a>';
			output += '    </li>';
			
		}
		d_pages = data.pages;
		output += '   </ul>';
		output += '</div>';
		output += '<div class="infinite-scroll-preloader">'+
				 '   <div class="preloader"></div>'+
				 '</div>';
		
		// Pull to refresh
		var ptrContent = $$('.pull-to-refresh-content');
		ptrContent.on('refresh', function () {
			app.pullToRefreshDone();
			window.location.reload(true);
		});
		
		$$(page.container).find('.page-content').append(output);
		app.hidePreloader();
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
					output += '     <li>';
					output += '	      <a href="'+ singleUri +'" class="item-link item-content">';
					output += '          <div class="item-media"><img src="'+ imgUrl +'"width="70px"></div>';
					output += '          <div class="item-inner">';
					output += '            <div class="item-title-row">';
					output += '              <div class="item-title"><b>' + data.posts[i].title + '</b></div>';
					output += '              <div class="item-after">' + '&nbsp;' + '</div>';
					output += '            </div>';
					output += '            <div class="item-text" align="right">Am&nbsp;<i>' + data.posts[i].date + '</i><br>Von&nbsp;<i>'+ data.posts[i].author.name + '</i></div>';
					output += '         </div>';
					output += '       </a>';
					output += '    </li>';
					
				}
		
				$$(page.container).find('.page-content .list-block ul').append(output);
			});				
		}else {
			var nomore = '<p>Keine weiteren Beitr&auml;ge.</p>';
		}
	});
});


app.init();


/** SINGLE POST **/
app.onPageInit('single-post', function(page) {
	app.showPreloader();
	app.params.swipePanel= false;
	
	
	// BEITRAG
	var url1 = 'http://jgg-mannheim.de/jggapi/get_post/?date_format=d.%20M%20Y,%20H:i&include=content,title,date,author&callback=?&id='+page.query.id;
	var output1 = '<div class="content-block">';
	
	$$.getJSON(url1, function(data){		
		output1 += '  <p><b><h3>'+ data.post.title +'</h3></b></p>';
		output1 += '  <div class="content-block-inner">';
		output1 += '    <p>'+ data.post.content +'</p>';
		output1 += '  </div>';
		output1 += '  <p>Am:&nbsp;<b>'+ data.post.date +'</b>&nbsp;&nbsp;Von:&nbsp;<b>'+ data.post.author.name +'</b></p>';
		output1 += '</div>';
		
		output1 = output1.replace(/<img[^>]*>/g,"");
			
		$$(page.container).find('.inhalt').append(output1);
		$$(page.container).find('.gallery').remove('div');
		$$(page.container).find('.content-block').find('a').addClass('external');
		app.hidePreloader();
	});
	
	
	
	// FOTOS
	app.showPreloader();
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
			    	output2 += '  </div>';
					output2 += ' <div class="row">';
			 	}
			 
			 	output2 += '     <div class="col-33">';
		     	output2 += '     	<a href="#"><img id="'+index+'" class="bild" src="'+uri+'" /></a>';
			 	output2 += '     </div>';
			 	index++;
			 }
		   }
		   output2+= '  </div>';
		   output2+= '</div>';
		}
		$$(page.container).find('.fotos').append(output2);
		$$(page.container).find('.num-pic').html(index);
		
		fotoSlider = app.photoBrowser({
		   photos : bilder,
		   theme: 'dark',
		   initialSlide: 0,
		   lazyLoading: true,
		   backLinkText: 'Schlie&szlig;en',
		   ofText: ' von ',
		   type: 'popup'
		});
		
		app.hidePreloader();
	});
	
	
	$$(document).on('click', '.bild', function () {
	   var id = $$(this).attr('id');
	   fotoSlider.open(id);
    });
  
});



/** Kategorien **/
app.onPageInit('categories', function(page) {
	app.showPreloader();
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
			
			output += '     <li>';
			output += '	      <a href="'+ singleUri +'" class="item-link item-content">';
			output += '          <div class="item-inner">';
			output += '            <div class="item-title-row">';
			output += '              <div class="item-title"><b>' + data.categories[i].title + '</b></div>';
			output += '              <div class="item-after">' + '&nbsp;' + '</div>';
			output += '            </div>';
			output += '            <div class="item-text" align="right">'+ data.categories[i].post_count + '&nbsp; Artikel</div>';
			output += '         </div>';
			output += '       </a>';
			output += '    </li>';
		}
		
	$$(page.container).find('.page-content').append(output);
		app.hidePreloader();
	});
});



/** Single Kategorie **/
app.onPageInit('category', function(page) {
	app.showPreloader();
	
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
			output += '     <li>';
			output += '	      <a href="'+ singleUri +'" class="item-link item-content">';
			output += '          <div class="item-media"><img src="'+ imgUrl +'"width="70px"></div>';
			output += '          <div class="item-inner">';
			output += '            <div class="item-title-row">';
			output += '              <div class="item-title"><b>' + data.posts[i].title + '</b></div>';
			output += '              <div class="item-after">' + '&nbsp;' + '</div>';
			output += '            </div>';
			output += '            <div class="item-text" align="right">Am&nbsp;<i>' + data.posts[i].date + '</i><br>Von&nbsp;<i>'+ data.posts[i].author.name + '</i></div>';
			output += '         </div>';
			output += '       </a>';
			output += '    </li>';
		}
		
		d_pages = data.pages;
		output += '   </ul>';
		output += '</div>';
		output += '<div class="infinite-scroll-preloader">'+
				 '   <div class="preloader"></div>'+
				 '</div>';
		
		$$(page.container).find('.page-content').append(output);
		app.hidePreloader();
		
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
					output += '     <li>';
					output += '	      <a href="'+ singleUri +'" class="item-link item-content">';
					output += '          <div class="item-media"><img src="'+ imgUrl +'"width="70px"></div>';
					output += '          <div class="item-inner">';
					output += '            <div class="item-title-row">';
					output += '              <div class="item-title"><b>' + data.posts[i].title + '</b></div>';
					output += '              <div class="item-after">' + '&nbsp;' + '</div>';
					output += '            </div>';
					output += '            <div class="item-text" align="right">Am&nbsp;<i>' + data.posts[i].date + '</i><br>Von&nbsp;<i>'+ data.posts[i].author.name + '</i></div>';
					output += '         </div>';
					output += '       </a>';
					output += '    </li>';
				}
		
				$$(page.container).find('.page-content .list-block ul').append(output);
			});				
		}else {
			var nomore = '<p>Keine weiteren Beitr&auml;ge.</p>';
		}
	});
});
 

 
/** Vertretungsplan **/
app.onPageBeforeInit('vp', function(page) {
	app.showPreloader();
	
	var urlH;
	var urlM;
	var auth = '0da147a6-48cd-4a50-8b85-5f15b9e884e6';
	var url = 'https://iphone.dsbcontrol.de/iPhoneService.svc/DSB/timetables/';
	url+= auth;
	
	$$.getJSON('http://webapp.pixelartdev.com/cors.php?url='+url, function(data) {			
		var nameH = data[0].timetablegroupname;
		var nameM = data[1].timetablegroupname;
		var conH;
		var conM;
		var filter = app.formGetData('vp-form');
		
		if(typeof filter !== 'undefined' && typeof filter.klasse !== 'undefined' && filter.klasse.indexOf('Alle') == -1) {
			filter = filter.klasse;
		}else {
			filter = false;
		}
		
		if (nameH.indexOf("morgen") > -1) {
			var temp1 = nameH;
			var temp2 = nameM;
			
			nameH = temp2;
			nameM = temp1;
			conH = data[1].timetableurl;
			conM = data[0].timetableurl;
			
		}else {
			var temp1 = nameH;
			var temp2 = nameM;
			
			nameH = temp1;
			nameM = temp2;
			urlH = data[0].timetableurl;
			urlM = data[1].timetableurl;
		}
		
		// Heute
		$$.get('http://webapp.pixelartdev.com/cors.php?url='+urlH, function(data) {
			nameH = jQuery(data).find(".mon_title").text();
			$$(document).find('.dateH').append(nameH);
			var table =  jQuery(data).find(".mon_list")[0];
			
			// Kein Vertretungsplan verfügbar
			if(typeof table === 'undefined') {
				var maint = '<div class="content-block">';
				maint+= '	<p>'+ jQuery(data).find(".textfuss")[0].text() + '</p>';
				maint+= '	<p>'+ jQuery(data).find(".textfuss")[1].text() + '</p>';
				maint+= '</div>';
				if(typeof maint !== 'undefined') {
					$$(page.container).find('.vpH').append(maint);
				}
			}else {			
				var out = '<div class="list-block accordion-list">';
					out+= '	<ul>';
				
				for (var i = 1, row; row = table.rows[i]; i++) {
					var cl = row.cells[0].textContent;
					if(filter != false && cl.indexOf(filter) > -1 || !filter) {
						var color = getColor(row.cells[0].style.backgroundColor);
						out+= '<li class="accordion-item '+color+' ">';
						out+= '	<a href="#" class="item-content item-link">';
						out+= '		<div class="item-inner">';
						out+= '				<div class="item-title"><b>'+ row.cells[0].textContent +'</b></div>';
						out+= '			<div class="item-subtitle"><b>Stunde: '+ row.cells[1].textContent +' | Vertretung: '+ getVertretung( row.cells[3].textContent ) +'</b></div>';
						out+= '		</div>';
						out+= '	</a>';
						out+= '		<div class="accordion-item-content">';
						out+= '			<div class="content-block">';
						out+= '				<p>Urspr&uuml;ngliches Fach: '+ row.cells[2].textContent +'</p>';
						out+= '				<p>Urspr&uuml;nglicher Raum: '+ row.cells[4].textContent +'</p>';
						out+= '				<p>Neuer Raum: '+ row.cells[5].textContent +'</p>';
						out+= '				<p>Anmerkung: '+ row.cells[6].textContent +'</p>';
						out+= '			</div>';
						out+= '		</div>';
						out+= '</li>';
					}else {
						if(i == table.rows.length-1 && out.indexOf('<div class="list-block accordion-list">	<ul><li ') == -1) {
							out+= '<li class="item-content" >';
							out+= '		<div class="item-inner">';
							out+= '				<div class="item-title">Keine Vertretung f&uuml;r dich</div>';
							out+= '		</div>';
							out+= '</li>';
						}
					}
				}
				
				out+= '	</ul>';
				out+='</div>';
				$$(page.container).find('.vpH').append(out);
			}
		});
		
		// Morgen
		conM = $$.get('http://webapp.pixelartdev.com/cors.php?url='+urlM, function(data) {
			nameM = jQuery(data).find(".mon_title").text();
			$$(document).find('.dateM').append(nameM);
			var table =  jQuery(data).find(".mon_list")[0];
			
			// Kein Vertretungsplan verfügbar
			if(typeof table === 'undefined') {
				var maint = '<div class="content-block">';
				maint+= '	<p>'+ jQuery(data).find(".textfuss")[0].text() + '</p>';
				maint+= '	<p>'+ jQuery(data).find(".textfuss")[1].text() + '</p>';
				maint+= '</div>';
				if(typeof maint !== 'undefined') {
					$$(page.container).find('.vpM').append(maint);
				}
			}else {			
				var out = '<div class="list-block accordion-list">';
					out+= '	<ul>';
				
				for (var i = 1, row; row = table.rows[i]; i++) {
					var cl = row.cells[0].textContent;
					if(filter != false && cl.indexOf(filter) > -1 || !filter) {
						var color = getColor(row.cells[0].style.backgroundColor);
						out+= '<li class="accordion-item '+color+' ">';
						out+= '	<a href="#" class="item-content item-link">';
						out+= '		<div class="item-inner">';
						out+= '				<div class="item-title"><b>'+ row.cells[0].textContent +'</b></div>';
						out+= '			<div class="item-subtitle"><b>Stunde: '+ row.cells[1].textContent +' | Vertretung: '+ getVertretung( row.cells[3].textContent ) +'</b></div>';
						out+= '		</div>';
						out+= '	</a>';
						out+= '		<div class="accordion-item-content">';
						out+= '			<div class="content-block">';
						out+= '				<p>Urspr&uuml;ngliches Fach: '+ row.cells[2].textContent +'</p>';
						out+= '				<p>Urspr&uuml;nglicher Raum: '+ row.cells[4].textContent +'</p>';
						out+= '				<p>Neuer Raum: '+ row.cells[5].textContent +'</p>';
						out+= '				<p>Anmerkung: '+ row.cells[6].textContent +'</p>';
						out+= '			</div>';
						out+= '		</div>';
						out+= '</li>';
					}else {
						if(i == table.rows.length-1 && out.indexOf('<div class="list-block accordion-list">	<ul><li ') == -1) {
							out+= '<li class="item-content" >';
							out+= '		<div class="item-inner">';
							out+= '				<div class="item-title">Keine Vertretung f&uuml;r dich</div>';
							out+= '		</div>';
							out+= '</li>';
						}
					}
				}
				
				out+= '	</ul>';
				out+='</div>';
				$$(page.container).find('.vpM').append(out);
			}
		});
		
		app.hidePreloader();		
	});
	
	// Pull to refresh
	var ptrContent = $$('.pull-to-refresh-content');
	ptrContent.on('refresh', function () {
		app.pullToRefreshDone();
		window.location.reload(true);
	});
	
	// Mehr Button
	$$(document).find('.mehr').on('click', function () {
		var buttons1 = [
			{ text: '<a href="#" data-popup=".popup-explain" class="open-popup">Vertretungsplan verstehen</a>', bold: true },
			{ text: '<a href="'+urlH+'" class="external" target="_blank">Vertretungsplan Heute original</a>' },
			{ text: '<a href="'+urlM+'" class="external" target="_blank">Vertretungsplan Morgen original</a>' }
		];
		var buttons2 = [
			{ text: 'Abbrechen', color: 'red' }
		];
		var groups = [buttons1, buttons2];
		app.actions(groups);
	});
	
});



/** VP settings page **/
app.onPageInit('settings-vp', function(page) {
	
	var pickerDependent = app.picker({
		input: '#picker',
		rotateEffect: true,
		toolbarCloseText: 'Fertig',
		cols: [
			{			
				textAlign: 'center',
				values: [ 
					'Alle', 
					'5.1', '5.2', '5.3', '5.4', 
					'6.1', '6.2', '6.3', '6.4', 
					'7.1', '7.2', '7.3', '7.4', 
					'8.1', '8.2', '8.3', '8.4', 
					'9.1', '9.2', '9.3', '9.4', 
					'10.1', '10.2', '10.3', '10.4', 
					'K1', 'K2' ]
			}
		]
	});
	
});



