package  com.dms.ptp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="catalog_pricing")
@Getter
@Setter
public class CatalogPricing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int id;
    private double rate;
    private int views;
    private int cpt;
    private int tvr;
    private int cpp;
    private String disclaimer;
    private int spots;
    @Column(name="spot_rate")
    private double spotRate;
    @Column(name="spot_length")
    private int spotLength ;
    @Column(name="validity_start")
    private String validityStart;
    @Column(name="validity_end")
    private String validityEnd;
    @Column(name="rate_card")
    private int rateCard;
    @Column(name="is_active")
    private int isActive;

    @ElementCollection
    private List<String> shows;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "pricing_id", referencedColumnName = "id")
    private List<SalesArea> salesAreaList;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "pricing_id", referencedColumnName = "id")
    private List<CatalogDaypart> daypart;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "pricing_id", referencedColumnName = "id")
    private List<CatalogWeekpart> weekpart;

}
