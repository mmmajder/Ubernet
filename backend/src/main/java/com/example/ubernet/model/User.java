package com.example.ubernet.model;

import com.example.ubernet.model.enums.AuthProvider;
import com.example.ubernet.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "Users")
public class User implements UserDetails {
    @Id
//    @SequenceGenerator(name="userSeqGen", sequenceName = "Seq", initialValue = 10000, allocationSize = 1)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userSeqGen")
    @Column(unique = true)
    private String email;
    private String password;
    private String name;
    private String surname;
    private String city;
    private String phoneNumber;
    private Boolean deleted=false;
    @OneToOne
    private UserAuth userAuth;
    private UserRole role;
    private Boolean isBlocked;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getUserAuth().getRoles();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
        return getUserAuth().getIsEnabled();
    }
}

