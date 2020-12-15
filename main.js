
$( document ).ready(function() {
    var mymap = L.map('mapid').setView([-21.117819, 55.521077], 10);
            
    L.tileLayer('https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
        //maxZoom: 18,
        attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, ' +
        '<a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
        'Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
        id: 'mapbox/streets-v11',
        tileSize: 512,
        zoomOffset: -1
    }).addTo(mymap);

    var geojsonLayer = new L.GeoJSON.AJAX("communes-millesimeespublic.geojson");       
    geojsonLayer.addTo(mymap);
    // mymap.dragging.disable();
    mymap.touchZoom.disable();
    mymap.doubleClickZoom.disable();santeMarkers
    // mymap.scrollWheelZoom.disable();
    mymap.boxZoom.disable();
    mymap.keyboard.disable();
    /*if (mymap.tap) mymap.tap.disable();
    document.getElementById('mapid').style.cursor='default';*/
    geojsonLayer.on("click", function (event) { // Assuming the clicked feature is a shape, not a point marker.
    mymap.fitBounds(event.layer.getBounds());
    mymap.setView([-21.117819, 55.521077], 10);
    });

    $('input[type="checkbox"]').each(function(){
        $(this).prop('checked', false);
    });
    var tourismeMarkers = new Array();
    $("#tourisme").on('click',function () {
        $(this).toggleClass("checked")
        queryTourismeUrl="http://localhost:3030/test/sparql?query=%0APREFIX+rdf%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F1999%2F02%2F22-rdf-syntax-ns%23%3E%0APREFIX+rdfs%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3E%0APREFIX+owl%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23%3E%0APREFIX+tm%3A+%3Chttp%3A%2F%2Fwww.thomasandmax.com%2F%3E%0A%0A%0ASELECT+%3Frandonnee+(SAMPLE+(%3Flongitude)+as+%3Flong)+(SAMPLE+(%3Flatitude)+as+%3Flat)+(SAMPLE+(%3Fd)+as+%3Fduree)+(SAMPLE+(%3Fdi)+as+%3Fdistance)+(SAMPLE+(%3Fdev)+AS+%3Fdenivele)%0AWHERE+%7B%0A++%3Frandonnee+rdf%3Atype+tm%3ARandonnee+.%0A++%3Frandonnee+tm%3Along+%3Flongitude+.%0A++%3Frandonnee+tm%3Alat+%3Flatitude+.%0A++%3Frandonnee+tm%3Aduree+%3Fd+.%0A++%3Frandonnee+tm%3Adistance+%3Fdi+.%0A++%3Frandonnee+tm%3Adenivele+%3Fdev+.%0A++FILTER+(%3Flongitude!%3D%220%22)%0A%7D%0AGROUP+BY+%3Frandonnee&format=json"
        $.ajax({url:queryTourismeUrl,success: function(result) {
            if ($("#tourisme").hasClass("checked")) {
                result['results']['bindings'].forEach(element => {
                    var marker = L.marker([element['lat']['value'],element['long']['value']]).addTo(mymap);
                    tourismeMarkers.push(marker)
                    let name = element['randonnee']['value'];
                    let duree = element['duree']['value'];
                    let distance = element['distance']['value'];
                    let denivele = element['denivele']['value'];
                    let newline = "<br />"
                    marker.bindPopup("<b>"+name.substring(name.lastIndexOf('/')+1).replaceAll("_"," ")+"</b>"+newline+"Duree (min) : "+duree+newline+"Distance (km) : "+distance+newline+"Dénivelé (m) : "+denivele).openPopup();
                    mymap.closePopup();
                })
            } else {
                for (let i=0;i<tourismeMarkers.length;i++) {
                    mymap.removeLayer(tourismeMarkers[i])
                }
                tourismeMarkers = []
            }
        }})
    })

    var greenIcon = new L.Icon({
        iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-green.png',
        shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
        iconSize: [25, 41],
        iconAnchor: [12, 41],
        popupAnchor: [1, -34],
        shadowSize: [41, 41]
    });

    var santeMarkers = new Array();
    $("#sante").on('click',function () {
        $(this).toggleClass("checked")
        querySanteURL="http://localhost:3030/test/sparql?query=PREFIX+rdf%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F1999%2F02%2F22-rdf-syntax-ns%23%3E%0APREFIX+rm%3A+%3Chttp%3A%2F%2Fjazz.net%2Fns%2Frm%23%3E%0Aprefix+rdfs%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3E%0Aprefix+owl%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23%3E%0Aprefix+tm%3A%3Chttp%3A%2F%2Fwww.thomasandmax.com%2F%3E%0A%0ASELECT+%3Fsubject+(SAMPLE+(%3Flong)+AS+%3Flongitude)++(SAMPLE+(%3Flat)+AS+%3Flatitude)+(SAMPLE+(%3Fadresse)+AS+%3Fadr)+(SAMPLE+(%3Fcategorie)+AS+%3Fcat)%0AWHERE+%7B%0A++%3Fsubject+rdf%3Atype+%3Fcategorie+.%0A++%3Fsubject+tm%3Along+%3Flong+.%0A++%3Fsubject+tm%3Alat+%3Flat+.%0A++%3Fsubject+tm%3AaAdresse+%3Fadresse+.%0A++FILTER+(%3Fcategorie+IN+(tm%3ACentres_Hospitaliers%2Ctm%3AEtablissements_d%5C'Hebergement_pour_Personnes_%C3%82gees%2Ctm%3ALaboratoires_de_Biologie_Medicale))%0A%7D%0AGROUP+BY+%3Fsubject+%3Fadresse%0ALIMIT+500&format=json"
        $.ajax({url:querySanteURL,success: function(result) {
            if ($("#sante").hasClass("checked")) {
                result['results']['bindings'].forEach(element => {
                    var marker = L.marker([element['latitude']['value'],element['longitude']['value']],{icon: greenIcon}).addTo(mymap);
                    santeMarkers.push(marker)
                    let name = element['subject']['value'];
                    let adresse = element['adr']['value'];
                    let cat = element['cat']['value'].substring(name.lastIndexOf('/')+1).replaceAll("_"," ");
                    marker.bindPopup("<b>"+name.substring(name.lastIndexOf('/')+1).replaceAll("_"," ")+"</b><br/>"+adresse+"<br />"+cat).openPopup();
                    mymap.closePopup();
                })
            } else {
                for (let i=0;i<santeMarkers.length;i++) {
                    mymap.removeLayer(santeMarkers[i])
                }
                santeMarkers = []
            }
        },async:false})
    })

    var yellowIcon = new L.Icon({
        iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-gold.png',
        shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
        iconSize: [25, 41],
        iconAnchor: [12, 41],
        popupAnchor: [1, -34],
        shadowSize: [41, 41]
    });

    var redIcon = new L.Icon({
        iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-red.png',
        shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
        iconSize: [25, 41],
        iconAnchor: [12, 41],
        popupAnchor: [1, -34],
        shadowSize: [41, 41]
    });

    var transportMarkers = new Array();
    $("#transport").on('click',function () {
        $(this).toggleClass("checked")
        queryTransportURL="http://localhost:3030/test/sparql?query=%0APREFIX+rdf%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F1999%2F02%2F22-rdf-syntax-ns%23%3E%0APREFIX+rdfs%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3E%0APREFIX+owl%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23%3E%0APREFIX+tm%3A+%3Chttp%3A%2F%2Fwww.thomasandmax.com%2F%3E%0A%0A%0ASELECT+%3Fsubject+(SAMPLE+(%3Flat)+AS+%3Flatitude)++(SAMPLE+(%3Flong)+AS+%3Flongitude)%0AWHERE+%7B%0A++%3Fsubject+rdf%3Atype+tm%3AStopCarJaune+.%0A++%3Fsubject+tm%3Along+%3Flong+.%0A++%3Fsubject+tm%3Alat+%3Flat+.%0A%7D%0AGROUP+BY+%3Fsubject&format=json"
        $.ajax({url:queryTransportURL,success: function(result) {
            if ($("#transport").hasClass("checked")) {
                result['results']['bindings'].forEach(element => {
                    var marker = L.marker([element['latitude']['value'],element['longitude']['value']],{icon: yellowIcon}).addTo(mymap);
                    transportMarkers.push(marker)
                    let name = element['subject']['value'];
                    marker.bindPopup("<b>"+name.substring(name.lastIndexOf('/')+1).replaceAll("_"," ")+"</b>").openPopup();
                    mymap.closePopup();
                })
            } else {
                for (let i=0;i<transportMarkers.length;i++) {
                    mymap.removeLayer(transportMarkers[i])
                }
                transportMarkers = []
            }
        },async:false})
    })

    
    var remarquablePlace = new Array();
    $("#tourisme2").on('click',function () {
        $(this).toggleClass("checked")
        queryRemarquablePlaceUrl="http://localhost:3030/test/sparql?query=%0APREFIX+rdf%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F1999%2F02%2F22-rdf-syntax-ns%23%3E%0APREFIX+rdfs%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3E%0APREFIX+owl%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23%3E%0APREFIX+tm%3A+%3Chttp%3A%2F%2Fwww.thomasandmax.com%2F%3E%0A%0A%0ASELECT+%3Flieu+(SAMPLE+(%3Flong)+as+%3Flongitude)+(SAMPLE+(%3Flat)+as+%3Flatitude)+(SAMPLE+(%3Facc)+as+%3Faccessibilite)%0AWHERE+%7B%0A++%3Flieu+rdf%3Atype+tm%3ALieuRemarquable+.%0A++%3Flieu+tm%3Along+%3Flong+.%0A++%3Flieu+tm%3Alat+%3Flat+.%0A++%3Flieu+tm%3AaccessibleMobiliteReduite+%3Facc+.%0A%7D%0AGROUP+BY+%3Flieu&format=json"
        $.ajax({url:queryRemarquablePlaceUrl,success: function(result) {
            if ($("#tourisme2").hasClass("checked")) {
                result['results']['bindings'].forEach(element => {
                    var marker = L.marker([element['latitude']['value'],element['longitude']['value']],{icon: redIcon}).addTo(mymap);
                    remarquablePlace.push(marker)
                    let name = element['lieu']['value'];
                    let accessibilite = element['accessibilite']['value'];
                    marker.bindPopup("<b>"+name.substring(name.lastIndexOf('/')+1).replaceAll("_"," ")+"</b><br />"+accessibilite).openPopup();
                    mymap.closePopup();
                })
            } else {
                for (let i=0;i<remarquablePlace.length;i++) {
                    mymap.removeLayer(remarquablePlace[i])
                }
                remarquablePlace = []
            }
        },async:false})
    })
});

