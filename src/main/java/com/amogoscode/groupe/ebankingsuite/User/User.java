package com.amogoscode.groupe.ebankingsuite.User;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @SequenceGenerator(
            name = "user_id_sequence",
            sequenceName = "user_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_id_sequence"
    )
    private Integer id;
    @Column(nullable = false)
    private String fullName;
    @Column(nullable = false)
    private String emailAddress;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private boolean isNotBlocked;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User(String fullName, String emailAddress, String password, boolean isNotBlocked) {
        this.fullName = fullName;
        this.emailAddress = emailAddress;
        this.password = password;
        this.createdAt=LocalDateTime.now();
        this.updatedAt=LocalDateTime.now();
        this.isNotBlocked = isNotBlocked;
    }

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return emailAddress;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isNotBlocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", password='" + password + '\'' +
                ", isNotBlocked=" + isNotBlocked +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return isNotBlocked == user.isNotBlocked && Objects.equals(id, user.id) && Objects.equals(fullName, user.fullName) && Objects.equals(emailAddress, user.emailAddress) && Objects.equals(password, user.password) && Objects.equals(createdAt, user.createdAt) && Objects.equals(updatedAt, user.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, emailAddress, password, isNotBlocked, createdAt, updatedAt);
    }
}
