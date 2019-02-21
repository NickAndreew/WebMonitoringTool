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
@Table(name = "websitecheckresult")
public class WebSiteCheckResult {


    public enum Status {
        OK("OK"), WARNING("WARNING"), CRITICAL("CRITICAL");

        private String value;

        Status(String value){
            this.value = value;
        }

        public String getValue(){
            return this.value;
        }
    }

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
    @Column(name="checkTime")
    private String checkTime;
    @Column(name="reponseStatus")
    private Status responseStatus;
    @Column(name="responseTime")
    private long responseTime;
    @Column(name="responseCode")
    private long responseCode;
    @Column(name="responseSize")
    private long responseSize;

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

    public String getCheckTime(){
        return this.checkTime;
    }

    public void setCheckTime(String checkTime){
        this.checkTime = checkTime;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public Status getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Status responseStatus) {
        this.responseStatus = responseStatus;
    }

    public long getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(long responseCode) {
        this.responseCode = responseCode;
    }

    public long getResponseSize() {
        return responseSize;
    }

    public void setResponseSize(long responseSize) {
        this.responseSize = responseSize;
    }
}
