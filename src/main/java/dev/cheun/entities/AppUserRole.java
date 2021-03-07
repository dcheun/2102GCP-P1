package dev.cheun.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="app_user_role")
public class AppUserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="role_name")
    private String roleName;

    // IMPORTANT: mappedBy is the name of the Bean field that has the JoinColumn
    // annotation, not the db column name.
    // Many users references a role.
    // Eager fetch: One giant SQL query performed to get users immediately.
    // Cascade allows us to persist changes to the user through this object.
    @OneToMany(mappedBy = "roleId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<AppUser> users = new HashSet<>();

    public AppUserRole(){}

    public AppUserRole(int id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Set<AppUser> getUsers() {
        return users;
    }

    public void setUsers(Set<AppUser> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "AppUserRole{" +
                "id=" + id +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}
