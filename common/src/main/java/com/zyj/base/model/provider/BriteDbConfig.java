package com.zyj.base.model.provider;

import android.database.sqlite.SQLiteOpenHelper;

import com.google.auto.value.AutoValue;

/**
 * Created by Zyj on 2018/5/2.
 * Email: zyj0707@outlook.com
 */
@AutoValue
public abstract class BriteDbConfig {

    public static Builder builder() {
        return new AutoValue_BriteDbConfig.Builder();
    }

    public abstract boolean enableLogging();

    public abstract SQLiteOpenHelper sqliteOpenHelper();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder enableLogging(final boolean enableLogging);

        public abstract Builder sqliteOpenHelper(final SQLiteOpenHelper sqliteOpenHelper);

        public abstract BriteDbConfig build();

    }

}
