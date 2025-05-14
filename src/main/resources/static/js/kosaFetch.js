/**
 * 
 */
async function kosaFetch(_options, onSuccess, onFailure) {
    const {url, ...options} = _options;
	const csrf = document.querySelector("meta[name='_csrf']");
	const csrf_header = document.querySelector("meta[name='_csrf_header']");
	const csrfToken = csrf ? csrf.content : null;
	const csrfHeader = csrf_header ? csrf_header.content : null;
	
	options.headers = {'Content-Type': 'application/json;charset=utf-8'};
	if (csrfToken) {
		options.headers[csrfHeader] = csrfToken;
	}

	if (_options.param) {
		options.body = JSON.stringify(_options.param); 
	}
	if (! _options.method) {
		options.method = 'post';
	}

	fetch(url, options)
	.then(response => response.json())
	.then(json => {
	  	if (json.status == "ok") {
			if (onSuccess) {
				onSuccess(json)
			}
		} else {
			if (onFailure) {
				onFailure(json)
			}
		}
	})
	.catch(error => {
		onFailure(error);
	});
	//asxio 변경 
	//ajax -> jquery 함수  
}
