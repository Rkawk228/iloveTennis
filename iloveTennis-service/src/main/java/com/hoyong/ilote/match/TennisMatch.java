package com.hoyong.ilote.match;


import io.swagger.annotations.ApiModelProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * 사람찾는데에 대한 상세정보
 */
@Entity
@Data
//@ToString
//@EqualsAndHashCode(of="id")
//@Getter
//@Setter
//@NoArgsConstructor
public class TennisMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * 제목
     */
    @ApiModelProperty(value="제목")
    private String title;

    /**
     * 내용
     */
    @ApiModelProperty(value="내용")
    private String contents;

    /**
     * 경기 시작시간
     */
//    @ApiModelProperty(value="경기 시작시간")
//    private LocalDateTime matchStartTime;

    /**
     * 경기 종료시간
     */
//    @ApiModelProperty(value="경기 종료시간")
//    private LocalDateTime matchEndtTime;

    /** 필요사항
     * */
//    @ApiModelProperty(value="경기 종료시간")
//    @AttributeOverride(name="contents", column = @Column(name="requiredContents"))
//    @Embedded
//    private RequiredThings required;

//    @OneToOne
//    private Stadium stadium;
}