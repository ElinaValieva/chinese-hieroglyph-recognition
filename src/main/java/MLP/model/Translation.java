package MLP.model;

import lombok.Data;

import javax.persistence.*;

/**
 * author: ElinaValieva on 27.04.2019
 */
@Entity
@Table(name = "translation")
@Data
public class Translation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "code")
    private Integer code;

    @Column(name = "transcription")
    private String transcription;

    @Column(name = "translation")
    private String translation;
}
