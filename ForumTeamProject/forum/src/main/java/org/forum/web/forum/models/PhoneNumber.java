package org.forum.web.forum.models;

import jakarta.persistence.*;

@Entity
@Table(name = "phone_numbers")
public class PhoneNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "phone_number_id")
    private int phoneNumberId;

    @Column(name = "phone_number")
    private String phoneNumber;

    public int getPhoneNumberId() {
        return phoneNumberId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumberId(int phoneNumberId) {
        this.phoneNumberId = phoneNumberId;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
