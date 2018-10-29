package com.rp.util.adapter;

public interface RAdapterPayloadWatcher<E> {

    Object getPayloadData(E oldData, E newData);
}
