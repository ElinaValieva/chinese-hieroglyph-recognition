package MLP.model;

import lombok.Data;

import javax.persistence.*;

/**
 * author: ElinaValieva on 27.04.2019
 */
@Entity
@Table(name = MLP.common.Table.TABLE_NAME)
@Data
public class Translation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = MLP.common.Table.ID)
    private Integer id;

    @Column(name = MLP.common.Table.CODE)
    private Integer code;

    @Column(name = MLP.common.Table.TRANSCRIPTION)
    private String transcription;

    @Column(name = MLP.common.Table.TRANSLATION_EN)
    private String translationEnglish;

    @Column(name = MLP.common.Table.TRANSLATION_RU)
    private String translationRussian;

    @Column(name = MLP.common.Table.KEY)
    private String key;
}
