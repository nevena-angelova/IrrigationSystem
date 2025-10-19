package irrigationsystem.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "relays")
public class Relay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Boolean used;

    @ManyToOne
    @JoinColumn(name = "device_id", nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Device device;

    public void setDevice(Device device) {
        this.device = device;
    }

    public Boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
