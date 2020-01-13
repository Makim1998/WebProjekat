var getUserurl = "../WebProjekat/rest/aplikacija/user";
var logouturl = "../WebProjekat/rest/aplikacija/logout";
var loginurl = "../WebProjekat/login.html"
var user;
//getUser();


$(document).ready(function(){
	getUser();
	$("a[href = #vm]").click(function(){
		$("div #page-content-wrapper").load("vm.html");
	});
	
	$("a[href = #korisnici]").click(function(){
		$("div #page-content-wrapper").load("korisnici.html");
	});
	
	$("a[href = #organizacije]").click(function(){
		$("div #page-content-wrapper").load("orgaizacije.html");
	});
	
	$("a[href = #diskovi]").click(function(){
		$("div #page-content-wrapper").load("diskovi.html");
	});
	
	$("a[href = #kategorije]").click(function(){
		$("div #page-content-wrapper").load("kategorije.html");
	});
	$("a[href = #profil]").click(function(){
		$("div #page-content-wrapper").load("profil.html");
	});
});

function getUser(){
	$.ajax({
		type: 'GET',
		url: getUserurl,
		//contentType: 'application/json',
		dataType: 'json',
		success: function(data){
			console.log(data);
			if(typeof data !== "undefined"){
				user = data;
				return;
			}
			alert("greska!");
			
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + textStatus);
		}
	});
	
}

$("button[name = odjavi]").click(function(){
	$.ajax({
		type: 'GET',
		url: logouturl,
		//contentType: 'application/json',
		dataType: 'text',
		success: function(data){
			window.location.replace(loginurl);

			
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + textStatus);
		}
	});
});
