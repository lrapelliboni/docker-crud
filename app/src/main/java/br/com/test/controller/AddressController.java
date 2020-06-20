package br.com.test.controller;

import br.com.test.domain.Address;
import br.com.test.domain.google.GoogleMapsResponse;
import br.com.test.domain.google.Location;
import br.com.test.domain.google.Result;
import br.com.test.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping({"/address"})
public class AddressController {
    @Autowired
    private AddressRepository addressRepository;

    @Value( "${google.maps.api.key}" )
    private String googleApiKey;

    @GetMapping
    public List<Address> findAll(){
        return addressRepository.findAll();
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<Address> findById(@PathVariable long id){
        return addressRepository.findById(id)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Address address, UriComponentsBuilder uriComponentsBuilder){
        if (address.getLatitude() == null || address.getLongitude() == null) {
            Location location = getLatLongByGoogle(address);
            System.out.println(location);
            address.setLatitude(location.getLat());
            address.setLongitude(location.getLng());
        }
        addressRepository.save(address);
        URI uri = uriComponentsBuilder.path("/addresss/{id}").buildAndExpand(address.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<Address> update(@PathVariable("id") long id,
                                          @Valid @RequestBody Address address){
        return addressRepository.findById(id)
                .map(record -> {
                    record.setStreetName(address.getStreetName());
                    record.setNumber(address.getNumber());
                    record.setComplement(address.getComplement());
                    record.setNeighbourhood(address.getNeighbourhood());
                    record.setCity(address.getCity());
                    record.setState(address.getState());
                    record.setCountry(address.getCountry());
                    record.setZipCode(address.getZipCode());
                    record.setLatitude(address.getLatitude());
                    record.setLongitude(address.getLongitude());
                    if (address.getLatitude() == null || address.getLongitude() == null) {
                        Location location = getLatLongByGoogle(address);
                        record.setLatitude(location.getLat());
                        record.setLongitude(location.getLng());
                    }
                    Address updated = addressRepository.save(record);
                    return ResponseEntity.ok().body(updated);
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(path ={"/{id}"})
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        return addressRepository.findById(id)
                .map(record -> {
                    addressRepository.deleteById(id);
                    return ResponseEntity.noContent().build();
                }).orElse(ResponseEntity.notFound().build());
    }

    private Location getLatLongByGoogle(Address address) {
        String addressString = new StringBuilder()
                .append(address.getStreetName()).append(" ")
                .append(address.getNumber()).append(" ")
                .append(address.getNeighbourhood()).append(" ")
                .append(address.getCity()).append(" ")
                .append(address.getState()).append(" ")
                .append(address.getCountry()).append(" ").toString();

        final String googleMapsApiUrl = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=%s", addressString, googleApiKey);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<GoogleMapsResponse> response = restTemplate.exchange(
                googleMapsApiUrl,
                HttpMethod.GET,   null,
                new ParameterizedTypeReference<GoogleMapsResponse>(){});

        GoogleMapsResponse googleMapsResponse = response.getBody();
        Location location = new Location();
        if (googleMapsResponse != null) {
            if (googleMapsResponse.getResults() != null) {
                Result result = googleMapsResponse.getResults().get(0);
                location = result.getGeometry().getLocation();
            }
        }
        return location;
    }
}
