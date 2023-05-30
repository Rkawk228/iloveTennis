package com.hoyong.ilote.sample.oneToone;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

@Entity
public class OneClass {

    @EmbeddedId
    EmbeddedPrimaryKey embeddedPrimaryKey;

    private String test2;
}
