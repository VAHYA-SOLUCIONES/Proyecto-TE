function inicianMap(){
    var coord = {lat: ,lng:};
    var map = new google.maps.Map(document.getElementById('map'),{ //map es el primer parámetro
        zoom: 10,//Ahora toma un objeto
        center: coord //Aquí vas a centrar el mapa
    });
}

/*
<script>
            function myMap() {
                var mapProp= {
                    center:new google.maps.LatLng(51.508742,-0.120850),
                    zoom:18,
                    disableDefaultUI: true
                };
                var map = new google.maps.Map(document.getElementById("googleMap"),mapProp);
                var map = new google.maps.Map(document.getElementById("googleMapFull"),mapProp);
            }
        </script>

        <script src="https://maps.googleapis.com/maps/api/js?key=SUA_CHAVE&callback=myMap"></script>
        <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
        <script>
            $(function () {
                'use strict'

                $('[data-toggle="offcanvas"]').on('click', function () {
                    $('.offcanvas-collapse').toggleClass('open')
                })
            });
        </script>*/