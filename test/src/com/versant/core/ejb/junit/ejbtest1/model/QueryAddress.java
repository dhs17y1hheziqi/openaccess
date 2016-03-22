
/*
 * Copyright (c) 1998 - 2005 Versant Corporation
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Versant Corporation - initial API and implementation
 */
package com.versant.core.ejb.junit.ejbtest1.model;

import javax.persistence.*;
import java.io.Serializable;
import static javax.persistence.GeneratorType.*;

@Entity
@Table(name = "QUERY_ADDRESS")
public class QueryAddress implements Serializable {
    private Integer id;
    private String street;
    private String city;
    private String province;
    private String postalCode;
    private String country;

    public QueryAddress() {
        city = "";
        province = "";
        postalCode = "";
        street = "";
        country = "";
    }

    public QueryAddress(String street, String city, String province, String country, String postalCode) {
        this.street = street;
        this.city = city;
        this.province = province;
        this.country = country;
        this.postalCode = postalCode;
    }

    @Id(generate = TABLE, generator = "QUERY_ADDRESS_GENERATOR")
    @TableGenerator(name = "QUERY_ADDRESS_GENERATOR", table = @Table(name = "QUERY_EMPLOYEE_GENERATOR_TABLE"), pkColumnValue = "ADDRESS_SEQ")
    @Column(name = "ADDRESS_ID")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Column(name = "P_CODE")
    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}

