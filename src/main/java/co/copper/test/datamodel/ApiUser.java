package co.copper.test.datamodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiUser {
    private Name name;
    private String email;
    private Login login;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class Name {
        private String first;
        private String last;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class Login {
        private String uuid;
        private String password;
    }
}
