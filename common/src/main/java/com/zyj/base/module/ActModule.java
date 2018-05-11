package com.zyj.base.module;

import android.support.v7.app.AppCompatActivity;

import com.zyj.base.qualifier.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Zyj on 2018/4/28.
 * Email: zyj0707@outlook.com
 */
@Module
public class ActModule {
    private final AppCompatActivity activity;

    public ActModule(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Provides @ActivityScope
    public AppCompatActivity provideActivity() {
        return activity;
    }
}
