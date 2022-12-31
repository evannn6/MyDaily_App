package com.evann6.mydaily.dashboard;

import com.evann6.mydaily.model.ModelDaily;

public interface Update {

    public void updateDaily(ModelDaily Daily);

    public void deleteDaily(ModelDaily Daily);
}
