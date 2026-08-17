package hwc_backend.coach.marketing.model;

public record MarketingImageAnnotation(
        double x,
        double y,
        double width,
        double height,
        String label,
        double confidence
) {
    public boolean isValid() {
        return x >= 0D && y >= 0D && width > 0D && height > 0D
                && x + width <= 1D && y + height <= 1D
                && label != null && !label.isBlank() && confidence >= 0D && confidence <= 1D;
    }
}
