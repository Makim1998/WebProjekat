var getMasineurl = "../WebProjekat/rest/masine/get";
var addurl = "../WebProjekat/rest/masine/dodaj";
var izmenaurl = "../WebProjekat/rest/masine/izmeni";
var brisanjeurl = "../WebProjekat/rest/masine/brisanje";
var aktivacijaurl = "../WebProjekat/rest/masine/aktivacija";

$(document).ready(function() {
	$.ajax({
		type : 'GET',
		url : getMasineurl,
		dataType : "json", // data type of response
		success : function(data){
            var list = data == null ? [] : (data instanceof Array ? data : [ data ]);
	
	        $.each(list, function(index, masina) {
            var tr = $('<tr></tr>');
            tr.append('<td>' + masina.ime + '</td>' +
                '<td>' + masina.kategorija + '</td>' + 
                '<td>' + masina.datumPaljenja + '</td>' + 
                '<td>' + masina.datumGasenja + '</td>' +
                '<td><button id = "btnizmeniVM" class="btn btn-primary btn-sm" data-toggle="modal" data-target="#edit">Izmeni</button><td>');
            $('#masine').append(tr);
	        });
        }
	});
});

$(document).on('submit','#dodajVM' ,function(e){
	e.preventDefault();
	var ime = $('form#dodajVM').find('input[name = ime]').val();
    var kategorija = $('form#dodajVM').find('input[name = kategorija]').val();
	$.ajax({
		type: 'POST',
		url: addurl,
		contentType: 'application/json',
		dataType: 'json',
		data: JSON.stringify({
			"ime" : ime,
			"kategorija" : kategorija
		}),
		
		success: function(data){
			if(data == null){
				alert("Neuspesno dodavanje! Postoji masina sa istim imenom!");
				return;
			}
			
			$("a[href = '#masine']",parent.document).trigger('click');
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + textStatus);
		}
	});
});

$(document).on('submit','#izmeniVM' ,function(e){
	e.preventDefault();
	var staro = $('form#izmeniVM').find('input[name = ime]').data("staro");
	var ime = $('form#izmeniVM').find('input[name = ime]').val();
	var kategorija = $('form#izmeniVM').find('select[name = kategorija]').val();
	$.ajax({
		type: 'GET',
		url: izmenaurl + '/' + staro,
		contentType: 'application/json',
		dataType: 'text',
		data: JSON.stringify({
			"ime" : ime,
			"kategorija" : kategorija,
		}),
		
		success: function(data){
			if(data != "OK"){
				alert("Izmena nije uspela!!");
			}
			$("a[href = '#masine']",parent.document).trigger('click');
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + textStatus);
		}
	});
});

$(document).on('click','#btnObrisiMasina',function(){
	$.ajax({
		type: 'POST',
		url: brisanjeurl,
		contentType: 'text/plain',
		dataType: 'text',
		data: $('form#izmeniVM').find('input[name = ime]').val(),
		success: function(data){
			if (data !== "OK"){
				alert("Brisnaje neuspesno!");
				return;
			}
			$("a[href = '#masine']",parent.document)[0].click();
			
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + textStatus);
		}
	});
});

$(document).on('click','#btnAktivirajMasina',function(){
	$.ajax({
		type: 'POST',
		url: aktivacijaurl,
		contentType: 'text/plain',
		dataType: 'text',
		data: $('form#izmeniVM').find('input[name = ime]').val(),
		success: function(data){
			if (data !== "OK"){
				alert("Brisnaje neuspesno!");
				return;
			}
			$("a[href = '#masine']",parent.document)[0].click();
			
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + textStatus);
		}
	});
});
