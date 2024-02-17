package org.forum.web.forum.helpers;

import org.forum.web.forum.models.PhoneNumber;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PhoneConverter implements Converter<String, PhoneNumber> {

    @Override
    public PhoneNumber convert(String source) {
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setPhoneNumber(source);
        return phoneNumber;
    }

}
