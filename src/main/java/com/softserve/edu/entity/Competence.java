package com.softserve.edu.entity;

import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Competence")
public class Competence extends AbstractEntity {

    /**
     * @return the id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * @return the name
     */
    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "created")
    private Date date;

    @ManyToMany(mappedBy = "competences", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST,
            CascadeType.MERGE})
    private Set<User> users;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "competence")
    private Set<Group> groups;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "competence")
    private Set<AchievementType> achievementTypes;

    public Competence() {
    }

    public Competence(String name, Date date) {
        this.name = name;
        this.date = date;
    }


    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> aGroups) {
        groups = aGroups;
    }


    public Set<AchievementType> getAchievementTypes() {
        return achievementTypes;
    }

    public void setAchievementTypes(Set<AchievementType> achievementTypes) {
        this.achievementTypes = achievementTypes;
    }


    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Competence other = (Competence) obj;
        if (id == null) {
            return other.id == null;
        } else {
            return id.equals(other.id);
        }
    }


}
