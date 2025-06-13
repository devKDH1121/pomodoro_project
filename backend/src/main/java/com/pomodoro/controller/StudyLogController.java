package com.pomodoro.controller;

import com.pomodoro.domain.StudyLog;
import com.pomodoro.dto.StudyLogRequestDto;
import com.pomodoro.repository.StudyLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@RequestMapping("/api/logs")
public class StudyLogController {

    private final StudyLogRepository studyLogRepository;

    // 공부 기록 생성 api
    @PostMapping
    public ResponseEntity<StudyLog> createLog(@RequestBody StudyLogRequestDto studyLogRequestDto){
        StudyLog newLog = new StudyLog();
        newLog.setMemo(studyLogRequestDto.getMemo());
        newLog.setDuration(studyLogRequestDto.getDuration());
        StudyLog savedLog = studyLogRepository.save(newLog);

        return ResponseEntity.ok(savedLog);

    }
    // 모든 기록 조회
    @GetMapping
    public ResponseEntity<List<StudyLog>> getAllLogs(){
        List<StudyLog> logs = studyLogRepository.findAll();
        logs.sort((a,b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        return ResponseEntity.ok(logs);
    }

    // 공부 기록 5개만 조회
    @GetMapping("/recent")
    public ResponseEntity<List<StudyLog>> getRecentLogs() {
        // createdAt 기준으로 내림차순 정렬하여 첫 페이지의 5개 항목을 가져옵니다.
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<StudyLog> logs = studyLogRepository.findAll(pageable).getContent();
        return ResponseEntity.ok(logs);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<StudyLog> getLogById(@PathVariable Long id) {
        return studyLogRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 공부 기록 수정
    @PutMapping("/{id}")
    public ResponseEntity<StudyLog> updateLog(@PathVariable Long id, @RequestBody StudyLogRequestDto studyLogRequestDto){
        Optional<StudyLog> optionalLog = studyLogRepository.findById(id);

        if(optionalLog.isPresent()){
            StudyLog existingLog = optionalLog.get();
            existingLog.setMemo(studyLogRequestDto.getMemo());
            StudyLog updatedLog = studyLogRepository.save(existingLog);

            return  ResponseEntity.ok(updatedLog);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 공부 기록 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<StudyLog> deleteLog(@PathVariable Long id){

        if(studyLogRepository.existsById(id)){
            studyLogRepository.deleteById(id);

            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
