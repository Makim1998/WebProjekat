var getKorisniciurl = "../WebProjekat/rest/korisnici/getKorisnici";
var addurl = "../WebProjekat/rest/korisnici/add";
var izmenaurl = "../WebProjekat/rest/korisnici/izmena";
var brisanjeurl = "../WebProjekat/rest/korisnici/brisanje";
var baseurl = "../WebProjekat/homepage.html#korisnici"

$(document).on('click','#btnIzmeni',function(){
    //alert($(this).data("korisnik").ime);
    console.log("izmena");
	$('form#izmeni').find('input[name = ime]').val($(this).data("korisnik").ime);
	$('form#izmeni').find('input[name = prezime]').val($(this).data("korisnik").prezime);
	$('form#izmeni').find('input[name = organizacija]').val($(this).data("korisnik").organizacija);
	$('form#izmeni').find('input[name = lozinka]').val($(this).data("korisnik").lozinka);
	$('form#izmeni').find('select[name = tip]').val($(this).data("korisnik").uloga);
	$('form#izmeni').find('input[name = email]').val($(this).data("korisnik").email);
});

$(document).on('click','#btnObrisi',function(){
    //alert($(this).data("korisnik").ime);
	console.log("brisanje")
	$.ajax({
		type: 'POST',
		url: brisanjeurl,
		contentType: 'text/plain',
		dataType: 'text',
		data: $('form#izmeni').find('input[name = email]').val(),
		success: function(data){
			console.log(data);
			$("a[href = '#korisnici']",parent.document)[0].click();
			
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + textStatus);
		}
	});
});

$(document).ready(function() {
	getKorisnici();
	
});

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
                '<td><button id = "btnIzmeni" class="btn btn-primary btn-sm" data-toggle="modal" data-target="#edit">Izmeni</button><td>');
        console.log(tr.find("button").text());
        tr.find("button").data("korisnik", {"ime": korisnik.ime,"prezime":korisnik.prezime,"email":korisnik.email,"organizacija":korisnik.organizacija,"lozinka":korisnik.lozinka,"uloga":korisnik.uloga})
        console.log("hi");
        $('#korisnici tbody').append(tr);
	});
}

$(document).on('submit','#dodajKategorija' ,function(e){
	e.preventDefault();
	console.log("Dodaj korisnika");
	var email = $('form#dodaj').find('input[name = email]').val();
	var ime = $('form#dodaj').find('input[name = ime]').val();
	var prezime = $('form#dodaj').find('input[name = prezime]').val();
	var lozinka = $('form#dodaj').find('input[name = lozinka]').val();
	var organizacija = $('form#dodaj').find('input[name = organizacija]').val();
	var tip = $('form#dodaj').find("select[name = tip]").val() ;
	$.ajax({
		type: 'POST',
		url: addurl,
		contentType: 'application/json',
		dataType: 'json',
		data: JSON.stringify({
			"email" : email,
			"lozinka" : lozinka,
			"ime": ime,
			"prezime": prezime,
			"organizacija" :organizacija,
			"uloga" : tip,
		}),
		
		success: function(data){
			console.log(data);
			//getKorisnici();
			$("a[href = '#korisnici']",parent.document).trigger('click');
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + textStatus);
		}
	});
});

$(document).on('submit','#izmeni' ,function(e){
	e.preventDefault();
	console.log("Izmeni korisnika");
	var email = $('form#izmeni').find('input[name = email]').val();
	var ime = $('form#izmeni').find('input[name = ime]').val();
	var prezime = $('form#izmeni').find('input[name = prezime]').val();
	var lozinka = $('form#izmeni').find('input[name = lozinka]').val();
	var organizacija = $('form#izmeni').find('input[name = organizacija]').val();
	var tip = $('form#izmeni').find("select[name = tip]").val();
	$.ajax({
		type: 'POST',
		url: izmenaurl,
		contentType: 'application/json',
		dataType: 'json',
		data: JSON.stringify({
			"email" : email,
			"lozinka" : lozinka,
			"ime": ime,
			"prezime": prezime,
			"organizacija" :organizacija,
			"uloga" : tip,
		}),
		
		success: function(data){
			console.log(data);
			$("a[href = '#korisnici']",parent.document).trigger('click');
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + textStatus);
		}
	});
});