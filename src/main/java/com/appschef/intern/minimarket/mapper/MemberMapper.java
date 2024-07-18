package com.appschef.intern.minimarket.mapper;

import com.appschef.intern.minimarket.dto.request.members.CreateMemberRequest;
import com.appschef.intern.minimarket.dto.response.members.MemberDetailResponse;
import com.appschef.intern.minimarket.dto.response.members.TopMemberResponse;
import com.appschef.intern.minimarket.entity.Members;
import com.appschef.intern.minimarket.projection.TopMemberProjection;

import java.util.Date;

public class MemberMapper {
    public static Members mapToMember(CreateMemberRequest createMemberRequest) {

        return new Members(
                null,
                null,
                createMemberRequest.getFullName(),
                createMemberRequest.getEmail(),
                createMemberRequest.getPhoneNumber(),
                createMemberRequest.getAddress(),
                0L,
                false,
                new Date(),
                null,
                null,
                null
        );
    }

    public static MemberDetailResponse mapToMemberDetailResponse(Members member) {

        return new MemberDetailResponse(
                member.getFullName(),
                member.getMemberNumber(),
                member.getEmail(),
                member.getPhoneNumber(),
                member.getAddress(),
                member.getPoint()
        );
    }

    public static TopMemberResponse mapToTopMemberResponse(TopMemberProjection topMemberProjection) {
        return new TopMemberResponse(
                topMemberProjection.getFullName(),
                topMemberProjection.getMemberNumber(),
                topMemberProjection.getTotalPurchase()
        );
    }
}
