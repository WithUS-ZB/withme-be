package com.withus.withmebe.gathering.Type;

import com.withus.withmebe.member.entity.Member;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ParticipantsType {
    MINOR("19세미만"){
        @Override
        public boolean isEligible(Member member) {
            return member.isAdult();
        }
    },
    ADULT("19세이상"){
        @Override
        public boolean isEligible(Member member) {
            return !member.isAdult();
        }
    },
    NO_RESTRICTIONS("제한없음"){
        @Override
        public boolean isEligible(Member member) {
            return true;
        }
    };

    private final String value;

    public abstract boolean isEligible(Member member);
}
