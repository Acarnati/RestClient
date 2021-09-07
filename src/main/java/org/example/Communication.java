package org.example;

import org.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class Communication {

    private RestTemplate restTemplate;
    private final HttpHeaders headers;
    private final String URL = "http://91.241.64.178:7081/api/users";

    public Communication(RestTemplate restTemplate, HttpHeaders headers) {
        this.restTemplate = restTemplate;
        this.headers = headers;
        this.headers.set("Cookie",
                String.join(";", restTemplate.headForHeaders(URL).get("Set-Cookie")));
    }

    public String getCode() {
        return saveUser().getBody() + updateUser().getBody() + deleteUser().getBody();
    }

    public List<User> getAllUsers() {
        ResponseEntity<List<User>> responseEntity = restTemplate.exchange(URL, HttpMethod.GET,
                null, new ParameterizedTypeReference<List<User>>() {});
        System.out.println(responseEntity.getHeaders());
        List<User> allUser = responseEntity.getBody();
        return allUser;
    }

    public User getUser(int id) {
        User user = restTemplate.getForObject(URL + "/" + id, User.class);
        return user;
    }

    public ResponseEntity<String> saveUser() {
        User user = new User(3L, "James", "Brown", (byte) 28);
        HttpEntity<User> entity = new HttpEntity<>(user, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(URL, entity, String.class);
        return responseEntity;
    }

    public ResponseEntity<String> updateUser() {
        User user = new User(3L, "Thomas", "Shelby", (byte) 28);
        HttpEntity<User> entity = new HttpEntity<>(user, headers);
        return restTemplate.exchange(URL, HttpMethod.PUT, entity, String.class, 3);
    }

    public ResponseEntity<String> deleteUser() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        return restTemplate.exchange(URL + "/3", HttpMethod.DELETE, entity, String.class, 3);
    }
}
