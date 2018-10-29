package com.rp.util.fragment;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public final class FragmentBuilder {

    private static FragmentManager fragmentManager;
    private static FragmentTransaction fragmentTransaction;

    private FragmentBuilder() {}

    public static void initManager(FragmentManager fragmentManager) {
        FragmentBuilder.fragmentManager = fragmentManager;
    }

    public static Builder builder() {
        fragmentTransaction = fragmentManager.beginTransaction();
        return new Builder(new FragmentBuilder());
    }

    protected FragmentTransaction replace(@IdRes int id, @NonNull Fragment fragment) {
        return fragmentTransaction.replace(id, fragment, fragment.getClass().getSimpleName());
    }

    protected FragmentTransaction replace(@IdRes int id, @NonNull Fragment fragment, @NonNull String tag) {
        return fragmentTransaction.replace(id, fragment, tag);
    }

    protected FragmentTransaction remove(@NonNull String tag) {
        Fragment fragmentByTag = fragmentManager.findFragmentByTag(tag);
        if (fragmentByTag != null)
            return fragmentTransaction.remove(fragmentByTag);
        else return fragmentTransaction;
    }

    protected void removeAll() {
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
