package io.tezos.octezconnect.client.dapp.storage

import io.tezos.octezconnect.core.internal.BeaconConfiguration
import io.tezos.octezconnect.core.scope.BeaconScope
import io.tezos.octezconnect.core.storage.Storage
import io.tezos.octezconnect.client.dapp.internal.storage.decorator.DecoratedDAppClientStorage
import io.tezos.octezconnect.client.dapp.internal.storage.plugin.DAppClientStoragePlugin

public interface DAppClientStorage : Storage, DAppClientStoragePlugin {
    override fun scoped(beaconScope: BeaconScope): DAppClientStorage
    override fun extend(beaconConfiguration: BeaconConfiguration): ExtendedDAppClientStorage = DecoratedDAppClientStorage(this, beaconConfiguration)
}