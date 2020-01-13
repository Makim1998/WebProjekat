var getKorisniciurl = "../WebProjekat/rest/korisnici/getKorisnici";
var addurl = "../WebProjekat/rest/korisnici/add";
var baseurl = "../WebProjekat/homepage.html#korisnici"

getKorisnici();

function getKorisnici(){
	console.log('nadji korisnike');
	$.ajax({
		type : 'GET',
		url : getKorisniciurl,
		dataType : "json", // data type of response
		success : renderList
	});
};

function renderList(data) {
	// JAX-RS serializes an empty list as null, and a 'collection of one' as an object (not an 'array of one')
	var list = data == null ? [] : (data instanceof Array ? data : [ data ]);
	
	$.each(list, function(index, korisnik) {
        var tr = $('<tr></tr>');
        tr.append('<td>' + korisnik.email + '</td>' +
                '<td>' + korisnik.ime + '</td>' + 
                '<td>' + korisnik.prezime + '</td>' + 
                '<td>' + korisnik.organizacija + '</td>' +
                '<td><button id = "izmeni" class="btn btn-primary btn-sm">Izmeni</button><td>');
        $('#korisnici tbody').append(tr);
	});
}

$(document).on('submit','#dodaj' ,function(){
	e.preventDefault();
	console.log("Dodaj korisnika");
	var email = $('input[name = email]').val();
	var ime = $('input[name = ime]').val();
	var prezime = $('input[name = prezime]').val();
	var lozinka = $('input[name = lozinka]').val();
	var organizacija = $('input[name = organizacija]').val();
	var tip = $("select[name = tip]").val() ;
	$.ajax({
		type: 'POST',
		url: addurl,
		contentType: 'application/json',
		dataType: 'application/json',
		data: JSON.stringify({
			"email" : email,
			"lozinka" : lozinka,
			"ime": ime,
			"prezime": prezime,
			"organizacija" :organizacija,
			"tip" : tip,
		}),
		
		success: function(data){
			window.location.replace(baseurl);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + textStatus);
		}
	});
});