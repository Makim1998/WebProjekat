var getOrganizacijeurl = "../WebProjekat/rest/organizacije/get";
var addurl = "../WebProjekat/rest/organizacije/dodaj";
var izmenaurl = "../WebProjekat/rest/organizacije/izmena";

$(document).ready(function() {
	getOrganizacije();
});

function getOrganizacije(){
	console.log('nadji organizacije');
	$.ajax({
		type : 'GET',
		url : getOrganizacijeurl,
		dataType : "json", // data type of response
		success : function(data){
			var list = data == null ? [] : (data instanceof Array ? data : [ data ]);
			$.each(list, function(index, organizacija){
				var tr = $('<tr></tr>');
				tr.append('<td>' + organizacija.ime + '</td>' +
                '<td>' + organizacija.logo + '</td>' + 
                '<td>' + organizacija.opis + '</td>' +
				'<td><button id = "btnIzmeniOrganizacija" class="btn btn-primary btn-sm" data-toggle="modal" data-target="#edit">Izmeni</button><td>');
				$('#organizacije').append(tr);
			})
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + textStatus);
		}
	});
}

$(document).on('submit','#dodajOrganizacija' ,function(e){
	e.preventDefault();
	var ime = $('form#dodajOrganizacija').find('input[name = ime]').val();
	var logo = $('form#dodajOrganizacija').find('input[name = logo]').val();
	var opis = $('form#dodajOrganizacija').find('input[name = opis]').val();
	$.ajax({
		type: 'POST',
		url: addurl,
		contentType: 'application/json',
		dataType: 'json',
		data: JSON.stringify({
			"ime" : ime,
			"logo" : logo,
			"opis": opis,
		}),
		
		success: function(data){
			if(data == null){
				alert("Neuspesno dodavanje! Postoji organizacija sa istim imenom!");
				return;
			}
			
			$("a[href = '#organizacije']",parent.document).trigger('click');
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + textStatus);
		}
	});
});

$(document).on('submit','#izmeniOrganizacija' ,function(e){
	e.preventDefault();
	var staro = $('form#izmeniOrganizacija').find('input[name = ime]').data("staro");
	var ime = $('form#izmeniOrganizacija').find('input[name = ime]').val();
	var logo = $('form#izmeniOrganizacija').find('input[name = logo]').val();
	var opis = $('form#izmeniOrganizacija').find('input[name = opis]').val();
	$.ajax({
		type: 'GET',
		url: izmenaurl + '/' + staro,
		contentType: 'application/json',
		dataType: 'text',
		data: JSON.stringify({
			"ime" : ime,
			"logo" : logo,
			"opis": opis,
			"novoIme": staro
		}),
		
		success: function(data){
			if(data != "OK"){
				alert("Izmena nije uspela!!");
			}
			$("a[href = '#kategorije']",parent.document).trigger('click');
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + textStatus);
		}
	});
});
