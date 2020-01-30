var getDiskoviurl = "../WebProjekat/rest/diskovi/get";
var addurl = "../WebProjekat/rest/diskovi/dodaj";
var izmenaurl = "../WebProjekat/rest/diskovi/izmeni";
var brisanjeurl = "../WebProjekat/rest/diskovi/brisanje";

$(document).ready(function() {
	$.ajax({
		type : 'GET',
		url : getDiskoviurl,
		dataType : "json", // data type of response
		success : function(data){
            var list = data == null ? [] : (data instanceof Array ? data : [ data ]);
	
	        $.each(list, function(index, disk) {
            var tr = $('<tr></tr>');
            tr.append('<td>' + disk.ime + '</td>' +
                '<td>' + disk.tip + '</td>' + 
                '<td>' + disk.kapacitet + '</td>' + 
				'<td>' + disk.virtuelnaMasina + '</td>' +
				'<td>' + disk.organizacija + '</td>' +
                '<td><button id = "btnIzmeniDisk" class="btn btn-primary btn-sm" data-toggle="modal" data-target="#edit">Izmeni</button><td>');
            $('#diskovi').append(tr);
	        });
        }
	});
});

$(document).on('submit','#dodajDisk' ,function(e){
	e.preventDefault();
	var ime = $('form#dodajDisk').find('input[name = ime]').val();
	var tip = $('form#dodajDisk').find('select[name = tip]').val();
	var kapacitet = $('form#dodajDisk').find('select[name = kapacitet]').val();
	var virtuelna = $('form#dodajDisk').find('input[name = virtuelna]').val();
	var organizacija = $('form#dodajDisk').find('input[name = organizacija]').val();
	$.ajax({
		type: 'POST',
		url: addurl,
		contentType: 'application/json',
		dataType: 'json',
		data: JSON.stringify({
			"ime" : ime,
			"tip" : tip,
			"kapacitet": parseInt(kapacitet),
			"virtuelnaMasina": virtuelna,
			"organizacija": organizacija
		}),
		
		success: function(data){
			if(data == null){
				alert("Neuspesno dodavanje! Postoji disk sa istim imenom!");
				return;
			}
			
			$("a[href = '#diskovi']",parent.document).trigger('click');
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + textStatus);
		}
	});
});

$(document).on('submit','#izmeniDisk' ,function(e){
	e.preventDefault();
	var staro = $('form#izmeniDisk').find('input[name = ime]').data("staro");
	var ime = $('form#izmeniDisk').find('input[name = ime]').val();
	var tip = $('form#izmeniDisk').find('select[name = tip]').val();
	var kapacitet = $('form#izmeniDisk').find('select[name = kapacitet]').val();
	var virtuelna = $('form#izmeniDisk').find('input[name = virtuelna]').val();
	var organizacija = $('form#izmeniDisk').find('input[name = organizacija]').val();
	$.ajax({
		type: 'GET',
		url: izmenaurl + '/' + staro,
		contentType: 'application/json',
		dataType: 'text',
		data: JSON.stringify({
			"ime" : ime,
			"tip" : tip,
			"kapacitet": parseInt(kapacitet),
			"virtuelnaMasina": virtuelna,
			"organizacija": organizacija
		}),
		
		success: function(data){
			if(data != "OK"){
				alert("Izmena nije uspela!!");
			}
			$("a[href = '#diskovi']",parent.document).trigger('click');
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + textStatus);
		}
	});
});

$(document).on('click','#btnObrisiDisk',function(){
	$.ajax({
		type: 'POST',
		url: brisanjeurl,
		contentType: 'text/plain',
		dataType: 'text',
		data: $('form#izmeniDisk').find('input[name = ime]').val(),
		success: function(data){
			if (data !== "OK"){
				alert("Brisnaje neuspesno!");
				return;
			}
			$("a[href = '#diskovi']",parent.document)[0].click();
			
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + textStatus);
		}
	});
});
