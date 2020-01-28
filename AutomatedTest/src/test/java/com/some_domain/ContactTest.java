package com.some_domain;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ContactTest {
    private static final Client CLIENT = Client.create();

    @Test
    public void positiveTestCheckID() {
        positiveTest("205", "Richard", "richard@gmail.com");
    }

    @Test
    public void positiveTestCheckName(){
        positiveTest("311", "Alina", "alina@jetbrains.ru");
    }

    @Test
    public void negativeTestNonExistentID(){
        negativeTest("801", "Richard");
    }

    @Test
    public void negativeTestNonExistentName() {
        negativeTest("147", "Alladin");
    }

    @Test
    public void negativeTestIncorrectCharactersInName() {
        negativeTest("205", "#$2%rr44");
    }

    @Test
    public void negativeTestIncorrectCharactersInID() {
        negativeTest("r!4r@^w", "Nikita");
    }

    @Test
    public void negativeTestMinusCharacterInID() {
        negativeTest("-205", "Alina");
    }

    @Test
    public void negativeTestEmptyInID() {
        negativeTest("", "Alina");
    }

    @Test
    public void negativeTestEmptyInName() {
        negativeTest("223", "");
    }

    private void positiveTest(String ID, String name, String email) {
        WebResource resource = CLIENT.resource("http://some_domain.com:8080/company/" + ID + "/users?name=" + name);
        ClientResponse response = resource.accept("application/json").get(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Request failed!");
        }

        String output = response.getEntity(String.class);
        JSONObject object = new JSONObject(output);

        Assert.assertEquals(name, object.get("name"));
        Assert.assertEquals(email, object.get("email"));
    }

    private void negativeTest(String ID, String name) {
        WebResource resource = CLIENT.resource("http://some_domain.com:8080/company/" + ID + "/users?name=" + name);
        ClientResponse response = resource.accept("application/json").get(ClientResponse.class);

        if (response.getStatus() != 404) {
            throw new RuntimeException("Request failed!");
        }
    }


}
