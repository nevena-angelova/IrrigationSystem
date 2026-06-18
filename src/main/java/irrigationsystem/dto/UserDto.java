package irrigationsystem.dto;

import lombok.Getter;

@Getter
public class UserDto {

    private String username;

    private String password;

    private String email;

    private String firstName;

    private String lastName;

    private int controllerNumber;

    private double latitude;

    private double altitude;
}
