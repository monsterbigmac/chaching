package com.monsterbigmac.chaching.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "app_user")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "description")
    private String description;

    @ToString.Exclude
    @OneToMany(mappedBy = "appUser", orphanRemoval = true)
    private List<FinancialRecord> financialRecords = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "appUser", orphanRemoval = true)
    private List<FinancialClassification> financialClassifications = new ArrayList<>();

}
