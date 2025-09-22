package com.hbm.animloader;

public class AnimationController {

    // i do not care if this class is just "setter and getter"
    // its been over 3 years since last commit

    protected AnimationWrapper activeAnim = AnimationWrapper.EMPTY;

    public void setAnim(AnimationWrapper wrapper) {
        activeAnim = wrapper;
    }

    public void stopAnim() {
        activeAnim = AnimationWrapper.EMPTY;
    }

    public  AnimationWrapper getAnim() {
        return activeAnim;
    }
}
