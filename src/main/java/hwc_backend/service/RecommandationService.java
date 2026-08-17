package hwc_backend.service;

import hwc_backend.dto.recommandation.PlanActionDTO;
import hwc_backend.dto.recommandation.RecommandationDTO;
import hwc_backend.dto.recommandation.RegleRecommandationDTO;
import hwc_backend.entity.Recommandation;
import java.util.List;

public interface RecommandationService {

    List<Recommandation> genererRecommandations(Long diagnosticId);

    List<RecommandationDTO> getRecommandations(Long diagnosticId, String email);

    PlanActionDTO getPlanAction(Long diagnosticId, String email);

    List<RegleRecommandationDTO> getRegles();

    RegleRecommandationDTO createRegle(RegleRecommandationDTO dto);

    RegleRecommandationDTO updateRegle(Long id, RegleRecommandationDTO dto);

    void deleteRegle(Long id);
}
