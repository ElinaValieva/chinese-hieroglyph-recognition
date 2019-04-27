package MLP.repository;

import MLP.model.Translation;
import org.springframework.data.repository.CrudRepository;

/**
 * author: ElinaValieva on 27.04.2019
 */
public interface TranslationRepository extends CrudRepository<Translation, Long> {

    Translation findByCode(Integer code);
}
