package com.srdcloud.taas.common.infra.persist;

import javax.persistence.*;

@Entity(name = "TestCase")
public class TestCasePo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String status;
    private long productVId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getProductVId() {
        return productVId;
    }

    public void setProductVId(long productVId) {
        this.productVId = productVId;
    }
}
