package io.tezos.octezconnect.client.dapp.storage

import io.tezos.octezconnect.core.internal.BeaconConfiguration
import io.tezos.octezconnect.core.scope.BeaconScope
import io.tezos.octezconnect.core.storage.ExtendedStorage
import io.tezos.octezconnect.client.dapp.internal.storage.plugin.ExtendedDAppClientStoragePlugin

public interface ExtendedDAppClientStorage : ExtendedStorage, DAppClientStorage, ExtendedDAppClientStoragePlugin {
    override fun scoped(beaconScope: BeaconScope): ExtendedDAppClientStorage
    override fun extend(beaconConfiguration: BeaconConfiguration): ExtendedDAppClientStorage = this
}