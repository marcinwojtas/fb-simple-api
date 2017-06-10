package pl.wojtas.fb.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;
import java.util.Set;

public class Facebook {
    private String id;
    private Long birthday;
    private String firstname;
    private String lastname;
    private String occupation;
    private String gender;
    private City city;
    private String work;
    private Set<String> friends;
    private String school;
    private String location;
    private String relationship;
    private List<Post> posts;

    protected Facebook() {}

    public Facebook(String id, Long birthday, String firstname, String lastname, String occupation, String gender, City city, String work, Set<String> friends, String school, String location, String relationship, List<Post> posts) {
        this.id = id;
        this.birthday = birthday;
        this.firstname = firstname;
        this.lastname = lastname;
        this.occupation = occupation;
        this.gender = gender;
        this.city = city;
        this.work = work;
        this.friends = friends;
        this.school = school;
        this.location = location;
        this.relationship = relationship;
        this.posts = posts;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getBirthday() {
        return birthday;
    }

    public void setBirthday(Long birthday) {
        this.birthday = birthday;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public Set<String> getFriends() {
        return friends;
    }

    public void setFriends(Set<String> friends) {
        this.friends = friends;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    @Override
    public boolean equals(Object candidate) {
        return EqualsBuilder.reflectionEquals(this, candidate);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}