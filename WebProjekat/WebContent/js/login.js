var loginurl = "../WebProjekat/rest/aplikacija/login"
var appurl = "../WebProjekat/homepage.html"
	
$(document).on('submit',function(e){
	e.preventDefault();
	
	var username = $("input[type= text]").val();
	var password = $("input[type = password]").val();
	console.log(username);
	console.log(password);
	provera(username,password);
	$.ajax({
		type: 'POST',
		url: loginurl,
		contentType: 'application/json',
		dataType: 'json',
		data: JSON.stringify({
			"username":username,
			"password": password
		}),
		success: function(data){
			console.log(data);
			if(typeof data !== "undefined"){
				window.location.replace(appurl);
				return;
			}
			alert("Pogresan par sifra i lozinka!");
			
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + textStatus);
		}
	});
	
	
});

function provera(username,password){
	if(username === ""){
		alert("Neispravno korisnicko ime");
	}
	if(password === ""){
		alert("Nije uneta lozinka");
	}
}