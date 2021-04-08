package com.requestrealpiano.songrequest.service;

import com.requestrealpiano.songrequest.domain.letter.request.ChangeReadyRequest;
import com.requestrealpiano.songrequest.global.admin.Admin;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdminService {

    private final Admin admin;

    public Boolean changeReadyToLetter(ChangeReadyRequest changeReadyRequest) {
        return admin.changeReadyToLetter(changeReadyRequest.getReadyToLetter());
    }

    public Boolean findReadyToLetter() {
        return admin.isReadyToLetter();
    }
}
