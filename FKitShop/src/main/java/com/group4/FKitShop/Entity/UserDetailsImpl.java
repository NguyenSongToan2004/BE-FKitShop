package com.group4.FKitShop.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.group4.FKitShop.Entity.Accounts;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Data
public class UserDetailsImpl implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    private String id;

    private String email;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;


    public static UserDetailsImpl build(Accounts accounts) {
        // Chuyển đổi role thành GrantedAuthority
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + accounts.getRole().toUpperCase());

        return new UserDetailsImpl(
                accounts.getAccountID(),
                accounts.getEmail(),
                accounts.getPassword(),  // Sửa lại để dùng đúng password
                List.of(authority)       // Trả về danh sách quyền từ role
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;  // Trả về danh sách quyền (authorities)
    }

    @Override
    public String getPassword() {
        return password;  // Trả về password của người dùng
    }

    @Override
    public String getUsername() {
        return email;  // Trả về email làm username
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
}
