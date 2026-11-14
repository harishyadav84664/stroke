package com.nidus.dashboard.scoring;

import com.nidus.dashboard.model.Modality;
import com.nidus.dashboard.model.Prediction;

/**
 * Stand-in for a trained neuroimaging model.
 * Bands follow NIHSS only so the workstation can be clicked through
 * before a real classifier is attached. It does not read image pixels.
 */
public final class DemoScorer {

    public record Score(Prediction prediction, double confidence, String territory, Integer aspects) {
    }

    private DemoScorer() {
    }

    public static Score score(int nihss, Modality modality) {
        if (nihss <= 3) {
            return new Score(Prediction.NO_STROKE, 0.74, "No acute territory", null);
        }
        if (nihss >= 10 && modality == Modality.CT) {
            int aspects = Math.max(0, 10 - nihss / 3);
            return new Score(Prediction.HEMORRHAGIC, 0.83, "Deep gray (basal ganglia)", aspects);
        }
        int aspects = Math.max(3, 10 - nihss / 2);
        String territory = nihss % 2 == 0 ? "Left MCA" : "Right MCA";
        double confidence = Math.min(0.93, 0.76 + nihss / 200.0);
        return new Score(Prediction.ISCHEMIC, confidence, territory, aspects);
    }
}
