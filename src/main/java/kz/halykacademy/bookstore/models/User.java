package kz.halykacademy.bookstore.models;

import kz.halykacademy.bookstore.dto.user.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    @Column(name = "is_blocked")
    private Boolean isBlocked;

    @OneToMany(mappedBy = "user")
    private List<Order> orders = new ArrayList<>();

    public UserResponse toUserDto() {

        return new UserResponse(
                this.id,
                this.login,
                this.role,
                this.isBlocked
        );
    }
}
