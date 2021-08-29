package com.awsjwtservice.domain;

public class RoundSearch {
    private String memberName;      //회원 이름
    private long accountId;

    //Getter, Setter
    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public long getAccountId() { return accountId; }

    public void setAccountId(long accountId) { this.accountId = accountId; }
}