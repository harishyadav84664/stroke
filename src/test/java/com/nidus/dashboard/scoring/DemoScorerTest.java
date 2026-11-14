package com.nidus.dashboard.scoring;

import com.nidus.dashboard.model.Modality;
import com.nidus.dashboard.model.Prediction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DemoScorerTest {

    @Test
    void lowNihssIsNoAcuteStroke() {
        DemoScorer.Score score = DemoScorer.score(2, Modality.MRI);
        assertEquals(Prediction.NO_STROKE, score.prediction());
        assertNull(score.aspects());
    }

    @Test
    void highNihssOnCtIsHemorrhage() {
        DemoScorer.Score score = DemoScorer.score(12, Modality.CT);
        assertEquals(Prediction.HEMORRHAGIC, score.prediction());
        assertEquals("Deep gray (basal ganglia)", score.territory());
    }

    @Test
    void midNihssOnMriIsIschemic() {
        DemoScorer.Score score = DemoScorer.score(8, Modality.MRI);
        assertEquals(Prediction.ISCHEMIC, score.prediction());
        assertEquals("Left MCA", score.territory());
    }
}
