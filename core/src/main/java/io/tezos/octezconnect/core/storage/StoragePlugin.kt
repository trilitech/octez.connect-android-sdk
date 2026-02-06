package io.tezos.octezconnect.core.storage

import io.tezos.octezconnect.core.scope.BeaconScope

public interface StoragePlugin {
    public fun scoped(beaconScope: BeaconScope): StoragePlugin
}