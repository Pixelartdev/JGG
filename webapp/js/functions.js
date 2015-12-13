/**
Outsourced functions for better readability
**/

function startup() {
	var direct = app.formGetData('vp-form');
	if(typeof direct !== 'undefined') {		
		if(direct.direct[0] == 'on') {
			console.log('redire');
			mainView.router.loadPage('vp.html');
		}
	}
}


function getColor(c) {
	if(c == 'rgb(255, 0, 0)') {
		return 'vp-red';
	}else if(c == 'rgb(128, 255, 128)') {
		return 'vp-green';
	}else if(c == 'rgb(128, 255, 255)') {
		return 'vp-tur';
	}else {
		return '1';
	}
}


function getVertretung(vp) {
	if(vp.indexOf('---') > -1) {
		return 'Frei';
	}else if(vp.indexOf('eva') > -1) {
		return 'EVA';
	}else if(vp.indexOf('Ver') > -1) {
		return 'Vertretung';
	}else {
		return vp;
	}
}