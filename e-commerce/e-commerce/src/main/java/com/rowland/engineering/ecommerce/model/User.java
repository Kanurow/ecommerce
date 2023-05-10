package com.rowland.engineering.ecommerce.model;

import com.rowland.engineering.ecommerce.model.audit.DateAudit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users_table", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username"
        }),
        @UniqueConstraint(columnNames = {
                "email"
        })
})
public class User extends DateAudit implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;


    private String username;

    private String mobile;



    private String email;

    private String password;

    private Collection<? extends GrantedAuthority> authorities;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();


        public static User create(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream().map(role ->
                new SimpleGrantedAuthority(role.getName().name())
        ).collect(Collectors.toList());

        return new User(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getMobile(),
                authorities
        );
    }
    public User(Long id, String name, String username, String email, String password, String mobile, Collection<? extends GrantedAuthority> authorities) {
            this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.authorities = authorities;
    }

    public User(String name, String username, String email, String password, String mobile) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
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
    public int hashCode() {
        return Objects.hash(id);
    }
}