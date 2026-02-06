package io.tezos.octezconnect.client.dapp.internal.storage.decorator

import io.tezos.octezconnect.core.internal.BeaconConfiguration
import io.tezos.octezconnect.core.internal.storage.decorator.DecoratedStorage
import io.tezos.octezconnect.core.scope.BeaconScope
import io.tezos.octezconnect.core.storage.ExtendedStorage
import io.tezos.octezconnect.client.dapp.internal.storage.plugin.ExtendedDAppClientStoragePlugin
import io.tezos.octezconnect.client.dapp.storage.DAppClientStorage
import io.tezos.octezconnect.client.dapp.storage.ExtendedDAppClientStorage

internal class DecoratedDAppClientStorage(
    private val storage: DAppClientStorage,
    private val beaconConfiguration: BeaconConfiguration,
): ExtendedDAppClientStorage, ExtendedStorage by DecoratedStorage(storage, beaconConfiguration), ExtendedDAppClientStoragePlugin by DecoratedDAppClientStoragePlugin(storage, beaconConfiguration) {
    override fun scoped(beaconScope: BeaconScope): ExtendedDAppClientStorage = DecoratedDAppClientStorage(storage.scoped(beaconScope), beaconConfiguration)
    override fun extend(beaconConfiguration: BeaconConfiguration): ExtendedDAppClientStorage = this
}