package com.hbm.render.loader;

import java.util.List;

public interface IModelCustomOldNamed extends IModelCustomOld {

    // A little messy, but this is the cleanest refactor, and can be useful in the future
    List<String> getPartNames();
}