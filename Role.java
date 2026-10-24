package com.skillbridge;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "job_roles")
public class Role {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, unique=true)
    private String title;
    @Column(nullable=false, length=1000)
    private String requiredSkills;

    protected Role() {}
    public Role(String title, String requiredSkills) {
        this.title = title;
        this.requiredSkills = requiredSkills;
    }
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getRequiredSkills() { return requiredSkills; }
    public void setTitle(String title) { this.title = title; }
    public void setRequiredSkills(String requiredSkills) { this.requiredSkills = requiredSkills; }
    public List<String> skillList() {
        return Arrays.stream(requiredSkills.split(","))
            .map(String::trim).filter(s -> !s.isBlank()).toList();
    }
}
