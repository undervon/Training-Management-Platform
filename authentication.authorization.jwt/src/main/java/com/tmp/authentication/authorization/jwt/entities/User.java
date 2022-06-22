package com.tmp.authentication.authorization.jwt.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employees")
public class User {

    @Id
    @Column(name = "id_employee")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    // HMI, VNI, ADAS, PSS
    private String department;
    private String employeeNumber;

    @Builder.Default
    private LocalDateTime joinDate = LocalDateTime.now();

    @Lob
    private byte[] image;

    @ManyToOne
    @JoinColumn(name = "id_manager", referencedColumnName = "id_manager")
    private Manager manager;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "employees_roles",
            joinColumns = @JoinColumn(name = "id_employee", referencedColumnName = "id_employee"),
            inverseJoinColumns = @JoinColumn(name = "id_role", referencedColumnName = "id_role")
    )
    private Set<Role> roles = new HashSet<>();
}
