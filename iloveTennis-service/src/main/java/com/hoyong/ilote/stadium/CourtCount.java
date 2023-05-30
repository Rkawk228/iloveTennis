package com.hoyong.ilote.stadium;

import jakarta.persistence.Embeddable;

@Embeddable
public class CourtCount {
    /** 실내 여부 */
    boolean isIndoor;

    /** 코트 타입 */
    CourtType courtType;

    /** 코트개수 */
    int courtCount;
}
