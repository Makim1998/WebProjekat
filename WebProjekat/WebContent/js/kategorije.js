var getKategorijeurl = "../WebProjekat/rest/kategorije/getKategorije";
var addurl = "../WebProjekat/rest/kategorije/add";
var izmenaurl = "../WebProjekat/rest/kategorije/izmena";
var brisanjeurl = "../WebProjekat/rest/kategorije/brisanje";

$(document).on('click','#btnIzmeni',function(){
    //alert($(this).data("korisnik").ime);
	$('form#izmeni').find('input[name = ime]').val($(this).data("kategorija").ime);
	$('form#izmeni').find('select[name = gpu]').val($(this).data("kategorija").gpu);
	$('form#izmeni').find('select[name = ram]').val($(this).data("kategorija").ram);
	$('form#izmeni').find('select[name = broj]').val($(this).data("kategorija").broj);
	
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
                '<td>' + kategorija.RAM + '</td>' + 
                '<td>' + kategorija.GPU + '</td>' +
                '<td><button id = "btnIzmeni" class="btn btn-primary btn-sm" data-toggle="modal" data-target="#edit">Izmeni</button><td>');
        console.log(tr.find("button").text());
        tr.find("button").data("kategorija", {"ime": kategorija.ime,"broj":kategorija.brojJezgara,"ram":kategorija.RAM,"gpu":kategorija.GPU})
        //console.log("hi");
        $('#kategorije tbody').append(tr);
	});
}