package online.mokkoji.result.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.mokkoji.common.auth.jwt.util.JwtUtil;
import online.mokkoji.result.service.ResultService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/results")
public class ResultController {

    private final ResultService resultService;
    private final JwtUtil jwtUtil;

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getResult(HttpServletRequest req) {
        log.info("행사 리스트 상세 요청");
        String provider = jwtUtil.getProvider(req);
        String email = jwtUtil.getEmail(req);

        Map<String, Object> result = resultService.readResult(provider, email);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    

}