package org.forum.web.forum.service;

import org.forum.web.forum.models.PhoneNumber;
import org.forum.web.forum.repository.PhoneNumberRepository;
import org.springframework.stereotype.Service;

@Service
public class PhoneNumberServiceImpl implements PhoneNumberService {

    private final PhoneNumberRepository phoneNumberRepository;

    public PhoneNumberServiceImpl(PhoneNumberRepository phoneNumberRepository) {
        this.phoneNumberRepository = phoneNumberRepository;
    }

    @Override
    public void create(PhoneNumber phoneNumber) {
        phoneNumberRepository.create(phoneNumber);
    }

    @Override
    public void update(PhoneNumber phoneNumber) {
        phoneNumberRepository.update(phoneNumber);
    }
}
