package org.forum.web.forum.repository.contracts;

import org.forum.web.forum.models.PhoneNumber;

public interface PhoneNumberRepository {

    void create(PhoneNumber phoneNumber);

    void update(PhoneNumber phoneNumber);

    void delete(PhoneNumber phoneNumber);
}
