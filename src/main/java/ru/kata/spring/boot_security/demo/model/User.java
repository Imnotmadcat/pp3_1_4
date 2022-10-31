package ru.kata.spring.boot_security.demo.model;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;


@Entity
@Table(name = "users")
// я не могу убрать энтити граф, без него возникают проблемы
// с аутентификацией, из-за fetch LAZY
@NamedEntityGraph(name = "roles",
        attributeNodes = @NamedAttributeNode("roles"))
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "last_name")
    private String lastname;

    @Column(name = "email")
    private String email;

    @Column(name = "age")
    private byte age;

    @Column(name = "password")
    private String password;
    @ManyToMany( fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public User() {

    }

    public User(String name, String lastname, String email, byte age, String password, Set<Role> roles) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.age = age;
        this.password = password;
        this.roles = roles;
    }

    public User(String name, String lastname, String email, byte age) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.age = age;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
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
        return true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastName) {
        this.lastname = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte getAge() {
        return age;
    }

    public void setAge(byte age) {
        this.age = age;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;

        if (age != user.age) return false;
        if (!name.equals(user.name)) return false;
        if (!lastname.equals(user.lastname)) return false;
        if (!email.equals(user.email)) return false;
        if (!password.equals(user.password)) return false;
        return roles.equals(user.roles);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + lastname.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + (int) age;
        result = 31 * result + password.hashCode();
        result = 31 * result + roles.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return name + " "
                + lastname + " "
                + email + " "
                + age;
    }
}
