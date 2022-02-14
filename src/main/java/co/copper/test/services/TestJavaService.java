package co.copper.test.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;

import org.asynchttpclient.AsyncHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.copper.test.datamodel.ApiResult;
import co.copper.test.datamodel.User;
import co.copper.test.storage.TestJavaRepository;


@Service
public class TestJavaService {

    private final TestJavaRepository testRepo;
    private final AsyncHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String baseUrl = "https://randomuser.me/api/?results=20";

    @Autowired
    public TestJavaService(TestJavaRepository testRepo, AsyncHttpClient httpClient) {
        this.testRepo = testRepo;
        this.httpClient = httpClient;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public CompletableFuture<String> getUsers() {
        return this.httpClient.prepareGet(this.baseUrl).execute().toCompletableFuture()
            .handle((res, t) -> {
                try {
                    final List<User> insertedUsers = new ArrayList<>();
                    final ApiResult apiResult = this.objectMapper.readValue(res.getResponseBody(), ApiResult.class);
                    // TODO: create a bulk "storeUsers" method to store a list of users in one SQL query.
                    apiResult.getResults().forEach((user) -> {
                        final List<User> insertedUser = testRepo.storeUser(user);
                        insertedUsers.addAll(insertedUser);
                    });
                    return this.objectMapper.writeValueAsString(insertedUsers);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    return "Exception when serializing result.";
                }
            });
    }
}
