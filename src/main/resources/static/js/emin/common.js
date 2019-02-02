var Emin = {};

function exportReport(url, data){
    var param = [];
    for (var key in data){
        var value = data[key];
        if (value.constructor == Array){
            value.forEach(function(_value){
                param.push(key + "=" + _value);
            });
        }else{
            param.push(key + '=' + value);
        }
    }
    param.push('ecmId=' + globalEcmId);
    param.push('token=' + globalToken);
    param = param.join("&");
	if(Emin.downloadFrame == undefined){
		var downloadFrame = document.createElement("iframe")
		downloadFrame.style.display = "none";
		Emin.downloadFrame = downloadFrame;
		document.body.appendChild(Emin.downloadFrame)
	}
	Emin.downloadFrame.src = encodeURI(url + "?" + param);
}

function charLen(val) {
	var l = val.length,
		blen = 0;
	
	for(i = 0; i < l; i++) {
		if ((val.charCodeAt(i) & 0xff00) != 0) {
			blen ++;
		}
		blen ++;
	}
	return blen;
}

function substrByCharLen(val, len) {
	var l = val.length,
		blen = 0,
		validLen = l;
	
	for(i = 0; i < l; i++) {
		if ((val.charCodeAt(i) & 0xff00) != 0) {
			blen ++;
		}
		blen ++;

		if (blen > len) {
			validLen = i - 1;
			break;
		}
	}
	return val.substring(0, validLen);
}