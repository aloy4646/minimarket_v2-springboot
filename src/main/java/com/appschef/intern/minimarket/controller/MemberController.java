package com.appschef.intern.minimarket.controller;

import com.appschef.intern.minimarket.dto.request.members.CreateMemberRequest;
import com.appschef.intern.minimarket.dto.response.WebResponse;
import com.appschef.intern.minimarket.dto.response.members.MemberDetailResponse;
import com.appschef.intern.minimarket.dto.response.members.PhotoUploadResponse;
import com.appschef.intern.minimarket.service.MemberService;
import com.appschef.intern.minimarket.service.ValidationService;
import com.appschef.intern.minimarket.util.FileDownloadUtil;
import com.appschef.intern.minimarket.util.FileUploadUtil;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/members")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    private ValidationService validationService;

    @PostMapping("/")
    public ResponseEntity<WebResponse<MemberDetailResponse>> createMember(@Valid @RequestBody CreateMemberRequest createMemberRequest,
                                                                          BindingResult bindingResult){

        validationService.validate(bindingResult);

        try{
            MemberDetailResponse savedMember = memberService.createMember(createMemberRequest);
            WebResponse<MemberDetailResponse> response = WebResponse.<MemberDetailResponse>builder()
                    .status("success")
                    .data(savedMember)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }catch (ValidationException e){
            throw e;
        } catch (Exception e){
            WebResponse<MemberDetailResponse> errorResponse = WebResponse.<MemberDetailResponse>builder()
                    .status("fail")
                    .error(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{memberNumber}")
    public ResponseEntity<WebResponse<MemberDetailResponse>> getMember(@PathVariable("memberNumber") String memberNumber){
        try{
            MemberDetailResponse memberResponseDTO = memberService.getMemberByMemberNumber(memberNumber);
            WebResponse<MemberDetailResponse> response = WebResponse.<MemberDetailResponse>builder()
                    .status("success")
                    .data(memberResponseDTO)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (ValidationException e){
            throw e;
        } catch (Exception e){
            WebResponse<MemberDetailResponse> errorResponse = WebResponse.<MemberDetailResponse>builder()
                    .status("fail")
                    .error(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/")
    public ResponseEntity<WebResponse<Page<MemberDetailResponse>>> getAllMember(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        try{
            Page<MemberDetailResponse> members = memberService.getAllMembers(PageRequest.of(page, size));
            WebResponse<Page<MemberDetailResponse>> response = WebResponse.<Page<MemberDetailResponse>>builder()
                    .status("success")
                    .data(members)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (ValidationException e){
            throw e;
        } catch (Exception e){
            WebResponse<Page<MemberDetailResponse>> errorResponse = WebResponse.<Page<MemberDetailResponse>>builder()
                    .status("fail")
                    .error(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{memberNumber}")
    public ResponseEntity<WebResponse<MemberDetailResponse>> updateMember(@PathVariable("memberNumber") String memberNumber,
                                                                       @Valid @RequestBody CreateMemberRequest createMemberRequest,
                                                                       BindingResult bindingResult){

        validationService.validate(bindingResult);

        try{
            MemberDetailResponse memberResponseDTO = memberService.updateMember(memberNumber, createMemberRequest);
            WebResponse<MemberDetailResponse> response = WebResponse.<MemberDetailResponse>builder()
                    .status("success")
                    .data(memberResponseDTO)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (ValidationException e){
            throw e;
        } catch (Exception e){
            WebResponse<MemberDetailResponse> errorResponse = WebResponse.<MemberDetailResponse>builder()
                    .status("fail")
                    .error(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/{memberNumber}")
    public ResponseEntity<WebResponse<String>> deleteMember(@PathVariable("memberNumber") String memberNumber){
        try{
            memberService.deleteMember(memberNumber);
            WebResponse<String> response = WebResponse.<String>builder()
                    .status("success")
                    .data("Member successfully deleted")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (ValidationException e){
            throw e;
        } catch (Exception e){
            WebResponse<String> errorResponse = WebResponse.<String>builder()
                    .status("fail")
                    .error(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/uploadFile/{memberNumber}")
    public ResponseEntity<PhotoUploadResponse> uploadFile(
            @RequestParam("file") MultipartFile multipartFile)
            throws IOException {

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        long size = multipartFile.getSize();

        String filecode = FileUploadUtil.saveFile(fileName, multipartFile);

        PhotoUploadResponse response = new PhotoUploadResponse();
        response.setFileName(fileName);
        response.setSize(size);
        response.setDownloadUrl("/downloadFile/" + filecode);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/downloadFile/{fileCode}")
    public ResponseEntity<?> downloadFile(@PathVariable("fileCode") String fileCode) {
        FileDownloadUtil downloadUtil = new FileDownloadUtil();

        Resource resource = null;
        try {
            resource = downloadUtil.getFileAsResource(fileCode);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }

        if (resource == null) {
            return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
        }

        String contentType;
        try {
            contentType = Files.probeContentType(Paths.get(resource.getFile().getAbsolutePath()));
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
        } catch (IOException e) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }
}


