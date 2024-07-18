package com.appschef.intern.minimarket.service;

import com.appschef.intern.minimarket.dto.request.members.CreateMemberRequest;
import com.appschef.intern.minimarket.dto.response.members.MemberDetailResponse;
import com.appschef.intern.minimarket.entity.Members;
import com.appschef.intern.minimarket.enumMessage.MembersErrorMessage;
import com.appschef.intern.minimarket.mapper.MemberMapper;
import com.appschef.intern.minimarket.repository.MembersRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
@AllArgsConstructor
public class MemberService {
    @Autowired
    private MembersRepository membersRepository;

    @Transactional
    public MemberDetailResponse createMember(CreateMemberRequest createMemberRequest) {
        Members members = MemberMapper.mapToMember(createMemberRequest);

        // Mendapatkan bulan dan tahun saat ini
        LocalDate currentDate = LocalDate.now();
        String format = currentDate.format(DateTimeFormatter.ofPattern("MMyy"));

        String nomorMemberTerakhir = membersRepository.getLastMemberNumber((format + "%"));
        if(nomorMemberTerakhir == null){
            members.setMemberNumber(format + "001");
        }else{
            String lastThreeNumber = nomorMemberTerakhir.substring(nomorMemberTerakhir.length() - 3);
            Long newMemberNumberLong = Long.parseLong(lastThreeNumber);
            newMemberNumberLong++;
            String newMemberNumber = format + String.format("%03d", newMemberNumberLong);
            members.setMemberNumber(newMemberNumber);
        }

        membersRepository.saveAndFlush(members);
        return MemberMapper.mapToMemberDetailResponse(members);
    }

    public MemberDetailResponse getMemberByMemberNumber(String nomorMember) {
        Members members = membersRepository.findByMemberNumberAndIsDeletedFalse(nomorMember)
                .orElseThrow(() -> new ValidationException(MembersErrorMessage.NOT_FOUND.getMessage()));
        return MemberMapper.mapToMemberDetailResponse(members);
    }

    public Page<MemberDetailResponse> getAllMembers(Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "fullName"));
        Page<Members> listMember = membersRepository.findByIsDeletedFalse(pageable);
        return listMember.map(MemberMapper::mapToMemberDetailResponse);
    }

    @Transactional
    public MemberDetailResponse updateMember(String nomorMember, CreateMemberRequest newMember) {
        Members oldMembers = membersRepository.findByMemberNumberAndIsDeletedFalse(nomorMember)
                .orElseThrow(() -> new ValidationException(MembersErrorMessage.NOT_FOUND.getMessage()));

        oldMembers.setFullName(newMember.getFullName());
        oldMembers.setEmail(newMember.getEmail());
        oldMembers.setPhoneNumber(newMember.getPhoneNumber());
        oldMembers.setAddress(newMember.getAddress());
        oldMembers.setUpdatedAt(new Date());

        membersRepository.saveAndFlush(oldMembers);

        return MemberMapper.mapToMemberDetailResponse(oldMembers);
    }

    @Transactional
    public void deleteMember(String nomorMember) {
        Long idMember = membersRepository.findIdByMemberNumberAndIsDeletedFalse(nomorMember)
                .orElseThrow(() -> new ValidationException(MembersErrorMessage.NOT_FOUND.getMessage()));

        int affectedRows = membersRepository.softDeleteById(idMember, new Date());
        if (affectedRows == 0){
            throw new ValidationException(MembersErrorMessage.GAGAL_HAPUS.getMessage());
        }
    }

}
