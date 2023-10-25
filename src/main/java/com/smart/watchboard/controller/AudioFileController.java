package com.smart.watchboard.controller;

import com.smart.watchboard.common.support.AwsS3Uploader;
import com.smart.watchboard.dto.S3Dto;
import com.smart.watchboard.service.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

@RestController
@RequestMapping("/documents")
@Tag(name = "강의 녹음파일 API", description = "강의 녹음파일 관련 API")
@RequiredArgsConstructor
@Slf4j
public class AudioFileController {
    private final AwsS3Uploader awsS3Uploader;
    private final NoteService noteService;
    private final RequestService requestService;
    private final STTService sttService;
    private final SummaryService summaryService;
    private final FileService fileService;

    @PostMapping("/{documentID}/audio")
    public ResponseEntity<?> uploadAudioFile(@PathVariable(value = "documentID") long documentId, @RequestParam("audioFile") MultipartFile audioFile, @RequestParam(value = "fileID", required = false) Long fileId, @RequestHeader("Authorization") String accessToken) throws UnsupportedAudioFileException, IOException {
        // 토큰 검증
        // s3에 오디오 파일 저장
        S3Dto s3Dto = new S3Dto(audioFile, documentId, fileId);
        String path = awsS3Uploader.uploadFile(s3Dto);
        // STT
        String sttResult = sttService.getSTT(path);
        //String sttResult = "알겠습니다. 나지 할게 나는 뭐 바꿨냐면 어제는 api 요청 방식 수정을 이게 뭔 얘기냐면 아까 얘기했듯이 ss를 최대한 활용을 하면서 그러니까 클라이언트 쪽에서 요청받은 거를 받아서 쿠키를 복제해서 서버에서 이제 api 콜을 하고 그거를 받아서 이제 다시 조합해서 클라이언트한테 돌려주는 방식이었는데 그거를 이제 약간 틀 같은 것만 ssr로 해서 받고 거기에 이제 내용을 채워넣는 거 그러니까 api 콜 했다가 받아서 채워넣는 그 부분은 클라이언트에서 하는 게 사실상 로딩 그러니까 첫 화면이 보일 때까지 시간이 더 짧다고 판단을 해서 그거를 바꿨어요. 그래서 그거를 수정을 다 하지 못했고 그러니까 이렇게 해야겠다라고 생각하고 이게 어떤 식으로 적용되는지 그런 것도 좀 테스트하고 그런데 시간을 좀 할애했고 이제 아마 오늘은 이 작업을 마무리 하지 않을까 그리고 어려운 리팩터링 할 때 엄청 많았다. 다 끝 그러면 나는 일단 자연호전 처리 확인이라고 한 게 저번에 얘기했었던 컨시스턴트 파싱에 대해서 조금 더 알아봤고 그리고 논문을 계속 보고 있고 그에 관해서 일단 오늘은 그거를 토대로 지니 생성을 하는 코드를 작성하고 디터브 리서치 래퍼를 생성해서 거의 다 그냥 아예 정리해서 드래프트랑 연결해 놓으려고 너무 더러워 보일 것 같아서 드리프트를 하니까 오늘 깨달았어 그리고 코프 인코더 구조 확인하고 백준 한 문제 풀기 딱 그리고 솔직히 예전보다는 지금은 확실히 아예 탄탄대로인 것 같아요. 아예 뭔가 아예 없는 상태가 아니라 뭔가 있는 상태여서 장애는 없었습니다. 일단 저는 스웨거 곡 api 작정을 끝냈는데 네 민석이가 뭐 수정하고 있어 그래 내 거에 수정을 하나 해놓고 이제 고 모비api 일단 현재까지 api는 작성을 다 했고 그리고 이제 erd 작성에 대해서 좀 고민을 하고 있었는데 그 팀 테이블 넣는 거 그래서 그거를 관계 설정을 어떻게 할까 그게 조금 어려운 고 이제 어떤 관계로 설정할지 못해서 이제 이거를 식별로 해야 할지 비족별로 해야 할지 그래서 그런 부분에서 좀 어려움이 있었고 그리고 오늘은 이제 이번 주에 스프링 시큐리티랑 로그인 구현을 마무리하기로 했으니까 일단 그거를 진행할 것 같습니다. 카카오 문제 풀기는 시간이 나면 하고요 시간이 아마 날지는 모르겠는데 그렇습니다.";

        // STT mysql에 저장
        noteService.createNote(documentId, sttResult);

        // STT 키워드 요청
        ResponseEntity<String> responseEntity = requestService.requestSTTKeywords(sttResult);

        // 요약본 요청
        String summary = requestService.requestSTTSummary(sttResult);
        summaryService.createSummary(documentId, summary);

        String body = fileService.createResponseBody(responseEntity, sttResult);

        // 응답 키워드, stt
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @PutMapping("/{documentID}/audio")
    public ResponseEntity<?> updateAudioFile(@PathVariable(value = "documentID") long documentId, @RequestParam("audioFile") MultipartFile audioFile, @RequestParam(value = "fileID", required = false) Long fileId, @RequestHeader("Authorization") String accessToken) throws UnsupportedAudioFileException, IOException {
        S3Dto s3Dto = new S3Dto(audioFile, documentId, fileId);
        String path = awsS3Uploader.uploadFile(s3Dto);

        String sttResult = sttService.getSTT(path);
        noteService.updateNote(documentId, sttResult);

        ResponseEntity<String> responseEntity = requestService.requestSTTKeywords(sttResult);
        String summary = requestService.requestSTTSummary(sttResult);
        summaryService.updateSummary(documentId, summary);

        String body = fileService.createResponseBody(responseEntity, sttResult);

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @DeleteMapping("/{documentID}/audio")
    public ResponseEntity<?> deleteAudioFile(@PathVariable(value = "documentID") long documentId, @RequestHeader("Authorization") String accessToken) throws UnsupportedAudioFileException, IOException {
        fileService.deleteAudioFile(documentId);
        // 음성 파일 삭제 했을 때, Stt, 요약본, 마인드맵 모두 삭제 처리
        noteService.deleteNote(documentId);
        summaryService.deleteSummary(documentId);
        // 마인드맵 삭제 구현

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/testffff")
    public ResponseEntity<?> test(@RequestHeader("Authorization") String accessToken) throws UnsupportedAudioFileException, IOException {
//        String body = """
//                {
//                    "keywords": ["asd", "qwe", "baad"]
//                }
//                """;
//        ResponseEntity<String> response1 = new ResponseEntity<>(body, HttpStatus.OK);
//        String summary = "qqqqqqqq";
//        System.out.println(response1.getBody());
//        String body2 = fileService.createResponseBody(response1, summary);

        String text = "asdasdasd";
        noteService.updateNote(9L, text);



        // 응답 키워드, stt
        return new ResponseEntity<>("", HttpStatus.OK);
    }

}
