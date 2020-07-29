package com.dms.ptp.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Table creation for Channel
 */
@Entity
@Table(name= "channel")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id",scope = Channel.class)
public class Channel {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "logo")
    private String logo;

    @Column(name = "launch_date")
    private LocalDateTime launch_date;

    @Column(name = "lmk_ref")
    private String lmk_ref;

    @Column(name= "telmar_ref")
    private String telmarRef;

    @Column(name = "network")
    private String network;

    @Column(name = "package")
    private String packageName;

    @Column(name = "distribution")
    private String distribution;

    @Column(name="lmkrefno")
    private Integer lmkRefNo;

    @Column(name = "sales_person")
    private Integer sales_person;


    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(name = "genre_channel",
            joinColumns = @JoinColumn(name = "channel_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id",referencedColumnName = "id"))
    private List<Genre> genres;
    
    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(name = "territory_channel",
            joinColumns = @JoinColumn(name = "channel_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "territory_id",referencedColumnName = "id"))
    private List<Territory> territory;
    
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "platform_id", referencedColumnName = "id")
    private Platform platform;

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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public LocalDateTime getLaunch_date() {
        return launch_date;
    }

    public void setLaunch_date(LocalDateTime launch_date) {
        this.launch_date = launch_date;
    }

    public String getLmk_ref() {
        return lmk_ref;
    }

    public void setLmk_ref(String lmk_ref) {
        this.lmk_ref = lmk_ref;
    }

    public String getTelmarRef() {
        return telmarRef;
    }

    public void setTelmarRef(String telmarRef) {
        this.telmarRef = telmarRef;
    }

    public List<Territory> getTerritory() {
        return territory;
    }

    public void setTerritory(List<Territory> territory) {
        this.territory = territory;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getDistribution() {
        return distribution;
    }

    public void setDistribution(String distribution) {
        this.distribution = distribution;
    }

    public Integer getLmkRefNo() {
        return lmkRefNo;
    }

    public void setLmkRefNo(Integer lmkRefNo) {
        this.lmkRefNo = lmkRefNo;
    }

    public Integer getSales_person() {
        return sales_person;
    }

    public void setSales_person(Integer sales_person) {
        this.sales_person = sales_person;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }


    
}
