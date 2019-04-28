package MLP.service.translation;

import MLP.model.Translation;
import MLP.repository.TranslationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * author: ElinaValieva on 27.04.2019
 */
@Service
public class TranslationService {

    private final TranslationRepository translationRepository;

    @Autowired
    public TranslationService(TranslationRepository translationRepository) {
        this.translationRepository = translationRepository;
    }

    public Translation translate(Integer code) {
        return translationRepository.findByCode(code);
    }

    public List<Translation> translate(List<Integer> codes){
        return codes.stream().map(this::translate).collect(Collectors.toList());
    }
}
