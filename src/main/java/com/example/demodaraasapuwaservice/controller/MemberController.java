package com.example.demodaraasapuwaservice.controller;

import com.example.demodaraasapuwaservice.dto.MemberDto;
import com.example.demodaraasapuwaservice.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@Api(value = "Member Management")
@RequestMapping("/member-management/")
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @ApiOperation(value = "Retrieve all members", response = ResponseEntity.class)
    @GetMapping("members")
    public ResponseEntity<List<MemberDto>> getMembers() {
        return memberService.getMembers();
    }

    @ApiOperation(value = "Retrieve member", response = ResponseEntity.class)
    @GetMapping("members/{id}")
    public ResponseEntity<MemberDto> getMember(@PathVariable Integer id) {
        return memberService.getMember(id);
    }

    @ApiOperation(value = "Modify member", response = ResponseEntity.class)
    @PutMapping("members/{id}")
    public ResponseEntity modifyMember(@Valid @RequestBody MemberDto resource, @PathVariable Integer id) {
        return memberService.modifyMember(id, resource);
    }

    @ApiOperation(value = "Add member", response = ResponseEntity.class)
    @PostMapping("members")
    public ResponseEntity addMember(@Valid @RequestBody MemberDto resource) {
        return memberService.addMember(resource);
    }

    @ApiOperation(value = "Delete member", response = ResponseEntity.class)
    @DeleteMapping("members/{id}")
    public ResponseEntity addMember(@PathVariable Integer id) {
        return memberService.deleteMember(id);
    }
}
