package br.com.test.controller;

import br.com.test.DockerCrudApplication;
import br.com.test.domain.Address;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DockerCrudApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AddressControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String getRootUrl() {
        return "http://localhost:" + port;
    }

    @Test
    public void contextLoads() {

    }

    @Test
    public void testGetAllPosts() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/address",
                HttpMethod.GET, entity, String.class);
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetById() {
        Address address = restTemplate.getForObject(getRootUrl() + "/address/1", Address.class);
        assertNotNull(address);
    }

    @Test
    public void testCreate() {
        Address address = new Address();
        address.setStreetName("Rua 1");
        address.setNumber(100);
        address.setComplement("Perto da esquina");
        address.setZipCode("13470987");
        address.setNeighbourhood("Bairro da vila");
        address.setCountry("Brasil");
        address.setState("SP");
        ResponseEntity<Address> response = restTemplate.postForEntity(getRootUrl() + "/address", address, Address.class);
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testUpdate() {
        int id = 1;
        Address address = restTemplate.getForObject(getRootUrl() + "/address/" + id, Address.class);
        address.setStreetName("Rua 1");
        address.setNumber(100);
        address.setComplement("Perto da esquina");
        address.setZipCode("13470987");
        address.setNeighbourhood("Bairro da vila");
        address.setCountry("Brasil");
        address.setState("SP");
        address.setLatitude("123456789");
        address.setLongitude("987654321");
        restTemplate.put(getRootUrl() + "/address/" + id, address);
        Address updated= restTemplate.getForObject(getRootUrl() + "/address/" + id, Address.class);
        assertNotNull(updated);
    }

    @Test
    public void testDelete() {
        int id = 1;
        Address address = restTemplate.getForObject(getRootUrl() + "/address/" + id, Address.class);
        assertNotNull(address);
        restTemplate.delete(getRootUrl() + "/address/" + id);
        try {
            address = restTemplate.getForObject(getRootUrl() + "/address/" + id, Address.class);
        } catch (final HttpClientErrorException e) {
            assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
        }
    }
}
