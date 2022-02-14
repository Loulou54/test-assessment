package co.copper.test.datamodel;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String id;
    @JsonAlias("firstname")
    private String firstName;
    @JsonAlias("lastname")
    private String lastName;
    private String email;
    private String password;
}
