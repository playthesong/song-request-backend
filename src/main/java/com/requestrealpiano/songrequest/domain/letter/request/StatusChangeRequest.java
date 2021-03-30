package com.requestrealpiano.songrequest.domain.letter.request;

import com.requestrealpiano.songrequest.domain.letter.RequestStatus;
import lombok.Getter;

@Getter
public class StatusChangeRequest {

    private RequestStatus requestStatus;
}
