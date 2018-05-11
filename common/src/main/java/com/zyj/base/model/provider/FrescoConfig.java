package com.zyj.base.model.provider;

import com.facebook.drawee.backends.pipeline.DraweeConfig;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.google.auto.value.AutoValue;

/**
 * Created by heyong on 2016/12/6.
 */
@AutoValue
public abstract class FrescoConfig {

    public abstract ImagePipelineConfig imagePipelineConfig();

    public abstract DraweeConfig draweeConfig();

    public static Builder builder() {
        return new AutoValue_FrescoConfig.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder imagePipelineConfig(final ImagePipelineConfig imagePipelineConfig);

        public abstract Builder draweeConfig(final DraweeConfig draweeConfig);

        public abstract FrescoConfig build();

    }

}
