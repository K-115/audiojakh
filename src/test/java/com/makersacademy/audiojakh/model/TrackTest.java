package com.makersacademy.audiojakh.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

public class TrackTest {

    @Test
    public void nullDurationFormatsAsEmptyString() {
        Track track = new Track();
        assertThat(track.getFormattedDuration(), is(""));
    }

    @Test
    public void formatsMinutesAndZeroPaddedSeconds() {
        assertThat(durationOf(215000), is("3:35"));
        assertThat(durationOf(65000), is("1:05"));
        assertThat(durationOf(5000), is("0:05"));
        assertThat(durationOf(0), is("0:00"));
    }

    private String durationOf(int ms) {
        Track track = new Track();
        track.setDurationMs(ms);
        return track.getFormattedDuration();
    }
}