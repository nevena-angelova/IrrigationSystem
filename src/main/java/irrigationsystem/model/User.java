package irrigationsystem.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Getter
    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String firstName;

    @Column(unique = true, nullable = false)
    private String lastName;

    @Getter
    @Column(nullable = false)
    LocalDateTime created;

    @Getter
    @Column(nullable = false)
    LocalDateTime updated;

    @Getter
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Device> devices = new ArrayList<>();

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getName()));
    }

    public List<Device> getDevices() {
        return devices;
    }
}
