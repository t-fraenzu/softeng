package mse.fhnw.ch.adapter;

import mse.fhnw.ch.TierUtil;

public class TierUtilAdapter implements ITierUtilAdapter {

    public Object assignTier(String birthYear, String score) {
        TierUtil tierUtil = new TierUtil(); // Note: This makes a Web Services
        return tierUtil.assignTier(birthYear, score);
    }
}
