package com.smart.watchboard.controller;

import com.smart.watchboard.dto.UserInformationDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
@Tag(name = "사용자 API", description = "사용자 관련 API(mock)")
public class UserController {
    @GetMapping()
    @Operation(summary = "사용자 정보 조회", description = "요청받은 사용자의 정보를 조회한다.")
    public ResponseEntity<?> getUserInformation(@RequestHeader("accessToken") String accessToken) {
        UserInformationDto userInformationDto = new UserInformationDto(1L, "권민석", "noparamin@naver.com");

        return new ResponseEntity<>(userInformationDto, HttpStatus.OK);
    }
}
