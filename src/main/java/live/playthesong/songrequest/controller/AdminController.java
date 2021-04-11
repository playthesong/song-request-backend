package live.playthesong.songrequest.controller;

import live.playthesong.songrequest.domain.letter.request.ChangeReadyRequest;
import live.playthesong.songrequest.global.response.ApiResponse;
import live.playthesong.songrequest.service.AdminService;
import live.playthesong.songrequest.global.response.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/letters/ready")
    public ApiResponse<Boolean> ready() {
        Boolean readyToLetter = adminService.findReadyToLetter();
        return ApiResponse.SUCCESS(StatusCode.OK, readyToLetter);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/letters/ready")
    public ApiResponse<Boolean> changeReady(@RequestBody ChangeReadyRequest changeReadyRequest) {
        Boolean readyToLetter = adminService.changeReadyToLetter(changeReadyRequest);
        return ApiResponse.SUCCESS(StatusCode.OK, readyToLetter);
    }
}
