package com.requestrealpiano.songrequest.controller;

import com.requestrealpiano.songrequest.domain.letter.request.ChangeReadyRequest;
import com.requestrealpiano.songrequest.global.response.ApiResponse;
import com.requestrealpiano.songrequest.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.requestrealpiano.songrequest.global.response.StatusCode.OK;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/letters/ready")
    public ApiResponse<Boolean> ready() {
        Boolean readyToLetter = adminService.findReadyToLetter();
        return ApiResponse.SUCCESS(OK, readyToLetter);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/letters/ready")
    public ApiResponse<Boolean> changeReady(@RequestBody ChangeReadyRequest changeReadyRequest) {
        Boolean readyToLetter = adminService.changeReadyToLetter(changeReadyRequest);
        return ApiResponse.SUCCESS(OK, readyToLetter);
    }
}
