package hwc_backend.coach.marketing;

import static org.assertj.core.api.Assertions.assertThat;

import hwc_backend.coach.marketing.model.MarketingImageAnnotation;
import org.junit.jupiter.api.Test;

class MarketingImageAnnotationTests {
    @Test
    void acceptsNormalizedBoxesAndRejectsOutOfBoundsAnnotations() {
        assertThat(new MarketingImageAnnotation(0.1, 0.2, 0.3, 0.4, "Titre", 0.9).isValid()).isTrue();
        assertThat(new MarketingImageAnnotation(0.8, 0.2, 0.3, 0.4, "Hors image", 0.9).isValid()).isFalse();
        assertThat(new MarketingImageAnnotation(0.1, 0.2, 0.3, 0.4, "", 0.9).isValid()).isFalse();
    }
}
