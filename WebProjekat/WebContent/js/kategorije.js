var getKategorijeurl = "../WebProjekat/rest/kategorije/getKategorije";
var addurl = "../WebProjekat/rest/kategorije/add";
var izmenaurl = "../WebProjekat/rest/kategorije/izmena";
var brisanjeurl = "../WebProjekat/rest/kategorije/brisanje";

$(document).on('click','#btnIzmeniKategorija',function(){
    //alert("button");
	//console.log($(this).text());
	console.log("alllo");
	$('form#izmeniKategorija').find('input[name = ime]').val($(this).data("kategorija").ime);
	$('form#izmeniKategorija').find('input[name = ime]').data("staro",$(this).data("kategorija").ime);
	$('form#izmeniKategorija').find('select[name = gpu]').val($(this).data("kategorija").gpu);
	$('form#izmeniKategorija').find('select[name = ram]').val($(this).data("kategorija").ram);
	$('form#izmeniKategorija').find('select[name = broj]').val($(this).data("kategorija").broj);
	
});

$(document).ready(function() {
	getKategorije();
	
});
function getKategorije(){
	console.log('nadji kategorije');
	$.ajax({
		type : 'GET',
		url : getKategorijeurl,
		dataType : "json", // data type of response
		success : renderList
	});
};

function renderList(data) {
	// JAX-RS serializes an empty list as null, and a 'collection of one' as an object (not an 'array of one')
	var list = data == null ? [] : (data instanceof Array ? data : [ data ]);
	
	$.each(list, function(index, kategorija) {
        var tr = $('<tr></tr>');
        tr.append('<td>' + kategorija.ime + '</td>' +
                '<td>' + kategorija.brojJezgara + '</td>' + 
                '<td>' + kategorija.ram + '</td>' + 
                '<td>' + kategorija.gpu + '</td>' +
                '<td><button id = "btnIzmeniKategorija" class="btn btn-primary btn-sm" data-toggle="modal" data-target="#edit">Izmeni</button><td>');
        console.log(tr.find("button").text());
        tr.find("button").data("kategorija", {"ime": kategorija.ime,"broj":kategorija.brojJezgara,"ram":kategorija.ram,"gpu":kategorija.gpu})
        console.log("hi");
        console.log(tr.find("button").data("kategorija").ram);
        $('#kategorije tbody').append(tr);
	});
}

$(document).on('submit','#dodajKategorija' ,function(e){
	e.preventDefault();
	console.log("Dodaj kategoriju");
	var ime = $('form#dodajKategorija').find('input[name = ime]').val();
	var broj = $('form#dodajKategorija').find('select[name = broj]').val();
	var ram = $('form#dodajKategorija').find('select[name = ram]').val();
	var gpu = $('form#dodajKategorija').find('select[name = gpu]').val();
	$.ajax({
		type: 'POST',
		url: addurl,
		contentType: 'application/json',
		dataType: 'json',
		data: JSON.stringify({
			"ime" : ime,
			"brojJezgara" : broj,
			"ram": ram,
			"gpu": gpu,
		}),
		
		success: function(data){
			console.log(data);
			//getKorisnici();
			if(data === undefined){
				alert("Neuspesno dodavanje! Postoji kategorija sa istim imenom!");
				return;
			}
			
			$("a[href = '#kategorije']",parent.document).trigger('click');
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + textStatus);
		}
	});
});

$(document).on('submit','#izmeniKategorija' ,function(e){
	e.preventDefault();
	console.log("Izmeni kategoriju");
	var staro = $('form#izmeniKategorija').find('input[name = ime]').data("staro");
	var ime = $('form#izmeniKategorija').find('input[name = ime]').val();
	var broj = $('form#izmeniKategorija').find('select[name = broj]').val();
	var ram = $('form#izmeniKategorija').find('select[name = ram]').val();
	var gpu = $('form#izmeniKategorija').find("select[name = gpu]").val();
	$.ajax({
		type: 'POST',
		url: izmenaurl,
		contentType: 'application/json',
		dataType: 'json',
		data: JSON.stringify({
			"staro": staro,
			"ime" : ime,
			"broj" : broj,
			"ram": ram,
			"gpu": gpu,
		}),
		
		success: function(data){
			console.log(data);
			if(data === undefined){
				alert("Izmena nije uspela!!");
			}
			$("a[href = '#kategorije']",parent.document).trigger('click');
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + textStatus);
		}
	});
});

$(document).on('click','#btnObrisiKategorija',function(){
    //alert($(this).data("korisnik").ime);
	console.log("brisanje")
	$.ajax({
		type: 'POST',
		url: brisanjeurl,
		contentType: 'text/plain',
		dataType: 'text',
		data: $('form#izmeniKategorija').find('input[name = ime]').val(),
		success: function(data){
			console.log(data);
			if (data !== "OK"){
				alert("Brisnaje neuspesno!");
				return;
			}
			$("a[href = '#kategorije']",parent.document)[0].click();
			
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + textStatus);
		}
	});
});

