package com.zyj.base.model.provider;

import com.google.auto.value.AutoValue;

/**
 * Created by Piasy{github.com/Piasy} on 5/12/16.
 */
@AutoValue
public abstract class HttpClientConfig {
    public static Builder builder() {
        return new AutoValue_HttpClientConfig.Builder();
    }

    public abstract boolean enableLog();

    public abstract int retryTimeout();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder enableLog(final boolean enableLog);

        public abstract Builder retryTimeout(final int retry);

        public abstract HttpClientConfig build();
    }
}
