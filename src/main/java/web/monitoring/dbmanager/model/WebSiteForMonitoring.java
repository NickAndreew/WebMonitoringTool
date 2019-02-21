package web.monitoring.dbmanager.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Version;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "websiteformonitoring")
public class WebSiteForMonitoring {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    @Version
    private long version;
    @Column(name="name")
    private String name;
    @Column(name="url")
    private String url;
    @Column(name="startDate")
    private String startDate;
    @Column(name="delayUpBound")
    private Long delayUpBound;
    @Column(name="delayLowBound")
    private Long delayLowBound;
    @Column(name="sizeUpBound")
    private Long sizeUpBound;
    @Column(name="sizeLowBound")
    private Long sizeLowBound;
    @Column(name="monitorFrequency")
    private long monitorFrequency;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Long getDelayUpBound() {
        return delayUpBound;
    }

    public void setDelayUpBound(Long delayUpBound) {
        this.delayUpBound = delayUpBound;
    }

    public Long getDelayLowBound() {
        return delayLowBound;
    }

    public void setDelayLowBound(Long delayLowBound) {
        this.delayLowBound = delayLowBound;
    }

    public Long getSizeUpBound() {
        return sizeUpBound;
    }

    public void setSizeUpBound(Long sizeUpBound) {
        this.sizeUpBound = sizeUpBound;
    }

    public Long getSizeLowBound() {
        return sizeLowBound;
    }

    public void setSizeLowBound(Long sizeLowBound) {
        this.sizeLowBound = sizeLowBound;
    }

    public long getMonitorFrequency() {
        return monitorFrequency;
    }

    public void setMonitorFrequency(long monitorFrequency) {
        this.monitorFrequency = monitorFrequency;
    }
}


