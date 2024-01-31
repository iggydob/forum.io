package org.forum.web.forum.service.contracts;

import org.forum.web.forum.models.PhoneNumber;

public interface PhoneNumberService {

    void create(PhoneNumber phoneNumber);

    void update(PhoneNumber phoneNumber);
}
