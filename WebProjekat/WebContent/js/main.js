var getUserurl = "../WebProjekat/rest/aplikacija/user";
var logouturl = "../WebProjekat/rest/aplikacija/logout";
var loginurl = "../WebProjekat/login.html"
var user;
//getUser();


$(document).ready(function(){
	getUser();
	//console.log($("button[name = 'odjavi']").prop('name'));
	$("a[href = '#vm']").click(function(){
		console.log('vm pregled');
		$("div #page-content-wrapper").load("vm.html");
	});
	
	$("a[href = '#korisnici']").click(function(){
		console.log('korisnici pregled');
		$("div #page-content-wrapper").load("korisnici.html");
	});
	
	$("a[href = '#organizacije']").click(function(){
		console.log('organizacije pregled');
		$("div #page-content-wrapper").load("orgaizacije.html");
	});
	
	$("a[href = '#diskovi']").click(function(){
		console.log('diskovi pregled');
		$("div #page-content-wrapper").load("diskovi.html");
	});
	
	$("a[href = #kategorije]").click(function(){
		console.log('kategorije pregled');
		$("div #page-content-wrapper").load("kategorije.html");
	});
	$("a[href = #profil]").click(function(){
		console.log('profil pregled');
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
			console.log(data === undefined);
			//console.log(data.uloga);
			if(data !== undefined){
				user = data;
				if(data.uloga === "ADMIN"){
					$("a[href = #kategorije]").remove();
					$("a[href = #organizacije]").remove();
				}
				if(data.uloga === "KORISNIK"){
					$("a[href = #kategorije]").remove();
					$("a[href = #organizacije]").remove();
					$("a[href = #korisnici]").remove();
				}
				return;
			}
			console
			window.location.replace(loginurl);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + textStatus);
		}
	});
	
};

$(document).on('click','#odjavi' ,function(){
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
