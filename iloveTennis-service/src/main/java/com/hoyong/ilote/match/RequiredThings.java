package com.hoyong.ilote.match;

import jakarta.persistence.Embeddable;

@Embeddable
public class RequiredThings {
    private String contents;
    private Integer ageFrom;
    private Integer ageTo;
    private Integer ntrpFrom;
    private Integer ntrpTo;

}
