package com.rowland.engineering.ecommerce.model;

import com.rowland.engineering.ecommerce.model.audit.DateAudit;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.NaturalId;
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
        }),
        @UniqueConstraint(columnNames = {
                "account_number"
        })
})
public class User extends DateAudit implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 40)
    private String name;

    @NotBlank
    @Size(max = 20)
    private String username;


    @NotBlank
    @Size(max = 15)
    private String mobile;

    @NotBlank
    @Size(max = 10)
    @NaturalId
    @Column(name = "account_number")
    private String accountNumber;

    @Email
    @NaturalId
    private String email;

    private Double voucherBalance;


    @NotNull
    private Double accountBalance;

    @NotBlank
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
                user.getAccountNumber(),
                user.getVoucherBalance(),
                user.getRoles(),
                authorities
        );
    }

    public User(Long id,  String username,String name, String email, String password, String mobile,String accountNumber, Double voucherBalance , Set<Role> roles,Collection<? extends GrantedAuthority> authorities ) {
            this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.accountNumber = accountNumber;
        this.voucherBalance = voucherBalance;
        this.roles = roles;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User that = (User) o;
        return Objects.equals(id, that.id);
    }
}