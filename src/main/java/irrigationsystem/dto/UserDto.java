package irrigationsystem.dto;

import lombok.Getter;

@Getter
public class UserDto {

    private String username;

    private String password;

    private String email;

    private String firstName;

    private String lastName;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
